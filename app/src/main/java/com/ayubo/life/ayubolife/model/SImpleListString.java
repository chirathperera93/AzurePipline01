package com.ayubo.life.ayubolife.model;

import android.graphics.Bitmap;

/**
 * Created by appdev on 8/22/2017.
 */

public class SImpleListString {

    Bitmap bitmap;
    String name;

    public SImpleListString(Bitmap bitmap, String name) {
        this.bitmap = bitmap;
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
