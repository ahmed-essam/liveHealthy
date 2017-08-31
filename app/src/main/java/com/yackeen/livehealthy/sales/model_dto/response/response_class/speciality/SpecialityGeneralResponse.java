package com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class SpecialityGeneralResponse extends BaseResponse {
    @SerializedName("Response")
    private List<Speciality> response = null;

    public List<Speciality> getResponse() {
        return response;
    }

    public void setResponse(List<Speciality> response) {
        this.response = response;
    }
}
