package com.ayubo.life.ayubolife.discover_search

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramMainResponse
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class DiscoverSearchVM @Inject constructor(val apiService: ApiService,
                                           val sharedPref: SharedPreferences
) {

    var mainResponse: List<DiscoverSearchData>? = null

    fun getSearchResults(type: String, keyword: String) = apiService.getSearchResults(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, keyword).map {
        if (it.isSuccessful && it.body() != null) {
            val mainRespons = it.body()

            mainResponse = mainRespons!!.data


            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}