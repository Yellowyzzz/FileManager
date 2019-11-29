package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


import com.example.filemanager.Entity.Category
import com.example.filemanager.R

class CategoryAdapter(context: Context, private val mResourceId: Int, objects: List<Category>) : ArrayAdapter<Category>(context, mResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val category = getItem(position)
        val view = LayoutInflater.from(context).inflate(mResourceId, parent, false)
        val listImage = view.findViewById<ImageView>(R.id.main_list_image)
        val listName = view.findViewById<TextView>(R.id.main_list_name)
        listImage.setImageResource(category!!.imageId)
        listName.text = category.name
        return view
    }
}
