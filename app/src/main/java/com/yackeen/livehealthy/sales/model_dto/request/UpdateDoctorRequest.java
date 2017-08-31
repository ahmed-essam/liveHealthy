package com.yackeen.livehealthy.sales.model_dto.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class UpdateDoctorRequest {
    @SerializedName("Doctor_ID")
    private Integer doctorID;
    @SerializedName("Mobile_Number")
    private String mobileNumber;
    @SerializedName("Name_AR")
    private String nameAR;
    @SerializedName("Name")
    private String name;
    @SerializedName("Speciality_ID")
    private Integer specialityID;
    @SerializedName("Email")
    private String email;
    @SerializedName("Descrpition")
    private String descrpition;
    @SerializedName("Descrpition_AR")
    private String descrpitionAR;
    @SerializedName("lang")
    private String lang;

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescrpition() {
        return descrpition;
    }

    public void setDescrpition(String descrpition) {
        this.descrpition = descrpition;
    }

    public String getDescrpitionAR() {
        return descrpitionAR;
    }

    public void setDescrpitionAR(String descrpitionAR) {
        this.descrpitionAR = descrpitionAR;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
