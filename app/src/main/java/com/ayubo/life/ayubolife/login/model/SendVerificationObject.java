package com.ayubo.life.ayubolife.login.model;

/**
 * Created by Chirath Perera on 2021-10-29.
 */
public class SendVerificationObject {

    String to;
    String message;
    String user_id;

    public SendVerificationObject(String to, String message, String user_id) {
        this.to = to;
        this.message = message;
        this.user_id = user_id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}