package com.ayubo.life.ayubolife.payment.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class EnterDialogPinNumberActivityVM @Inject constructor(val apiService: ApiService,
                                                         val sharedPref: SharedPreferences){

    var serverRef: String? =null

       fun sendDialogNumberToGetOTP(mobileNumber:String,type:String) = apiService.sendDialogNumberToGetOTP(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,mobileNumber,type).map {
           if((it.isSuccessful && it.body()!= null) && (it.body()!!.result==0)){
               val  res=it.body()!!.data
               serverRef=  res.reference_code
               State(true, MSG_SUCCESS)
           } else {
               State(false, MSG_FAILED_REQUEST)
           }
       }.onErrorReturn {
           Log.d("Erro.........",it.stackTrace.toString())
           State(false, MSG_FAILED_REQUEST)
       }!!

    fun verifyDialogOTP(serverRef:String,otp:String) = apiService.verifyDialogOTP(sharedPref.getAuthToken(),otp,serverRef).map {
        if(it.isSuccessful){
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }!!

}