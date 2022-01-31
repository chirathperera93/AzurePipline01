package com.ayubo.life.ayubolife.map_challenges.treasureview

import com.google.gson.annotations.SerializedName


data class MainTreasureResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : MainTreasure
)

data class MainTreasure (

        @SerializedName("header") val header : String,
        @SerializedName("sub_heading") val sub_heading : String,
        @SerializedName("banner_text") val banner_text : String,
        @SerializedName("treaure_image") val treaure_image : String,
        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String,
        @SerializedName("banner_image") val banner_image : String,
        @SerializedName("gradient1") val gradient1 : String,
        @SerializedName("gradient2") val gradient2 : String,
        @SerializedName("font_color") val font_color : String
)
