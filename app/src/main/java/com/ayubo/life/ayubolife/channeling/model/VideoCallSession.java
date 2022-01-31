package com.ayubo.life.ayubolife.channeling.model;

import java.io.Serializable;
import java.util.Date;

public class VideoCallSession implements Serializable{

    private Date start;
    private Date end;
    private int duration_minutes;

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
}
