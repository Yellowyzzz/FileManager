package com.example.filemanager.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadBitmapUtils {

    public static int calculateSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int inSampleSize = 1;
        int imgHeight = options.outHeight;
        int imgWidth = options.outWidth;
        if(imgHeight > reqHeight || imgWidth > reqWidth){
            int heightRatio = Math.round((float) imgHeight / (float) reqHeight);
            int widthRatio = Math.round((float) imgWidth / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  //选择较小的比例作为值
        }
        return inSampleSize;
    }

    public static Bitmap doCompression(String path,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }
}
