


        package com.ayubo.life.ayubolife.pojo.timeline.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//f8f5f3
public class TopTile implements Parcelable
{

    @SerializedName("title")
    @Expose
    private String title;


    @SerializedName("sub_title")
    @Expose
    private String subTitle;


    @SerializedName("tile_status")
    @Expose
    private String titleStatus;

    @SerializedName("related_id")
    @Expose
    private String relatedId;


    @SerializedName("action_text")
    @Expose
    private String actionText;


    @SerializedName("bg_image")
    @Expose
    private String bgImage;


    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("icon")
    @Expose
    private String icon;


    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("meta")
    @Expose
    private String meta;

    @SerializedName("from_date")
    @Expose
    private String from_date;

    @SerializedName("end_date")
    @Expose
    private String to_date ;


    public final static Parcelable.Creator<TopTile> CREATOR = new Creator<TopTile>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TopTile createFromParcel(Parcel in) {
            return new TopTile(in);
        }

        public TopTile[] newArray(int size) {
            return (new TopTile[size]);
        }

    }
            ;

    protected TopTile(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.subTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.actionText = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.meta = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.from_date = ((String) in.readValue((String.class.getClassLoader())));
        this.to_date = ((String) in.readValue((String.class.getClassLoader())));
        this.titleStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.relatedId = ((String) in.readValue((String.class.getClassLoader())));

    }

    public TopTile(String title, String subTitle, String actionText, String image, String type, String meta, String icon, String bgImage,String from_date,String to_date,String titleStatus,String relatedId) {
        this.title = title;
        this.subTitle = subTitle;
        this.actionText = actionText;
        this.image = image;
        this.type = type;
        this.meta = meta;
        this.icon = icon;
        this.bgImage=bgImage;
        this.from_date=from_date;
        this.to_date=to_date;
        this.titleStatus=titleStatus;
        this.relatedId=relatedId;

    }

    public TopTile() {
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getTitleStatus() {
        return titleStatus;
    }

    public void setTitleStatus(String titleStatus) {
        this.titleStatus = titleStatus;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(subTitle);
        dest.writeValue(actionText);
        dest.writeValue(image);
        dest.writeValue(type);
        dest.writeValue(meta);
        dest.writeValue(from_date);
        dest.writeValue(to_date);
    }

    public int describeContents() {
        return 0;
    }

}