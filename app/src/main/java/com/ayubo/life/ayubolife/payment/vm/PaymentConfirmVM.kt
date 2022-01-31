package com.ayubo.life.ayubolife.payment.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.*
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import okhttp3.RequestBody
import javax.inject.Inject


class PaymentConfirmVM @Inject constructor(val apiService: ApiService,
                                           val sharedPref: SharedPreferences) {

    lateinit var dataObj: PaymentConfirmMainDataNew
    lateinit var apiPayObj: ApiPAayMainData
    lateinit var submitPinObj: SubmitPinResponseData
    lateinit var paymentSummaryObj: PaymentSummaryResponse


    fun getPaymentConfirmation(
            item_price_master_id: Int,
            text: String,
            amount: String,
            relate_type_id: String,
            related_id: String,
            payment_source_id: String,
            payment_frequency: String,
            payment_frequency_id: String,
            custom_param: String,
            user_payment_method_id: String
    ) = apiService.getPaymentConfirmation(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,
            item_price_master_id,
            text,
            amount,
            relate_type_id,
            related_id,
            payment_source_id,
            payment_frequency,
            payment_frequency_id,
            custom_param,
            user_payment_method_id).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            dataObj = it.body()?.data as PaymentConfirmMainDataNew
            State(true, MSG_SUCCESS)
        } else {
            Log.d("Erro.........", it.errorBody().toString())

            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun makePaymentRequest(urll: String, paymentParams: HashMap<String, String>) = apiService.makeAPayment(urll, sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, paymentParams).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            apiPayObj = it.body()!!.data

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

    fun submitPin(payment_id: Int, pin: Int) = apiService.submitPin(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, payment_id, pin).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            submitPinObj = it.body()?.data as SubmitPinResponseData
            State(true, MSG_SUCCESS)
        } else {
            Log.d("Erro.........", it.message())
            Log.d("Erro.........", it.errorBody().toString())

            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

    fun getPaymentSummary(payment_id: Int) = apiService.getPaymentSummary(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, payment_id).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            paymentSummaryObj = it.body() as PaymentSummaryResponse
            State(true, MSG_SUCCESS)
        } else {
            Log.d("Erro.........", it.message())
            Log.d("Erro.........", it.errorBody().toString())

            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}
