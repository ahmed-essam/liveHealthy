package com.yackeen.livehealthy.sales.model_dto.response.response_class.schedule;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DaysGeneralResponse  extends BaseResponse{
    @SerializedName("Response")
    private DayesResponse response;

    public DayesResponse getResponse() {
        return response;
    }

    public void setResponse(DayesResponse response) {
        this.response = response;
    }
}
