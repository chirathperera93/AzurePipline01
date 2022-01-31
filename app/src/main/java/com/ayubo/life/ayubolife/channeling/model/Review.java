package com.ayubo.life.ayubolife.channeling.model;

import java.io.Serializable;

public class Review implements Serializable {
    private String enable;
    private String meta;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
