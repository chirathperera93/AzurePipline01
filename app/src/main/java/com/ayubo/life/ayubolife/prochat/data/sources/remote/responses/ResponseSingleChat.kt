package com.ayubo.mobileemr.data.sources.remote.responses

import com.ayubo.life.ayubolife.prochat.data.model.Conversation
import com.ayubo.life.ayubolife.prochat.data.model.OtherUser

data class ResponseSingleChat(var result:Int, var data: ResponseSingleChatData)

data class ResponseSingleChatData(var appointment_id:String, var status:String, var other_user: OtherUser, var conversations:ArrayList<Conversation>)