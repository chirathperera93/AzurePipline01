package com.ayubo.life.ayubolife.revamp.v1.view_model

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.revamp.v1.model.DoRedeemRewardBody
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointRewardData
import javax.inject.Inject

class RedeemPointsViewModel @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {
    lateinit var redeemPointRewardData: RedeemPointRewardData

    fun getAllRewards() = apiService.getAllRewards(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID
    )
        .map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                redeemPointRewardData = it.body()?.data as RedeemPointRewardData

                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }

    fun doRedeemRewards(doRedeemRewardBody: DoRedeemRewardBody) =
        apiService.doRedeemRewards(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            doRedeemRewardBody
        )
            .map {

                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

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