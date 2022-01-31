package com.ayubo.life.ayubolife.home_popup_menu;

/**
 * Created by appdev on 10/6/2017.
 */

public class DoctorObj {

    String id;
    String name;
    String spec;
    String image;

    public DoctorObj(String id, String name, String spec, String image) {
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
