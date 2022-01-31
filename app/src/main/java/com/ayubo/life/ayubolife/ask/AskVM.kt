package com.ayubo.life.ayubolife.ask

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject


class AskVM @Inject constructor(val apiService: ApiService,
                                val sharedPref: SharedPreferences
) {

    var askDataList: ArrayList<AskCustomObject>? = null
    // var askDataList : ArrayList<AskData>?=null


    fun getAskData(method: String, rest_data: String) = apiService.getAskData(AppConfig.APP_BRANDING_ID, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {
            val mainResponse = it.body()
            val askList = mainResponse!!.data
            var specsDataList: ArrayList<String>? = ArrayList<String>()
            askDataList = ArrayList<AskCustomObject>()

            var askSingleCategoryDataList: ArrayList<AskCustomData>? = null


            for (c in 0 until askList.size) {
                val data = askList[c]
                if (specsDataList!!.size > 0) {
                    if (!specsDataList.contains(data.speciality)) {
                        specsDataList.add(data.speciality)
                    }
                } else {
                    specsDataList.add(data.speciality)

                }
            }


            for (n in 0 until specsDataList!!.size) {
                askSingleCategoryDataList = ArrayList<AskCustomData>()
                val speciality = specsDataList[n]
                // val spec= data.speciality

                for (i in 0 until askList.size) {
                    val data = askList[i]
                    if (askList[i].speciality == speciality) {
                        val consultant = AskCustomData(data.id, data.name, data.speciality, data.image, data.chatLink, data.unread, data.status)
                        askSingleCategoryDataList.add(consultant)
                    }
                }

                val singleCategoryConsultList = AskCustomObject(speciality, askSingleCategoryDataList)
                askDataList!!.add(singleCategoryConsultList)
            }


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