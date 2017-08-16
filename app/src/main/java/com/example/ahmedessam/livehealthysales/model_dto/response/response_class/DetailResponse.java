package com.example.ahmedessam.livehealthysales.model_dto.response.response_class;

import com.example.ahmedessam.livehealthysales.models.DoctorDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DetailResponse {
    @SerializedName("DoctorDetials")
    @Expose
    private DoctorDetail doctorDetials;

    public DoctorDetail getDoctorDetials() {
        return doctorDetials;
    }

    public void setDoctorDetials(DoctorDetail doctorDetials) {
        this.doctorDetials = doctorDetials;
    }
}
