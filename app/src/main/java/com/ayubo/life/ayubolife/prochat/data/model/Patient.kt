package com.ayubo.life.ayubolife.prochat.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Patient(
    var id: Int,
    @SerializedName("first_name") var firstName: String?,
    @SerializedName("last_name") var lastName: String?,
    var age: Int,
    var gender: String?,
    var title: String?,
    var profile_picture_link: String?,
    var mobile_number: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeString(title)
        parcel.writeString(profile_picture_link)
        parcel.writeString(mobile_number)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getFullName(): String {
        val fullName = firstName!!.trim().plus(" ").plus(lastName!!.trim())
        return fullName
    }

    companion object CREATOR : Parcelable.Creator<Patient> {
        override fun createFromParcel(parcel: Parcel): Patient {
            return Patient(parcel)
        }

        override fun newArray(size: Int): Array<Patient?> {
            return arrayOfNulls(size)
        }
    }


}
