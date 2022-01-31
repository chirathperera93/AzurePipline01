package com.ayubo.life.ayubolife.login.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class UserProfileMainResponse(
    @SerializedName("result") val result: Int,
    @SerializedName("data") val data: UserProfileData
)


data class UserProfileData(

    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("date_of_birth") val date_of_birth: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("nic") val nic: String
)

data class MembershipCardResponse(

    @SerializedName("result") val result: Int,
    @SerializedName("data") val data: ArrayList<Any>
)


data class GetAllCitiesDataResponse(
    val result: Int,
    val data: ArrayList<CityDataInfo>,
    val message: String = ""
)

data class CityDataInfo(
    val city: String = "",
    val city_ascii: String = "",
    val lat: Float,
    val lng: Float,
    val country: String = "",
    val iso2: String = "",
    val iso3: String = "",
    val admin_name: String = "",
    val capital: String = "",
    val population: Int,
    val id: String = ""
)