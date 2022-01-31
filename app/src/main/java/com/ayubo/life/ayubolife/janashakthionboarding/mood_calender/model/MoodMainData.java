package com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoodMainData implements Serializable
        {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("media_type")
@Expose
private String mediaType;
@SerializedName("media_url")
@Expose
private String mediaUrl;
private final static long serialVersionUID = 3238312911605197629L;

public Integer getId() {
        return id;
        }

public void setId(Integer id) {
        this.id = id;
        }

public String getMediaType() {
        return mediaType;
        }

public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
        }

public String getMediaUrl() {
        return mediaUrl;
        }

public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        }

        }