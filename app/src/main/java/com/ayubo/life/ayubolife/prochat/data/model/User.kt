package com.ayubo.life.ayubolife.prochat.data.model


import androidx.annotation.NonNull
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ayubo.mobileemr.data.model.enums.Gender
import com.google.gson.annotations.SerializedName


data class User(
    @PrimaryKey @NonNull var userId: String?,
    var result: Int?,
    @SerializedName("first_name") var firstName: String?,
    @SerializedName("last_name") var lastName: String?,
    @SerializedName("date_of_birth") var dateOfBirth: String?,
    var gender: Gender?,
    var email: String?,
    @SerializedName("profile_pic") var profilePic: String?,
    var hashToken: String?,
    var publicToken: String?,
    var accessToken: String?,
    @Ignore var healthData: HealthData?,
    @SerializedName("slmc_no") var slmcNo: String?,
    var verified: Int?,
    @SerializedName("lumen_token") var lumenToken: String?,
    @SerializedName("default_home_mode") var defaultHomeMode: String?,
    var code: Int?,
    var phone: String?
) {
    constructor() : this(
        null, null, null, null, null,
        null, null, null, null, null, null,
        null, null, null, null, null, null, null
    )
}