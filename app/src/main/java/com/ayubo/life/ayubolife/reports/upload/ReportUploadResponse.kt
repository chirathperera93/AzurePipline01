package com.ayubo.life.ayubolife.reports.upload

import com.ayubo.life.ayubolife.reports.getareview.ReviewData
import com.google.gson.annotations.SerializedName


data class ReportUploadResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : ReportUploadData
)
data class ReportUploadData (

        @SerializedName("action") val action : String,
        @SerializedName("meta") val meta : String
)

