package com.ayubo.life.ayubolife.home_popup_menu;

/**
 * Created by appdev on 10/6/2017.
 */

public class ChatObj {

    String id,name,link,speciality,image,unread,status;

    public ChatObj(String id, String name, String link, String speciality, String image, String unread, String status) {
        this.id = id;
        this.name = name;

        this.link = link;
        this.speciality = speciality;
        this.image = image;
        this.unread = unread;
        this.status = status;
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


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
