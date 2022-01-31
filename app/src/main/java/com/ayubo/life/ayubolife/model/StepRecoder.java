package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 1/12/2016.
 */
public class StepRecoder {


    int id;
    float last_minit_steps;
    float metVlaues;
    String timestamp;

    // constructors
    public StepRecoder() {
    }

    public StepRecoder(int  last_minit_steps, float metVlaues, String timestamp) {
        this.last_minit_steps = last_minit_steps;
        this.metVlaues = metVlaues;
        this.timestamp = timestamp;
    }
    public StepRecoder(int id, float  last_minit_steps, float metVlaues, String timestamp) {
        this.id = id;
        this.last_minit_steps = last_minit_steps;
        this.metVlaues = metVlaues;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLast_minit_steps() {
        return last_minit_steps;
    }

    public void setLast_minit_steps(int last_minit_steps) {
        this.last_minit_steps = last_minit_steps;
    }

    public float getMetVlaues() {
        return metVlaues;
    }

    public void setMetVlaues(float metVlaues) {
        this.metVlaues = metVlaues;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}