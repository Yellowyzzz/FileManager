package com.example.filemanager.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;

public class OpenSelectedFile {

    public static void openFile(Context context,String path,String fileType){
        File flie = new File(path);
        Uri contentUri = FileProvider.getUriForFile(context,"com.example.filemanager.provider",flie);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION  | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(contentUri,fileType);
        context.startActivity(intent);
    }
}
