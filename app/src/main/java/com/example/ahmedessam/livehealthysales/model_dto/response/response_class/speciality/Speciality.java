package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdelrhman Walid on 7/13/2017.
 */

public class Speciality {
    @SerializedName("Speciality_ID")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Name_AR")
    private String nameAr;

    public String getPrice() {
        return nameAr;
    }



    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public int getId() {
        return id;
    }

    public Speciality setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Speciality setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
