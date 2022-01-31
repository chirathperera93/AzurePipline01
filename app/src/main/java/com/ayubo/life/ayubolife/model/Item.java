package com.ayubo.life.ayubolife.model;


import android.graphics.Bitmap;

/**
 *
 * @author manish.s
 *
 */

public class Item {
    Bitmap image;
    String title;
    String imagePath;
    String met;
    public Item(Bitmap image, String title, String imagePath, String met) {
        super();
        this.image = image;
        this.title = title;
        this.imagePath = imagePath;
        this.met = met;
    }

    public String getMet() {
        return met;
    }

    public void setMet(String met) {
        this.met = met;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
