package com.ayubo.life.ayubolife.janashakthionboarding.questionnaire

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.*
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.MultiQuestionSubmit
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model.MoodMainData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.common.getCurrentUser
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.utility.Ram
import javax.inject.Inject

class JanashakthiQuestionnaireVM @Inject constructor(val apiService: ApiService,
                                                   val sharedPref: SharedPreferences) {
    var list = ArrayList<QuestionDetails>()
    var dataObj :QuestionResponse?=null

    var moodObj : MoodMainData?=null
var submitResponse:MultiQuestionSubmit?=null

    var settingsObj : Settings?=null
    //var dataObj :QuestionResponse?=null

    fun getJanashakthiQuestionnaire() = apiService.getJanashakthiQuestionnaire(sharedPref.getAuthToken())
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
                   /// list = it.body()?.data as ArrayList<QuestionDetails>
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }


    fun getSeaShellsQuestionnaire(group_id: String) = apiService.getSeaShellsQuestionnaire(sharedPref.getAuthToken(), group_id)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
                    dataObj=  it.body()?.data as QuestionResponse

                    settingsObj=  dataObj?.settings
                    list= dataObj?.questions as ArrayList<QuestionDetails>

                    list.forEachIndexed { index, e ->
                        e.position=index+1
                    }
//
                    Ram.setQuestionList(list)
                    Ram.setQuestionSetting(settingsObj)

                    Log.d("TEXT","....................")
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.stackTrace
             //   Log.d("Error...",it.stackTrace.)
                State(false, MSG_FAILED_REQUEST)
            }

    fun getMoodCalenderData(mood_type:String) = apiService.getMoodCalenderData(sharedPref.getAuthToken(),mood_type)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {

                    moodObj=  it.body()?.data as MoodMainData
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }

    fun setMoodResponse(id:String,feedback:String) = apiService.setMoodResponse(sharedPref.getAuthToken(),id,feedback)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!= null) {

                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }
    fun submitAnswersMultiOptions(answers:List<JanashakthiMultiOptionAnswer>) = apiService.submitAnswersMultiOptions(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,answers)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.result == 0) {

                  val mainData=  it.body()
                    submitResponse=  mainData!!.data

                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                it.printStackTrace()
                State(false, MSG_FAILED_REQUEST)
            }


    fun submitAnswers(answers:List<JanashakthiAnswer>) = apiService.submitAnswers(sharedPref.getAuthToken(),answers)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.result == 0) {
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }

    fun getCurrentUser() = sharedPref.getCurrentUser()
}