package com.example.ahmedessam.livehealthysales.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class Day {
    @SerializedName(value = "DayID", alternate = "Day_Id")
    private Integer dayId;
    @SerializedName("Day")
    private String day;
    @SerializedName("From_Hour")
    private String fromHour;
    @SerializedName("To_Hour")
    private String toHour;

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFromHour() {
        return fromHour;
    }

    public void setFromHour(String fromHour) {
        this.fromHour = fromHour;
    }

    public String getToHour() {
        return toHour;
    }

    public void setToHour(String toHour) {
        this.toHour = toHour;
    }
}
