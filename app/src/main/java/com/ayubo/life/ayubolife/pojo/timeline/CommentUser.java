package com.ayubo.life.ayubolife.pojo.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class CommentUser implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    public final static Parcelable.Creator<CommentUser> CREATOR = new Creator<CommentUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CommentUser createFromParcel(Parcel in) {
            return new CommentUser(in);
        }

        public CommentUser[] newArray(int size) {
            return (new CommentUser[size]);
        }

    }
            ;

    protected CommentUser(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePicture = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CommentUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(profilePicture);
    }

    public int describeContents() {
        return 0;
    }

}