package com.ayubo.life.ayubolife.lifeplus.NewDashboard


import com.google.gson.JsonObject

/**
 * Created by Chirath Perera on 2021-10-29.
 */
data class CreateDashboardLogObj(
    val user_id: String,
    val data: JsonObject
)