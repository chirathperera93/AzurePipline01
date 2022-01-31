package com.ayubo.life.ayubolife.reports.activity;

public class GetFieldByIdObj {

    String name;
    Object parameter;

    public GetFieldByIdObj(String name, Object parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public Object getParameter() {
        return parameter;
    }
}
