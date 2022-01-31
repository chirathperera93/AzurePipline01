package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TableHeader implements Serializable
{

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("download_text")
    @Expose
    private String downloadText;
    @SerializedName("download_link")
    @Expose
    private String downloadLink;
    private final static long serialVersionUID = 7749566052749114803L;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadText() {
        return downloadText;
    }

    public void setDownloadText(String downloadText) {
        this.downloadText = downloadText;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

}
