package com.ayubo.life.ayubolife.prochat.data.model.enums

import com.google.gson.annotations.SerializedName

enum class ChatTypes {
    @SerializedName("PDF") PDF,
    @SerializedName("IMAGE") IMAGE,
    @SerializedName("MP3") AUDIO,
    @SerializedName("TEXT") TEXT,
    @SerializedName("MP4") MP4,
    @SerializedName("GIF") GIF,
    @SerializedName("SYSTEM") SYSTEM,
    @SerializedName("DOCUMENT") DOCUMENT,
    @SerializedName("BUTTON") BUTTONS
}