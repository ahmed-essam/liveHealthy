package com.yackeen.livehealthy.sales.model_dto.response.response_class.clinic;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class ClinicGeneralResponse extends BaseResponse {
    @SerializedName("Response")
    @Expose
    private ClinicResponse response;

    public ClinicResponse getResponse() {
        return response;
    }

    public void setResponse(ClinicResponse response) {
        this.response = response;
    }
}
