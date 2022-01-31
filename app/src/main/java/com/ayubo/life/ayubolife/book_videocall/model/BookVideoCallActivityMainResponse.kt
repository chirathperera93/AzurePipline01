package com.ayubo.life.ayubolife.book_videocall.model

import android.os.Parcel
import android.os.Parcelable
import com.ayubo.life.ayubolife.channeling.model.Expert
import com.google.gson.annotations.SerializedName


data class BookVideoCallActivityMainResponse(

    @SerializedName("result") val result: Int,
    @SerializedName("data") val data: List<Expert>
)

data class BookVideoCallListData(

    @SerializedName("title") val result: String,
    @SerializedName("list") val data: List<BookVideoCallActivityMainResponseData>
)

data class BookVideoCallActivityMainResponseData(

    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("speciality") val speciality: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("next") val next: String?,
    @SerializedName("locations") val locations: List<Locations>,
    @SerializedName("video") val video: Video?,
    @SerializedName("channel") val channel: Channel?,
    @SerializedName("review") val review: Review?,
    @SerializedName("ask") val ask: Ask?,
    @SerializedName("profile") val profile: Profile?,
    @SerializedName("booking") val booking: Booking?

) : Parcelable {
    constructor(parcel: Parcel) : this(

        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        ArrayList<Locations>().apply { parcel.readList(this, Locations::class.java.classLoader) },
        parcel.readParcelable(Video::class.java.classLoader),
        parcel.readParcelable(Channel::class.java.classLoader),
        parcel.readParcelable(Review::class.java.classLoader),
        parcel.readParcelable(Ask::class.java.classLoader),
        parcel.readParcelable(Profile::class.java.classLoader),
        parcel.readParcelable(Booking::class.java.classLoader)

    ) {
    }
    //(s: String, s1: String, toString: String,
    // s2: String, s3: String, text: ChatTypes, chatUser: ChatUser): Conversation {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(speciality)
        parcel.writeString(name)
        parcel.writeString(picture)
        parcel.writeString(next)
        parcel.writeList(locations)
        parcel.writeParcelable(video, flags)
        parcel.writeParcelable(channel, flags)
        parcel.writeParcelable(review, flags)
        parcel.writeParcelable(ask, flags)
        parcel.writeParcelable(profile, flags)
        parcel.writeParcelable(booking, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookVideoCallActivityMainResponseData> {
        override fun createFromParcel(parcel: Parcel): BookVideoCallActivityMainResponseData {
            return BookVideoCallActivityMainResponseData(parcel)
        }

        override fun newArray(size: Int): Array<BookVideoCallActivityMainResponseData?> {
            return arrayOfNulls(size)
        }
    }

}


data class Booking(

    @SerializedName("action") val action: String?,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(action)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }
}

data class Profile(

    @SerializedName("action") val action: String?,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(action)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {
        override fun createFromParcel(parcel: Parcel): Profile {
            return Profile(parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }
}

data class Video(

    @SerializedName("enable") val enable: Int,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enable)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }
}

data class Review(

    @SerializedName("enable") val enable: Int,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enable)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Review> {
        override fun createFromParcel(parcel: Parcel): Review {
            return Review(parcel)
        }

        override fun newArray(size: Int): Array<Review?> {
            return arrayOfNulls(size)
        }
    }
}

data class Locations(

    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("fee") val fee: String?,
    @SerializedName("fee_value") val fee_value: Int,
    @SerializedName("next_available") val next_available: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(fee)
        parcel.writeInt(fee_value)
        parcel.writeString(next_available)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Locations> {
        override fun createFromParcel(parcel: Parcel): Locations {
            return Locations(parcel)
        }

        override fun newArray(size: Int): Array<Locations?> {
            return arrayOfNulls(size)
        }
    }

}

data class Channel(

    @SerializedName("enable") val enable: Int,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enable)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Channel> {
        override fun createFromParcel(parcel: Parcel): Channel {
            return Channel(parcel)
        }

        override fun newArray(size: Int): Array<Channel?> {
            return arrayOfNulls(size)
        }
    }
}


data class Ask(

    @SerializedName("enable") val enable: Int,
    @SerializedName("meta") val meta: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enable)
        parcel.writeString(meta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ask> {
        override fun createFromParcel(parcel: Parcel): Ask {
            return Ask(parcel)
        }

        override fun newArray(size: Int): Array<Ask?> {
            return arrayOfNulls(size)
        }
    }
}