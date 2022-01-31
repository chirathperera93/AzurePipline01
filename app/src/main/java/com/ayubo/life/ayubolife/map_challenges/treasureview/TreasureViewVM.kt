package com.ayubo.life.ayubolife.map_challenges.treasureview

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class TreasureViewVM @Inject constructor(val apiService: ApiService,
                                         val sharedPref: SharedPreferences){

    var response: MainTreasureResponse? =null

    fun getTreasureData(challenge_id:String,treasure_id:String) = apiService.getTreasureData(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,challenge_id,treasure_id).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
            response=it.body()

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }!!



}