package com.ayubo.life.ayubolife.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class WorkoutProgram implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("expert")
    @Expose
    private String expert;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("common_image")
    @Expose
    private String commonImage;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("meta")
    @Expose
    private String meta;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = 2572349833802980036L;


    public WorkoutProgram(String name, String image, String link, String title, String expert, String buttonText, String commonImage, String action, String meta) {
        this.name = name;
        this.image = image;
        this.link = link;
        this.title = title;
        this.expert = expert;
        this.buttonText = buttonText;
        this.commonImage = commonImage;
        this.action = action;
        this.meta = meta;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getCommonImage() {
        return commonImage;
    }

    public void setCommonImage(String commonImage) {
        this.commonImage = commonImage;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}