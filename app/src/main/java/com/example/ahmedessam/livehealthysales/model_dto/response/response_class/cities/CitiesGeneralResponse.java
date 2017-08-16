package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities;

import com.example.ahmedessam.livehealthysales.model_dto.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 13/08/2017.
 */

public class CitiesGeneralResponse extends BaseResponse {
    @SerializedName("Response")
    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
