package com.ayubo.life.ayubolife.post.model;



        import java.io.Serializable;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ConciergeMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 4337210667737417184L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}