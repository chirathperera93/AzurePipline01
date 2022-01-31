package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TableRow implements Serializable
{

    @SerializedName("uom")
    @Expose
    private String uom;

    @SerializedName("parameter_name")
    @Expose
    private String parameterName;
    @SerializedName("values")
    @Expose
    private List<Values> values = null;
    private final static long serialVersionUID = -3475078463763030577L;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}