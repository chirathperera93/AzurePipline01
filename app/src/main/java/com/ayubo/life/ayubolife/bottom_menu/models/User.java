package com.ayubo.life.ayubolife.bottom_menu.models;

public class User{
    private String imageUrl;
    private String name;
    private String occupation;

    // getters and setters
    // ...


    public User(String imageUrl, String name, String occupation) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.occupation = occupation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}