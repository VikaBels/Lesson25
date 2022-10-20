package com.example.lesson24.models

import android.os.Parcel
import android.os.Parcelable

class PostInfo(
    val id: Long,
    val title: String?,
    val email: String?,
    val body: String?,
    val fullName: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(email)
        parcel.writeString(body)
        parcel.writeString(fullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostInfo> {
        override fun createFromParcel(parcel: Parcel): PostInfo {
            return PostInfo(parcel)
        }

        override fun newArray(size: Int): Array<PostInfo?> {
            return arrayOfNulls(size)
        }
    }


}