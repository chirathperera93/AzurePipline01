package com.ayubo.life.ayubolife.payment.model



data class PaymentSettingsResponse (
        val result : Int,
        val data : List<PaymentSettingsResponseData>
)

data class PaymentSettingsResponseData (
        val header : String,
        val icon_url : String,
        val action : String,
        val meta : String,
        val added_list : List<Added_list>
)

data class Added_list (
        val id : Int,
        val number : String,
        val icon_url : String,
        val action : String,
        val meta : String,
        val default:Boolean,
        val expired:Boolean
)



