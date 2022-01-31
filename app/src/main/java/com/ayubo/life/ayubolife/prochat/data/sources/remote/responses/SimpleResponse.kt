package com.ayubo.life.ayubolife.prochat.data.sources.remote.responses

import com.google.gson.JsonElement
import okhttp3.ResponseBody

data class SimpleResponse(var result: Int, var data: JsonElement)