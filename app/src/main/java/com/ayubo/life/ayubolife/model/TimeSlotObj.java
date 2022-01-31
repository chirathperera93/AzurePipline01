package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/6/2017.
 */

public class TimeSlotObj {

    String time;
    String name;
    String appintment;
    int hour;

    public TimeSlotObj(String time, String name, String appintment,int hour) {
        this.time = time;
        this.name = name;
        this.appintment = appintment;
        this.hour=hour;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppintment() {
        return appintment;
    }

    public void setAppintment(String appintment) {
        this.appintment = appintment;
    }
}
