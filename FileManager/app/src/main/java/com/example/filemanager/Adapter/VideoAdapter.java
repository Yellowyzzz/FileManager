package com.example.filemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.filemanager.Entity.Music;
import com.example.filemanager.Entity.Video;
import com.example.filemanager.R;

import java.text.DecimalFormat;
import java.util.List;

public class VideoAdapter extends ArrayAdapter<Video> {
    private int mResourceId;

    public VideoAdapter(@NonNull Context context, int textViewResourceId, List<Video> objects) {
        super(context,textViewResourceId,objects);
        mResourceId = textViewResourceId;
    }

    class ViewHolder{
        ImageView videoImage;
        TextView videoName;
        TextView videoInfo;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Video video = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.videoImage = view.findViewById(R.id.file_image);
            viewHolder.videoName = view.findViewById(R.id.file_name);
            viewHolder.videoInfo = view.findViewById(R.id.file_info);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.videoImage.setImageBitmap(video.getVideoBitmap());
        viewHolder.videoName.setText(video.getVideoName());
        double size = (video.getVideoSize().doubleValue())/1024/1024;
        DecimalFormat format = new DecimalFormat("0.00");
        viewHolder.videoInfo.setText(format.format(size)+" MB");
        return view;
    }
}
