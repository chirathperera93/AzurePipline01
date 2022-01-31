package com.ayubo.life.ayubolife.login;

public class CountryItem {

    private String name;
    private String dialCode;
    private String isoCode;
    private String flag;

    public CountryItem(String name, String dialCode, String isoCode, String flag) {
        this.name = name;
        this.dialCode = dialCode;
        this.isoCode = isoCode;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public String getDialCode() {
        return dialCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getFlag() {
        return flag;
    }
}
