package com.yackeen.livehealthy.sales.model_dto.response.response_class;

import com.yackeen.livehealthy.sales.models.Doctor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 08/08/2017.
 */

public class DoctorsResponse {
    @SerializedName("Doctors")
    @Expose
    private List<Doctor> doctors = null;

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
