package com.ayubo.life.ayubolife.prochat.appointment

import com.ayubo.life.ayubolife.prochat.data.model.Conversation


data class ChatByDateObject(
        val dateLong: String,
        val conversations: ArrayList<Conversation>?)
