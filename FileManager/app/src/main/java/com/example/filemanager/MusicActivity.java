package com.example.filemanager;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.filemanager.Adapter.FileAdapter;
import com.example.filemanager.Entity.Music;
import com.example.filemanager.Utils.BaseActivity;
import com.example.filemanager.Utils.OpenSelectedFile;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends BaseActivity {

    private List<Music> mMusicList = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mListView = findViewById(R.id.music_list_view);
        setTitleText();
        loadList(new LoadTask());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenSelectedFile.openFile(MusicActivity.this,mMusicList.get(position).getMusicUrl(),"audio/*");
            }
        });
    }

    public class LoadTask extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Cursor cursor = null;
            try{
                cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                        Music music = new Music(id,name,url,size);
                        mMusicList.add(music);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                FileAdapter adapter = new FileAdapter(MusicActivity.this,R.layout.flie_item,mMusicList);
                mListView.setAdapter(adapter);
            }
        }
    }
}
