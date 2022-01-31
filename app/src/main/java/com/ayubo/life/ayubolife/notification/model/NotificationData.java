package com.ayubo.life.ayubolife.notification.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer notificationMasterId;
    @SerializedName("relate_type")
    @Expose
    private String type;
    @SerializedName("relate_id")
    @Expose
    private Object relateId;
    @SerializedName("text")
    @Expose
    private String title;
    @SerializedName("heading")
    @Expose
    private String header;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("is_read")
    @Expose
    private boolean isRead;
    @SerializedName("patient_id")
    @Expose
    private Object patientId;
    @SerializedName("community_id")
    @Expose
    private Object communityId;
    @SerializedName("icon_url")
    @Expose
    private String icon;
    @SerializedName("unread_count")
    @Expose
    private Integer unread_count;
    @SerializedName("isNewAPI")
    @Expose
    private Boolean isNewAPI;
    @SerializedName("session_id")
    @Expose
    private String session_id;
    @SerializedName("unread_info")
    @Expose
    private JsonObject unread_info;
    @SerializedName("status")
    @Expose
    private String status;


    private final static long serialVersionUID = -6045025439459553934L;

    public Integer getNotificationMasterId() {
        return notificationMasterId;
    }

    public void setNotificationMasterId(Integer notificationMasterId) {
        this.notificationMasterId = notificationMasterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getRelateId() {
        return relateId;
    }

    public void setRelateId(Object relateId) {
        this.relateId = relateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Object getPatientId() {
        return patientId;
    }

    public void setPatientId(Object patientId) {
        this.patientId = patientId;
    }

    public Object getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Object communityId) {
        this.communityId = communityId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Integer getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(Integer unread_count) {
        this.unread_count = unread_count;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Boolean getNewAPI() {
        return isNewAPI;
    }

    public void setNewAPI(Boolean newAPI) {
        isNewAPI = newAPI;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public JsonObject getUnread_info() {
        return unread_info;
    }

    public void setUnread_info(JsonObject unread_info) {
        this.unread_info = unread_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
