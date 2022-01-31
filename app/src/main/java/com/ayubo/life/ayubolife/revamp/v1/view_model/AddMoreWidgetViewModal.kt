package com.ayubo.life.ayubolife.revamp.v1.view_model

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.revamp.v1.model.PostWidgetData
import com.ayubo.life.ayubolife.revamp.v1.model.WidgetData
import javax.inject.Inject

class AddMoreWidgetViewModal @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    lateinit var widgetData: WidgetData
    lateinit var postWidgetData: PostWidgetData

    fun getWidgets() = apiService.getWidgets(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID
    )
        .map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                widgetData = it.body()?.data as WidgetData


                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            it.message?.let { it1 -> Log.d("Erro Message.........", it1) }
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }

    fun updateWidgetStatus(widgetId: Int, activatedStatus: String) = apiService.updateWidgetStatus(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID,
        widgetId,
        activatedStatus
    )
        .map {


            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {

                postWidgetData = it.body()?.data as PostWidgetData


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