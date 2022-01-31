package com.ayubo.life.ayubolife.payment.model


data class PaymentMethodsResponse(
        val result: Int,
        val data: PaymentMethodsResponseDataNew
)

data class PaymentMethodsResponseDataNew(
        val image: String,
        val header: String,
        val sub_heading: String,
        val description: String,
        val save_payments: List<PaymentMethodsResponseData>,
        val other_payments: List<OtherMethodLists>,
        val terms_action: String,
        val terms_meta: String
)

data class PaymentMethodsResponseData(
        val header: String,
        val icon_url: String,
        val action: String,
        val meta: String,
        val list: List<MethodLists>
)

data class OtherMethodLists(
        val header: String,
        val icon_url: String,
        val price_list: List<PriceList>
)

data class MethodLists(
        val icon_url: String,
        val number: String,
        val action: String,
        val meta: String,
        val price_list: List<PriceList>
)

data class PriceList(
        val item_price_master_id: Int,
        val text: String,
        val amount: String,
        val relate_type_id: String,
        val related_id: String,
        val payment_source_id: String,
        val payment_frequency: String,
        val service_payment_frequency_source_id: String,
        val custom_param: String,
        val user_payment_method_id: String
)