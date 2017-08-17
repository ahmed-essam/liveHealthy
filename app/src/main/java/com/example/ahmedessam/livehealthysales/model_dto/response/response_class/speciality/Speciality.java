package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality;

import com.example.ahmedessam.livehealthysales.database.AppDataBase;
import com.example.ahmedessam.livehealthysales.database.CreateDoctorDB;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Abdelrhman Walid on 7/13/2017.
 */
@Table(database = AppDataBase.class)
public class Speciality extends BaseModel {
    @SerializedName("Speciality_ID")
    @PrimaryKey
    private int id;
    @SerializedName("Name")
    @Column
    private String name;
    @SerializedName("Name_AR")
    @Column
    private String nameAr;

    public static void clearSpecialityDB(){
        List<Speciality> movieList = SQLite.select().from(Speciality.class).queryList();
        FlowManager.getModelAdapter(Speciality.class).deleteAll(movieList);
    }

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
