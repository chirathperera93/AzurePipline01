package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 12/11/2017.
 */

public class MenuObj {

    String image,link,lable;

    public MenuObj(String image, String link, String lable) {
        this.image = image;
        this.link = link;
        this.lable = lable;
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

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
}
