package com.ayubo.life.ayubolife.new_payment.model

import com.ayubo.life.ayubolife.health.PaymentItems
import com.google.gson.JsonObject

/**
 * Created by Chirath Perera on 2021-10-06.
 */

data class NewGetMyCardsResponse(
    val result: Int,
    val data: ArrayList<NewMyCardItem>,
    val message: String
)

data class NewPaymentDetailResponse(
    val result: Int,
    val data: NewPaymentDetail,
    val message: String
)

data class NewPaymentMethodResponse(
    val result: Int,
    val data: JsonObject,
    val message: String
)

data class NewMyCardItem(
    val id: String,
    val user_id: String,
    val app_id: String,
    val last_used: String,
    val card_type: String,
    val card_name: String,
    val card_no: String,
    val card_expiry: String,
    val icon_url: String,
    val enabled: Boolean = false,
    var isSelect: Boolean = false
)

data class NewPaymentMethodTypeItem(
    val id: String,
    val title: String,
    val description: String,
    val icon_url: String,
    val icon_urls: ArrayList<String> = ArrayList<String>(),
    val enabled: Boolean = false,
    var isSelect: Boolean = false
)

data class NewPaymentDetail(
    val id: String,
    val app_id: String,
    val user_id: String,
    val payment_type: String,
    val payment_reference_id: String,
    val payment_status: String,
    val createdatetime: Long,
    val payment: JsonObject,
    val order: JsonObject,
    val headings: JsonObject,
    val last_card: JsonObject?,
)

data class ChargePaymentItem(
    val payment_id: String,
    val payment_method: String
)

data class ChargePaymentResponse(
    val result: Int,
    val data: ChargePaymentDetail,
    val message: String
)

data class CreatePaymentItem(
    val payment_type: String,
    val payment_reference_id: String
)

data class ChargePaymentDetail(
    val title: String?,
    val title_color: String?,
    val status: Boolean,
    val description: String?,
    val icon_url: String?,
    val headings: JsonObject?,
    val payment_details: ArrayList<PaymentItems>?,
    val data: JsonObject?,
    val button_one: JsonObject?,
    val button_two: JsonObject?,
    val texts: JsonObject?,
    val payment_total: Int?,
    val payment_currency: String?

)



