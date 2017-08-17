package com.example.ahmedessam.livehealthysales.database;

import com.google.gson.annotations.SerializedName;
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
public class CreateDoctorDB extends BaseModel{
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private Boolean houseVisit;
    @Column
    private Integer consultantID;
    @Column
    private String description;
    @Column
    private String descriptionAR;
    @Column
    private String email;
    @Column
    private String gender;
    @Column
    private String landLine;
    @Column
    private String location;
    @Column
    private String mobileNumber;
    @Column
    private String name;
    @Column
    private String nameAR;
    @Column
    private Boolean nursery;
    @Column
    private String socialMedia;
    @Column
    private Integer specialityID;
    @Column
    private String title;
    @Column
    private String titleAR;
    @Column
    private String workAuthNo;
    @Column
    private String lang;

    public static void ClearCreateDoctorDB(){
        List<CreateDoctorDB> movieList = SQLite.select().from(CreateDoctorDB.class).queryList();
        FlowManager.getModelAdapter(CreateDoctorDB.class).deleteAll(movieList);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean isHouseVisit() {
        return houseVisit;
    }

    public void setHouseVisit(Boolean houseVisit) {
        this.houseVisit = houseVisit;
    }

    public Integer getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(Integer consultantID) {
        this.consultantID = consultantID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAR() {
        return descriptionAR;
    }

    public void setDescriptionAR(String descriptionAR) {
        this.descriptionAR = descriptionAR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public Boolean isNursery() {
        return nursery;
    }

    public void setNursery(Boolean nursery) {
        this.nursery = nursery;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAR() {
        return titleAR;
    }

    public void setTitleAR(String titleAR) {
        this.titleAR = titleAR;
    }

    public String getWorkAuthNo() {
        return workAuthNo;
    }

    public void setWorkAuthNo(String workAuthNo) {
        this.workAuthNo = workAuthNo;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
