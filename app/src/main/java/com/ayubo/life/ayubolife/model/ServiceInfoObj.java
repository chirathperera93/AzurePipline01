package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/14/2017.
 */

public class ServiceInfoObj {

    String id;
    String name;
    String minutes;
    String duration;
    String wipro_appointment_c;

    public ServiceInfoObj(String id, String name, String minutes, String duration, String wipro_appointment_c) {
        this.id = id;
        this.name = name;
        this.minutes = minutes;
        this.duration = duration;
        this.wipro_appointment_c = wipro_appointment_c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWipro_appointment_c() {
        return wipro_appointment_c;
    }

    public void setWipro_appointment_c(String wipro_appointment_c) {
        this.wipro_appointment_c = wipro_appointment_c;
    }
}
