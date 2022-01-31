package com.ayubo.life.ayubolife.services_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by appdev on 1/27/2017.
 */

public class User {
    @SerializedName("userId")
    public String userId;
    @SerializedName("result")
    public int result;
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("date_of_birth")
    public String date_of_birth;
    @SerializedName("gender")
    public String gender;
    @SerializedName("email")
    public String email;
    @SerializedName("hashToken")
    public String hashToken;
    @SerializedName("publicToken")
    public String publicToken;
    @SerializedName("accessToken")
    public String accessToken;
    @SerializedName("healthData")
    public HealthData healthData;


    public User(String userId, int result, String first_name, String last_name, String date_of_birth, String gender, String email, String hashToken, String publicToken, String accessToken, HealthData healthData) {
        this.userId = userId;
        this.result = result;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.email = email;
        this.hashToken = hashToken;
        this.publicToken = publicToken;
        this.accessToken = accessToken;
        this.healthData = healthData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashToken() {
        return hashToken;
    }

    public void setHashToken(String hashToken) {
        this.hashToken = hashToken;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public HealthData getHealthData() {
        return healthData;
    }

    public void setHealthData(HealthData healthData) {
        this.healthData = healthData;
    }
}
