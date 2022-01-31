package com.ayubo.life.ayubolife.walk_and_win;

public class WalkWinJoinChallengeObj {
    private String challenge_id;
    private Long chj_startDateTime;
    private Long chj_endDateTime;
    private String user_id;
    private String chj_timezone;


    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    public Long getChj_startDateTime() {
        return chj_startDateTime;
    }

    public void setChj_startDateTime(Long chj_startDateTime) {
        this.chj_startDateTime = chj_startDateTime;
    }

    public Long getChj_endDateTime() {
        return chj_endDateTime;
    }

    public void setChj_endDateTime(Long chj_endDateTime) {
        this.chj_endDateTime = chj_endDateTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChj_timezone() {
        return chj_timezone;
    }

    public void setChj_timezone(String chj_timezone) {
        this.chj_timezone = chj_timezone;
    }
}
