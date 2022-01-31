package com.ayubo.life.ayubolife.challenges

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject


class ChallangeVM @Inject constructor(val apiService: ApiService,
                                      val sharedPref: SharedPreferences
) {

    var askDataList: ArrayList<Any>? = null
    val url = "https://devo.ayubo.life/custom/service/v6/rest.php"
    fun getChallangeData(method: String, rest_data: String) = apiService.getChallangeData(AppConfig.APP_BRANDING_ID, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {
            val mainResponse = it.body()
            val askList = mainResponse!!.data


            //   sharedPref.saveTimelinePosts(timelinePosts)
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}