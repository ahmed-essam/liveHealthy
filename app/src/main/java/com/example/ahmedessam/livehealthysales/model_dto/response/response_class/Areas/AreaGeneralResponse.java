package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.Areas;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities.City;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 13/08/2017.
 */

public class AreaGeneralResponse extends BaseResponse {
    @SerializedName("Response")
    private List<Area> Areas;

    public List<Area> getAreas() {
        return Areas;
    }

    public void setAreas(List<Area> areas) {
        Areas = areas;
    }
}
