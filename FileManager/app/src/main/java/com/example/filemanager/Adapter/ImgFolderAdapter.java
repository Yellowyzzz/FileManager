package com.example.filemanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.filemanager.Entity.ImgFolder;
import com.example.filemanager.R;
import com.example.filemanager.Utils.LoadBitmapUtils;

import java.util.List;

public class ImgFolderAdapter extends ArrayAdapter<ImgFolder> {

    private int mResourceId;
    public ImgFolderAdapter(@NonNull Context context,int textViewResourceId, @NonNull List<ImgFolder> objects) {
        super(context,textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    class ViewHolder{
        ImageView img;
        TextView folderName;
        TextView imgCount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImgFolder imgFolder = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.img = view.findViewById(R.id.img_folder_image);
            viewHolder.folderName = view.findViewById(R.id.img_folder_name);
            viewHolder.imgCount = view.findViewById(R.id.img_count);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        Glide.with(view).load(imgFolder.getFirstImgPath()).into(viewHolder.img);
        viewHolder.folderName.setText(imgFolder.getName());
        viewHolder.imgCount.setText("" + imgFolder.getCount());
        return view;
    }
}
