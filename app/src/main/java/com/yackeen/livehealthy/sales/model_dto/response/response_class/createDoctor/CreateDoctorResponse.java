package com.yackeen.livehealthy.sales.model_dto.response.response_class.createDoctor;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 22/08/2017.
 */

public class CreateDoctorResponse extends BaseResponse {
    @SerializedName("Response")
private DoctorIdResponse doctorIdResponse;

    public DoctorIdResponse getDoctorIdResponse() {
        return doctorIdResponse;
    }

    public void setDoctorIdResponse(DoctorIdResponse doctorIdResponse) {
        this.doctorIdResponse = doctorIdResponse;
    }
}
