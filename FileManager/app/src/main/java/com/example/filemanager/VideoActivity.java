package com.example.filemanager;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.filemanager.Adapter.VideoAdapter;
import com.example.filemanager.Entity.Video;
import com.example.filemanager.Utils.BaseActivity;
import com.example.filemanager.Utils.OpenSelectedFile;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends BaseActivity {

    private ListView mListView;
    private List<Video> mVideoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mListView = findViewById(R.id.video_list_view);
        setTitleText();
        loadList(new LoadTask());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenSelectedFile.openFile(VideoActivity.this,mVideoList.get(position).getVideoUri(),"video/*");
            }
        });
    }

    private class LoadTask extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Cursor cursor = null;
            try{
                cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Video.Media.DEFAULT_SORT_ORDER);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(),id,MediaStore.Images.Thumbnails.MICRO_KIND,options);
                        Video video = new Video(id,name,url,size,bitmap);
                        mVideoList.add(video);
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
                VideoAdapter adapter = new VideoAdapter(VideoActivity.this,R.layout.flie_item,mVideoList);
                mListView.setAdapter(adapter);
            }
        }
    }

}
