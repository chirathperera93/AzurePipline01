package com.ayubo.life.ayubolife.timeline.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PopupMainData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("button")
    @Expose
    private PopupButton button;
    private final static long serialVersionUID = -2740588971135089127L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public PopupButton getButton() {
        return button;
    }

    public void setButton(PopupButton button) {
        this.button = button;
    }

}
