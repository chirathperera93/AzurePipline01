package com.ayubo.life.ayubolife.login.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Chirath Perera on 2021-09-14.
 */
data class NewUserProfileMainResponse(
    @SerializedName("result") val result: Int,
    @SerializedName("data") val data: NewUserProfileData,
    @SerializedName("message") val message: String
)

data class NewUserProfileData(
    @SerializedName("id") val id: String = "",
    @SerializedName("user_name") val user_name: String = "",
    @SerializedName("first_name") val first_name: String = "",
    @SerializedName("last_name") val last_name: String = "",
    @SerializedName("date_of_birth") val date_of_birth: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("gender") val gender: Int = 0,
    @SerializedName("nic") val nic: String = "",
    @SerializedName("phone_mobile") val phone_mobile: String = "",
    @SerializedName("countrycode_c") val countrycode_c: String = "",
    @SerializedName("image_url") val image_url: String? = "",
    @SerializedName("city") val city: String? = "",
    @SerializedName("city_name") val city_name: String? = "",
    @SerializedName("corporate_email") val corporate_email: String? = "",
    @SerializedName("corporate_email_verification") val corporate_email_verification: Boolean? = false
)

data class NewUpdateCity(
    val city: String
)


