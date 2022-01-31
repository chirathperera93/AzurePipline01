package com.ayubo.life.ayubolife.programs.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Entitlement implements Serializable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("used_count")
    @Expose
    private Integer usedCount;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("meta")
    @Expose
    private String meta;
    @SerializedName("unlimted")
    @Expose
    private Boolean unlimted;
    private final static long serialVersionUID = 4377001748510937130L;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Boolean getUnlimted() {
        return unlimted;
    }

    public void setUnlimted(Boolean unlimted) {
        this.unlimted = unlimted;
    }

}