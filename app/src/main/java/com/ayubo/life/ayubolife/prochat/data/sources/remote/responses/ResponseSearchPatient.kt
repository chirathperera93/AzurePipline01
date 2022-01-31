package com.ayubo.life.ayubolife.prochat.data.sources.remote.responses

import com.ayubo.life.ayubolife.prochat.data.model.Patient


data class ResponseSearchPatient(var result: Int, var data: List<Patient>)