package com.ayubo.life.ayubolife.programs

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Expert
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.programs.data.model.Header
import com.ayubo.life.ayubolife.programs.data.model.Program
import com.ayubo.life.ayubolife.programs.data.model.Setting
import com.ayubo.life.ayubolife.programs.data.model.Tag
import javax.inject.Inject

class ProgramActivityVM @Inject constructor(val apiService: ApiService,
                                            val sharedPref: SharedPreferences){

    var list = ArrayList<Program>()
    var headerObj: Header? =null
    var settingObj: Setting? =null
    var serviceMeta: String? =null

    fun getProgramsHeader() = apiService.getProgramsHeader("2",sharedPref.getAuthToken()).map {
        if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
            list = it.body()?.data as ArrayList<Program>
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
      Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

    fun setUnsubscribeProgram(meta:String) = apiService.unsubscribeProgram(sharedPref.getAuthToken(), meta).map {
        if (it.isSuccessful && it.body() != null && it.body() != null) {
          //  serverRef=it.body()!!.data
            serviceMeta=meta
            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }



    fun getProgramsList(meta:String) = apiService.getProgramsList(sharedPref.getAuthToken(),meta).map {
        if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
            if(list.size>0){
                list.clear()
            }
            serviceMeta=meta
            list = it.body()?.data?.list as ArrayList<Program>

            var tagList = ArrayList<Tag>()
            tagList.add(Tag("",""))

           // it.body()?.data?.header.header_logo

            headerObj=it.body()?.data?.header

            settingObj=it.body()?.data?.setting

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........",it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }


    fun getEmptyObject(type:String){

    }
}