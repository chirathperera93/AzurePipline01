package com.ayubo.life.ayubolife.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayData implements Serializable
{

    @SerializedName("payment_link")
    @Expose
    private String paymentLink;
    private final static long serialVersionUID = -4151291017799483409L;

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

}

