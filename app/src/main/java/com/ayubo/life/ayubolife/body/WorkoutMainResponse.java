package com.ayubo.life.ayubolife.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WorkoutMainResponse implements Serializable

{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private WorkoutMain data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public WorkoutMain getData() {
        return data;
    }

    public void setData(WorkoutMain data) {
        this.data = data;
    }

}



 class WorkoutMain implements Serializable
{

    @SerializedName("programs")
    @Expose
    private List<WorkoutProgram> programs = null;
    private final static long serialVersionUID = 1155676957457264258L;

    public List<WorkoutProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<WorkoutProgram> programs) {
        this.programs = programs;
    }

}


