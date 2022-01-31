package com.ayubo.life.ayubolife.login.vm

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.login.model.CityDataInfo
import com.ayubo.life.ayubolife.login.model.NewUpdateCity
import com.ayubo.life.ayubolife.login.model.NewUserProfileData
import com.ayubo.life.ayubolife.login.model.UserProfileData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class UserProfileVM @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    lateinit var dataProfile: UserProfileData
    lateinit var newUserProfileData: NewUserProfileData
    lateinit var cityDataInfoArrayList: ArrayList<CityDataInfo>

    fun getProfile() =
        apiService.getProfile(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID).map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                //  dataList = it.body()?.data as ArrayList<OtherPaymentOptionsData>
                dataProfile = it.body()?.data as UserProfileData
                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }


    fun updateUserProfile(
        first_name: String,
        last_name: String,
        date_of_birth: String,
        email: String,
        gender: String,
        strNic: String
    ) = apiService.updateUserProfile(
        sharedPref.getAuthToken(),
        AppConfig.APP_BRANDING_ID,
        first_name,
        last_name,
        date_of_birth,
        email,
        gender,
        strNic
    ).map {
        if (it.isSuccessful) {
            //  dataList = it.body()?.data as ArrayList<OtherPaymentOptionsData>

            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

    fun getNewProfile() =
        apiService.getNewProfile(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID).map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                newUserProfileData = it.body()?.data as NewUserProfileData
                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }


    fun updateNewUserProfile(newUserProfileData: NewUserProfileData) =
        apiService.updateNewUserProfile(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            newUserProfileData
        )
            .map {
                if (it.isSuccessful) {
                    //  dataList = it.body()?.data as ArrayList<OtherPaymentOptionsData>

                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }


    fun getNewProfileById(userId: String) =
        apiService.getNewProfileById(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, userId)
            .map {

                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                    newUserProfileData = it.body()?.data as NewUserProfileData
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }


    fun updateNewUserProfileImage(profileData: NewUserProfileData) =
        apiService.updateUserImage(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            profileData
        )
            .map {
                if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                    newUserProfileData = it.body()?.data as NewUserProfileData
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

    fun getAllCities() =
        apiService.getAllCities(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID).map {

            if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
                cityDataInfoArrayList = it.body()?.data as ArrayList<CityDataInfo>
                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            Log.d("Erro.........", it.stackTrace.toString())
            State(false, MSG_FAILED_REQUEST)
        }


    fun updateCity(newUpdateCity: NewUpdateCity) =
        apiService.updateCity(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            newUpdateCity
        )
            .map {
                if (it.isSuccessful) {
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                Log.d("Erro.........", it.stackTrace.toString())
                State(false, MSG_FAILED_REQUEST)
            }

}