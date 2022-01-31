package com.ayubo.mobileemr.data.sources.remote.requests

import com.ayubo.mobileemr.data.model.enums.Gender
import com.google.gson.annotations.SerializedName

data class RequestRegisterDoctor(
    var mobile_number: String, var title: Int, var first_name: String, var last_name: String,
    var date_of_birth: String, var gender: Int,
    var email: String, var push_token:String)