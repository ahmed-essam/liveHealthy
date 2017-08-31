package com.yackeen.livehealthy.sales.model_dto.response.response_class;

import com.yackeen.livehealthy.sales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DoctorDetailResponse extends BaseResponse {
    @SerializedName("Response")
    private DetailResponse detailResponse;

    public DetailResponse getDetailResponse() {
        return detailResponse;
    }

    public void setDetailResponse(DetailResponse detailResponse) {
        this.detailResponse = detailResponse;
    }
}
