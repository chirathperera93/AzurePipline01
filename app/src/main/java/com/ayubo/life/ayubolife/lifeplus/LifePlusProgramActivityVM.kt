package com.ayubo.life.ayubolife.lifeplus

import android.content.SharedPreferences
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by Chirath Perera on 2021-09-06.
 */
class LifePlusProgramActivityVM @Inject constructor(
    var apiService: ApiService,
    val sharedPreferences: SharedPreferences
) {

    var discoverDataDetailJsonObject: JsonObject = JsonObject();

    fun getAyuboBannerData(): Flowable<State> =
        apiService.getAyuboDiscoverBannerData(
            AppConfig.APP_BRANDING_ID,
            sharedPreferences.getAuthToken()
        ).map {
            if (it.isSuccessful && it.body()?.data != null) {

                discoverDataDetailJsonObject = Gson().toJsonTree(it.body()?.data!!).asJsonObject;

                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            State(false, MSG_FAILED_REQUEST)
        }
}