package com.ayubo.mobileemr.data.sources.remote.responses

import com.ayubo.life.ayubolife.prochat.data.model.ErrorResult

data class ResponsePendingAppointmentCount(var result: Int, var data: ResponsePendingAppointmentCountData, var error: ErrorResult?)

data class ResponsePendingAppointmentCountData(var count:Int)