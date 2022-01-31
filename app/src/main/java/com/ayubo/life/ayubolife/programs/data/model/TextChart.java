package com.ayubo.life.ayubolife.programs.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TextChart implements Serializable
{

    @SerializedName("test_id")
    @Expose
    private String testId;

    @SerializedName("parameter_name")
    @Expose
    private String parameter_name;


    @SerializedName("updatable")
    @Expose
    private Boolean updatable;
    @SerializedName("values")
    @Expose
    private List<String> values = null;
    @SerializedName("colors")
    @Expose
    private List<String> colors = null;
    @SerializedName("current")
    @Expose
    private String current;
    @SerializedName("target")
    @Expose
    private String target;
    private final static long serialVersionUID = -7258714042716226762L;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParameterName() {
        return parameter_name;
    }

    public void setParameterName(String parameter_name) {
        this.parameter_name = parameter_name;
    }
}
