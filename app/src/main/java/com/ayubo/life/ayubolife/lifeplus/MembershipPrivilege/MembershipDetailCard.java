package com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege;

public class MembershipDetailCard {

    private String heading;
    private String text;
    private String action;
    private String meta;

    public MembershipDetailCard(String heading, String text, String action, String meta) {
        this.heading = heading;
        this.text = text;
        this.action = action;
        this.meta = meta;
    }

    public String getHeading() {
        return heading;
    }

    public String getText() {
        return text;
    }

    public String getAction() {
        return action;
    }

    public String getMeta() {
        return meta;
    }
}
