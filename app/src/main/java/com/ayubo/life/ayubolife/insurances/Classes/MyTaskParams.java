package com.ayubo.life.ayubolife.insurances.Classes;

public class MyTaskParams {
    private String imageUrl;
    private int count;
    private String mediaName;
    private String mediaFolder;
    private String mediaType;
    private String mediaNote;

    public MyTaskParams(String imageUrl, int count, String mediaName, String mediaFolder, String mediaType, String mediaNote) {
        this.imageUrl = imageUrl;
        this.count = count;
        this.mediaName = mediaName;
        this.mediaFolder = mediaFolder;
        this.mediaType = mediaType;
        this.mediaNote = mediaNote;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaFolder() {
        return mediaFolder;
    }

    public void setMediaFolder(String mediaFolder) {
        this.mediaFolder = mediaFolder;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaNote() {
        return mediaNote;
    }

    public void setMediaNote(String mediaNote) {
        this.mediaNote = mediaNote;
    }
}
