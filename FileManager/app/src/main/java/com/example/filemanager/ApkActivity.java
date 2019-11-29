package com.example.filemanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.filemanager.Entity.Apk;
import com.example.filemanager.Fragment.ApkFragment;
import com.example.filemanager.Utils.ApkSearchUtils;
import com.example.filemanager.Utils.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Apk> mInstalledApkList = new ArrayList<>();
    private List<Apk> mUnInstalledApkList = new ArrayList<>();
    private List<ApkFragment> mFragments = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk);
        setTitleText();

        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewpager);
        ApkFragment installed =  new ApkFragment();
        ApkFragment notInstalled = new ApkFragment();

        mFragments.add(notInstalled);
        mFragments.add(installed);
        mTitle.add("未安装");
        mTitle.add("已安装");
        loadList(new LoadTask());
    }

    private class LoadTask extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            File file = Environment.getExternalStorageDirectory();
            ApkSearchUtils apkSearchUtils = new ApkSearchUtils(ApkActivity.this);
            apkSearchUtils.FindAllAPKFile(file,mInstalledApkList,mUnInstalledApkList);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    ApkFragment apkFragment = mFragments.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    if(position == 0){
                        ArrayList list = new ArrayList();
                        list.add(mUnInstalledApkList);
                        bundle.putParcelableArrayList("list",list);
                    }else {
                        ArrayList list = new ArrayList();
                        list.add(mInstalledApkList);
                        bundle.putParcelableArrayList("list",list);
                    }
                    apkFragment.setArguments(bundle);
                    return apkFragment;
                }

                @Override
                public int getCount() {
                    return mFragments.size();
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return mTitle.get(position);
                }
            });
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

}
