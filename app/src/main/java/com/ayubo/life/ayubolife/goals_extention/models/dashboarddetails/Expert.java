package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expert implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("profile_picture_link")
    @Expose
    private String profilePictureLink;

    @SerializedName("redirect_link")
    @Expose
    private String redirectLink;

    public final static Parcelable.Creator<Expert> CREATOR = new Creator<Expert>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Expert createFromParcel(Parcel in) {
            return new Expert(in);
        }

        public Expert[] newArray(int size) {
            return (new Expert[size]);
        }

    }
            ;

    protected Expert(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.fullName = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePictureLink = ((String) in.readValue((String.class.getClassLoader())));
        this.redirectLink = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Expert() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(fullName);
        dest.writeValue(profilePictureLink);
        dest.writeValue(redirectLink);

    }

    public int describeContents() {
        return 0;
    }

}
