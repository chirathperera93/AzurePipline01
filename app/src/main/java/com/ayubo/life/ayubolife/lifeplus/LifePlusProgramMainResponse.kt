package com.ayubo.life.ayubolife.lifeplus

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class LifePlusProgramMainResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : LifePlusProgram
)

data class LifePlusProgram (
        @SerializedName("explore_title") val explore_title : String,
        @SerializedName("explore") val explore : List<PExplore>,
        @SerializedName("scroll_programs") val scroll_programs : List<ScrollPrograms>,
        @SerializedName("program_list") val program_list : List<ProgramList>,
        @SerializedName("banner_list") val banner_list : List<BannerItem>
       )


data class BannerItem (
        @SerializedName("item_name") val item_name : String,
        @SerializedName("banner_image") val banner_image : String,
        @SerializedName("item_sub_text") val item_sub_text : String,
        @SerializedName("item_sub_category") val item_sub_category : String,
        @SerializedName("item_short_description") val item_short_description : String,
        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String
)

data class PExplore (
        @SerializedName("title") val title : String,
        @SerializedName("bg_img") val bg_img : String,
        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String
)

data class ProgramList (
        @SerializedName("category") val category : String?,
        @SerializedName("title") val title : String?,
        @SerializedName("bg_img") val bg_img : String?,
        @SerializedName("action") val action : String?,
        @SerializedName("meta") val meta : String?,
        @SerializedName("offer") val offer : Boolean,
        @SerializedName("offer_text") val offer_text : String?,
        @SerializedName("description") val description : String?,
        @SerializedName("btn_text") val btn_text : String?

): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(bg_img)
        parcel.writeString(action)
        parcel.writeString(meta)
        parcel.writeByte(if (offer) 1 else 0)
        parcel.writeString(offer_text)
        parcel.writeString(description)
        parcel.writeString(btn_text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProgramList> {
        override fun createFromParcel(parcel: Parcel): ProgramList {
            return ProgramList(parcel)
        }

        override fun newArray(size: Int): Array<ProgramList?> {
            return arrayOfNulls(size)
        }
    }
}

data class ScrollPrograms (
        @SerializedName("title") val title : String,
        @SerializedName("list") val list : List<ListSP>
)


data class ListSP (
        @SerializedName("title") val title : String,
        @SerializedName("bg_img") val bg_img : String,
        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String,
        @SerializedName("offer") val offer : Boolean,
        @SerializedName("offer_text") val offer_text : String,
        @SerializedName("item_short_description") val item_short_description : String,
        @SerializedName("item_sub_text") val item_sub_text : String,
        @SerializedName("item_sub_category") val item_sub_category : String
)