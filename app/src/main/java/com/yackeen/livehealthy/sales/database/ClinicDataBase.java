package com.yackeen.livehealthy.sales.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
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
public class ClinicDataBase extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private int id ;
    @Column
    private Integer clinicID;
    @Column
    private String clinicName;
    @Column
    private String clinicNameAR;
    @Column
    private Integer price;
    @Column
    private String address;
    @Column
    private String addressAR;
    @Column
    private String cityName;
    @Column
    private Integer cityID;
    @Column
    private String areaName;
    @Column
    private Integer areaID;
    @Column
    private String mobileNumber;
    @Column
    private String landLine;
    @Column
    private Integer requestsPerDay;
    @Column
    private Boolean editable;
    @Column
    private Integer discount;
    @ForeignKey(tableClass = CreateClinicDB.class , references = @ForeignKeyReference(columnName = "request_id", foreignKeyColumnName = "clinicID"))
    private long requestId;

    public static void ClearClinicDB(){
        List<ClinicDataBase> movieList = SQLite.select().from(ClinicDataBase.class).queryList();
        FlowManager.getModelAdapter(ClinicDataBase.class).deleteAll(movieList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Integer getClinicID() {
        return clinicID;
    }

    public void setClinicID(Integer clinicID) {
        this.clinicID = clinicID;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicNameAR() {
        return clinicNameAR;
    }

    public void setClinicNameAR(String clinicNameAR) {
        this.clinicNameAR = clinicNameAR;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressAR() {
        return addressAR;
    }

    public void setAddressAR(String addressAR) {
        this.addressAR = addressAR;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaID() {
        return areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public Integer getRequestsPerDay() {
        return requestsPerDay;
    }

    public void setRequestsPerDay(Integer requestsPerDay) {
        this.requestsPerDay = requestsPerDay;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }



}
