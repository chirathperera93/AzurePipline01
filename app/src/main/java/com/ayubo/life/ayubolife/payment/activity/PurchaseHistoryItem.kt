package com.ayubo.life.ayubolife.payment.activity

class PurchaseHistoryItem(
        val id: String,
        val appId: String,
        val userId: String,
        val title: String,
        val description: String,
        val icon: String,
        val status: String,
        val currency: String,
        val cost: Int,
        val created_datetime: Long,
        val updated_datetime: Long

) {
}