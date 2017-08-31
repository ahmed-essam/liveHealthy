package com.yackeen.livehealthy.sales.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by ahmed essam on 16/08/2017.
 */
@Table(database = AppDataBase.class)
public class UpdateDoctorDb extends BaseModel {
    @PrimaryKey
    private Integer doctorID;
    @Column
    private String mobileNumber;
    @Column
    private String nameAR;
    @Column
    private String name;
    @Column
    private Integer specialityID;
    @Column
    private String email;
    @Column
    private String descrpition;
    @Column
    private String descrpitionAR;
    @Column
    private String lang;

    public static void ClearUpdateDoctorDB(){
        List<UpdateDoctorDb> movieList = SQLite.select().from(UpdateDoctorDb.class).queryList();
        FlowManager.getModelAdapter(UpdateDoctorDb.class).deleteAll(movieList);
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescrpition() {
        return descrpition;
    }

    public void setDescrpition(String descrpition) {
        this.descrpition = descrpition;
    }

    public String getDescrpitionAR() {
        return descrpitionAR;
    }

    public void setDescrpitionAR(String descrpitionAR) {
        this.descrpitionAR = descrpitionAR;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
