package com.example.filemanager.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.filemanager.Adapter.ApkAdapter;
import com.example.filemanager.Entity.Apk;
import com.example.filemanager.R;
import com.example.filemanager.Utils.OpenSelectedFile;

import java.util.ArrayList;
import java.util.List;

public class ApkFragment extends Fragment {
    private List<Apk> mApkList = new ArrayList<>();
    private ListView mListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.akp_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList list = getArguments().getParcelableArrayList("list");
        mApkList = (List<Apk>)list.get(0);
        mListView = view.findViewById(R.id.apk_list_view);
        ApkAdapter adapter = new ApkAdapter(view.getContext(),R.layout.apk_item,mApkList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenSelectedFile.openFile(view.getContext(),mApkList.get(position).getApkPath(),"application/vnd.android.package-archive");
            }
        });
    }

}
