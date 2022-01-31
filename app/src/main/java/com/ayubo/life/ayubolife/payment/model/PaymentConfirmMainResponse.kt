package com.ayubo.life.ayubolife.payment.model


data class PaymentConfirmMainResponse (
        val result : Int,
        val data : PaymentConfirmMainData
)
data class PaymentConfirmMainData (
        val heading : String,
        val sub_heading : String,
        val meta : String,
        val action : String,
        val payment_action : String,
        val payment_meta : String,
        val id : Int,
        val payment_params : List<PaymentParams>,
        val table : List<ConfirmationTable>
)

data class ConfirmationTable (
        val label : String,
        val value : String
)
data class PaymentParams (
        val key : String,
        val value : String
)

data class PaymentConfirmMainResponseNew (
        val result : Int,
        val data : PaymentConfirmMainDataNew
)

data class PaymentConfirmMainDataNew (
        val action : String,
        val meta : String
)

data class SubmitPinResponse (
        val result : Int,
        val data : SubmitPinResponseData
)

data class SubmitPinResponseData (
        val action : String,
        val meta : String
)