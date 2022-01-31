package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 4/20/2018.
 */

public class NewHomeMenuOneObj {

    String title,image,button,button_text,link;


    public NewHomeMenuOneObj(String title, String image, String button, String button_text, String link) {
        this.title = title;
        this.image = image;
        this.button = button;
        this.button_text = button_text;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
