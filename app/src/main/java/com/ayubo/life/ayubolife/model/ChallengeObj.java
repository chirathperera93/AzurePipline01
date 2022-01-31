package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 8/30/2017.
 */

public class ChallengeObj {

    String steps;
    String  challenge_name;
    String no_of_days;
    String date;
    String created_by;
    String type;

    public ChallengeObj(String steps, String challenge_name, String no_of_days, String date, String created_by,String type) {
        this.steps = steps;
        this.challenge_name = challenge_name;
        this.no_of_days = no_of_days;
        this.date = date;
        this.created_by = created_by;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
