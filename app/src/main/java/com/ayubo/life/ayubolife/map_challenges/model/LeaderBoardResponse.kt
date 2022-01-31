package com.ayubo.life.ayubolife.map_challenges.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeaderBoardMainResponse(

        val result: Int,
        val data: LeaderBoardResponse
)

data class LeaderBoardResponse(

        @SerializedName("header_image") val header_image: String,
        @SerializedName("header_text") val header_text: String,
        @SerializedName("subheading_text") val subheading_text: String,
        val info_button: InfoButton,
        val leaderboards: List<Leaderboards>
) : Serializable


data class Leaderboards(
        @SerializedName("title") val title: String?,
        @SerializedName("list") val list: List<MemberList>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(MemberList)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Leaderboards> {
        override fun createFromParcel(parcel: Parcel): Leaderboards {
            return Leaderboards(parcel)
        }

        override fun newArray(size: Int): Array<Leaderboards?> {
            return arrayOfNulls(size)
        }
    }
}

data class InfoButton(
        @SerializedName("show") val show: Boolean?,
        @SerializedName("action") val action: String?,
        @SerializedName("meta") val meta: String?
)

data class MemberList(

        @SerializedName("position") val position: Int,
        @SerializedName("highlight_color") val highlight_color: String?,
        @SerializedName("picture") val picture: String?,
        @SerializedName("full_name") val full_name: String?,
        @SerializedName("main_value") val main_value: String?,
        @SerializedName("main_icon") val main_icon: String?,
        @SerializedName("sub_value") val sub_value: String?,
        @SerializedName("sub_icon") val sub_icon: String?,
        @SerializedName("tags") val tags: Array<Tags>
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position)

        parcel.writeString(highlight_color)
        parcel.writeString(picture)
        parcel.writeString(full_name)
        parcel.writeString(main_value)
        parcel.writeString(main_icon)
        parcel.writeString(sub_value)
        parcel.writeString(sub_icon)
        parcel.writeArray(tags)
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberList

        if (!tags.contentEquals(other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        return tags.contentHashCode()
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            (parcel.readArray(Array<Tags>::class.java.classLoader) as Array<Tags>)
    )

    companion object CREATOR : Parcelable.Creator<MemberList> {
        override fun createFromParcel(parcel: Parcel): MemberList {
            return MemberList(parcel)
        }

        override fun newArray(size: Int): Array<MemberList?> {
            return arrayOfNulls(size)
        }
    }
}


data class Tags(
        @SerializedName("text") val text: String?,
        @SerializedName("color") val color: String?
) : Parcelable {
    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(color)
    }


    companion object CREATOR : Parcelable.Creator<Leaderboards> {
        override fun createFromParcel(parcel: Parcel): Leaderboards {
            return Leaderboards(parcel)
        }

        override fun newArray(size: Int): Array<Leaderboards?> {
            return arrayOfNulls(size)
        }
    }
}
//data class Tags (
//        val text : String,
//        val color : String): Serializable