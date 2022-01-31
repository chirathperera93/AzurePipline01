package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 1/29/2016.
 */
public class workoutEntity {

    int id;
    String uid;
    String type;
    int steps; int runs;
    float energy;
    float cal;
    int distance;
    String start_time;
    String end_time;
    String date;
    String workout_type;
    int update_type;
    String duration;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public workoutEntity(int id, String uid, String type, int steps, int runs, float energy, float cal, int distance, String start_time, String end_time, String date, int update_type, String duration, String status) {
        this.id = id;
        this.uid=uid;
        this.type = type;
        this.steps = steps;
        this.runs = runs;
        this.energy = energy;
        this.cal = cal;
        this.distance = distance;
        this.start_time = start_time;
        this.end_time = end_time;
        this.date = date;
        this.update_type = update_type;
        this.duration=duration;
        this.status=status;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWorkout_type() {
        return workout_type;
    }

    public void setWorkout_type(String workout_type) {
        this.workout_type = workout_type;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getCal() {
        return cal;
    }

    public void setCal(float cal) {
        this.cal = cal;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int update_type) {
        this.update_type = update_type;
    }
}
