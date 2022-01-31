package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FamilyMemberData implements Serializable
{

        @SerializedName("patient_id")
        @Expose
        private String patientId;
        @SerializedName("uhid")
        @Expose
        private String uhid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("relationship")
        @Expose
        private String relationship;
        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;
        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;
        private final static long serialVersionUID = -4992138703905663442L;

        public String getPatientId() {
                return patientId;
        }

        public void setPatientId(String patientId) {
                this.patientId = patientId;
        }

        public String getUhid() {
                return uhid;
        }

        public void setUhid(String uhid) {
                this.uhid = uhid;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getRelationship() {
                return relationship;
        }

        public void setRelationship(String relationship) {
                this.relationship = relationship;
        }

        public String getDateOfBirth() {
                return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
        }

        public String getProfilePicture() {
                return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
                this.profilePicture = profilePicture;
        }

}