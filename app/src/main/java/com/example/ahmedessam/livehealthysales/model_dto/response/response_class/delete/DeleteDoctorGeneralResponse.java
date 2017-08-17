package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.delete;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import okhttp3.Response;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DeleteDoctorGeneralResponse extends BaseResponse{
    @SerializedName("Response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
