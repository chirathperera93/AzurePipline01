package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/13/2017.
 */

public class AppointmentObj {

    String startTime;
    String endTime;
    String availability;;
    String service_type;
    String consultant;
    String appointment_id;
    String location;
    String editable;
    String overlap;
    String channel_ref;
    String assigned_user;

    public AppointmentObj(String startTime, String endTime, String availability, String service_type, String consultant, String appointment_id, String location, String editable, String overlap, String channel_ref,String assigned_user) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.availability = availability;
        this.service_type = service_type;
        this.consultant = consultant;
        this.appointment_id = appointment_id;
        this.location = location;
        this.editable = editable;
        this.overlap = overlap;
        this.channel_ref = channel_ref;
        this.assigned_user = assigned_user;
    }

    public String getAssigned_user() {
        return assigned_user;
    }

    public void setAssigned_user(String assigned_user) {
        this.assigned_user = assigned_user;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOverlap() {
        return overlap;
    }

    public void setOverlap(String overlap) {
        this.overlap = overlap;
    }

    public String getChannel_ref() {
        return channel_ref;
    }

    public void setChannel_ref(String channel_ref) {
        this.channel_ref = channel_ref;
    }
}
