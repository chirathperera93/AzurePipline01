package com.ayubo.life.ayubolife.insurances.Classes;

public class RequestBankAccount {

    private String acc_holder_name;
    private String acc_holder_nickname;
    private String acc_number;
    private String acc_bank_name;
    private String acc_branch_name;

    public RequestBankAccount(String acc_holder_name, String acc_holder_nickname, String acc_number, String acc_bank_name, String acc_branch_name) {
        this.acc_holder_name = acc_holder_name;
        this.acc_holder_nickname = acc_holder_nickname;
        this.acc_number = acc_number;
        this.acc_bank_name = acc_bank_name;
        this.acc_branch_name = acc_branch_name;
    }

    public String getAcc_holder_name() {
        return acc_holder_name;
    }

    public void setAcc_holder_name(String acc_holder_name) {
        this.acc_holder_name = acc_holder_name;
    }

    public String getAcc_holder_nickname() {
        return acc_holder_nickname;
    }

    public void setAcc_holder_nickname(String acc_holder_nickname) {
        this.acc_holder_nickname = acc_holder_nickname;
    }

    public String getAcc_number() {
        return acc_number;
    }

    public void setAcc_number(String acc_number) {
        this.acc_number = acc_number;
    }

    public String getAcc_bank_name() {
        return acc_bank_name;
    }

    public void setAcc_bank_name(String acc_bank_name) {
        this.acc_bank_name = acc_bank_name;
    }

    public String getAcc_branch_name() {
        return acc_branch_name;
    }

    public void setAcc_branch_name(String acc_branch_name) {
        this.acc_branch_name = acc_branch_name;
    }
}
