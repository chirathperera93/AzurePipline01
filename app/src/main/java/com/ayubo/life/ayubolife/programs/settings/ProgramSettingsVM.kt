package com.ayubo.life.ayubolife.programs.settings

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.PaymentConfirmMainData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject


class ProgramSettingsVM @Inject constructor(val apiService: ApiService,
                                            val sharedPref: SharedPreferences){

    lateinit var responseData : ProgramSettingsResponse



    fun setProgramPause(policy_user_master_id :String) = apiService.setProgramPause(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,policy_user_master_id).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
            responseData = it.body() as ProgramSettingsResponse

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun setProgramUnsubscribe(policy_user_master_id :String) = apiService.setProgramUnsubscribe(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,policy_user_master_id).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
            responseData = it.body() as ProgramSettingsResponse

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun setProgramSettingsRestart(policy_user_master_id :String) = apiService.setProgramSettingsRestart(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,policy_user_master_id).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
            responseData = it.body() as ProgramSettingsResponse

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun setProgramResume(policy_user_master_id :String) = apiService.setProgramResume(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,policy_user_master_id).map {
        if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
            responseData = it.body() as ProgramSettingsResponse

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}
