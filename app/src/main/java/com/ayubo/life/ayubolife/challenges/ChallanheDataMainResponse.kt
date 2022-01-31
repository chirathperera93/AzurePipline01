package com.ayubo.life.ayubolife.challenges

import com.google.gson.annotations.SerializedName


data class ChallanheDataMainResponse(

        @SerializedName("result") val result: Int,
        @SerializedName("data") val data: ChData
)

data class ChData(

        @SerializedName("counter") val counter: Int,
        @SerializedName("day") val day: Int,
        @SerializedName("treatures") val treatures: List<Treatures>,
        @SerializedName("cards") val cards: List<Cards>,
        @SerializedName("service_checkpoints") val service_checkpoints: Boolean,
        @SerializedName("enabled_checkpoints") val enabled_checkpoints: List<Int>,
        @SerializedName("snow") val snow: Boolean,
        @SerializedName("weekSteps") val weekSteps: Int,
        @SerializedName("white_lines") val white_lines: Boolean,
        @SerializedName("tip") val tip: String,
        @SerializedName("tip_icon") val tip_icon: String,
        @SerializedName("tipheading") val tipheading: String,
        @SerializedName("tip_header_1") val tip_header_1: String,
        @SerializedName("tip_header_2") val tip_header_2: String,
        @SerializedName("tip_type") val tip_type: String,
        @SerializedName("tip_meta") val tip_meta: String
)

data class Treatures(

        @SerializedName("latp") val latp: Double,
        @SerializedName("longp") val longp: Double,
        @SerializedName("action") val action: String,
        @SerializedName("meta") val meta: String,
        @SerializedName("steps") val steps: Int,
        @SerializedName("objimg") val objimg: String,
        @SerializedName("status") val status: String,
        @SerializedName("bubble_txt") val bubble_txt: String,
        @SerializedName("bubble_link") val bubble_link: String,
        @SerializedName("auto_hide") val auto_hide: Boolean
)

data class Cards(

        @SerializedName("type") val type: String,
        @SerializedName("name") val name: String,
        @SerializedName("weather_status") val weather_status: String,
        @SerializedName("weather_icon") val weather_icon: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("action") val action: String,
        @SerializedName("share_image") val share_image: String,
        @SerializedName("bg_img") val bg_img: String,
        @SerializedName("status") val status: String,
        @SerializedName("city") val city: String,
        @SerializedName("steps") val steps: String,
        @SerializedName("days") val days: String,
        @SerializedName("meta") val meta: String,
        @SerializedName("text") val text: String,
        @SerializedName("image") val image: String,
        @SerializedName("stepstonext") val stepstonext: String,
        @SerializedName("degree") val degree: Double,
        @SerializedName("description") val description: String,
        @SerializedName("max_degree") val max_degree: Double,
        @SerializedName("min_degree") val min_degree: Double,
        @SerializedName("completed_steps") val completed_steps: Int,
        @SerializedName("completed_days") val completed_days: Int,
        @SerializedName("completed_avg") val completed_avg: Int,
        @SerializedName("remaining_steps") val remaining_steps: Int,
        @SerializedName("remaining_days") val remaining_days: Int,
        @SerializedName("remaining_avg") val remaining_avg: Int,
        @SerializedName("heading") val heading: String
)


data class AdventureChallengeRouteMainResponse(

        @SerializedName("result") val result: Int,
        @SerializedName("json") val json: String
)
