package com.ayubo.life.ayubolife.prochat.data.model


import com.google.gson.annotations.SerializedName


data class HealthData(
    var height: String?,
    var weight: String?,
    @SerializedName("waist_size") var waistSize: String?,
    var bmi: String?
) {
    constructor() : this("0", "0", "0", "0")
}

