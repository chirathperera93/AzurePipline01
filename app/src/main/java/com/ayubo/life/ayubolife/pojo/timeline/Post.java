package com.ayubo.life.ayubolife.pojo.timeline;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable
{

    @SerializedName("postId")
    @Expose
    private Integer postId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("isLiked")
    @Expose
    private Boolean isLiked;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("likedUsersText")
    @Expose
    private String likedUsersText;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("linkInfo")
    @Expose
    private LinkInfo linkInfo;
    @SerializedName("gifThumbnail")
    @Expose
    private String gifThumbnail;
    @SerializedName("gifUrl")
    @Expose
    private String gifUrl;
    @SerializedName("postThumbnailUrl")
    @Expose
    private String postThumbnailUrl;
    @SerializedName("postImageUrl")
    @Expose
    private String postImageUrl;

    @SerializedName("videoThumbnail")
    @Expose
    private String videoThumbnail;

    @SerializedName("videoUrl")
    @Expose
    private String videoUrl;

    @SerializedName("isExternalLink")
    @Expose
    private boolean isExternalLink;

    @SerializedName("interactionsEnabled")
    @Expose
    private boolean interactionsEnabled;

    @SerializedName("redirectUrl")
    @Expose
    private String redirectUrl;

    @SerializedName("sub_title")
    @Expose
    private String subTitle;

    @SerializedName("button_text")
    @Expose
    private String buttonText;

    @SerializedName("redirect_type")
    @Expose
    private String redirect_type;

    @SerializedName("url")
    @Expose
    private String meta_url;


    public final static Parcelable.Creator<Post> CREATOR = new Creator<Post>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return (new Post[size]);
        }

    };
    public boolean hasUserInteraction(){

        return  interactionsEnabled;

    }

    public boolean hasButtonText(){
        boolean hasTitle=false;
        if((this.buttonText==null) || (this.buttonText.equals(""))){

        }
        else{
            hasTitle=true;
        }
        return  hasTitle;
    }

   public boolean hasTitle(){
       boolean hasTitle=false;
       if((this.title==null) || (this.title.equals(""))){
         }
       else{
           hasTitle=true;
       }
       return  hasTitle;
   }
    public boolean hasBody(){
        boolean hasTitle=false;
        if((this.body==null) || (this.body.equals(""))){
        }
        else{
            hasTitle=true;
        }
        return  hasTitle;
    }

    public boolean hasImage(){
        boolean postThumbnailUrl;
        if((this.postThumbnailUrl==null) || (this.postThumbnailUrl.equals(""))){
            postThumbnailUrl=false;
        }
        else{
            postThumbnailUrl=true;
        }
        return  postThumbnailUrl;
    }
    public String getOverview() {
        return likedUsersText;
    }

    protected Post(Parcel in) {
        this.postId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.body = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isLiked = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.likeCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.likedUsersText = ((String) in.readValue((String.class.getClassLoader())));
        this.commentCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.linkInfo = ((LinkInfo) in.readValue((LinkInfo.class.getClassLoader())));
        this.gifThumbnail = ((String) in.readValue((String.class.getClassLoader())));
        this.gifUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.postThumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.postImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.videoThumbnail = ((String) in.readValue((String.class.getClassLoader())));
        this.videoUrl = ((String) in.readValue((String.class.getClassLoader())));

        this.isExternalLink = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.interactionsEnabled = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.redirectUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.subTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.buttonText = ((String) in.readValue((String.class.getClassLoader())));
        this.redirect_type = ((String) in.readValue((String.class.getClassLoader())));
        this.meta_url = ((String) in.readValue((String.class.getClassLoader())));

    }

    public void updateLikeText(){

        boolean youType1Found=false; boolean youType2Found=false; boolean youType3Found=false; boolean hasYouText=false;

        if(likedUsersText.contains("You and ")){
            youType3Found=true;  hasYouText=true;
        }
        if(likedUsersText.contains("You, ")){
            youType2Found=true;  hasYouText=true;
        }
        if(likedUsersText.contains("You")){
            youType1Found=true;  hasYouText=true;
        }
        if(hasYouText){

            if (!isLiked) {

                if(youType3Found){
                    likedUsersText.substring(8);
                }else if(youType2Found) {
                    likedUsersText.substring(5);
                }else if(youType1Found) {
                    likedUsersText.substring(3);
                }
            }

        }else{
            if (isLiked) {
                if(likeCount == 1){
                    likedUsersText = "You";
                }else if(likeCount == 2){
                    likedUsersText = "You and "+likedUsersText;
                }else{
                    likedUsersText = "You, "+ likedUsersText;
                }
            }

        }
    }

    public Post() {
    }

    public Post(Integer postId, String title, String body, String type, Integer timestamp, Boolean isLiked, Integer likeCount, String likedUsersText, Integer commentCount, User user, LinkInfo linkInfo, String gifThumbnail, String gifUrl, String postThumbnailUrl, String postImageUrl, String videoThumbnail, String videoUrl, boolean isExternalLink, boolean interactionsEnabled, String redirectUrl,String subTitle,String buttonText,String redirect_type,String meta_url) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.type = type;
        this.timestamp = timestamp;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.likedUsersText = likedUsersText;
        this.commentCount = commentCount;
        this.user = user;
        this.linkInfo = linkInfo;
        this.gifThumbnail = gifThumbnail;
        this.gifUrl = gifUrl;
        this.postThumbnailUrl = postThumbnailUrl;
        this.postImageUrl = postImageUrl;
        this.videoThumbnail = videoThumbnail;
        this.videoUrl = videoUrl;
        this.isExternalLink = isExternalLink;
        this.interactionsEnabled = interactionsEnabled;
        this.redirectUrl = redirectUrl;
        this.subTitle = redirectUrl;
        this.buttonText=buttonText;
        this.redirect_type=redirect_type;
        this.meta_url=meta_url;


    }

    public String getRedirect_type() {
        return redirect_type;
    }

    public void setRedirect_type(String redirect_type) {
        this.redirect_type = redirect_type;
    }

    public String getMeta_url() {
        return meta_url;
    }

    public void setMeta_url(String meta_url) {
        this.meta_url = meta_url;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public boolean isExternalLink() {
        return isExternalLink;
    }

    public void setExternalLink(boolean externalLink) {
        isExternalLink = externalLink;
    }

    public boolean isInteractionsEnabled() {
        return interactionsEnabled;
    }

    public void setInteractionsEnabled(boolean interactionsEnabled) {
        this.interactionsEnabled = interactionsEnabled; }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikedUsersText() {
        return likedUsersText;
    }

    public void setLikedUsersText(String likedUsersText) {
        this.likedUsersText = likedUsersText;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LinkInfo getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    public String getGifThumbnail() {
        return gifThumbnail;
    }

    public void setGifThumbnail(String gifThumbnail) {
        this.gifThumbnail = gifThumbnail;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getPostThumbnailUrl() {
        return postThumbnailUrl;
    }

    public void setPostThumbnailUrl(String postThumbnailUrl) {
        this.postThumbnailUrl = postThumbnailUrl;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeValue(title);
        dest.writeValue(body);
        dest.writeValue(type);
        dest.writeValue(timestamp);
        dest.writeValue(isLiked);
        dest.writeValue(likeCount);
        dest.writeValue(likedUsersText);
        dest.writeValue(commentCount);
        dest.writeValue(user);
        dest.writeValue(linkInfo);
        dest.writeValue(gifThumbnail);
        dest.writeValue(gifUrl);
        dest.writeValue(postThumbnailUrl);
        dest.writeValue(postImageUrl);
        dest.writeValue(videoThumbnail);
        dest.writeValue(videoUrl);
    }

    public int describeContents() {
        return 0;
    }

}
