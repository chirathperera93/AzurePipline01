package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 1/26/2018.
 */

public class CardsEntity {

    String type,text,image,heading,heading2,link,status,value,min_val,max_val,desc,title,subtitle,action,meta,related_id;
  //  String type,text,image,heading,heading2,link,status,value,min_val,max_val,desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeading2() {
        return heading2;
    }

    public void setHeading2(String heading2) {
        this.heading2 = heading2;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMin_val() {
        return min_val;
    }

    public void setMin_val(String min_val) {
        this.min_val = min_val;
    }

    public String getMax_val() {
        return max_val;
    }

    public void setMax_val(String max_val) {
        this.max_val = max_val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public CardsEntity(String type, String text, String image, String heading, String heading2, String link, String status, String value, String min_val, String max_val, String desc, String title,
                       String subtitle, String action, String meta, String related_id) {
        this.type = type;
        this.text = text;
        this.image = image;
        this.heading = heading;
        this.heading2 = heading2;
        this.link = link;
        this.status = status;
        this.value = value;
        this.min_val = min_val;
        this.max_val = max_val;
        this.desc = desc;

        this.title = title;
        this.subtitle = subtitle;
        this.action = action;
        this.meta = meta;
        this.related_id=related_id;
    }
}
