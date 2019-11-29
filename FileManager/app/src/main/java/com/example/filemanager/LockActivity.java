package com.example.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filemanager.Entity.User;
import com.example.filemanager.View.LockView;
import com.example.filemanager.View.LockViewListener;

import org.litepal.LitePal;

import java.util.List;

public class LockActivity extends AppCompatActivity {

    private LockView mLockView;
    private LockViewListener mListener = new LockViewListener() {

        @Override
        public String onStart() {
            User myUser = LitePal.findFirst(User.class);
            if(myUser != null){
                return myUser.getPassword();
            }else {
                myUser = new User();
                myUser.setPassword("[0, 3, 6, 7, 8]");
                myUser.save();
                return myUser.getPassword();
            }
        }

        @Override
        public void onComplete(List<Integer> selectedDotNumberList) {
            User user = LitePal.findFirst(User.class);
            if(user.getPassword().equals(selectedDotNumberList.toString())){
                Intent intent = new Intent(LockActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        mLockView = findViewById(R.id.lockView);
        mLockView.addListener(mListener);
    }
}
