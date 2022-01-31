package com.ayubo.life.ayubolife.map_challenges.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.map_challenges.model.InfoButton
import com.ayubo.life.ayubolife.map_challenges.model.Leaderboards
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class LeaderBoardVM @Inject constructor(val apiService: ApiService,
                                        val sharedPref: SharedPreferences) {

    lateinit var header_image: String
    lateinit var header_text: String
    lateinit var subheading_text: String

    lateinit var infoButton: InfoButton
    lateinit var leaderboards: ArrayList<Leaderboards>


    fun getLeaderBoard(challangeID: String) = apiService.getLeaderBoard(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, challangeID).map {
        if ((it.isSuccessful && it.body()!!.data != null)) {

            header_image = it.body()!!.data.header_image as String
            header_text = it.body()!!.data.header_text as String
            subheading_text = it.body()!!.data.subheading_text as String

            infoButton = it.body()!!.data.info_button as InfoButton
            leaderboards = it.body()!!.data.leaderboards as ArrayList<Leaderboards>



            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}