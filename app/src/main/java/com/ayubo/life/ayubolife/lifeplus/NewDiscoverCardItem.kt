package com.ayubo.life.ayubolife.lifeplus

import com.google.gson.JsonObject

/**
 * Created by Chirath Perera on 2021-07-06.
 */
class NewDiscoverCardItem(
    val title: String,
    val summary: String,
    val icon_url: String,
    val label: String,
    val card_image_url: String,
    val action: String,
    val meta: String,
    var bannerData: ArrayList<SliderData>? = null

) {
}