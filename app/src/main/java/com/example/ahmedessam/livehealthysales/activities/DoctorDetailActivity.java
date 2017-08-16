package com.example.ahmedessam.livehealthysales.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.adapters.DemoCollectionPagerAdapter;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.AllDoctorsResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.DoctorDetailResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.clinic.ClinicGeneralResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.delete.DeleteDoctorGeneralResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.example.ahmedessam.livehealthysales.models.Clinic;
import com.example.ahmedessam.livehealthysales.models.DoctorDetail;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.example.ahmedessam.livehealthysales.util.UserHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_doctor_image)
    CircleImageView doctorImageView;
    @BindView(R.id.edit_button)
    Button EditButton;
    @BindView(R.id.stop_button)
    Button stopButton;
    @BindView(R.id.detail_doctor_name)
    TextView doctorName;
    @BindView(R.id.detail_doctor_email)
    TextView email;
    @BindView(R.id.detail_doctor_id)
    TextView id;
    @BindView(R.id.detail_doctor_location)
    TextView location;
    @BindView(R.id.detail_doctor_phone)
    TextView phone;
    @BindView(R.id.no_clinic)
    TextView noClinic;
    @BindView(R.id.dates_icon_plus)
    ImageView datesIcons;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewpager;
    @BindView(R.id.Linear_Layout)
    LinearLayout linear_layout;



    Call<DoctorDetailResponse> responceCall;
    Call<ClinicGeneralResponse> clinicResponceCall;
    Call<DeleteDoctorGeneralResponse> deleteDoctorResponseCall;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    List<Clinic> clinics;
    private int datesState;
    DoctorDetail doctorDetail;
    public static final String DoctorArg = "doctor_id";
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        datesState =0;
        ButterKnife.bind(this);
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        doctorId = getDoctorId();
        fetchDoctorDetail();
        datesIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDates();
            }
        });

    }



    @OnClick(R.id.edit_button)
    public void setEditButton(){
        if (doctorDetail != null) {
          Intent intent =  AddDoctorActivity.newAddDoctorIntent(this,doctorDetail );
            startActivity(intent);
        }
    }


    public void showDates(){
        if (datesState ==0){
            datesState = 1;
            linear_layout.setVisibility(View.VISIBLE);
            datesIcons.setImageResource(R.drawable.ic_action_minus);
            fetchClinics();
        }else{
            datesState = 0;
            linear_layout.setVisibility(View.GONE);
            datesIcons.setImageResource(R.drawable.ic_action_add);
        }
    }

    public static Intent newDoctorDetail(Context context, int doctor) {
        Intent intent = new Intent(context, DoctorDetailActivity.class);
        intent.putExtra(DoctorArg, doctor);
        return intent;
    }

    public int getDoctorId() {
        int id = getIntent().getIntExtra(DoctorArg, 0);
        return id;
    }

    public void addView(DoctorDetail response) {
        if (Locale.getDefault().getDisplayLanguage().equals("العربيه") && response.getNameAR()!= null){
            doctorName.setText(response.getNameAR());

        } else {
            doctorName.setText(response.getName());
        }
        if (UserHelper.getAppLang(this).equals("ar") && response.getDescriptionAR()!= null){
            id.setText(response.getDescriptionAR());
        }else{
            id.setText(response.getDescription());
        }
        if (response.getEmail() != null) {
            email.setText(response.getEmail().toString());
        } else {
            email.setText("");
        }
        phone.setText(response.getMobileNumber());
        location.setText(response.getLocation());

            Glide.with(this).load(response.getImage())
                    .placeholder(R.drawable.doctor_icon)
                    .error(R.drawable.doctor_icon)
                    .into(doctorImageView);
//        fetchClinics();
    }

    public Boolean verifyResponse(Response<DoctorDetailResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isSuccess()) {
                if (response.body().getDetailResponse() != null) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void fetchDoctorDetail() {
        responceCall = NetworkProvider.provideNetworkMethods(this).getDoctorDetail(doctorId, Locale.getDefault().getDisplayLanguage());
        responceCall.enqueue(new Callback<DoctorDetailResponse>() {
            @Override
            public void onResponse(Call<DoctorDetailResponse> call, Response<DoctorDetailResponse> response) {
                if (verifyResponse(response)) {
                    doctorDetail = response.body().getDetailResponse().getDoctorDetials();
                    addView(doctorDetail);
                }
            }

            @Override
            public void onFailure(Call<DoctorDetailResponse> call, Throwable t) {
                Toast.makeText(DoctorDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public Boolean verifyClinicResponse(Response<ClinicGeneralResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isSuccess()) {
                if (response.body().getResponse() != null) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void fetchClinics() {
        clinicResponceCall = NetworkProvider.provideNetworkMethods(this).getClinics(doctorId, Locale.getDefault().getDisplayLanguage());
        clinicResponceCall.enqueue(new Callback<ClinicGeneralResponse>() {
            @Override
            public void onResponse(Call<ClinicGeneralResponse> call, Response<ClinicGeneralResponse> response) {
                if (verifyClinicResponse(response)) {
                    clinics = response.body().getResponse().getClinics();
                    if (clinics.size() != 0){
                        noClinic.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        mDemoCollectionPagerAdapter =
                                new DemoCollectionPagerAdapter(
                                        getSupportFragmentManager(), clinics, doctorId);
                        viewpager.setAdapter(mDemoCollectionPagerAdapter);
                        tabLayout.setupWithViewPager(viewpager);
                    }else{
                        noClinic.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ClinicGeneralResponse> call, Throwable t) {
                Toast.makeText(DoctorDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteDoctor(){
        deleteDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).deleteDoctor(doctorId);
        deleteDoctorResponseCall.enqueue(new Callback<DeleteDoctorGeneralResponse>() {
            @Override
            public void onResponse(Call<DeleteDoctorGeneralResponse> call, Response<DeleteDoctorGeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body() != null) {
                            Toast.makeText(DoctorDetailActivity.this, R.string.doctor_deleted, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DoctorDetailActivity.this,DoctorsActivity.class));
                        } else {
                            Toast.makeText(DoctorDetailActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(DoctorDetailActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(DoctorDetailActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DeleteDoctorGeneralResponse> call, Throwable t) {
                Toast.makeText(DoctorDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        fetchDoctorDetail();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        responceCall.cancel();
        super.onStop();
    }
}


