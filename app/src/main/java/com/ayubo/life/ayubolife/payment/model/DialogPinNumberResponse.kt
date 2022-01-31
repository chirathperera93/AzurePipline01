package com.ayubo.life.ayubolife.payment.model



data class DialogPinNumberResponse (

        val result : Int,
        val data : Data
)

data class Data(val reference_code : String)