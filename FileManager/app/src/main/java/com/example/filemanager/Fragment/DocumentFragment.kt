package com.example.filemanager.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.filemanager.Adapter.DocumentAdapter
import com.example.filemanager.Entity.Document
import com.example.filemanager.R
import com.example.filemanager.Utils.OpenSelectedFile

class DocumentFragment : Fragment() {

    private var mAllDocumentList = ArrayList<Document>()
    private lateinit var mListView : ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.akp_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAllDocumentList = arguments?.getParcelableArrayList("list")!!
        mListView = view.findViewById(R.id.apk_list_view)
        val adapter = DocumentAdapter(view.context,R.layout.apk_item,mAllDocumentList)
        mListView.adapter = adapter
        mListView.setOnItemClickListener { parent, view, position, id ->
            OpenSelectedFile.openFile(view.context,mAllDocumentList.get(position).documentUri,"application/*")
        }
    }
}

