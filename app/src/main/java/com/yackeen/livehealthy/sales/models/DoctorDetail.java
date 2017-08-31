package com.yackeen.livehealthy.sales.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DoctorDetail implements Serializable {
    @SerializedName("Image")
    private String image;
    @SerializedName("Description")
    private String description;
    @SerializedName("Description_AR")
    private String descriptionAR;
    @SerializedName("Doctor_ID")
    private Integer doctorID;
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
    @SerializedName("Reviews")
    private Integer reviews;
    @SerializedName("Title")
    private String title;
    @SerializedName("Social_Media")
    private Object socialMedia;
    @SerializedName("Speciality")
    private String speciality;
    @SerializedName("Email")
    private Object email;
    @SerializedName("Speciality_ID")
    private Integer specialityID;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
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

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(Object socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }
}
