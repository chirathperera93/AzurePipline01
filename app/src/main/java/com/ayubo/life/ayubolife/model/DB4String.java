package com.ayubo.life.ayubolife.model;

import java.io.Serializable;

/**
 * Created by appdev on 3/3/2017.
 */


public class DB4String implements Serializable {

    String id;
    String name;
    String specility;
    String docname;

    public DB4String(String id, String name, String specility, String docname) {
        this.id = id;
        this.name = name;
        this.specility = specility;
        this.docname = docname;
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

    public String getSpecility() {
        return specility;
    }

    public void setSpecility(String specility) {
        this.specility = specility;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }
}