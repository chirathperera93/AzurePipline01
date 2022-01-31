package com.ayubo.life.ayubolife.prochat.appointment

import com.google.gson.JsonObject

/**
 * Created by Chirath Perera on 2021-08-23.
 */


class SessionDataInfo(
    var id: String,
    var app_id: String,
    var user_id: String,
    var category_id: String,
    var start_datetime: Long,
    var members: ArrayList<String>,
    var end_datetime: Long,
    var status: String,
    var type: String,
    var category: String,
    var unread_count: Int,
    var user_details: NewChatUser,
    var lastmessage_timestamp: Long

)

class NewMessage(
    var id: String,
    var app_id: String,
    var user_id: String,
    var message: String,
    var content_type: String,
    var type: String,
    var read: Int,
    var create_datetime: Long,
    var lastupdate_datetime: Long,
    var lastmessage_timestamp: Long,
    var session_id: String,
    var user: NewChatUser,
    var delivered_to: ArrayList<String>,
    var read_by: ArrayList<String>?

)

class CommonPayload(
    var notification_type: String,
    var payload: JsonObject,
    var subscriber: Any
)

class NewChatUser(
    var name: String,
    var image_url: String,
    var message_summary: String = ""
)

class ChatUserTypingPayload(
    var text: String,
    var status: String,
    var session_id: String = ""
)