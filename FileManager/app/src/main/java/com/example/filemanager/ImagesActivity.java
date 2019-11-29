package com.example.filemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.filemanager.Adapter.ImgFolderAdapter;
import com.example.filemanager.Entity.ImgFolder;
import com.example.filemanager.Utils.BaseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class ImagesActivity extends BaseActivity {

    private List<ImgFolder> mImgFolderList = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_images);
        mListView = findViewById(R.id.img_list_view);
        setTitleText();
        loadList(new LoadTask());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImgFolder selectedFolder = mImgFolderList.get(position);
                Intent imgIntent = new Intent(ImagesActivity.this,ImgListActivity.class);
                imgIntent.putExtra("name",selectedFolder.getName());
                imgIntent.putExtra("path",selectedFolder.getPath());
                imgIntent.putExtra("count",selectedFolder.getCount());
                startActivity(imgIntent);
            }
        });
    }

    private class LoadTask extends AsyncTask<Void,Integer,Boolean> {

        /*
        接受参数，在子线程中处理耗时操作
        寻找所有图片文件，初始化imgFolderList
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            Cursor cursor = null;
            try{
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media.MIME_TYPE + "= ? or " +
                        MediaStore.Images.Media.MIME_TYPE + "= ?",new String[]{"image/jpeg","image/png"},MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                List<String> folderPathList = new ArrayList<String>(); //保存已经添加的图片
                if(cursor != null){
                    while (cursor.moveToNext()){
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        File parentFile = new File(path).getParentFile();
                        if(parentFile == null){
                            continue;
                        }
                        String parentPath = parentFile.getAbsolutePath();
                        if(folderPathList.contains(parentPath)){
                            continue;
                        }
                        folderPathList.add(parentPath);
                        int count = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                if(name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")){
                                    return true;
                                }
                                return false;
                            }
                        }).length;
                        ImgFolder imgFolder = new ImgFolder(parentPath,path,parentFile.getName(),count);
                        mImgFolderList.add(imgFolder);
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
                ImgFolderAdapter adapter = new ImgFolderAdapter(ImagesActivity.this,R.layout.img_folder_item,mImgFolderList);
                mListView.setAdapter(adapter);
            }
        }
    }
}
