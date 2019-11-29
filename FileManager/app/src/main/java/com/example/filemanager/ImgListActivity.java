package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.filemanager.Adapter.ImageWallAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImgListActivity extends AppCompatActivity {
    private Intent mIntent;
    private TextView mTitleTextView;
    private GridView mImageWall;
    private View mParentView;
    private View mWallLayout;
    private Info mInfo;
    private PhotoView mPhotoView;
    private ArrayList<String> mImageUri;
    private AlphaAnimation startSelectAnimation = new AlphaAnimation(1,0);
    private AlphaAnimation endSelectAnimation = new AlphaAnimation(0,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_list);
        startSelectAnimation.setDuration(200);
        endSelectAnimation.setDuration(200);

        endSelectAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mWallLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startSelectAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mWallLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIntent = getIntent();
        mWallLayout = findViewById(R.id.image_wall_layout);
        mTitleTextView = findViewById(R.id.title_text);
        mImageWall = findViewById(R.id.img_wall);
        mPhotoView = findViewById(R.id.selected_image);
        mPhotoView.setAnimaDuring(200);
        mParentView = findViewById(R.id.parent);
        mTitleTextView.setText(mIntent.getStringExtra("name"));
        new LoadTask().execute();
        mImageWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mImageWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout relativeLayout = (RelativeLayout) view;
                PhotoView p = relativeLayout.findViewById(R.id.each_image);
                mInfo = p.getInfo();
                String selectedUri = mImageUri.get(position);
                Glide.with(ImgListActivity.this)
                        .load(mIntent.getStringExtra("path")+"/"+selectedUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mPhotoView.setImageDrawable(resource);
                        mWallLayout.startAnimation(startSelectAnimation);
                        mParentView.setVisibility(View.VISIBLE);
                        mPhotoView.animaFrom(mInfo);
                    }
                });
            }
        });
        mPhotoView.enable();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWallLayout.startAnimation(endSelectAnimation);
                mPhotoView.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mParentView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private class LoadTask extends AsyncTask<Void,Integer,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                mImageWall.setAdapter(new ImageWallAdapter(ImgListActivity.this, R.layout.img_layout, mImageUri,mImageWall,mIntent.getStringExtra("path")));
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                File ParentFile = new File(mIntent.getStringExtra("path"));
                mImageUri = new ArrayList<>(Arrays.asList(ParentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                })));
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (mParentView.getVisibility() == View.VISIBLE) {
        mWallLayout.startAnimation(endSelectAnimation);
            mPhotoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    mParentView.setVisibility(View.GONE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
