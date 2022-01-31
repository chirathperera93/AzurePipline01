package com.ayubo.life.ayubolife.insurances.Classes;

public class RequestRemoveFileClaimImage {
    private String URL;
    private String MediaName;
    private String MediaFolder;

    public RequestRemoveFileClaimImage(String URL, String mediaName, String mediaFolder) {
        this.URL = URL;
        MediaName = mediaName;
        MediaFolder = mediaFolder;
    }

    public String getURL() {
        return URL;
    }

    public String getMediaName() {
        return MediaName;
    }

    public String getMediaFolder() {
        return MediaFolder;
    }
}
