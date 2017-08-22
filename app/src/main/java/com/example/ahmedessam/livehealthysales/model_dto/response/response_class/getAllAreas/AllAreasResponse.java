package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.getAllAreas;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import okhttp3.*;

/**
 * Created by ahmed essam on 22/08/2017.
 */

public class AllAreasResponse extends BaseResponse {
    @SerializedName("Response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
