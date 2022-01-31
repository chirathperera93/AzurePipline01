package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import com.google.gson.JsonArray

class SubscriptionCardItem(
        val title: String,
        val summary: String,
        val currency: String,
        val rate: String,
        val frequency: String,
        val list: JsonArray,
        val action: String,
        val meta: String,
        val button_text: String
) {
}