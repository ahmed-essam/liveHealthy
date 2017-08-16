package com.example.ahmedessam.livehealthysales.model_dto.request;

import com.example.ahmedessam.livehealthysales.models.Clinic;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 10/08/2017.
 */

public class UpdateDoctorClinicsRequestBody {
    @SerializedName("Doctor_ID")
    private long Doctor_ID;
    @SerializedName("lang")
    private String lang;
    @SerializedName("AllClinics")
    private List<Clinic> AllClinics;

    public long getDoctor_ID() {
        return Doctor_ID;
    }

    public void setDoctor_ID(long doctor_ID) {
        Doctor_ID = doctor_ID;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<Clinic> getAllClinics() {
        return AllClinics;
    }

    public void setAllClinics(List<Clinic> allClinics) {
        AllClinics = allClinics;
    }
}
