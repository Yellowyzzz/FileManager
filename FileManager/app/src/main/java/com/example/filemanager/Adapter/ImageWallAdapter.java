package com.example.filemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.filemanager.R;

import java.util.List;

public class ImageWallAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener {

    private int mResourceId;
    private boolean mIsFirst = true;
    private String mParentPath;
    private List<String> mImageUri;
    private View mView;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private GridView mImageWall;

    public ImageWallAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<String> objects, GridView gridView,String parentUri) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
        mImageUri = objects;
        mImageWall = gridView;
        mParentPath = parentUri;
//        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        mImageWall.setOnScrollListener(this);
    }

    class ViewHolder{
        PhotoView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String uri = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.each_image);
            viewHolder.imageView.disenable();

            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.imageView.setTag(uri);
        Glide.with(view)
                .load(mParentPath+"/"+uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);
        mView = view;
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            loadBitmaps(mFirstVisibleItem,mVisibleItemCount);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if(mIsFirst && visibleItemCount > 0){
            loadBitmaps(mFirstVisibleItem,mVisibleItemCount);
            mIsFirst = false;
        }
    }

    private void loadBitmaps(int firstVisibleItem,int visibleItemCount){
        try {
            for(int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount;i++){
                String imageUri = mImageUri.get(i);
                ImageView imageView = mImageWall.findViewWithTag(imageUri);
                Glide.with(mView)
                        .load(mParentPath+"/"+imageUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
