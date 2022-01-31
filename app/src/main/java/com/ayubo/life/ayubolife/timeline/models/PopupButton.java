package com.ayubo.life.ayubolife.timeline.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PopupButton implements Serializable {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("meta")
    @Expose
    private String meta;
    @SerializedName("action")
    @Expose
    private String action;
    private final static long serialVersionUID = -6983117787743601962L;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}