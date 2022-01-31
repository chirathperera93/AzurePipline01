package com.ayubo.life.ayubolife.pojo.doctor_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by appdev on 4/26/2018.
 */

public class Expert implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date_entered")
    @Expose
    private String dateEntered;
    @SerializedName("date_modified")
    @Expose
    private String dateModified;
    @SerializedName("modified_user_id")
    @Expose
    private String modifiedUserId;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("assigned_user_id")
    @Expose
    private String assignedUserId;
    @SerializedName("salutation")
    @Expose
    private String salutation;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("department")
    @Expose
    private Object department;
    @SerializedName("do_not_call")
    @Expose
    private String doNotCall;
    @SerializedName("phone_home")
    @Expose
    private Object phoneHome;
    @SerializedName("phone_mobile")
    @Expose
    private String phoneMobile;
    @SerializedName("phone_work")
    @Expose
    private Object phoneWork;
    @SerializedName("phone_other")
    @Expose
    private Object phoneOther;
    @SerializedName("phone_fax")
    @Expose
    private Object phoneFax;
    @SerializedName("primary_address_street")
    @Expose
    private Object primaryAddressStreet;
    @SerializedName("primary_address_city")
    @Expose
    private Object primaryAddressCity;
    @SerializedName("primary_address_state")
    @Expose
    private Object primaryAddressState;
    @SerializedName("primary_address_postalcode")
    @Expose
    private Object primaryAddressPostalcode;
    @SerializedName("primary_address_country")
    @Expose
    private Object primaryAddressCountry;
    @SerializedName("alt_address_street")
    @Expose
    private Object altAddressStreet;
    @SerializedName("alt_address_city")
    @Expose
    private Object altAddressCity;
    @SerializedName("alt_address_state")
    @Expose
    private Object altAddressState;
    @SerializedName("alt_address_postalcode")
    @Expose
    private Object altAddressPostalcode;
    @SerializedName("alt_address_country")
    @Expose
    private Object altAddressCountry;
    @SerializedName("assistant")
    @Expose
    private Object assistant;
    @SerializedName("assistant_phone")
    @Expose
    private Object assistantPhone;
    @SerializedName("lead_source")
    @Expose
    private Object leadSource;
    @SerializedName("reports_to_id")
    @Expose
    private String reportsToId;
    @SerializedName("birthdate")
    @Expose
    private Object birthdate;
    @SerializedName("campaign_id")
    @Expose
    private String campaignId;
    @SerializedName("joomla_account_id")
    @Expose
    private Object joomlaAccountId;
    @SerializedName("portal_account_disabled")
    @Expose
    private String portalAccountDisabled;
    @SerializedName("portal_user_type")
    @Expose
    private String portalUserType;
    @SerializedName("id_c")
    @Expose
    private String idC;
    @SerializedName("specialization_c")
    @Expose
    private String specializationC;
    @SerializedName("experttype_c")
    @Expose
    private String experttypeC;
    @SerializedName("chat_availability_c")
    @Expose
    private String chatAvailabilityC;
    @SerializedName("available_for_package_c")
    @Expose
    private String availableForPackageC;
    @SerializedName("slmc_no_c")
    @Expose
    private String slmcNoC;
    @SerializedName("qualification_and_destinatio_c")
    @Expose
    private String qualificationAndDestinatioC;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("online")
    @Expose
    private String online;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;
    public final static Parcelable.Creator<Expert> CREATOR = new Creator<Expert>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Expert createFromParcel(Parcel in) {
            return new Expert(in);
        }

        public Expert[] newArray(int size) {
            return (new Expert[size]);
        }

    }
            ;

    protected Expert(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.dateEntered = ((String) in.readValue((String.class.getClassLoader())));
        this.dateModified = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.createdBy = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((Object) in.readValue((Object.class.getClassLoader())));
        this.deleted = ((String) in.readValue((String.class.getClassLoader())));
        this.assignedUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.salutation = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((Object) in.readValue((Object.class.getClassLoader())));
        this.department = ((Object) in.readValue((Object.class.getClassLoader())));
        this.doNotCall = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneHome = ((Object) in.readValue((Object.class.getClassLoader())));
        this.phoneMobile = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneWork = ((Object) in.readValue((Object.class.getClassLoader())));
        this.phoneOther = ((Object) in.readValue((Object.class.getClassLoader())));
        this.phoneFax = ((Object) in.readValue((Object.class.getClassLoader())));
        this.primaryAddressStreet = ((Object) in.readValue((Object.class.getClassLoader())));
        this.primaryAddressCity = ((Object) in.readValue((Object.class.getClassLoader())));
        this.primaryAddressState = ((Object) in.readValue((Object.class.getClassLoader())));
        this.primaryAddressPostalcode = ((Object) in.readValue((Object.class.getClassLoader())));
        this.primaryAddressCountry = ((Object) in.readValue((Object.class.getClassLoader())));
        this.altAddressStreet = ((Object) in.readValue((Object.class.getClassLoader())));
        this.altAddressCity = ((Object) in.readValue((Object.class.getClassLoader())));
        this.altAddressState = ((Object) in.readValue((Object.class.getClassLoader())));
        this.altAddressPostalcode = ((Object) in.readValue((Object.class.getClassLoader())));
        this.altAddressCountry = ((Object) in.readValue((Object.class.getClassLoader())));
        this.assistant = ((Object) in.readValue((Object.class.getClassLoader())));
        this.assistantPhone = ((Object) in.readValue((Object.class.getClassLoader())));
        this.leadSource = ((Object) in.readValue((Object.class.getClassLoader())));
        this.reportsToId = ((String) in.readValue((String.class.getClassLoader())));
        this.birthdate = ((Object) in.readValue((Object.class.getClassLoader())));
        this.campaignId = ((String) in.readValue((String.class.getClassLoader())));
        this.joomlaAccountId = ((Object) in.readValue((Object.class.getClassLoader())));
        this.portalAccountDisabled = ((String) in.readValue((String.class.getClassLoader())));
        this.portalUserType = ((String) in.readValue((String.class.getClassLoader())));
        this.idC = ((String) in.readValue((String.class.getClassLoader())));
        this.specializationC = ((String) in.readValue((String.class.getClassLoader())));
        this.experttypeC = ((String) in.readValue((String.class.getClassLoader())));
        this.chatAvailabilityC = ((String) in.readValue((String.class.getClassLoader())));
        this.availableForPackageC = ((String) in.readValue((String.class.getClassLoader())));
        this.slmcNoC = ((String) in.readValue((String.class.getClassLoader())));
        this.qualificationAndDestinatioC = ((String) in.readValue((String.class.getClassLoader())));
        this.picture = ((String) in.readValue((String.class.getClassLoader())));
        this.online = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.locations, (Location.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Expert() {
    }

    /**
     *
     * @param dateModified
     * @param campaignId
     * @param altAddressCountry
     * @param leadSource
     * @param assignedUserId
     * @param primaryAddressCity
     * @param primaryAddressStreet
     * @param assistant
     * @param description
     * @param altAddressCity
     * @param specializationC
     * @param dateEntered
     * @param lastName
     * @param altAddressState
     * @param doNotCall
     * @param deleted
     * @param picture
     * @param altAddressStreet
     * @param phoneMobile
     * @param createdBy
     * @param phoneHome
     * @param phoneFax
     * @param portalAccountDisabled
     * @param availableForPackageC
     * @param department
     * @param experttypeC
     * @param reportsToId
     * @param slmcNoC
     * @param online
     * @param id
     * @param title
     * @param locations
     * @param idC
     * @param qualificationAndDestinatioC
     * @param chatAvailabilityC
     * @param birthdate
     * @param primaryAddressPostalcode
     * @param firstName
     * @param joomlaAccountId
     * @param phoneOther
     * @param modifiedUserId
     * @param phoneWork
     * @param photo
     * @param altAddressPostalcode
     * @param primaryAddressState
     * @param portalUserType
     * @param salutation
     * @param assistantPhone
     * @param primaryAddressCountry
     */
    public Expert(String id, String dateEntered, String dateModified, String modifiedUserId, String createdBy, Object description, String deleted, String assignedUserId, String salutation, String firstName, String lastName, String title, Object photo, Object department, String doNotCall, Object phoneHome, String phoneMobile, Object phoneWork, Object phoneOther, Object phoneFax, Object primaryAddressStreet, Object primaryAddressCity, Object primaryAddressState, Object primaryAddressPostalcode, Object primaryAddressCountry, Object altAddressStreet, Object altAddressCity, Object altAddressState, Object altAddressPostalcode, Object altAddressCountry, Object assistant, Object assistantPhone, Object leadSource, String reportsToId, Object birthdate, String campaignId, Object joomlaAccountId, String portalAccountDisabled, String portalUserType, String idC, String specializationC, String experttypeC, String chatAvailabilityC, String availableForPackageC, String slmcNoC, String qualificationAndDestinatioC, String picture, String online, List<Location> locations) {
        super();
        this.id = id;
        this.dateEntered = dateEntered;
        this.dateModified = dateModified;
        this.modifiedUserId = modifiedUserId;
        this.createdBy = createdBy;
        this.description = description;
        this.deleted = deleted;
        this.assignedUserId = assignedUserId;
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.photo = photo;
        this.department = department;
        this.doNotCall = doNotCall;
        this.phoneHome = phoneHome;
        this.phoneMobile = phoneMobile;
        this.phoneWork = phoneWork;
        this.phoneOther = phoneOther;
        this.phoneFax = phoneFax;
        this.primaryAddressStreet = primaryAddressStreet;
        this.primaryAddressCity = primaryAddressCity;
        this.primaryAddressState = primaryAddressState;
        this.primaryAddressPostalcode = primaryAddressPostalcode;
        this.primaryAddressCountry = primaryAddressCountry;
        this.altAddressStreet = altAddressStreet;
        this.altAddressCity = altAddressCity;
        this.altAddressState = altAddressState;
        this.altAddressPostalcode = altAddressPostalcode;
        this.altAddressCountry = altAddressCountry;
        this.assistant = assistant;
        this.assistantPhone = assistantPhone;
        this.leadSource = leadSource;
        this.reportsToId = reportsToId;
        this.birthdate = birthdate;
        this.campaignId = campaignId;
        this.joomlaAccountId = joomlaAccountId;
        this.portalAccountDisabled = portalAccountDisabled;
        this.portalUserType = portalUserType;
        this.idC = idC;
        this.specializationC = specializationC;
        this.experttypeC = experttypeC;
        this.chatAvailabilityC = chatAvailabilityC;
        this.availableForPackageC = availableForPackageC;
        this.slmcNoC = slmcNoC;
        this.qualificationAndDestinatioC = qualificationAndDestinatioC;
        this.picture = picture;
        this.online = online;
        this.locations = locations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public Object getDepartment() {
        return department;
    }

    public void setDepartment(Object department) {
        this.department = department;
    }

    public String getDoNotCall() {
        return doNotCall;
    }

    public void setDoNotCall(String doNotCall) {
        this.doNotCall = doNotCall;
    }

    public Object getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(Object phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public Object getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(Object phoneWork) {
        this.phoneWork = phoneWork;
    }

    public Object getPhoneOther() {
        return phoneOther;
    }

    public void setPhoneOther(Object phoneOther) {
        this.phoneOther = phoneOther;
    }

    public Object getPhoneFax() {
        return phoneFax;
    }

    public void setPhoneFax(Object phoneFax) {
        this.phoneFax = phoneFax;
    }

    public Object getPrimaryAddressStreet() {
        return primaryAddressStreet;
    }

    public void setPrimaryAddressStreet(Object primaryAddressStreet) {
        this.primaryAddressStreet = primaryAddressStreet;
    }

    public Object getPrimaryAddressCity() {
        return primaryAddressCity;
    }

    public void setPrimaryAddressCity(Object primaryAddressCity) {
        this.primaryAddressCity = primaryAddressCity;
    }

    public Object getPrimaryAddressState() {
        return primaryAddressState;
    }

    public void setPrimaryAddressState(Object primaryAddressState) {
        this.primaryAddressState = primaryAddressState;
    }

    public Object getPrimaryAddressPostalcode() {
        return primaryAddressPostalcode;
    }

    public void setPrimaryAddressPostalcode(Object primaryAddressPostalcode) {
        this.primaryAddressPostalcode = primaryAddressPostalcode;
    }

    public Object getPrimaryAddressCountry() {
        return primaryAddressCountry;
    }

    public void setPrimaryAddressCountry(Object primaryAddressCountry) {
        this.primaryAddressCountry = primaryAddressCountry;
    }

    public Object getAltAddressStreet() {
        return altAddressStreet;
    }

    public void setAltAddressStreet(Object altAddressStreet) {
        this.altAddressStreet = altAddressStreet;
    }

    public Object getAltAddressCity() {
        return altAddressCity;
    }

    public void setAltAddressCity(Object altAddressCity) {
        this.altAddressCity = altAddressCity;
    }

    public Object getAltAddressState() {
        return altAddressState;
    }

    public void setAltAddressState(Object altAddressState) {
        this.altAddressState = altAddressState;
    }

    public Object getAltAddressPostalcode() {
        return altAddressPostalcode;
    }

    public void setAltAddressPostalcode(Object altAddressPostalcode) {
        this.altAddressPostalcode = altAddressPostalcode;
    }

    public Object getAltAddressCountry() {
        return altAddressCountry;
    }

    public void setAltAddressCountry(Object altAddressCountry) {
        this.altAddressCountry = altAddressCountry;
    }

    public Object getAssistant() {
        return assistant;
    }

    public void setAssistant(Object assistant) {
        this.assistant = assistant;
    }

    public Object getAssistantPhone() {
        return assistantPhone;
    }

    public void setAssistantPhone(Object assistantPhone) {
        this.assistantPhone = assistantPhone;
    }

    public Object getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(Object leadSource) {
        this.leadSource = leadSource;
    }

    public String getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(String reportsToId) {
        this.reportsToId = reportsToId;
    }

    public Object getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Object birthdate) {
        this.birthdate = birthdate;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Object getJoomlaAccountId() {
        return joomlaAccountId;
    }

    public void setJoomlaAccountId(Object joomlaAccountId) {
        this.joomlaAccountId = joomlaAccountId;
    }

    public String getPortalAccountDisabled() {
        return portalAccountDisabled;
    }

    public void setPortalAccountDisabled(String portalAccountDisabled) {
        this.portalAccountDisabled = portalAccountDisabled;
    }

    public String getPortalUserType() {
        return portalUserType;
    }

    public void setPortalUserType(String portalUserType) {
        this.portalUserType = portalUserType;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getSpecializationC() {
        return specializationC;
    }

    public void setSpecializationC(String specializationC) {
        this.specializationC = specializationC;
    }

    public String getExperttypeC() {
        return experttypeC;
    }

    public void setExperttypeC(String experttypeC) {
        this.experttypeC = experttypeC;
    }

    public String getChatAvailabilityC() {
        return chatAvailabilityC;
    }

    public void setChatAvailabilityC(String chatAvailabilityC) {
        this.chatAvailabilityC = chatAvailabilityC;
    }

    public String getAvailableForPackageC() {
        return availableForPackageC;
    }

    public void setAvailableForPackageC(String availableForPackageC) {
        this.availableForPackageC = availableForPackageC;
    }

    public String getSlmcNoC() {
        return slmcNoC;
    }

    public void setSlmcNoC(String slmcNoC) {
        this.slmcNoC = slmcNoC;
    }

    public String getQualificationAndDestinatioC() {
        return qualificationAndDestinatioC;
    }

    public void setQualificationAndDestinatioC(String qualificationAndDestinatioC) {
        this.qualificationAndDestinatioC = qualificationAndDestinatioC;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(dateEntered);
        dest.writeValue(dateModified);
        dest.writeValue(modifiedUserId);
        dest.writeValue(createdBy);
        dest.writeValue(description);
        dest.writeValue(deleted);
        dest.writeValue(assignedUserId);
        dest.writeValue(salutation);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(title);
        dest.writeValue(photo);
        dest.writeValue(department);
        dest.writeValue(doNotCall);
        dest.writeValue(phoneHome);
        dest.writeValue(phoneMobile);
        dest.writeValue(phoneWork);
        dest.writeValue(phoneOther);
        dest.writeValue(phoneFax);
        dest.writeValue(primaryAddressStreet);
        dest.writeValue(primaryAddressCity);
        dest.writeValue(primaryAddressState);
        dest.writeValue(primaryAddressPostalcode);
        dest.writeValue(primaryAddressCountry);
        dest.writeValue(altAddressStreet);
        dest.writeValue(altAddressCity);
        dest.writeValue(altAddressState);
        dest.writeValue(altAddressPostalcode);
        dest.writeValue(altAddressCountry);
        dest.writeValue(assistant);
        dest.writeValue(assistantPhone);
        dest.writeValue(leadSource);
        dest.writeValue(reportsToId);
        dest.writeValue(birthdate);
        dest.writeValue(campaignId);
        dest.writeValue(joomlaAccountId);
        dest.writeValue(portalAccountDisabled);
        dest.writeValue(portalUserType);
        dest.writeValue(idC);
        dest.writeValue(specializationC);
        dest.writeValue(experttypeC);
        dest.writeValue(chatAvailabilityC);
        dest.writeValue(availableForPackageC);
        dest.writeValue(slmcNoC);
        dest.writeValue(qualificationAndDestinatioC);
        dest.writeValue(picture);
        dest.writeValue(online);
        dest.writeList(locations);
    }

    public int describeContents() {
        return 0;
    }

}