package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import com.google.gson.JsonObject

class WeeklyTrackerCardItem(
    val id: String,
    val title: String,
    val summary: String,
    val type: String,
    val category: String,
    val description: JsonObject?,
    val plusbutton: JsonObject?,
    val click: JsonObject?,
    val options: JsonObject?,
    val bottom: JsonObject?,
    val chart_data: JsonObject?,
    val badge_url: String?,
    val badge_title: String?

) {
}