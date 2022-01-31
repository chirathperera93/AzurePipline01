package com.ayubo.mobileemr.data.sources.remote.requests

import com.ayubo.mobileemr.data.model.enums.Gender
import com.google.gson.annotations.SerializedName

data class RequestRegisterPatient(
    var mobile_number: String, var title: String, var first_name: String, var last_name: String,
    var age: Int, var gender: Int)