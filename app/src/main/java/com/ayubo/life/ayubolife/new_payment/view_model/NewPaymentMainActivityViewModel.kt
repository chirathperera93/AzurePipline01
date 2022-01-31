package com.ayubo.life.ayubolife.new_payment.view_model

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.new_payment.model.*
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Chirath Perera on 2021-10-06.
 */
class NewPaymentMainActivityViewModel @Inject constructor(
        val apiService: ApiService,
        val sharedPref: SharedPreferences
) {
    lateinit var newMyCardsResponse: ArrayList<NewMyCardItem>
    lateinit var newPaymentDetailResponse: NewPaymentDetail
    lateinit var newPaymentMethodsResponse: JsonObject
    lateinit var chargePaymentResponse: ChargePaymentDetail


    fun createNewPayment(createPaymentItem: CreatePaymentItem) = apiService.createNewPayment(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            createPaymentItem
    )
            .map {


                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                    newPaymentDetailResponse = it.body()?.data as NewPaymentDetail;


                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

    fun getMyCards() = apiService.getMyCards(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID
    )
            .map {

                System.out.println("First step" + it)

                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                    newMyCardsResponse = it.body()?.data as ArrayList<NewMyCardItem>;


                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

    fun getNewPaymentDetails(paymentId: String) = apiService.getNewPaymentDetails(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            paymentId
    )
            .map {


                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                    newPaymentDetailResponse = it.body()?.data as NewPaymentDetail;


                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

    fun getNewPaymentMethods(paymentId: String) = apiService.getNewPaymentMethods(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            paymentId
    )
            .map {


                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                    newPaymentMethodsResponse = it.body()?.data as JsonObject;


                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

    fun chargePayment(chargePaymentItem: ChargePaymentItem) = apiService.doChargePayment(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            chargePaymentItem
    )
            .map {


                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                    chargePaymentResponse = it.body()?.data as ChargePaymentDetail;


                    State(true, MSG_SUCCESS)
                } else {


                    val r = it.message()

                    print(r)

                    State(false, r)
//                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }
}