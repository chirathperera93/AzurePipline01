package com.ayubo.life.ayubolife.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotiCountMainData  implements Serializable
{

    @SerializedName("unread_count")
    @Expose
    private Integer unreadCount;
    private final static long serialVersionUID = -5048426197653364318L;

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

}
