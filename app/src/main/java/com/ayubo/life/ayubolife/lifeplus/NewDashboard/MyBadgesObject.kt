package com.ayubo.life.ayubolife.lifeplus.NewDashboard


data class MyBadgeObject(
    val type: String,
    val myBadgeList: ArrayList<MyBadge>?
)


data class MyBadge(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val image_url_active: String,
    val image_url_inactive: String,
    val app_id: String,
    val status: String
)