package com.ayubo.mobileemr.data.sources.remote.responses

import com.ayubo.life.ayubolife.prochat.data.model.Appointment

data class ResponseAppointmentHistory(var result: Int, var data: List<Appointment>)