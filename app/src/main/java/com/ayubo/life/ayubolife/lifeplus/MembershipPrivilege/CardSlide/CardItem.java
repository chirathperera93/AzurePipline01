package com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CardItem {

    private String mCardId;
    private String mBackgroundImage;
    private String mType;
    private String mTitle;
    private JsonArray mBenefits;
    private JsonObject mSupport;
    private JsonObject mTerms;
    private String mExpireDate;
    private String mFullName;
    private String mMainLogo;
    private String mSponsorLogo;

    public CardItem(
            String cardId,
            String backgroundImage,
            String title,
            String type,
            JsonArray benefits,
            JsonObject support,
            JsonObject terms,
            String expireDate,
            String fullName,
            String mainLogo,
            String sponsorLogo
    ) {
        mCardId = cardId;
        mBackgroundImage = backgroundImage;
        mTitle = title;
        mType = type;
        mBenefits = benefits;
        mSupport = support;
        mTerms = terms;
        mExpireDate = expireDate;
        mFullName = fullName;
        mMainLogo = mainLogo;
        mSponsorLogo = sponsorLogo;
    }

    public String getCardId() {
        return mCardId;
    }

    public String getBackgroundImage() {
        return mBackgroundImage;
    }

    public String getText() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public JsonArray getBenefits() {
        return mBenefits;
    }

    public JsonObject getSupport() {
        return mSupport;
    }

    public JsonObject getTerms() {
        return mTerms;
    }

    public String getExpireDate() {
        return mExpireDate;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getMainLogo() {
        return mMainLogo;
    }

    public String getSponsorLogo() {
        return mSponsorLogo;
    }
}
