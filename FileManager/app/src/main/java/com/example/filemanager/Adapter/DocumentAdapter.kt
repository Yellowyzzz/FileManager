package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.filemanager.Entity.Document
import com.example.filemanager.R
import java.text.DecimalFormat

class DocumentAdapter(context: Context,private val mResourceId: Int,objects: List<Document>) : ArrayAdapter<Document>(context,mResourceId,objects) {

    inner class ViewHolder{
        lateinit var documentImage: ImageView
        lateinit var documentName: TextView
        lateinit var documentInfo: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val document = getItem(position)
        val view:View
        val viewHolder:ViewHolder
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(mResourceId,parent,false)
            viewHolder = ViewHolder()
            viewHolder.documentImage = view.findViewById(R.id.apk_image)
            viewHolder.documentName = view.findViewById(R.id.apk_name)
            viewHolder.documentInfo = view.findViewById(R.id.apk_info)
            view.setTag(viewHolder)
        }else{
            view = convertView
            viewHolder = view.getTag() as ViewHolder
        }
        when(document?.documentType){
            "XLS" -> viewHolder.documentImage.setImageResource(R.drawable.excel)
            "PPT","PPTX" -> viewHolder.documentImage.setImageResource(R.drawable.ppt)
            "DOC" -> viewHolder.documentImage.setImageResource(R.drawable.word)
            "PDF" -> viewHolder.documentImage.setImageResource(R.drawable.pdf)
        }
        viewHolder.documentName.text = document?.documentName
        val size = document!!.documentSize.toDouble()/1024
        val format = DecimalFormat("0.00")
        viewHolder.documentInfo.text = format.format(size) + "KB"
        return view
    }
}