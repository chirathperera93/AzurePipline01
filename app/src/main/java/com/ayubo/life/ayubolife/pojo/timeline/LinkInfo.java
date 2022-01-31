package com.ayubo.life.ayubolife.pojo.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkInfo implements Parcelable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("linkUrl")
    @Expose
    private String linkUrl;
    public final static Parcelable.Creator<LinkInfo> CREATOR = new Creator<LinkInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LinkInfo createFromParcel(Parcel in) {
            return new LinkInfo(in);
        }

        public LinkInfo[] newArray(int size) {
            return (new LinkInfo[size]);
        }

    }
            ;

    protected LinkInfo(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.linkUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LinkInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(thumbnailUrl);
        dest.writeValue(linkUrl);
    }

    public int describeContents() {
        return 0;
    }

}
