package com.ayubo.life.ayubolife.payment.model

data class OtherPaymentOptionsResponse(val result : Int, val data : List<OtherPaymentOptionsData> )

data class OtherPaymentOptionsData ( val header : String, val list : List<OtherPaymentOptions>)

data class OtherPaymentOptions (
        val icon_url : String,
        val action : String,
        val meta : String,
        val price_list : List<PriceList>
)

