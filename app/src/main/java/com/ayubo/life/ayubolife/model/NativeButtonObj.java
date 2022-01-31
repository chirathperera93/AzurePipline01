package com.ayubo.life.ayubolife.model;

public class NativeButtonObj {

    private String label;
    private String action;
    private String type;
    private String status;
    private String meta;


    public NativeButtonObj(String label, String action, String type, String status, String meta) {
        this.label = label;
        this.action = action;
        this.type = type;
        this.status = status; this.meta = meta;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
