package com.example.filemanager.Utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.filemanager.R;


public class BaseActivity extends AppCompatActivity {

    private TextView mTitleTextViewt;
    private Intent mIntent;
    private AsyncTask<Void,Integer,Boolean> loadTask =null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void loadList(AsyncTask<Void,Integer,Boolean> loadTask){
        this.loadTask = loadTask;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            loadTask.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadTask.execute();
                }else {
                    Toast.makeText(this,"denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void setTitleText(){
        mTitleTextViewt = findViewById(R.id.title_text);
        mIntent = getIntent();
        mTitleTextViewt.setText(mIntent.getStringExtra("selected"));
    }
}
