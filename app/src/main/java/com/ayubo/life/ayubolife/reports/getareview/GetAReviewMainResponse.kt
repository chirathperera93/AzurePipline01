package com.ayubo.life.ayubolife.reports.getareview

import com.ayubo.life.ayubolife.ask.AskData
import com.google.gson.annotations.SerializedName


data class GetAReviewMainResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : List<ReviewData>
)

data class ReviewData (

        @SerializedName("category") val category : String,
        @SerializedName("experts") val experts : List<ReviewExperts>
)

data class ReviewExperts (

        @SerializedName("consultant_name") val consultant_name : String,
        @SerializedName("speciality") val speciality : String,
        @SerializedName("contact_id") val contact_id : String,
        @SerializedName("review_charge") val review_charge : String,
        @SerializedName("profile_picture") val profile_picture : String
)