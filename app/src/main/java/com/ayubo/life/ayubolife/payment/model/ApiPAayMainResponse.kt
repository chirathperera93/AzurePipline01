package com.ayubo.life.ayubolife.payment.model

import com.google.gson.annotations.SerializedName


data class ApiPAayMainResponse (
        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : ApiPAayMainData
)



data class ApiPAayMainData (

        @SerializedName("icon_url") val icon_url : String,
        @SerializedName("payment_status") val payment_status : String,
        @SerializedName("header") val header : String,
        @SerializedName("list") val list : List<ApiPAayList>
)

data class ApiPAayList (

        @SerializedName("key") val key : String,
        @SerializedName("value") val value : String
)