package com.example.ahmedessam.livehealthysales.model_dto.response.response_class;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.example.ahmedessam.livehealthysales.models.Doctor;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed essam on 08/08/2017.
 */

public class AllDoctorsResponse extends BaseResponse {
    @SerializedName("Response")
    private DoctorsResponse doctorsResponse;

    public DoctorsResponse getDoctorsResponse() {
        return doctorsResponse;
    }

    public void setDoctorsResponse(DoctorsResponse doctorsResponse) {
        this.doctorsResponse = doctorsResponse;
    }
}
