package com.example.ahmedessam.livehealthysales.database;

import com.example.ahmedessam.livehealthysales.models.Clinic;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by ahmed essam on 21/08/2017.
 */
@Table(database = AppDataBase.class)
public class CreateClinicDB extends BaseModel {
    @Column
    @ForeignKey(tableClass = CreateDoctorDB.class)
    private  long Doctor_ID;
    @Column
    private String lang;
    @PrimaryKey(autoincrement = true)
    private int clinicID;

    public static void ClearCreateClinicDB(){
        List<CreateClinicDB> movieList = SQLite.select().from(CreateClinicDB.class).queryList();
        FlowManager.getModelAdapter(CreateClinicDB.class).deleteAll(movieList);
    }

    public  long getDoctor_ID() {
        return Doctor_ID;
    }

    public  void setDoctor_ID(long doctor_ID) {
        Doctor_ID = doctor_ID;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    public int getClinicID() {
        return clinicID;
    }

    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }
}
