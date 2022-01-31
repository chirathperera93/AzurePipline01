package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 8/31/2017.
 */

public class ChallengeListObj {

    String id;
    ChallengeObj value;

    public ChallengeListObj(String id, ChallengeObj value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChallengeObj getValue() {
        return value;
    }

    public void setValue(ChallengeObj value) {
        this.value = value;
    }
}
