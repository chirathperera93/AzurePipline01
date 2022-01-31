package com.ayubo.life.ayubolife.reports.getareview

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class GetAReviewVM @Inject constructor(val apiService: ApiService,
                                       val sharedPref: SharedPreferences
) {

    var askDataList: ArrayList<ReviewData>? = null

    fun getExpertsForReview(filterKey: String) = apiService.getExpertsForReview(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, filterKey).map {
        if (it.isSuccessful && it.body() != null) {
            val mainResponse = it.body()
            askDataList = mainResponse!!.data as ArrayList<ReviewData>

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun getExpertsForReviewNew(filterKey: String, doctor_id: String) = apiService.getExpertsForReviewNew(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, filterKey, doctor_id).map {
        if (it.isSuccessful && it.body() != null) {
            val mainResponse = it.body()
            askDataList = mainResponse!!.data as ArrayList<ReviewData>

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}