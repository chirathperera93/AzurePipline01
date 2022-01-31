package com.ayubo.life.ayubolife.services_models;

/**
 * Created by appdev on 1/27/2017.
 */

public class HealthData {

    public String height;
    public String weight;
    public String waist_size;
    public String bmi;

    public HealthData(String height, String weight, String waist_size, String bmi) {
        this.height = height;
        this.weight = weight;
        this.waist_size = waist_size;
        this.bmi = bmi;
    }

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

    public String getWaist_size() {
        return waist_size;
    }

    public void setWaist_size(String waist_size) {
        this.waist_size = waist_size;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }
}
