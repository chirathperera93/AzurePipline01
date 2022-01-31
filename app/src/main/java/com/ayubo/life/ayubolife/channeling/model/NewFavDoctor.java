package com.ayubo.life.ayubolife.channeling.model;

import java.io.Serializable;

public class NewFavDoctor implements Serializable {

    private String doctor_code,doctor_name,specialization_id,specialization,doc_image;




    public String getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(String doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String doc_image) {
        this.doc_image = doc_image;
    }
}
