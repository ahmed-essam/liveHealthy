package com.example.ahmedessam.livehealthysales.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class Clinic implements Parcelable {
    @SerializedName("Clinic_ID")
    @Expose
    private Integer clinicID;
    @SerializedName("Clinic_Name")
    @Expose
    private String clinicName;
    @SerializedName("Clinic_Name_AR")
    @Expose
    private String clinicNameAR;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Address_AR")
    @Expose
    private String addressAR;
    @SerializedName("City_Name")
    @Expose
    private String cityName;
    @SerializedName("City_ID")
    @Expose
    private Integer cityID;
    @SerializedName("Area_Name")
    @Expose
    private String areaName;
    @SerializedName("Area_ID")
    @Expose
    private Integer areaID;
    @SerializedName("Mobile_Number")
    @Expose
    private String mobileNumber;
    @SerializedName("Land_Line")
    @Expose
    private String landLine;
    @SerializedName("Requests_Per_Day")
    @Expose
    private Integer requestsPerDay;
    @SerializedName("Editable")
    @Expose
    private Boolean editable;
    @SerializedName("Discount")
    @Expose
    private Integer discount;

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

    public Boolean getEditable() {
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

    public boolean isEditable() {
        return editable;
    }

    public void setDiscount(int disount) {
        this.discount = disount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.clinicID);
        dest.writeString(this.clinicName);
        dest.writeString(this.clinicNameAR);
        dest.writeValue(this.price);
        dest.writeString(this.address);
        dest.writeString(this.addressAR);
        dest.writeString(this.cityName);
        dest.writeValue(this.cityID);
        dest.writeString(this.areaName);
        dest.writeValue(this.areaID);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.landLine);
        dest.writeValue(this.requestsPerDay);
        dest.writeValue(this.editable);
        dest.writeValue(this.discount);
    }

    public Clinic() {
    }

    protected Clinic(Parcel in) {
        this.clinicID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clinicName = in.readString();
        this.clinicNameAR = in.readString();
        this.price = (Integer) in.readValue(Integer.class.getClassLoader());
        this.address = in.readString();
        this.addressAR = in.readString();
        this.cityName = in.readString();
        this.cityID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.areaName = in.readString();
        this.areaID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mobileNumber = in.readString();
        this.landLine = in.readString();
        this.requestsPerDay = (Integer) in.readValue(Integer.class.getClassLoader());
        this.editable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.discount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Clinic> CREATOR = new Parcelable.Creator<Clinic>() {
        @Override
        public Clinic createFromParcel(Parcel source) {
            return new Clinic(source);
        }

        @Override
        public Clinic[] newArray(int size) {
            return new Clinic[size];
        }
    };
}
