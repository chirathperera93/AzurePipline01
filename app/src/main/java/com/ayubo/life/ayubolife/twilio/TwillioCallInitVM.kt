package com.ayubo.life.ayubolife.twilio

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.PaymentMethodsResponseData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY
import javax.inject.Inject

class TwillioCallInitVM @Inject constructor(val apiService: ApiService,
                                            val sharedPref: SharedPreferences) {


    var twillioDataMainResponse: TwillioDataMainResponse? = null
    val apikeyAyuboApp = "4e395952655a68764b2f31354a4a4c2b70724c3079756c30682f486f45344d6749324d71394f5568376d6f3d"
    val url = MAIN_URL_LIVE_HAPPY + "custom/service/v7/rest.php"

    fun getTwillioData(rest_data: String) = apiService.getTwillioData(
            url,
            AppConfig.APP_BRANDING_ID,
            apikeyAyuboApp,
            "TwillioVideoCallInit",
            "JSON",
            "JSON",
            rest_data).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            twillioDataMainResponse = it.body()
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}
