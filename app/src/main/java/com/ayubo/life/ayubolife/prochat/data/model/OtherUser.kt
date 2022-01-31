package com.ayubo.life.ayubolife.prochat.data.model

import android.os.Parcel
import android.os.Parcelable


data class OtherUser(var full_name: String?, var mobile_number: String?, var speciality: String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(full_name)
        parcel.writeString(mobile_number)
        parcel.writeString(speciality)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OtherUser> {
        override fun createFromParcel(parcel: Parcel): OtherUser {
            return OtherUser(parcel)
        }

        override fun newArray(size: Int): Array<OtherUser?> {
            return arrayOfNulls(size)
        }
    }

}