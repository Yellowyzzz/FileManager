package com.example.filemanager

import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.filemanager.Entity.Document
import com.example.filemanager.Fragment.DocumentFragment
import com.example.filemanager.Utils.BaseActivity
import com.example.filemanager.Utils.searchDocument
import com.google.android.material.tabs.TabLayout
import java.util.stream.Collectors


class DocumentActivity : BaseActivity() {

    private lateinit var mTabLayout : TabLayout
    private lateinit var mViewPager : ViewPager
    private var mDocumentList = ArrayList<Document>()
    private var mTypeList = ArrayList<String>()
    private var mFragmentList = ArrayList<DocumentFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        setTitleText()

        mTabLayout = findViewById(R.id.tablayout)
        mViewPager = findViewById(R.id.viewpager)
        mTypeList.add("全部")
        loadList(LoadTask())

    }

    private inner class LoadTask : AsyncTask<Void, Int, Boolean>() {

        override fun onPostExecute(result: Boolean?) {
            for(i in mTypeList.indices){
                val documentFragment = DocumentFragment()
                mFragmentList.add(documentFragment)
            }
            mViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    val documentFragment = mFragmentList.get(position)
                    val bundle = Bundle()
                    if(mTypeList.get(position).equals("全部")){
                        bundle.putParcelableArrayList("list",mDocumentList)
                    }else{
                        val filterDocumentList = mDocumentList.stream()
                                .filter({t: Document? -> t!!.documentType == mTypeList.get(position) })
                                .collect(Collectors.toList()) as ArrayList<Document>
                        bundle.putParcelableArrayList("list",filterDocumentList)
                    }
                    documentFragment.arguments = bundle
                    return documentFragment
                }

                override fun getCount(): Int {
                    return mFragmentList.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return mTypeList.get(position)
                }
            }
            mTabLayout.setupWithViewPager(mViewPager)
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            return searchDocument(this@DocumentActivity,mDocumentList,mTypeList)
        }
    }
}
