package com.ayubo.life.ayubolife.janashakthionboarding.experts

import android.content.SharedPreferences
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Expert
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class ExpertsVM @Inject constructor(val apiService: ApiService,
                                    val sharedPref: SharedPreferences) {
    var list = ArrayList<Expert>()

    fun getExpertList() = apiService.getExpertList(sharedPref.getAuthToken()).map {
        if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
            list = it.body()?.data?.experts as ArrayList<Expert>
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        State(false, MSG_FAILED_REQUEST)
    }

}