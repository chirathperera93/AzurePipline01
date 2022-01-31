package com.ayubo.life.ayubolife.ask

import com.google.gson.annotations.SerializedName


data class AskMainResponse (

    @SerializedName("result") val result : Int,
    @SerializedName("data") val data : List<AskData>
)

data class AskData (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("speciality") val speciality : String,
    @SerializedName("image") val image : String,
    @SerializedName("chatLink") val chatLink : String,
    @SerializedName("unread") val unread : Int,
    @SerializedName("status") val status : String
)