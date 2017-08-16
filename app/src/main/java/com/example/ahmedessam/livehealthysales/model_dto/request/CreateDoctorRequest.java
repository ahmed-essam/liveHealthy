package com.example.ahmedessam.livehealthysales.model_dto.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 10/08/2017.
 */

public class CreateDoctorRequest {
    @SerializedName("House_Visit")
    private Boolean houseVisit;
    @SerializedName("Consultant_ID")
    private Integer consultantID;
    @SerializedName("Description")
    private String description;
    @SerializedName("Description_AR")
    private String descriptionAR;
    @SerializedName("Email")
    private String email;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Land_Line")
    private String landLine;
    @SerializedName("Location")
    private String location;
    @SerializedName("Mobile_Number")
    private String mobileNumber;
    @SerializedName("Name")
    private String name;
    @SerializedName("Name_AR")
    private String nameAR;
    @SerializedName("Nursery")
    private Boolean nursery;
    @SerializedName("Social_Media")
    private String socialMedia;
    @SerializedName("Speciality_ID")
    private Integer specialityID;
    @SerializedName("Title")
    private String title;
    @SerializedName("Title_AR")
    private String titleAR;
    @SerializedName("Work_Auth_No")
    private String workAuthNo;
    @SerializedName("lang")
    private String lang;

    public Boolean getHouseVisit() {
        return houseVisit;
    }

    public void setHouseVisit(Boolean houseVisit) {
        this.houseVisit = houseVisit;
    }

    public Integer getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(Integer consultantID) {
        this.consultantID = consultantID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAR() {
        return descriptionAR;
    }

    public void setDescriptionAR(String descriptionAR) {
        this.descriptionAR = descriptionAR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public Boolean getNursery() {
        return nursery;
    }

    public void setNursery(Boolean nursery) {
        this.nursery = nursery;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAR() {
        return titleAR;
    }

    public void setTitleAR(String titleAR) {
        this.titleAR = titleAR;
    }

    public String getWorkAuthNo() {
        return workAuthNo;
    }

    public void setWorkAuthNo(String workAuthNo) {
        this.workAuthNo = workAuthNo;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
