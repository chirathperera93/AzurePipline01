package com.ayubo.life.ayubolife.revamp.v1.view_model

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.revamp.v1.model.CampaignData
import com.ayubo.life.ayubolife.revamp.v1.model.JoinCampaignRequestBody
import javax.inject.Inject

class CampaignJoinViewModel @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    lateinit var campaignData: CampaignData

    fun getCampaignDetails(campaignId: String) = apiService.getCampaignDetails(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID,
        campaignId
    )
        .map {
            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                campaignData = it.body()?.data as CampaignData


                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }

    fun joinCampaign(joinCampaignRequestBody: JoinCampaignRequestBody) = apiService.joinCampaign(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID,
        joinCampaignRequestBody
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