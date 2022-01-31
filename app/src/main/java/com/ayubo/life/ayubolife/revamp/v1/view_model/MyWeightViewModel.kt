package com.ayubo.life.ayubolife.revamp.v1.view_model

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.revamp.v1.model.WeightSummaryData
import javax.inject.Inject

class MyWeightViewModel @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    lateinit var weightSummaryData: WeightSummaryData

    fun getWeightSummary() = apiService.getWeightSummary(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID
    )
        .map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                weightSummaryData = it.body()?.data as WeightSummaryData

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