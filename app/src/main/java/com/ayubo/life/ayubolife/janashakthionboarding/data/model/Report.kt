package com.ayubo.life.ayubolife.janashakthionboarding.data.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.R.attr.name



class Report : Serializable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null


    override fun toString(): String {
        return name.toString()
    }
    companion object {
        private const val serialVersionUID = 5799875284792489474L
    }

}