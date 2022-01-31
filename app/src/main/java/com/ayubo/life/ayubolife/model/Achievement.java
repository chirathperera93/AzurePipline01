package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 2/16/2018.
 */

public class Achievement {

    String id,name,image,status,share_image,last_achive_date,count,description;

    public Achievement(String id, String name, String image, String status, String share_image, String last_achive_date, String count, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.share_image = share_image;
        this.last_achive_date = last_achive_date;
        this.count = count;
        this.description = description;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShare_image() {
        return share_image;
    }

    public void setShare_image(String share_image) {
        this.share_image = share_image;
    }

    public String getLast_achive_date() {
        return last_achive_date;
    }

    public void setLast_achive_date(String last_achive_date) {
        this.last_achive_date = last_achive_date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
