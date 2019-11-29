package com.example.filemanager.Utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.filemanager.Entity.Document
import java.lang.Exception

fun searchDocument(context: Context, documentList: ArrayList<Document>,typeList: ArrayList<String>): Boolean {
    var mCursor: Cursor?
    try {
        mCursor = context.contentResolver.query(MediaStore.Files.getContentUri("external"), null,
                MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ?",
                arrayOf("application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "application/vnd.ms-powerpoint", "application/x-cprplayer",
                        "application/msword", "application/vnd.ms-excel"), null)
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                val uri = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                val documentSize = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                val documentName = uri.substring(uri.lastIndexOf("/")+1)
                val documentType = uri.substring(uri.lastIndexOf(".")+1).toUpperCase()
                val document = Document(uri, documentName, documentType, documentSize)
                documentList.add(document)
                if(!typeList.contains(documentType)){
                    typeList.add(documentType)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}