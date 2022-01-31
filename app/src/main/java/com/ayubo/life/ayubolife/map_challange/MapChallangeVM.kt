package com.ayubo.life.ayubolife.map_challange

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.challenges.ChData
import com.ayubo.life.ayubolife.challenges.ChallanheDataMainResponse
import com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID

import com.ayubo.life.ayubolife.map_challange.model.LocationData
import com.ayubo.life.ayubolife.map_challange.model.apChallangeStepsMainResponse
import com.ayubo.life.ayubolife.map_challange.model.roadJSONMainData
import com.ayubo.life.ayubolife.payment.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.payment.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService

import com.google.gson.Gson


import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import javax.inject.Inject

class MapChallangeVM @Inject constructor(val apiService: ApiService,
                                         val sharedPref: SharedPreferences) {
    var roadsArry: JSONArray? = null

    //   var mainResponse: apChallangeStepsMainResponse?=null
    var sreverResponse = 500
    var roadJsonMainData: roadJSONMainData? = null
    var mainResponseObj: ChData? = null
    var mainResponseAdventureChallenge: String = ""

    fun getChallangeData(method: String, rest_data: String) = apiService.getChallangeData(APP_BRANDING_ID, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {

            if (it.body()!!.result == 0) {
                sreverResponse = 0
                val data = it.body()
                mainResponseObj = data!!.data
            }
            if (it.body()!!.result == 23) {
                sreverResponse = 23
            }
            State(true, MSG_SUCCESS)
        } else {
            State(false, it.message())
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.localizedMessage.toString())

        State(false, it.localizedMessage.toString())
    }


    fun getAdventureChallengeRoutes(method: String, rest_data: String) = apiService.getAdventureChallengeRoutes(APP_BRANDING_ID, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {

            if (it.body()!!.result == 0) {
                sreverResponse = 0
                val data = it.body()
                mainResponseAdventureChallenge = data!!.json



                val dataStringArray = it.body()
                var obj: JSONObject? = null


                try {
                    obj = JSONObject(dataStringArray!!.json)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                try {
                    roadsArry = obj!!.getJSONArray("route")

                } catch (e: Exception) {
                    e.printStackTrace()
                }








            }
            if (it.body()!!.result == 23) {
                sreverResponse = 23
            }
            State(true, MSG_SUCCESS)
        } else {
            State(false, it.message())
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.localizedMessage.toString())

        State(false, it.localizedMessage.toString())
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)


    fun getRoadJSON(method: String, rest_data: String) = apiService.getRoadJSON(APP_BRANDING_ID, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {

            val dataStringArray = it.body()
            var obj: JSONObject? = null


            try {
                obj = JSONObject(dataStringArray!!.data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            try {
                roadsArry = obj!!.getJSONArray("route")

            } catch (e: Exception) {
                e.printStackTrace()
            }

//             try{
//                 writeJSONtoFile("$challangeID.json",roadsArry.toString())
//             } catch (e: Exception) {
//                 e.printStackTrace()
//             }

            //  val messageType = object : TypeToken<List<LocationData>>() {}.type
            //    Gson().fromJson<List<LocationData>>(dataStringArray, messageType)
            //val dataArray = Gson().fromString<List<LocationData>>(dataStringArray, messageType)
            //  val mPosts: List<LocationData> = Gson().fromJson(dataStringArray)
            //     val turns: List<LocationData> = Gson().fromJson(dataStringArray.toString())


            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

//    fun getRoadJSON(method:String,rest_data:String) = apiService.getRoadJSON(APP_BRANDING_ID,method,"JSON","JSON",rest_data).map {
//        if (it.isSuccessful && it.body() != null) {
//
//            val dataStringArray= it.body()
//            var obj: JSONObject? = null
//
//
//            try {
//                 obj = JSONObject(dataStringArray!!.data)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//            try {
//                roadsArry = obj!!.getJSONArray("route")
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
////             try{
////                 writeJSONtoFile("$challangeID.json",roadsArry.toString())
////             } catch (e: Exception) {
////                 e.printStackTrace()
////             }
//
//            //  val messageType = object : TypeToken<List<LocationData>>() {}.type
//        //    Gson().fromJson<List<LocationData>>(dataStringArray, messageType)
//            //val dataArray = Gson().fromString<List<LocationData>>(dataStringArray, messageType)
//          //  val mPosts: List<LocationData> = Gson().fromJson(dataStringArray)
//       //     val turns: List<LocationData> = Gson().fromJson(dataStringArray.toString())
//
//
//            State(true, MSG_SUCCESS)
//        } else {
//            State(false, MSG_FAILED_REQUEST)
//        }
//    }.onErrorReturn {
//        Log.d("Erro.........",it.stackTrace.toString())
//        State(false, MSG_FAILED_REQUEST)
//    }
//    private fun writeJSONtoFile(fileName:String,strData:String) {
//        try {
//            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//                it.write(strData.toByteArray())
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }


}