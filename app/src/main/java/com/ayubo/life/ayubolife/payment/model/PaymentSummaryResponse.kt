package com.ayubo.life.ayubolife.payment.model


data class PaymentSummaryResponse(
        val result: Int,
        val data: PaymentSummaryData
)

data class PaymentSummaryData(
        val heading: String,
        val payment_success: Boolean,
        val action: String,
        val meta: String,
        val table: List<PaymentSummaryTable>
)

data class PaymentSummaryTable(
        val label: String,
        val value: String
)