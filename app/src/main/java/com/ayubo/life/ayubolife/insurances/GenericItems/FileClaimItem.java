package com.ayubo.life.ayubolife.insurances.GenericItems;

import com.google.gson.JsonObject;

public class FileClaimItem {

    private String heading;

    private JsonObject jsonObject;

    public FileClaimItem(String heading, JsonObject jsonObject) {
        this.heading = heading;
        this.jsonObject = jsonObject;
    }

    public String getHeading() {
        return heading;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }
}
