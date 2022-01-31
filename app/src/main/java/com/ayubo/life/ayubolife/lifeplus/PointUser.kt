package com.ayubo.life.ayubolife.lifeplus


class PointUser(
    val points: Int,
    val points_label: String,
    val position: Int,
    val user_id: String,
    val last_updated_time: Long,
    val me: Boolean,
    val first_name: String,
    val last_name: String,
    val image_url: String,
    val manual_steps: Boolean = false,
    val record_label: RecordLabel,
    val action: String = "",
    val meta: String = ""

)

class RecordLabel(
    val text: String,
    val icon_url: String,
    val color: String
)