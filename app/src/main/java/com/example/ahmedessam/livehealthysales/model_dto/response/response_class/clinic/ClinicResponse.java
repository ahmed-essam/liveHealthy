package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.clinic;

import com.example.ahmedessam.livehealthysales.models.Clinic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class ClinicResponse {
    @SerializedName("Clinics")
    @Expose
    private List<Clinic> clinics = null;

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }
}
