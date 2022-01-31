package com.ayubo.life.ayubolife.payment.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.PaymentSettingsResponse
import com.ayubo.life.ayubolife.payment.model.PaymentSettingsResponseData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class PaymentSettingsVM @Inject constructor(val apiService: ApiService,
                                            val sharedPref: SharedPreferences){


    lateinit var mainResponse : ArrayList<PaymentSettingsResponseData>

    fun sendPaymentOptions() = apiService.sendPaymentOptions(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){

            mainResponse = it.body()?.data as ArrayList<PaymentSettingsResponseData>

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }
}