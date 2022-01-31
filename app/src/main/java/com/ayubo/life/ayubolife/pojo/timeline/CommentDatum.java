package com.ayubo.life.ayubolife.pojo.timeline;


        import android.os.Parcel;
                import android.os.Parcelable;
                import android.os.Parcelable.Creator;
                import com.google.gson.annotations.Expose;
                import com.google.gson.annotations.SerializedName;

public class CommentDatum implements Parcelable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("commentId")
    @Expose
    private String commentId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("pictureUrl")
    @Expose
    private Object pictureUrl;
    public final static Parcelable.Creator<CommentDatum> CREATOR = new Creator<CommentDatum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CommentDatum createFromParcel(Parcel in) {
            return new CommentDatum(in);
        }

        public CommentDatum[] newArray(int size) {
            return (new CommentDatum[size]);
        }

    };

    public CommentDatum(User user, String commentId, String timestamp, String body, Object pictureUrl) {
        this.user = user;
        this.commentId = commentId;
        this.timestamp = timestamp;
        this.body = body;
        this.pictureUrl = pictureUrl;
    }

    protected CommentDatum(Parcel in) {
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.commentId = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((String) in.readValue((String.class.getClassLoader())));
        this.body = ((String) in.readValue((String.class.getClassLoader())));
        this.pictureUrl = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public CommentDatum() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(Object pictureUrl) {
        this.pictureUrl = pictureUrl;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        dest.writeValue(commentId);
        dest.writeValue(timestamp);
        dest.writeValue(body);
        dest.writeValue(pictureUrl);
    }

    public int describeContents() {
        return 0;
    }

}