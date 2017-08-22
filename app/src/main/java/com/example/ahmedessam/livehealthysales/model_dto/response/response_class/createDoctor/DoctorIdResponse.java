package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.createDoctor;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 22/08/2017.
 */

public class DoctorIdResponse {
    @SerializedName("DoctorID")
    private long DoctorID;

    public long getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(long doctorID) {
        DoctorID = doctorID;
    }
}
