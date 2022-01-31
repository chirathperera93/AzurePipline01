package com.ayubo.life.ayubolife.retro_models;

/**
 * Created by appdev on 12/30/2017.
 */


import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class retro_mobilelogin_model implements Serializable
    {

        @SerializedName("result")
        @Expose
        private Integer result;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("hashToken")
        @Expose
        private String hashToken;
        @SerializedName("publicToken")
        @Expose
        private String publicToken;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("healthData")
        @Expose
        private Retro_heathData healthData;
        @SerializedName("code")
        @Expose
        private Integer code;
        private final static long serialVersionUID = 2806733256593418728L;

        public Integer getResult() {
            return result;
        }

        public void setResult(Integer result) {
            this.result = result;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
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

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
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

        public Retro_heathData getHealthData() {
            return healthData;
        }

        public void setHealthData(Retro_heathData healthData) {
            this.healthData = healthData;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

    }





