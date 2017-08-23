package com.example.ahmedessam.livehealthysales.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.adapters.ClinicsAdapter;
import com.example.ahmedessam.livehealthysales.adapters.DemoCollectionPagerAdapter;
import com.example.ahmedessam.livehealthysales.database.ClinicDataBase;
import com.example.ahmedessam.livehealthysales.database.ClinicDataBase_Table;
import com.example.ahmedessam.livehealthysales.database.CreateClinicDB;
import com.example.ahmedessam.livehealthysales.database.CreateClinicDB_Table;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.clinic.ClinicGeneralResponse;
import com.example.ahmedessam.livehealthysales.models.Clinic;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClinicsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.editScheduleRecycler)
    RecyclerView clinicsRecycler;
    @BindView(R.id.add_clinic_button)
    Button addClinic;
    private ClinicsAdapter adapter;
    Call<ClinicGeneralResponse> clinicResponceCall;
    private static final String ARD_DOCTOR_ID = "doctor_id";
    private static final String ARD_ONLINE = "online";

    int doctorID;
    ArrayList<Clinic> clinics;
    boolean isOnline;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics);
        clinics = new ArrayList<>();
        ButterKnife.bind(this);
        if (savedInstanceState!=null){
            isOnline = savedInstanceState.getBoolean(ARD_ONLINE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.clinics);
        addClinic.setEnabled(false);
        doctorID = getDoctorId();
        adapter = new ClinicsAdapter(new ArrayList<Clinic>(), false ,doctorID,isOnline);
        clinicsRecycler.setAdapter(adapter);
        clinicsRecycler.setLayoutManager(new LinearLayoutManager(this));
        fetchClinics();
    }

    public static Intent newClinics(Context context,int doctor_id ,boolean online){
        Intent intent = new Intent(context,ClinicsActivity.class);
        intent.putExtra(ARD_DOCTOR_ID,doctor_id);
        intent.putExtra(ARD_ONLINE,online);
        return intent;
    }

    @OnClick(R.id.add_clinic_button)
    public void addClinic(){
        EditClinicActivity.start(ClinicsActivity.this,clinics,-2,(long)doctorID,true,isOnline);
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
        progressBar.setVisibility(View.VISIBLE);
        fetchClinics();
        super.onRestart();
    }

    public int getDoctorId(){
        isOnline = getIntent().getBooleanExtra(ARD_ONLINE,false);
        return getIntent().getIntExtra(ARD_DOCTOR_ID,0);
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (clinicResponceCall != null) {
            clinicResponceCall.cancel();
        }
        super.onStop();
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

    public void getClinicsFromClinicRequest(List<ClinicDataBase> clinicDataBaseList){
        clinics.clear();
        for (int i=0 ; i<clinicDataBaseList.size();i++){
            ClinicDataBase clinicDataBase = clinicDataBaseList.get(i);
            Clinic clinic = new Clinic();
            clinic.setClinicID(0);
            clinic.setAddress(clinicDataBase.getAddress());
            clinic.setAddressAR(clinicDataBase.getAddressAR());
            clinic.setAreaID(clinicDataBase.getAreaID());
            clinic.setAreaName(clinicDataBase.getAreaName());
            clinic.setCityID(clinicDataBase.getCityID());
            clinic.setCityName(clinicDataBase.getCityName());
            clinic.setClinicName(clinicDataBase.getClinicName());
            clinic.setClinicNameAR(clinicDataBase.getClinicNameAR());
            clinic.setDiscount(clinicDataBase.getDiscount());
            clinic.setLandLine(clinicDataBase.getLandLine());
            clinic.setEditable(clinicDataBase.isEditable());
            clinic.setMobileNumber(clinicDataBase.getMobileNumber());
            clinic.setPrice(clinicDataBase.getPrice());
            clinic.setRequestsPerDay(clinicDataBase.getRequestsPerDay());
            clinics.add(clinic);
        }
    }
    public void fetchClinics() {
        if (!isOnline){
            List<CreateClinicDB> createClinicDBs = SQLite.select().from(CreateClinicDB.class).where(CreateClinicDB_Table.doctor_id.eq((long) doctorID)).queryList();
            if (createClinicDBs != null &&createClinicDBs.size()>0 ) {
                List<ClinicDataBase> clinicDataBases = createClinicDBs.get(0).getClinicDataBaseList();
                getClinicsFromClinicRequest(clinicDataBases);
                adapter.setClinics(clinics);
                addClinic.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.GONE);
                addClinic.setEnabled(true);
            }
            return;
        }
        clinicResponceCall = NetworkProvider.provideNetworkMethods(this).getClinics(doctorID, Locale.getDefault().getDisplayLanguage());
        clinicResponceCall.enqueue(new Callback<ClinicGeneralResponse>() {
            @Override
            public void onResponse(Call<ClinicGeneralResponse> call, Response<ClinicGeneralResponse> response) {
                if (verifyClinicResponse(response)) {
                   clinics = (ArrayList<Clinic>) response.body().getResponse().getClinics();
                    if (clinics != null) {
                        adapter.setClinics(clinics);
                        addClinic.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ClinicGeneralResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ClinicsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(ARD_ONLINE,isOnline);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
