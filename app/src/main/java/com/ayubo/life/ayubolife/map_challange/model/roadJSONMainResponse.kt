package com.ayubo.life.ayubolife.map_challange.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class roadJSONMainResponse(
    @SerializedName("json") val data: String

)


data class roadJSONMainData(

    @SerializedName("route") val route: List<LocationData>
)

//List<LocationData>
data class LocationData(

    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val long: Double,

    @SerializedName("distance") val distance: Int,
    @SerializedName("steps") val steps: Int,
    @SerializedName("zooml") val zooml: String?,

    @SerializedName("link") val link: String?,
    @SerializedName("flag_act") val flag_act: String?,
    @SerializedName("flag_deact") val flag_deact: String?,
    @SerializedName("action") val action: String?,
//    @SerializedName("meta") val meta : String,

    @SerializedName("stepstonext") val stepstonext: Int,

    @SerializedName("nextcity") val nextcity: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("citymsg") val citymsg: String?,
    @SerializedName("cityimg") val cityimg: String?,
    @SerializedName("wc") val wc: String?,
    @SerializedName("currentcity") val currentcity: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {


        try {
            parcel.writeDouble(lat)
            parcel.writeDouble(long)
            parcel.writeInt(distance)
            parcel.writeInt(steps)
            parcel.writeDouble(zooml!!.toDouble())
            parcel.writeString(link)
            parcel.writeString(flag_act)
            parcel.writeString(flag_deact)
            parcel.writeString(action)
            parcel.writeInt(stepstonext)
            parcel.writeString(nextcity)
            parcel.writeString(city)
            parcel.writeString(citymsg)
            parcel.writeString(cityimg)
            parcel.writeString(wc)
            parcel.writeString(currentcity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<LocationData> {
        override fun createFromParcel(parcel: Parcel): LocationData {
            return LocationData(parcel)
        }

        override fun newArray(size: Int): Array<LocationData?> {
            return arrayOfNulls(size)
        }
    }

}