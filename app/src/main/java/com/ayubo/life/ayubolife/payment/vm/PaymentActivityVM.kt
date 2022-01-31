package com.ayubo.life.ayubolife.payment.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.PaymentMethodsResponseDataNew
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class PaymentActivityVM @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    lateinit var mainResponse: PaymentMethodsResponseDataNew

    fun getPaymentMethods(item_master_id: String) = apiService.getPaymentMethods(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID,
        item_master_id
    )
        .map {

            System.out.println("First step" + it)

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                System.out.println("Second step" + it.body())
                mainResponse = it.body()?.data as PaymentMethodsResponseDataNew;
                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }

}