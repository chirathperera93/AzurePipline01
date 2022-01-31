package com.ayubo.life.ayubolife.pojo.timeline.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {




    @SerializedName("show")
    @Expose
    private Boolean show;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("meta")
    @Expose
    private String meta;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public final static Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        public Banner[] newArray(int size) {
            return (new Banner[size]);
        }

    }
            ;

    protected Banner(Parcel in) {
        this.show = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.heading = ((String) in.readValue((String.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.buttonText = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.action = ((String) in.readValue((String.class.getClassLoader())));
        this.meta = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Banner() {
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(show);
        dest.writeValue(heading);
        dest.writeValue(text);
        dest.writeValue(buttonText);
        dest.writeValue(image);
        dest.writeValue(action);
        dest.writeValue(meta);
    }

    public int describeContents() {
        return 0;
    }

}