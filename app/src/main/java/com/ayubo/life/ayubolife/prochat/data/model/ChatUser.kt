package com.ayubo.life.ayubolife.prochat.data.model

import android.os.Parcel
import android.os.Parcelable

data class ChatUser(
    var user_id: String?,
    var full_name: String?,
    var profile_picture_url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(full_name)
        parcel.writeString(profile_picture_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatUser> {
        override fun createFromParcel(parcel: Parcel): ChatUser {
            return ChatUser(parcel)
        }

        override fun newArray(size: Int): Array<ChatUser?> {
            return arrayOfNulls(size)
        }
    }

}