package com.malqaa.androidappp.newPhase.domain.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class ImageSelectModel(
    val uri: Uri? = null,
    val base64: String = "",
    var is_main: Boolean = false,
    var url: String = "",
    var isEdit: Boolean = false,
    val id :Int=0,
    val type: Int=0,
    var isMainMadia: Boolean=false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeString(base64)
        parcel.writeByte(if (is_main) 1 else 0)
        parcel.writeString(url)
        parcel.writeByte(if (isEdit) 1 else 0)
        parcel.writeInt(id)
        parcel.writeInt(type)
        parcel.writeByte(if (isMainMadia) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageSelectModel> {
        override fun createFromParcel(parcel: Parcel): ImageSelectModel {
            return ImageSelectModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageSelectModel?> {
            return arrayOfNulls(size)
        }
    }
}