package com.ayubo.life.ayubolife.pojo.timeline.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuickLink implements Parcelable
{

    @SerializedName("title1")
    @Expose
    private String title;

    @SerializedName("title2")
    @Expose
    private String title2;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("meta")
    @Expose
    private String meta;


    public final static Parcelable.Creator<QuickLink> CREATOR = new Creator<QuickLink>() {


        @SuppressWarnings({
                "unchecked"
        })
        public QuickLink createFromParcel(Parcel in) {
            return new QuickLink(in);
        }

        public QuickLink[] newArray(int size) {
            return (new QuickLink[size]);
        }

    }
            ;

    protected QuickLink(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.title2 = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.meta = ((String) in.readValue((String.class.getClassLoader())));

    }

    public QuickLink() {
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(title2);
        dest.writeValue(type);
        dest.writeValue(icon);
        dest.writeValue(meta);

    }

    public QuickLink(String title,String title2, String type, String icon,String meta) {
        this.title = title;
        this.title2 = title2;
        this.type = type;
        this.icon = icon;
        this.meta = meta;
    }

    public int describeContents() {
        return 0;
    }

}