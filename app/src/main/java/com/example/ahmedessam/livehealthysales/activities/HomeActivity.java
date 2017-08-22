package com.example.ahmedessam.livehealthysales.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.Areas.AreaGeneralResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities.CitiesGeneralResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities.City;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.getAllAreas.AllArea;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.getAllAreas.AllAreasResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality.Speciality;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.example.ahmedessam.livehealthysales.util.Connectivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    //bind view
    @BindView(R.id.doctor_icon) ImageView doctorIcon;
    @BindView(R.id.lab_icon) ImageView labsIcon;
    @BindView(R.id.rays_icon) ImageView raysIcon;
    @BindView(R.id.pharmacy_icon) ImageView pharmacyIcon;
    @BindView(R.id.doctor_text) TextView doctorText;
    @BindView(R.id.lab_text) TextView labText;
    @BindView(R.id.rays_text) TextView raysText;
    @BindView(R.id.pharmacy_text) TextView pharmacyText;
    Call<SpecialityGeneralResponse> specialityGeneralResponseCall;
    Call<CitiesGeneralResponse> citiesResponseCall;
    Call<AllAreasResponse> areaResponseCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getSpecialities();
        fetchCities();
        fetchAreas();
    }
    @OnClick(R.id.doctor_icon)
    public void doctorClicked(){
        startActivity(new Intent(HomeActivity.this,DoctorsActivity.class));

    }

    public void saveSpeciality(List<Speciality> specialites){
        if (specialites != null && specialites.size()!=0){
            Speciality.clearSpecialityDB();
            for (int i=0;i<specialites.size();i++){
                Speciality speciality = specialites.get(i);
                speciality.save();
            }
        }
    }

    public void saveCities(List<City> cityList  ){
        if (cityList != null && cityList.size()!=0){
            City.clearCitiesDB();
            for (int i=0;i<cityList.size();i++){
                City city = cityList.get(i);
                city.save();
            }
        }
    }

    public void saveAreas(List<AllArea> areaList  ){
        if ( areaList!= null && areaList.size()!=0){
            AllArea.ClearAreasDB();
            for (int i=0;i<areaList.size();i++){
                AllArea allArea = areaList.get(i);
                allArea.save();
            }
        }
    }
    public void getSpecialities() {
        specialityGeneralResponseCall = NetworkProvider.provideNetworkMethods(this).getSpeciality(1, 10000);
        specialityGeneralResponseCall.enqueue(new Callback<SpecialityGeneralResponse>() {
            @Override
            public void onResponse(Call<SpecialityGeneralResponse> call, Response<SpecialityGeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body() != null) {
                            List<Speciality> list = response.body().getResponse();
                            if (list != null) {
                                saveSpeciality(list);
                            }

                        }
                    } else {
                        Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<SpecialityGeneralResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchCities() {
        citiesResponseCall = NetworkProvider.provideNetworkMethods(this).getCities(1, 10000);
        citiesResponseCall.enqueue(new Callback<CitiesGeneralResponse>() {
            @Override
            public void onResponse(Call<CitiesGeneralResponse> call, Response<CitiesGeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body().getCities() != null) {
                            List<City> cityList = response.body().getCities();
                            if (cityList != null){
                                saveCities(cityList);
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<CitiesGeneralResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void fetchAreas() {
        areaResponseCall = NetworkProvider.provideNetworkMethods(this).getAllAreas();
        areaResponseCall.enqueue(new Callback<AllAreasResponse>() {
            @Override
            public void onResponse(Call<AllAreasResponse> call, Response<AllAreasResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body().getResponse().getAllAreas() != null) {
                            saveAreas(response.body().getResponse().getAllAreas());
                        } else {
                            Toast.makeText(HomeActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(HomeActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AllAreasResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        if (citiesResponseCall != null){
            citiesResponseCall.cancel();
        }
        if (specialityGeneralResponseCall != null){
            specialityGeneralResponseCall.cancel();
        }
        super.onStop();
    }
}
