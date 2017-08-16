package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.schedule;

import com.example.ahmedessam.livehealthysales.models.Day;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class DayesResponse {
    @SerializedName("Days")
    private List<Day> days = null;

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
