package com.ayubo.life.ayubolife.model;

public class DiamondObj {
    double roadPath_lat,roadPath_longitude;
    String steps,objimg,objlink, status,bubble_text,bubble_link;

    public DiamondObj(double roadPath_lat, double roadPath_longitude, String steps, String objimg, String objlink, String status, String bubble_text, String bubble_link) {
        this.roadPath_lat = roadPath_lat;
        this.roadPath_longitude = roadPath_longitude;
        this.steps = steps;
        this.objimg = objimg;
        this.objlink = objlink;
        this.status = status;
        this.bubble_text = bubble_text;
        this.bubble_link = bubble_link;
    }

    public double getRoadPath_lat() {
        return roadPath_lat;
    }

    public void setRoadPath_lat(double roadPath_lat) {
        this.roadPath_lat = roadPath_lat;
    }

    public double getRoadPath_longitude() {
        return roadPath_longitude;
    }

    public void setRoadPath_longitude(double roadPath_longitude) {
        this.roadPath_longitude = roadPath_longitude;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getObjimg() {
        return objimg;
    }

    public void setObjimg(String objimg) {
        this.objimg = objimg;
    }

    public String getObjlink() {
        return objlink;
    }

    public void setObjlink(String objlink) {
        this.objlink = objlink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBubble_text() {
        return bubble_text;
    }

    public void setBubble_text(String bubble_text) {
        this.bubble_text = bubble_text;
    }

    public String getBubble_link() {
        return bubble_link;
    }

    public void setBubble_link(String bubble_link) {
        this.bubble_link = bubble_link;
    }
}
