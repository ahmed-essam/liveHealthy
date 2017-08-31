package com.yackeen.livehealthy.sales.network;

import com.yackeen.livehealthy.sales.model_dto.request.CreateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.request.DoctorsRequest;
import com.yackeen.livehealthy.sales.model_dto.request.LoginRequest;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorClinicsRequestBody;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorTimesRequestBody;
import com.yackeen.livehealthy.sales.model_dto.response.GeoCoderResult;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.AllDoctorsResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.Areas.AreaGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.DoctorDetailResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.LoginResponce;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.cities.CitiesGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.clinic.ClinicGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.createDoctor.CreateDoctorResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.delete.DeleteDoctorGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.getAllAreas.AllAreasResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.schedule.DaysGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.updateDoctor.UpdateDoctorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public interface NetworkMethods {

    @POST("Users/DoLogin")
    Call<LoginResponce> doLogin(@Body LoginRequest loginRequest);

    @POST("Doctors/DoFilterDoctor")
    Call<AllDoctorsResponse> getDoctors(@Body DoctorsRequest doctorsRequest);

    @GET("Doctors/DoGetDoctorDetails?")
    Call<DoctorDetailResponse> getDoctorDetail(@Query("Doctor_ID") int doctorID,@Query("lang") String lang);

    @GET("Doctors/DoGetDoctorClinics?")
    Call<ClinicGeneralResponse> getClinics(@Query("Doctor_ID") int id,@Query("lang") String lang);

    @GET("Doctors/DoDoctorTimes?")
    Call<DaysGeneralResponse> getSchedule(@Query("Doctor_ID") long docId,@Query("Clinic_ID") int clinicId,@Query("lang") String lang);

    @GET("Specialities/GetSpecialities?")
    Call<SpecialityGeneralResponse> getSpeciality(@Query("PageNumber") int pagenum,@Query("NumberRecords") int packageNum);

    @GET("Cities/GetCities?")
    Call<CitiesGeneralResponse> getCities(@Query("PageNumber") int pagenum, @Query("NumberRecords") int packageNum);

    @GET("Areas/GetAreas?")
    Call<AreaGeneralResponse> getAreas(@Query("City_ID") long CityId, @Query("PageNumber") int pagenum,@Query("NumberRecords") int packageNum );

    @GET("Areas/GetAllAreas")
    Call<AllAreasResponse> getAllAreas();

    @POST("Doctors/DoUpdateDoctorDetails")
    Call<UpdateDoctorResponse> updateDoctor(@Body UpdateDoctorRequest updateDoctorRequest);

    @POST("Doctors/DoCreateDoctorRequest")
    Call<CreateDoctorResponse> createNewDoctor(@Body CreateDoctorRequest createDoctorRequest);

    @POST("Doctors/DoUpdateDoctorClinics")
    Call<UpdateDoctorResponse> updateClinic(@Body UpdateDoctorClinicsRequestBody updateClinicRequest);

    @POST("Doctors/DoUpdateDoctorTimes")
    Call<UpdateDoctorResponse> updateClinicTimes(@Body UpdateDoctorTimesRequestBody updateTimesRequest);

    @POST
    Call<GeoCoderResult> getLocationAddress(@Url String url);

    @DELETE("Doctors/DeleteDoctor/{id}")
    Call<DeleteDoctorGeneralResponse> deleteDoctor(@Path("id") int id);

}
