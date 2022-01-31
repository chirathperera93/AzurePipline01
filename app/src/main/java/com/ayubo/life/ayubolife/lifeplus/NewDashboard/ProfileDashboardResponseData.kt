package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import com.google.gson.annotations.SerializedName
import java.util.*

data class ProfileDashboardResponseData(
        @SerializedName("result") val result: Int,
        @SerializedName("data") val data: Any,
        @SerializedName("message") val message: String
)