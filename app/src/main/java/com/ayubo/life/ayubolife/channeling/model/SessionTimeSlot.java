package com.ayubo.life.ayubolife.channeling.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SessionTimeSlot implements Serializable {

    private Date date;
    private Date start;
    private Date end;
    private int duration_minutes;
    private List<Date> time_slots;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getDuration_minutes() {
        return duration_minutes;
    }

    public void setDuration_minutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public List<Date> getTime_slots() {
        return time_slots;
    }

    public void setTime_slots(List<Date> time_slots) {
        this.time_slots = time_slots;
    }
}
