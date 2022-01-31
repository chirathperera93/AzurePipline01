package com.ayubo.life.ayubolife.lifeplus;

/**
 * Created by Chirath Perera on 2021-08-03.
 */
public class SliderData {
    private String image_url;
    private String action;
    private String meta;

    public SliderData(String image_url, String action, String meta) {
        this.image_url = image_url;
        this.action = action;
        this.meta = meta;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
}