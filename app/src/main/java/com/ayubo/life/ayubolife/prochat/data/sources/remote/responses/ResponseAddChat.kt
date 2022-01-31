package com.ayubo.life.ayubolife.prochat.data.sources.remote.responses

import com.ayubo.life.ayubolife.prochat.data.model.Conversation
import com.google.gson.JsonElement
import java.util.*

class ResponseAddChat(var result: Int, var data: Conversation, var error: ErrorResultData?)

data class ErrorResultData(var error:List<Objects>?)