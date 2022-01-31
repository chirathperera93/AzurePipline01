package com.ayubo.life.ayubolife.lifeplus


data class FeedBackObj(
        var id: String,
        var app_id: String,
        var user_id: String,
        var category: String,
        var status: String,
        var priority: Int,
        var rating: String,
        var _rid: String,
        var _self: String,
        var _etag: String,
        var _attachments: String,
        var _ts: Int
)