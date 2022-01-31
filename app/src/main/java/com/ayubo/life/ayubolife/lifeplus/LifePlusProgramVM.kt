package com.ayubo.life.ayubolife.lifeplus

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.login.model.MembershipCardResponse
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class LifePlusProgramVM @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    var mainResponse: LifePlusProgramMainResponse? = null
    var membershipCardResponse: MembershipCardResponse? = null


    fun getDiscoverSearchedResults(id: String = "", name: String = "", type: String = "") =
        apiService.getDiscoverSearchedResults(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            id,
            name,
            type
        ).map {
            if (it.isSuccessful && it.body() != null) {
                val mainRespons = it.body()

                mainResponse = mainRespons


                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }


    fun getLifePlusPrograms() =
        apiService.getLifePlusPrograms(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID).map {
            if (it.isSuccessful && it.body() != null) {
                val mainRespons = it.body()

                mainResponse = mainRespons


                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }


}