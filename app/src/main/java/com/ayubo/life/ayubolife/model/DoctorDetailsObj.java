package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 10/6/2017.
 */

public class DoctorDetailsObj {

    String id;
    String name;
    String fee;
    String link;
    String next_avaialble;

    public DoctorDetailsObj(String id, String name, String fee, String link, String next_avaialble) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.link = link;
        this.next_avaialble = next_avaialble;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNext_avaialble() {
        return next_avaialble;
    }

    public void setNext_avaialble(String next_avaialble) {
        this.next_avaialble = next_avaialble;
    }
}
