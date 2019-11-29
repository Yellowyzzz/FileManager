package com.example.filemanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView

import com.example.filemanager.Adapter.CategoryAdapter
import com.example.filemanager.Entity.Category

import java.util.ArrayList
import android.os.StatFs
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.MEDIA_MOUNTED
import android.provider.MediaStore
import android.text.format.Formatter
import android.util.Log
import android.widget.TextView
import com.example.filemanager.View.MemoryView
import java.io.File


class MainActivity : Activity() {

    private val mCategoryList = ArrayList<Category>()
    private lateinit var mMemoryInfoText : TextView
    private lateinit var mMemoryView : MemoryView
    private var mUsedMemory: Long? = null
    private var mTotalMemory: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCategory()
        readSDMenInfo()
        val adapter = CategoryAdapter(this@MainActivity, R.layout.category_item, mCategoryList)
        val listView = findViewById<ListView>(R.id.category_list_view)
        mMemoryView = findViewById(R.id.memory_view)
        mMemoryView.setScales(mUsedMemory!! * 1.0/ mTotalMemory!!)
        mMemoryInfoText = findViewById(R.id.meminfo)
        mMemoryInfoText.text = Formatter.formatFileSize(this, mUsedMemory!!) + "/" + Formatter.formatFileSize(this, mTotalMemory!!)


        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val select = mCategoryList[position].name
            when (select) {
                "音频" -> {
                    val musicIntent = Intent(this@MainActivity, MusicActivity::class.java)
                    musicIntent.putExtra("selected", select)
                    startActivity(musicIntent)
                }
                "视频" -> {
                    val videoIntent = Intent(this@MainActivity, VideoActivity::class.java)
                    videoIntent.putExtra("selected", select)
                    startActivity(videoIntent)
                }
                "图片" -> {
                    val imageIntent = Intent(this@MainActivity, ImagesActivity::class.java)
                    imageIntent.putExtra("selected", select)
                    startActivity(imageIntent)
                }
                "文档" -> {
                    val documentIntent = Intent(this@MainActivity,DocumentActivity::class.java)
                    documentIntent.putExtra("selected",select)
                    startActivity(documentIntent)
                }
                "安装包" -> {
                    val apkIntent = Intent(this@MainActivity, ApkActivity::class.java)
                    apkIntent.putExtra("selected", select)
                    startActivity(apkIntent)
                }
            }
        }
    }

    private fun initCategory() {
        val music = Category("音频", R.drawable.music)
        mCategoryList.add(music)
        val video = Category("视频", R.drawable.video)
        mCategoryList.add(video)
        val picture = Category("图片", R.drawable.picture)
        mCategoryList.add(picture)
        val document = Category("文档", R.drawable.document)
        mCategoryList.add(document)
        val apk = Category("安装包", R.drawable.apk)
        mCategoryList.add(apk)
    }

    private fun readSDMenInfo() {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.getPath())
        val blockSize = stat.blockSize.toLong()   // 获得一个扇区的大小
        val totalBlocks = stat.blockCount.toLong() // 获得扇区的总数
        val availableBlocks = stat.availableBlocks.toLong()   // 获得可用的扇区数量
        mTotalMemory = totalBlocks * blockSize
        mUsedMemory = (totalBlocks - availableBlocks) * blockSize
    }
}
