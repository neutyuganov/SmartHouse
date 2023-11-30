package com.example.smarthouse

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

data class RoomsTypesDataClass(val id:String, val name:String, val image_white:String, val image:Drawable) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        TODO("image")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image_white)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomsTypesDataClass> {
        override fun createFromParcel(parcel: Parcel): RoomsTypesDataClass {
            return RoomsTypesDataClass(parcel)
        }

        override fun newArray(size: Int): Array<RoomsTypesDataClass?> {
            return arrayOfNulls(size)
        }
    }
}
