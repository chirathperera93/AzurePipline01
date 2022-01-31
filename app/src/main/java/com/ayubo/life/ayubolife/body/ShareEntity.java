package com.ayubo.life.ayubolife.body;

/**
 * Created by appdev on 9/26/2017.
 */

public class ShareEntity {



    String id;
    String name;
    String community_type_c;
    String company_logo_c;
    String company_enrolled_c;
    String no_members;
    String logo_image;

    public ShareEntity(String id, String name, String community_type_c, String company_logo_c, String company_enrolled_c, String no_members, String logo_image) {
        this.id = id;
        this.name = name;
        this.community_type_c = community_type_c;
        this.company_logo_c = company_logo_c;
        this.company_enrolled_c = company_enrolled_c;
        this.no_members = no_members;
        this.logo_image = logo_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommunity_type_c() {
        return community_type_c;
    }

    public void setCommunity_type_c(String community_type_c) {
        this.community_type_c = community_type_c;
    }

    public String getCompany_logo_c() {
        return company_logo_c;
    }

    public void setCompany_logo_c(String company_logo_c) {
        this.company_logo_c = company_logo_c;
    }

    public String getCompany_enrolled_c() {
        return company_enrolled_c;
    }

    public void setCompany_enrolled_c(String company_enrolled_c) {
        this.company_enrolled_c = company_enrolled_c;
    }

    public String getNo_members() {
        return no_members;
    }

    public void setNo_members(String no_members) {
        this.no_members = no_members;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }
}
