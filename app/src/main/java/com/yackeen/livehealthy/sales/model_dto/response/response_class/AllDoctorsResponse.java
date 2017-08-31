package com.yackeen.livehealthy.sales.model_dto.response.response_class;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

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
