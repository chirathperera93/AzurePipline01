package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 2/9/2018.
 */

public class TreasureObj {

    String lat,longi,steps,image,link, status;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TreasureObj(String lat, String longi, String steps, String image, String link, String status) {
        this.lat = lat;
        this.longi = longi;
        this.steps = steps;
        this.image = image;
        this.link = link;
        this.status = status;
    }
}
