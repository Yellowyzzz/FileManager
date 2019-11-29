package com.example.filemanager.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;

import com.example.filemanager.Entity.Apk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkSearchUtils {
    private Context mContext;
    private List<PackageInfo> mPackageInfoList ;
    private List<PackageInfo> mInstalledApps = new ArrayList<>();
    private PackageManager mPackageManager;
    private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
    private static int UNINSTALLED = 1; // 表示未安装
    private static int INSTALLED_UPDATE =2; // 表示已经安装，版本比现在这个版本要低


    public ApkSearchUtils(Context context) {

        this.mContext = context;
        mPackageManager = context.getPackageManager();
        mPackageInfoList = mPackageManager.getInstalledPackages(0);
        for(PackageInfo packageInfo:mPackageInfoList){
            if((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0){
                mInstalledApps.add(packageInfo);
            }
        }
    }

    public void FindAllAPKFile(File file,List<Apk> installedApkList,List<Apk> unInstalledApkList) {

        Cursor cursor = null;
        try{
//            String select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.apk'" + ")";
            String select = "(" + MediaStore.Files.FileColumns.MIME_TYPE + "= 'application/vnd.android.package-archive')" ;
            cursor = mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, select , null, null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String apkPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                    PackageInfo packageInfo = mPackageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
                    if(packageInfo != null){
                        ApplicationInfo appInfo = packageInfo.applicationInfo;
                        appInfo.sourceDir = apkPath;
                        appInfo.publicSourceDir = apkPath;
                        Drawable apkIcon = appInfo.loadIcon(mPackageManager);
                        String packageName = packageInfo.packageName;
                        String versionName = packageInfo.versionName;
                        int versionCode = packageInfo.versionCode;
                        String appName = mPackageManager.getApplicationLabel(appInfo).toString();
                        int type = doType(packageName, versionCode);
                        Apk apk = new Apk(appName,packageName,versionName,apkPath,apkIcon,type);
                        if(type == UNINSTALLED){
                            unInstalledApkList.add(apk);
                        }else {
                            installedApkList.add(apk);
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private int doType(String packageName, int versionCode) {
        for (PackageInfo packageInfo : mInstalledApps) {
            String installedPackageName = packageInfo.packageName;
            int installedVersionCode = packageInfo.versionCode;
            if(packageName.endsWith(installedPackageName)){
                if(versionCode <= installedVersionCode){
                    return INSTALLED;
                }else if(versionCode > installedVersionCode){
                    return INSTALLED_UPDATE;
                }
            }
        }
        return UNINSTALLED;
    }
}

