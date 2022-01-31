package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendProgram implements Parcelable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("redirect_link")
    @Expose
    private String redirectLink;
    public final static Parcelable.Creator<RecommendProgram> CREATOR = new Creator<RecommendProgram>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RecommendProgram createFromParcel(Parcel in) {
            return new RecommendProgram(in);
        }

        public RecommendProgram[] newArray(int size) {
            return (new RecommendProgram[size]);
        }

    }
            ;

    protected RecommendProgram(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.imageLink = ((String) in.readValue((String.class.getClassLoader())));
        this.redirectLink = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RecommendProgram() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(imageLink);
        dest.writeValue(redirectLink);
    }

    public int describeContents() {
        return 0;
    }

}
