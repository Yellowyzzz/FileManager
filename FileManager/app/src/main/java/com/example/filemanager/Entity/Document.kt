package com.example.filemanager.Entity

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Document (val documentUri:String?,val documentName:String?,val documentType:String?,val documentSize:Long) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()
    ){}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(documentUri)//To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        return 0//To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<Document>{
        override fun createFromParcel(source: Parcel): Document {
            return  Document(source)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)//To change body of created functions use File | Settings | File Templates.
        }
    }
}