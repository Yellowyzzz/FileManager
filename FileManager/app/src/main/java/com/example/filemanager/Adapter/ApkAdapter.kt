package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.filemanager.Entity.Apk
import com.example.filemanager.R

class ApkAdapter(context: Context, private val mResourceId: Int, objects: List<Apk>) : ArrayAdapter<Apk>(context, mResourceId, objects) {

    inner class ViewHolder{
        lateinit var apkImage:ImageView
        lateinit var packageName:TextView
        lateinit var apkInfo:TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val apk = getItem(position)
        val view:View
        val viewHolder:ViewHolder
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(mResourceId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.apkImage = view.findViewById(R.id.apk_image)
            viewHolder.packageName = view.findViewById(R.id.apk_name)
            viewHolder.apkInfo = view.findViewById(R.id.apk_info)
            view.setTag(viewHolder)
        }else{
            view = convertView
            viewHolder = view.getTag() as ViewHolder
        }
        viewHolder.apkImage.setImageDrawable(apk!!.apkIcon)
        viewHolder.packageName.text = apk.packageName
        viewHolder.apkInfo.text = apk.apkName + "  版本号:  " + apk.apkVersion
        return view
    }
}
