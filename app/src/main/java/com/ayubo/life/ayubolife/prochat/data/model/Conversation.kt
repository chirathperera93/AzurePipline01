package com.ayubo.life.ayubolife.prochat.data.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.ayubo.life.ayubolife.reports.model.ActionButton

data class Conversation(
    var conversation_id: Int,
    var type: String?,
    var timestamp: Long,
    var text: String?,
    var media_url: String?,
    var media_thumbnail_url: String?,
    var title: String?,
    var header: String?,
    var button_ids: String?,
    var button_array: Array<ActionButton>,
    var media_type: String?,
    var doctor_download: Boolean,
    var doctor_share: Boolean,
    var user_download: Boolean,
    var user_share: Boolean,
    var seen: Boolean,
    var source: String?,
    var external_expert_id: String?,
    var expert_specialization_id: String?,
    var medicine_delivery_source: String?,
    var user: ChatUser

) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArray(Array<ActionButton>::class.java.classLoader) as Array<ActionButton>,
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(ChatUser::class.java.classLoader)!! as ChatUser


    ) {
    }
    //(s: String, s1: String, toString: String,
    // s2: String, s3: String, text: ChatTypes, chatUser: ChatUser): Conversation {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(conversation_id)
        parcel.writeString(type)
        parcel.writeLong(timestamp)
        parcel.writeString(text)
        parcel.writeString(media_url)
        parcel.writeString(media_thumbnail_url)
        parcel.writeString(title)
        parcel.writeString(header)
        parcel.writeString(button_ids.toString())
        parcel.writeArray(button_array)
        parcel.writeString(media_type)
        parcel.writeByte(if (doctor_download) 1 else 0)
        parcel.writeByte(if (doctor_share) 1 else 0)
        parcel.writeByte(if (user_download) 1 else 0)
        parcel.writeByte(if (user_share) 1 else 0)
        parcel.writeByte(if (seen) 1 else 0)
        parcel.writeString(source)
        parcel.writeString(external_expert_id)
        parcel.writeString(expert_specialization_id)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Conversation> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Conversation {
            return Conversation(parcel)
        }

        override fun newArray(size: Int): Array<Conversation?> {
            return arrayOfNulls(size)
        }
    }

}