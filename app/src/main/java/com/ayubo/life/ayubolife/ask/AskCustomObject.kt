package com.ayubo.life.ayubolife.ask


//data class AskCustomObject (
//        @SerializedName("title") val title : String,
//        @SerializedName("list") val list : List<AskCustomData>)


data class AskCustomObject(
        val title: String,
        val list: ArrayList<AskCustomData>?)

data class AskCustomData(
        val id: String,
        val name: String,
        val speciality: String,
        val image: String,
        val chatLink: String,
        val unread: Int,
        val status: String)


//data class AskSustomData(
//        @SerializedName("id") val id: String,
//        @SerializedName("name") val name: String,
//        @SerializedName("speciality") val speciality: String,
//        @SerializedName("image") val image: String,
//        @SerializedName("chatLink") val chatLink: String,
//        @SerializedName("unread") val unread: Int,
//        @SerializedName("status") val status: String
//) : Parcelable {
//    constructor(source: Parcel) : this(
//            source.readString(),
//            source.readString(),
//            source.readString(),
//            source.readString(),
//            source.readString(),
//            source.readInt(),
//            source.readString()
//    )
//
//    override fun describeContents() = 0
//
//    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
//        writeString(id)
//        writeString(name)
//        writeString(speciality)
//        writeString(image)
//        writeString(chatLink)
//        writeInt(unread)
//        writeString(status)
//    }
//
//    companion object {
//        @JvmField
//        val CREATOR: Parcelable.Creator<AskSustomData> = object : Parcelable.Creator<AskSustomData> {
//            override fun createFromParcel(source: Parcel): AskSustomData = AskSustomData(source)
//            override fun newArray(size: Int): Array<AskSustomData?> = arrayOfNulls(size)
//        }
//    }
//}

