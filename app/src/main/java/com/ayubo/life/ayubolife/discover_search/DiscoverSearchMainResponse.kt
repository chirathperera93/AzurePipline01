package com.ayubo.life.ayubolife.discover_search

import com.google.gson.annotations.SerializedName


data class DiscoverSearchMainResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : List<DiscoverSearchData>
)



data class DiscoverSearchData (

        @SerializedName("id") val id : Int,
        @SerializedName("name") val name : String,
        @SerializedName("type") val type : String
)