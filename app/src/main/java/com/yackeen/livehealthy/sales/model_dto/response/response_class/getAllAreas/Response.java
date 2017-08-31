package com.yackeen.livehealthy.sales.model_dto.response.response_class.getAllAreas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 22/08/2017.
 */

public class Response {
    @SerializedName("AllAreas")
    private List<AllArea> allAreas;

    public List<AllArea> getAllAreas() {
        return allAreas;
    }

    public void setAllAreas(List<AllArea> allAreas) {
        this.allAreas = allAreas;
    }
}
