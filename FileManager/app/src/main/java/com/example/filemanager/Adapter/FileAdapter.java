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
import com.example.filemanager.R;

import java.text.DecimalFormat;
import java.util.List;

public class FileAdapter extends ArrayAdapter<Music> {

    private int mResourceId;

    public FileAdapter(@NonNull Context context, int textViewResourceId, List<Music> objects) {
        super(context,textViewResourceId,objects);
        mResourceId = textViewResourceId;
    }

    class ViewHolder{
        ImageView musicImage;
        TextView musicName;
        TextView musicSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.musicImage = view.findViewById(R.id.file_image);
            viewHolder.musicName = view.findViewById(R.id.file_name);
            viewHolder.musicSize = view.findViewById(R.id.file_info);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.musicImage.setImageResource(R.drawable.music);
        viewHolder.musicName.setText(music.getMusicName());
        double size = (music.getMusicSize().doubleValue())/1024/1024;
        DecimalFormat format = new DecimalFormat("0.00");
        viewHolder.musicSize.setText(format.format(size)+" MB");
        return view;
    }

}
