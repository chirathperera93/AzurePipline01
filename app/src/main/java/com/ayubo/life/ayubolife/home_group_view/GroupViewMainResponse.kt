package com.ayubo.life.ayubolife.home_group_view

import com.google.gson.annotations.SerializedName


data class GroupViewMainResponse (
        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : GroupViewMainData
)
data class GroupViewMainData (

        @SerializedName("title") val title : String,
        @SerializedName("list") val list : List<GroupViewMainDataList>
)
data class GroupViewMainDataList (

        @SerializedName("title") val title : String,
        @SerializedName("bg_img") val bg_img : String,
        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String,
        @SerializedName("item_sub_text") val item_sub_text : String,
        @SerializedName("item_sub_category") val item_sub_category : String,
        @SerializedName("item_short_description") val item_short_description : String,
        @SerializedName("offer") val offer : Boolean,
        @SerializedName("offer_text") val offer_text : String
)