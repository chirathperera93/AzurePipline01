package com.ayubo.life.ayubolife.twilio

import com.google.gson.annotations.SerializedName


data class TwillioDataMainResponse (

        @SerializedName("data") val data : TwillioDataMain,
        @SerializedName("result") val result : Int
)



data class TwillioDataMain (

        @SerializedName("CallerAccesstoken") val callerAccesstoken : String
)