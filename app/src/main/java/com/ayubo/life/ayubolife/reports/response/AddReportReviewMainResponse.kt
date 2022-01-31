package com.ayubo.life.ayubolife.reports.response

import com.ayubo.life.ayubolife.payment.model.PaymentConfirmMainData

data class AddReportReviewMainResponse (
        val result : Int,
        val data : ReportsData
)

data class ReportsData (
        val members : List<Members>,
        val reports : ArrayList<Reports>
)


data class Reports (

        val id : Int,
        val hospital : String,
        val reportName : String,
        val illness : String,
        val doctor_name : String,
        val description : String,
        val reportDate : String,
        val reportType : String,
        val ocrnumber : String,
        val hos_uid : String,
        val table_id : String,
        val assigned_user_id : String,
        val testorder_id : String,
        val enc_id : String,
        val full_name : String,
        val read      : Int,
        val icon : String,
        val download_url : String

)

data class Members(
        val id : String,
        val uhid : String,
        val name : String,
        val relationship : String,
        val profile_picture_url : String,
        val downnotification_countload_url : String


)

