package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 12/18/2017.
 */

public class NotificationObj {

   String id, title,text, icon, datetime, link, read, type;

    public NotificationObj(String id, String title, String text, String icon, String datetime, String link, String read, String type) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.icon = icon;
        this.datetime = datetime;
        this.link = link;
        this.read = read;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
