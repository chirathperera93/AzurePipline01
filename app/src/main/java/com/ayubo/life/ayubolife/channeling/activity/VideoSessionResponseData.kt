package com.ayubo.life.ayubolife.channeling.activity

import com.google.gson.annotations.SerializedName

/**
 * Created by Chirath Perera on 2021-08-02.
 */
class VideoSessionResponseData(
    @SerializedName("result") val result: Int,
    @SerializedName("data") val data: Any

) {
}