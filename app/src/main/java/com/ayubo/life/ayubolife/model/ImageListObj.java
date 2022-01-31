package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 10/18/2017.
 */

public class ImageListObj {

    String name,image,link1,link2,action,meta;

    public ImageListObj(String name, String image, String link1, String link2, String action, String meta) {
        this.name = name;
        this.image = image;
        this.link1 = link1;
        this.link2 = link2;  this.action = action;  this.meta = meta;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }
}
