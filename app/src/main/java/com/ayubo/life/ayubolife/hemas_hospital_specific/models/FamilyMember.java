package com.ayubo.life.ayubolife.hemas_hospital_specific.models;

/**
 * Created by appdev on 3/22/2018.
 */


public class FamilyMember {
    public FamilyMember(String id, String name, String relationship, String assigned_user_id, String user_pic, String uhid) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.assigned_user_id = assigned_user_id;
        this.user_pic = user_pic;
        this.uhid = uhid;
    }

    String id,name,relationship,assigned_user_id,user_pic,uhid;

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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAssigned_user_id() {
        return assigned_user_id;
    }

    public void setAssigned_user_id(String assigned_user_id) {
        this.assigned_user_id = assigned_user_id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }
}
