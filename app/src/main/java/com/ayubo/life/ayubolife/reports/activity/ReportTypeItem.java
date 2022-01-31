package com.ayubo.life.ayubolife.reports.activity;

public class ReportTypeItem {

    String name;
    Integer Seq;
    String PanicMax;
    String PanicMin;
    String UOM;
    String HasUoM;
    Integer ParameterType;
    String ResultType;
    Double IsNumericType;
    String id;
    String value;

    public ReportTypeItem(String name, Integer seq, String PanicMax, String PanicMin, String UOM, String hasUoM, Integer parameterType, String resultType, Double isNumericType, String id, String value) {
        this.name = name;
        this.Seq = seq;
        this.PanicMax = PanicMax;
        this.PanicMin = PanicMin;
        this.UOM = UOM;
        this.HasUoM = hasUoM;
        this.ParameterType = parameterType;
        this.ResultType = resultType;
        this.IsNumericType = isNumericType;
        this.id = id;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getPanicMax() {
        return PanicMax;
    }

    public String getPanicMin() {
        return PanicMin;
    }

    public String getUOM() {
        return UOM;
    }

    public Integer getSeq() {
        return Seq;
    }

    public String getHasUoM() {
        return HasUoM;
    }

    public Integer getParameterType() {
        return ParameterType;
    }

    public String getResultType() {
        return ResultType;
    }

    public Double getIsNumericType() {
        return IsNumericType;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeq(Integer seq) {
        Seq = seq;
    }

    public void setPanicMax(String panicMax) {
        PanicMax = panicMax;
    }

    public void setPanicMin(String panicMin) {
        PanicMin = panicMin;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public void setHasUoM(String hasUoM) {
        HasUoM = hasUoM;
    }

    public void setParameterType(Integer parameterType) {
        ParameterType = parameterType;
    }

    public void setResultType(String resultType) {
        ResultType = resultType;
    }

    public void setIsNumericType(Double isNumericType) {
        IsNumericType = isNumericType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
