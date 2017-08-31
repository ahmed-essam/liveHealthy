package com.yackeen.livehealthy.sales.model_dto.request;

import com.yackeen.livehealthy.sales.models.Day;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed essam on 10/08/2017.
 */

public class UpdateDoctorTimesRequestBody {
    @SerializedName("AllSlots")
    private List<Day> daysItems;
    @SerializedName("DoctorID")
    private long doctorID;
    @SerializedName("Clinic_ID")
    private long clinicID;

    public long getClinicID() {
        return clinicID;
    }

    public UpdateDoctorTimesRequestBody setClinicID(long clinicID) {
        this.clinicID = clinicID;
        return this;
    }

    public long getDoctorID() {
        return doctorID;
    }

    public UpdateDoctorTimesRequestBody setDoctorID(long doctorID) {
        this.doctorID = doctorID;
        return this;
    }

    public List<Day> getDaysItems() {
        return daysItems;
    }

    public UpdateDoctorTimesRequestBody setDaysItems(List<Day> daysItems) {
        this.daysItems = daysItems;
        return this;
    }
}
