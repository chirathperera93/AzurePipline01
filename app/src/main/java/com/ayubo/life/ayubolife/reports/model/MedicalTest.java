package com.ayubo.life.ayubolife.reports.model;

public class MedicalTest {

    String id;
    String Name;

    String colesterol;
    String ldl;
    String hdl;
    String tringle;
    String ratio;

    public MedicalTest(String id, String name, String colesterol, String ldl, String hdl, String tringle, String ratio) {
        this.id = id;
        Name = name;
        this.colesterol = colesterol;
        this.ldl = ldl;
        this.hdl = hdl;
        this.tringle = tringle;
        this.ratio = ratio;
    }

    public String getColesterol() {
        return colesterol;
    }

    public void setColesterol(String colesterol) {
        this.colesterol = colesterol;
    }

    public String getLdl() {
        return ldl;
    }

    public void setLdl(String ldl) {
        this.ldl = ldl;
    }

    public String getHdl() {
        return hdl;
    }

    public void setHdl(String hdl) {
        this.hdl = hdl;
    }

    public String getTringle() {
        return tringle;
    }

    public void setTringle(String tringle) {
        this.tringle = tringle;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
