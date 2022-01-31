package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 3/10/2016.
 */
public class profileimageEn {

    String imagePath;
    String imageUri;
    public profileimageEn(String imagePath, String imageUri){
        imagePath=imagePath;
        imageUri=imageUri;
    }
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
