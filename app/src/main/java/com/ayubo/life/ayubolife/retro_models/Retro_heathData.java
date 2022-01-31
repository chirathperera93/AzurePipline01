package com.ayubo.life.ayubolife.retro_models;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by appdev on 12/30/2017.
 */



    class Retro_heathData implements Serializable
    {

        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("waist_size")
        @Expose
        private String waistSize;
        @SerializedName("bmi")
        @Expose
        private String bmi;
        private final static long serialVersionUID = 8842439979300320685L;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getWaistSize() {
            return waistSize;
        }

        public void setWaistSize(String waistSize) {
            this.waistSize = waistSize;
        }

        public String getBmi() {
            return bmi;
        }

        public void setBmi(String bmi) {
            this.bmi = bmi;
        }

    }


