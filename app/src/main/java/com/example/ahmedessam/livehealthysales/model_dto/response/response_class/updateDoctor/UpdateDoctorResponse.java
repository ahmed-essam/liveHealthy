package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.updateDoctor;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import okhttp3.Response;

/**
 * Created by ahmed essam on 10/08/2017.
 */

public class UpdateDoctorResponse extends BaseResponse {
    @SerializedName("Response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
