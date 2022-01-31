package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/13/2017.
 */

public class AppointmentRawObj {

    String startTime;
    String endTime;
    String slotType;
    String appointment_time;
    String appointment_id;
    String user_id;
    public AppointmentRawObj(String startTime, String endTime, String isAppointment, String appointment_time,String appointment_id,String user_id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotType = isAppointment;
        this.appointment_time = appointment_time;
        this.appointment_id=appointment_id;
        this.user_id=user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String isAppointment() {
        return slotType;
    }

    public void setAppointment(String appointment) {
        slotType = appointment;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }
}
