package com.ayubo.life.ayubolife.timeline.models;

public class TimelineObj {
    String type;
    String postId;
    String title;
    String sub_title;
    String button_text;
    String body;
    String gifUrl;
    String postThumbnailUrl;
    String postImageUrl;
    String videoUrl;
    String videoThumbnail;
    String timestamp;
    String isLiked;
    String likeCount;
    String likedUsersText;
    String likeUsersText;
    String commentCount;
    String interactionsEnabled;
    String isExternalLink;
    String redirectUrl;
    String linkInfo;
    String user;
    String gifThumbnail;

    public TimelineObj(String type,String postId, String title, String sub_title, String button_text, String body, String gifUrl, String postThumbnailUrl, String postImageUrl, String videoUrl, String videoThumbnail, String timestamp, String isLiked, String likeCount, String likedUsersText, String likeUsersText, String commentCount, String interactionsEnabled, String isExternalLink, String redirectUrl, String linkInfo, String user,String gifThumbnail) {
        this.type = type;
        this.postId = postId;
        this.title = title;
        this.sub_title = sub_title;
        this.button_text = button_text;
        this.body = body;
        this.gifUrl = gifUrl;
        this.postThumbnailUrl = postThumbnailUrl;
        this.postImageUrl = postImageUrl;
        this.videoUrl = videoUrl;
        this.videoThumbnail = videoThumbnail;
        this.timestamp = timestamp;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.likedUsersText = likedUsersText;
        this.likeUsersText = likeUsersText;
        this.commentCount = commentCount;
        this.interactionsEnabled = interactionsEnabled;
        this.isExternalLink = isExternalLink;
        this.redirectUrl = redirectUrl;
        this.linkInfo = linkInfo;
        this.user = user;
        this.gifThumbnail=gifThumbnail;
    }

    public String getGifThumbnail() {
        return gifThumbnail;
    }

    public void setGifThumbnail(String gifThumbnail) {
        this.gifThumbnail = gifThumbnail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikedUsersText() {
        return likedUsersText;
    }

    public void setLikedUsersText(String likedUsersText) {
        this.likedUsersText = likedUsersText;
    }

    public String getLikeUsersText() {
        return likeUsersText;
    }

    public void setLikeUsersText(String likeUsersText) {
        this.likeUsersText = likeUsersText;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getInteractionsEnabled() {
        return interactionsEnabled;
    }

    public void setInteractionsEnabled(String interactionsEnabled) {
        this.interactionsEnabled = interactionsEnabled;
    }

    public String getIsExternalLink() {
        return isExternalLink;
    }

    public void setIsExternalLink(String isExternalLink) {
        this.isExternalLink = isExternalLink;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
