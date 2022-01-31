package com.ayubo.life.ayubolife.prochat.data.sources.remote.requests

import io.reactivex.annotations.NonNull

data class RequestChatAll(var appointment_id: String = "", @NonNull var contact_id: String)
