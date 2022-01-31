package com.ayubo.life.ayubolife.programs.data.model


import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName


//data class Experts(var profile_picture:String,var action:String,var meta:String)

data class Experts(
        @SerializedName("full_name") var full_name: String?,
        @SerializedName("speciality") var speciality: String?,
        @SerializedName("meta") var meta: String?,
        @SerializedName("action") var action: String?,
        @SerializedName("profile_picture") var profile_picture: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(full_name)
        parcel.writeString(speciality)
        parcel.writeString(meta)
        parcel.writeString(action)
        parcel.writeString(profile_picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Experts> {
        override fun createFromParcel(parcel: Parcel): Experts {
            return Experts(parcel)
        }

        override fun newArray(size: Int): Array<Experts?> {
            return arrayOfNulls(size)
        }
    }

}