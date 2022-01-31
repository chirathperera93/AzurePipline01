package com.ayubo.life.ayubolife.janashakthionboarding.data.model


import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportTypesMainResponse : Serializable {

    @SerializedName("reports")
    @Expose
    var reports: List<Report>? = null

    companion object {
        private const val serialVersionUID = -1565847945994886204L
    }

}