package com.ayubo.life.ayubolife.programs.settings

import com.google.gson.annotations.SerializedName


data class ProgramSettingsResponse (

        @SerializedName("result") val result : Int,
        @SerializedName("data") val data : Int
)