package com.yackeen.livehealthy.sales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yackeen.livehealthy.sales.R;
import com.yackeen.livehealthy.sales.adapters.DoctorsAdapter;
import com.yackeen.livehealthy.sales.database.ClinicDataBase;
import com.yackeen.livehealthy.sales.database.CreateClinicDB;
import com.yackeen.livehealthy.sales.database.CreateDoctorDB;
import com.yackeen.livehealthy.sales.database.UpdateDoctorDb;
import com.yackeen.livehealthy.sales.model_dto.request.CreateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.request.DoctorsRequest;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorClinicsRequestBody;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.AllDoctorsResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.createDoctor.CreateDoctorResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.updateDoctor.UpdateDoctorResponse;
import com.yackeen.livehealthy.sales.models.Clinic;
import com.yackeen.livehealthy.sales.models.Doctor;
import com.yackeen.livehealthy.sales.network.NetworkProvider;
import com.yackeen.livehealthy.sales.util.Connectivity;
import com.yackeen.livehealthy.sales.util.EndlessRecyclerViewScrollListener;
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

public class DoctorsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int backageSize = 20;
    private static final String TAG = DoctorsActivity.class.getSimpleName();
    @BindView(R.id.doctors_toolbar)
    Toolbar doctorsToolbar;
    @BindView(R.id.doctors_recycler_view)
    RecyclerView doctorRecyclerView;
    @BindView(R.id.refresh_icon)
    ImageButton refreshIcon;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.add_floating_button)
    FloatingActionButton addFloatingActionButton;
    @BindView(R.id.no_data_text)
    TextView noDataText;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.un_saved_num)
    TextView unSaveCount;


    //variables
    boolean isSuccess;
    DoctorsAdapter doctorsAdapter;
    Call<AllDoctorsResponse> responceCall;
    Call<UpdateDoctorResponse> updateDoctorResponseCall;
    Call<CreateDoctorResponse> createDoctorResponse;
    Call<UpdateDoctorResponse> updateClinicResponseCall;
    Animation rotateAboutCenterAnimation;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageNum;
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        ButterKnife.bind(this);
        noDataText.setVisibility(View.GONE);
        searchKey = "";
        isSuccess = false;
        pageNum = 1;
        int requestNum = getRequestsNum();
        if (requestNum > 0) {
            unSaveCount.setVisibility(View.VISIBLE);
            unSaveCount.setText("" + requestNum);
        } else {
            unSaveCount.setVisibility(View.GONE);
        }
        rotateAboutCenterAnimation = AnimationUtils.loadAnimation(this, R.anim.loade_animation);
        rotateAboutCenterAnimation.setRepeatCount(Animation.INFINITE);
        if (!Connectivity.isConnected(this)) {
            noDataText.setVisibility(View.VISIBLE);
            noDataText.setText(R.string.network_error);
            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            noDataText.setVisibility(View.GONE);
        }
        fetchDoctors("");
        setSupportActionBar(doctorsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

        });
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mSwipeRefreshLayout.setRefreshing(true);
                pageNum = pageNum + 1;
                fetchDoctors(searchKey);
            }
        };

        doctorRecyclerView.setLayoutManager(linearLayout);
        doctorsAdapter = new DoctorsAdapter(this);
        doctorRecyclerView.setAdapter(doctorsAdapter);
        doctorRecyclerView.addOnScrollListener(scrollListener);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doctorsAdapter.clearAdapter();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pageNum = 1;
                searchKey = charSequence.toString();
                fetchDoctors(charSequence.toString());
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public DoctorsRequest getRequest(String name) {
        DoctorsRequest doctorsRequest = new DoctorsRequest();
        doctorsRequest.setName(name);
        doctorsRequest.setLang(Locale.getDefault().getDisplayLanguage());
        doctorsRequest.setNumberRecords(backageSize);
        doctorsRequest.setPageNumber(pageNum);
        return doctorsRequest;
    }

    public int getRequestsNum() {
        int sum = 0;
        List<CreateDoctorDB> createDoctorDBs = SQLite.select().from(CreateDoctorDB.class).queryList();
        List<UpdateDoctorDb> updateDoctorDbs = SQLite.select().from(UpdateDoctorDb.class).queryList();
        if (createDoctorDBs != null) {
            sum = sum + createDoctorDBs.size();
        }
        if (updateDoctorDbs != null) {
            sum = sum + updateDoctorDbs.size();
        }
        Log.d(TAG, "getRequestsNum: "+sum);
        return sum;
    }

    @OnClick(R.id.refresh_icon)
    public void refreshRequests() {
        Toast.makeText(this, "please waite untill save", Toast.LENGTH_SHORT).show();
        refreshIcon.startAnimation(rotateAboutCenterAnimation);
        if (Connectivity.isConnected(this)) {
            if (getRequestsNum() > 0) {
                List<CreateDoctorDB> createDoctorDBList = SQLite.select().from(CreateDoctorDB.class).queryList();
                List<UpdateDoctorDb> updateDoctorDbList = SQLite.select().from(UpdateDoctorDb.class).queryList();
                if (createDoctorDBList.size() > 0) {
                    for (int i = 0; i < createDoctorDBList.size(); i++) {
                        CreateDoctorDB createDoctorDB = createDoctorDBList.get(i);
                        CreateDoctorRequest createDoctorRequest = createDoctorRequest(createDoctorDB);
                        createDoctor(createDoctorRequest, createDoctorDB);
//                        if (isSuccess){
//                           createDoctorDB.delete();
//
//                            unSaveCount.setText(""+getRequestsNum());
//                        }
                    }
                }
                if (updateDoctorDbList.size() > 0) {
                    for (int i = 0; i < updateDoctorDbList.size(); i++) {
                        UpdateDoctorDb updateDoctorDb = updateDoctorDbList.get(i);
                        UpdateDoctorRequest updateDoctorRequest = getUpdateDoctorRequest(updateDoctorDb);
                        updateDoctor(updateDoctorRequest, updateDoctorDb);
//                        if (isSuccess){
//                            updateDoctorDb.delete();
//                            unSaveCount.setText(""+(Integer.parseInt(unSaveCount.getText().toString())-1));
//                        }
                    }
                }


                if (getRequestsNum() > 0) {
                    unSaveCount.setVisibility(View.VISIBLE);
                    unSaveCount.setText("" + getRequestsNum());
                } else {
                    unSaveCount.setVisibility(View.GONE);
                }
            } else {
                unSaveCount.setVisibility(View.GONE);
                Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();
            }
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchDoctors(String name) {
        noDataText.setVisibility(View.GONE);
        DoctorsRequest doctorsRequest = getRequest(name);
        responceCall = NetworkProvider.provideNetworkMethods(this).getDoctors(doctorsRequest);
        responceCall.enqueue(new Callback<AllDoctorsResponse>() {
            @Override
            public void onResponse(Call<AllDoctorsResponse> call, Response<AllDoctorsResponse> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    noDataText.setVisibility(View.GONE);
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            if (response.body().getDoctorsResponse().getDoctors() != null) {
                                List<Doctor> doctorList = response.body().getDoctorsResponse().getDoctors();
//                                progressBar.setVisibility(View.GONE);

                                if (doctorList.size() > 0) {
                                    noDataText.setVisibility(View.GONE);
                                    if (pageNum == 1) {
                                        doctorsAdapter.addAll(doctorList);
                                    } else {
                                        doctorsAdapter.addItems(doctorList);
                                    }
                                } else {
                                    noDataText.setText(R.string.no_data);
                                    noDataText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                noDataText.setText(R.string.no_data);
                                noDataText.setVisibility(View.VISIBLE);
//                                progressBar.setVisibility(View.GONE);

                            }
                        } else {
                            noDataText.setText(R.string.network_error);
                            noDataText.setVisibility(View.VISIBLE);
//                            progressBar.setVisibility(View.GONE);

                        }
                    } else {

                        noDataText.setText(R.string.no_data);
                        noDataText.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(DoctorsActivity.this, "" + response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<AllDoctorsResponse> call, Throwable t) {
                noDataText.setText(R.string.network_error);
                noDataText.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public UpdateDoctorRequest getUpdateDoctorRequest(UpdateDoctorDb updateDoctorDb) {
        UpdateDoctorRequest updateDoctorRequest = new UpdateDoctorRequest();
        updateDoctorRequest.setSpecialityID(updateDoctorDb.getSpecialityID());
        updateDoctorRequest.setDescrpition(updateDoctorDb.getDescrpition());
        updateDoctorRequest.setDescrpitionAR(updateDoctorDb.getDescrpitionAR());
        updateDoctorRequest.setDoctorID(updateDoctorDb.getDoctorID());
        updateDoctorRequest.setEmail(updateDoctorDb.getEmail());
        updateDoctorRequest.setMobileNumber(updateDoctorDb.getMobileNumber());
        updateDoctorRequest.setName(updateDoctorDb.getName());
        updateDoctorRequest.setNameAR(updateDoctorDb.getNameAR());
        updateDoctorRequest.setLang(updateDoctorDb.getLang());
        return updateDoctorRequest;
    }

    public CreateDoctorRequest createDoctorRequest(CreateDoctorDB createDoctorDB) {
        CreateDoctorRequest createDoctorRequest = new CreateDoctorRequest();
        createDoctorRequest.setLang(createDoctorDB.getLang());
        createDoctorRequest.setSpecialityID(createDoctorDB.getSpecialityID());
        createDoctorRequest.setName(createDoctorDB.getName());
        createDoctorRequest.setMobileNumber(createDoctorDB.getMobileNumber());
        createDoctorRequest.setDescription(createDoctorDB.getDescription());
        createDoctorRequest.setDescriptionAR(createDoctorDB.getDescriptionAR());
        createDoctorRequest.setEmail(createDoctorDB.getEmail());
        createDoctorRequest.setNameAR(createDoctorDB.getNameAR());
        createDoctorRequest.setTitle(createDoctorDB.getTitle());
        createDoctorRequest.setTitleAR(createDoctorDB.getTitleAR());
        createDoctorRequest.setGender(createDoctorDB.getGender());
        createDoctorRequest.setHouseVisit(createDoctorDB.isHouseVisit());
        createDoctorRequest.setConsultantID(createDoctorDB.getConsultantID());
        createDoctorRequest.setNursery(createDoctorDB.isNursery());
        createDoctorRequest.setLocation(createDoctorDB.getLocation());
        createDoctorRequest.setSocialMedia(createDoctorDB.getSocialMedia());
        return createDoctorRequest;
    }

    public List<Clinic> getClinicsFromClinicsDB(List<ClinicDataBase> clinicDataBaseList) {
        List<Clinic> clinics = new ArrayList<>();
        for (int i = 0; i < clinicDataBaseList.size(); i++) {
            Clinic clinic = new Clinic();
            ClinicDataBase clinicDataBase = clinicDataBaseList.get(i);
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
        return clinics;
    }

    public List<ClinicDataBase> getClinicsFromDb(long doctorId, List<CreateClinicDB> createClinicDBs) {
        List<CreateClinicDB> createClinicDBList = createClinicDBs;

        List<ClinicDataBase> clinicDataBaseList = new ArrayList<>();
        for (int i = 0; i < createClinicDBList.size(); i++) {
            List<ClinicDataBase> clinicDataBases = createClinicDBList.get(i).getClinicDataBaseList();
            clinicDataBaseList.addAll(clinicDataBases);
        }
        return clinicDataBaseList;
    }

    public UpdateDoctorClinicsRequestBody createClinicRequest(List<Clinic> clinics, long doctorId) {
        UpdateDoctorClinicsRequestBody clinicsRequestBody = new UpdateDoctorClinicsRequestBody();
        clinicsRequestBody.setDoctor_ID((int) doctorId);
        clinicsRequestBody.setLang(Locale.getDefault().getDisplayLanguage());
        clinicsRequestBody.setAllClinics(clinics);
        return clinicsRequestBody;
    }

    public void updateDoctor(UpdateDoctorRequest updateDoctorRequest, final UpdateDoctorDb updateDoctorDb) {
        refreshIcon.startAnimation(rotateAboutCenterAnimation);
        updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).updateDoctor(updateDoctorRequest);
        updateDoctorResponseCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        isSuccess = true;
                        rotateAboutCenterAnimation.reset();
                        if (updateDoctorDb.delete()) {
                            unSaveCount.setText("" + getRequestsNum());
                        }
                    } else {
                        isSuccess = false;
                        Toast.makeText(DoctorsActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isSuccess = false;
                    Toast.makeText(DoctorsActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();
            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                isSuccess = false;
                Toast.makeText(DoctorsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();
            }
        });

    }

    public void createDoctor(CreateDoctorRequest createDoctorRequest, final CreateDoctorDB createDoctorDB) {
        refreshIcon.startAnimation(rotateAboutCenterAnimation);
        createDoctorResponse = NetworkProvider.provideNetworkMethods(this).createNewDoctor(createDoctorRequest);
        createDoctorResponse.enqueue(new Callback<CreateDoctorResponse>() {
            @Override
            public void onResponse(Call<CreateDoctorResponse> call, Response<CreateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        isSuccess = true;
                        Toast.makeText(DoctorsActivity.this, R.string.doctor_added, Toast.LENGTH_SHORT).show();
                        rotateAboutCenterAnimation.reset();
                        List<CreateClinicDB> createClinicDBList = createDoctorDB.getCreateClinicDBList();
                        if (createClinicDBList != null && createClinicDBList.size() > 0) {
                            List<ClinicDataBase> clinicDataBases = getClinicsFromDb(createDoctorDB.getId(), createClinicDBList);
                            List<Clinic> clinics = getClinicsFromClinicsDB(clinicDataBases);
                            if (clinics != null && clinics.size() > 0) {
                                UpdateDoctorClinicsRequestBody updateDoctorClinicsRequestBody = createClinicRequest(clinics, response.body().getDoctorIdResponse().getDoctorID());
                                updateClinic(updateDoctorClinicsRequestBody, clinicDataBases, createClinicDBList, createDoctorDB);
                            }else {
                                if(createDoctorDB.delete()){
                                    unSaveCount.setText(""+getRequestsNum());
                                }
                            }
                        }else {
                        if(createDoctorDB.delete()){
                           unSaveCount.setText(""+getRequestsNum());
                        }
                        }
                    } else {

                        isSuccess = false;
                        Toast.makeText(DoctorsActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    isSuccess = false;
                    Toast.makeText(DoctorsActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();
            }


            @Override
            public void onFailure(Call<CreateDoctorResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                isSuccess = false;
                Toast.makeText(DoctorsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();


            }
        });

    }

    public Boolean verifyUpdateClinicResponse(Response<UpdateDoctorResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isSuccess()) {
                return true;

            } else {
                Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "verifyUpdateClinicResponse: " + response.body().getErrorMessage());

                return false;

            }
        } else {
            Toast.makeText(this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "verifyUpdateClinicResponse: " + response.body().getErrorMessage());

            return false;
        }
    }

    public void updateClinic(final UpdateDoctorClinicsRequestBody updateClinicsRequest,
                             final List<ClinicDataBase> clincs, final List<CreateClinicDB> createClinicDBList,
                             final CreateDoctorDB createDoctorDB) {
        refreshIcon.startAnimation(rotateAboutCenterAnimation);
        updateClinicResponseCall = NetworkProvider.provideNetworkMethods(this).updateClinic(updateClinicsRequest);
        updateClinicResponseCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (verifyUpdateClinicResponse(response)) {
                    List<ClinicDataBase> clinicDataBaseList = clincs;
                    for (int i = 0; i < clinicDataBaseList.size(); i++) {
                        ClinicDataBase clinicDataBase = clinicDataBaseList.get(i);
                        rotateAboutCenterAnimation.reset();
                        rotateAboutCenterAnimation.cancel();
                        if (clinicDataBase.delete()) {
                            Log.d(TAG, "onResponse: " + "ok" + i);
                        }
                    }
                    for (int i = 0; i < createClinicDBList.size(); i++) {
                        CreateClinicDB createClinicDB = createClinicDBList.get(i);
                        if (createClinicDB.delete()) {
                            Log.d(TAG, "onResponse:createClinic " + "ok" + i);
                        }
                    }
                    if (createDoctorDB.delete()) {
                        unSaveCount.setText("" + getRequestsNum());
                    }

                } else {
                    Log.e(TAG, "onResponse: " + response.body().getErrorMessage());
                    rotateAboutCenterAnimation.reset();
                    rotateAboutCenterAnimation.cancel();

                }

            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                rotateAboutCenterAnimation.reset();
                rotateAboutCenterAnimation.cancel();

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


    @OnClick(R.id.add_floating_button)
    public void addDoctor() {
        startActivity(new Intent(this, AddDoctorActivity.class));
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        if (!"".equals(searchKey)|| searchKey!= null){
            fetchDoctors(searchKey);
        }else {
            fetchDoctors("");
        }
    }

    @Override
    protected void onRestart() {
        fetchDoctors(searchEditText.getText().toString());
        mSwipeRefreshLayout.setRefreshing(true);
        if (getRequestsNum() > 0) {
            unSaveCount.setVisibility(View.VISIBLE);
            unSaveCount.setText("" + getRequestsNum());
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (responceCall != null) {
            responceCall.cancel();
        }
        if (updateDoctorResponseCall != null) {
            updateDoctorResponseCall.cancel();
        }
        if (createDoctorResponse != null) {
            createDoctorResponse.cancel();
        }
        super.onStop();
    }

}
