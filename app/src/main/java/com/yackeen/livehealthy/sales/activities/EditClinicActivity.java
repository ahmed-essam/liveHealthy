package com.yackeen.livehealthy.sales.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yackeen.livehealthy.sales.R;
import com.yackeen.livehealthy.sales.adapters.ScheduleAdapter;
import com.yackeen.livehealthy.sales.database.ClinicDataBase;
import com.yackeen.livehealthy.sales.database.CreateClinicDB;
import com.yackeen.livehealthy.sales.database.CreateClinicDB_Table;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorClinicsRequestBody;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorTimesRequestBody;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.Areas.Area;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.Areas.AreaGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.cities.CitiesGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.cities.City;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.getAllAreas.AllArea;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.getAllAreas.AllArea_Table;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.schedule.DaysGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.updateDoctor.UpdateDoctorResponse;
import com.yackeen.livehealthy.sales.models.Clinic;
import com.yackeen.livehealthy.sales.models.Day;
import com.yackeen.livehealthy.sales.network.NetworkProvider;
import com.yackeen.livehealthy.sales.util.AddScheduleDialog;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isDigitsOnly;
import static android.text.TextUtils.isEmpty;

public class EditClinicActivity extends AppCompatActivity implements AddScheduleDialog.onDismissListener, ScheduleAdapter.onItemRemovedListener {


    private static final String KEY_INDEX = "INDEX";
    private static final String Doctor_id = "DoctorID";
    private static final String Create = "isCreate";
    private static final String KEY_CLINICS = "CLINICS";
    private static final String KEY_ONLINE = "online";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.name)
    TextInputEditText name;
    @BindView(R.id.nameAr)
    TextInputEditText nameAr;
    @BindView(R.id.city)
    TextInputEditText city;
    @BindView(R.id.area)
    TextInputEditText area;

    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.addressAr)
    TextInputEditText addressAr;
    @BindView(R.id.mobileNumber)
    TextInputEditText mobileNumber;
    @BindView(R.id.price)
    TextInputEditText price;
    @BindView(R.id.discount)
    TextInputEditText discount;
    @BindView(R.id.requests)
    TextInputEditText requests;
    @BindView(R.id.scheduleRecycler)
    RecyclerView scheduleRecycler;
    @BindView(R.id.addSchedule)
    ImageButton addScedule;
    @BindView(R.id.schedule_layout)
    LinearLayout scheduleLayout;

    private ArrayList<Clinic> clinics;
    private int index;
    private Clinic clinic;
    private ScheduleAdapter adapter;
    private List<Day> days;
    private long doctorID;
    Call<DaysGeneralResponse> daysGeneralResponseCall;
    Call<UpdateDoctorResponse> updateDoctorResponseCall;
    Call<UpdateDoctorResponse> updateDoctorTimesCall;
    Call<CitiesGeneralResponse> citiesResponseCall;
    Call<AreaGeneralResponse> areaResponseCall;
    private final String TAG = "EditClinicActivity";

    private List<City> cities;
    private List<Area> areas;
    private ArrayAdapter<City> citiesArrayAdapter;
    private ArrayAdapter<Area> areasArrayAdapter;
    private ListPopupWindow
            citiesPopupWindow,
            areasPopUpWindow;

    private int areaID, cityID;
    boolean isCreate;
    boolean isOnline;

    public static void start(Context context, ArrayList<Clinic> clinics, int index, long doctorID , boolean create , boolean online) {
        Intent starter = new Intent(context, EditClinicActivity.class);
        starter.putParcelableArrayListExtra(KEY_CLINICS, clinics);
        starter.putExtra(KEY_INDEX, index);
        starter.putExtra(Doctor_id, doctorID);
        starter.putExtra(Create, create);
        starter.putExtra(KEY_ONLINE,online);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clinic);
        ButterKnife.bind(this);
        days = new ArrayList<>();
        cities = new ArrayList<>();
        areas = new ArrayList<>();

        if (getIntent() != null) {
            index = getIntent().getIntExtra(KEY_INDEX, -1);
            isOnline = getIntent().getBooleanExtra(KEY_ONLINE,false);
            clinics = getIntent().getParcelableArrayListExtra(KEY_CLINICS);
            if (index == -1 || clinics == null || clinics.size() <= index || Doctor_id == null) {
                finish();
                return;
            }
            doctorID = getIntent().getLongExtra(Doctor_id, 0);
            if (index == -2){
                clinic = new Clinic();
            }else {
                clinic = clinics.get(index);
            }
            isCreate = getIntent().getBooleanExtra(Create,false);
        }
        if (isCreate){
            scheduleRecycler.setVisibility(View.GONE);
            addScedule.setVisibility(View.GONE);

        }
        if (!isOnline){
            scheduleRecycler.setVisibility(View.GONE);
            scheduleLayout.setVisibility(View.GONE);
        }else{
            scheduleRecycler.setVisibility(View.VISIBLE);
            scheduleLayout.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Locale.getDefault().getDisplayLanguage() == "english")
            setTitle(clinic.getClinicName());
        else
            setTitle(clinic.getClinicNameAR());

        adapter = new ScheduleAdapter(new ArrayList<Day>(), this,this);
        adapter.setHideDelete(false);

        scheduleRecycler.setAdapter(adapter);
        scheduleRecycler.setLayoutManager(new LinearLayoutManager(this));
        scheduleRecycler.setNestedScrollingEnabled(false);
        if (!isCreate) {
            if (isOnline) {
                fetchSchedule();
            }
            showDate();
        }else{
            if (isOnline) {
                fetchCities();
                fetchAreas();
            }else{
                fetchCitiesFromDB();
                fetchAreasFromDB(cityID);
            }
        }

    }


    private void showDate() {
            name.setText(clinic.getClinicName());
            nameAr.setText(clinic.getClinicNameAR());
            requests.setText(String.valueOf(clinic.getRequestsPerDay()));
            price.setText(String.valueOf(clinic.getPrice()));
            addressAr.setText(clinic.getAddressAR());
            address.setText(clinic.getAddress());
            mobileNumber.setText(clinic.getMobileNumber());
            discount.setText(String.valueOf(clinic.getDiscount()));
            city.setText(clinic.getCityName());
            area.setText(clinic.getAreaName());
            cityID = clinic.getCityID();
            areaID = clinic.getAreaID();
        if (isOnline) {
            fetchCities();
            fetchAreas();
        }else{
            fetchCitiesFromDB();
            fetchAreasFromDB(cityID);
        }
    }

    public void fetchCitiesFromDB(){
        cities = SQLite.select().from(City.class).queryList();
    }
    public void fetchAreasFromDB(int cityId){
        List<AllArea> allAreas = SQLite.select().from(AllArea.class).where(AllArea_Table.City_ID.eq(cityId)).queryList();
        List<Area> areaList = new ArrayList<>();
        for (int i = 0; i<allAreas.size();i++){
            AllArea allArea = allAreas.get(i);
            Area area = new Area();
            area.setID(allArea.getID());
            area.setName(allArea.getName());
            area.setName_AR(allArea.getName_AR());
            areaList.add(area);
        }
        areas.clear();
        areas.addAll(areaList);
    }
    public void saveCitiesInDB(List<City> cityList){
        City.clearCitiesDB();
        for (int i =0 ; i<cityList.size();i++){
            City city = cityList.get(i);
            city.save();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                save();
                return true;
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveInDB( List<Clinic> clinics){
        CreateClinicDB createClinicDB = new CreateClinicDB();
        createClinicDB.setLang(Locale.getDefault().getDisplayLanguage());
        createClinicDB.setDoctor_ID(doctorID);
        createClinicDB.save();
        CreateClinicDB createclinic= SQLite.select(CreateClinicDB_Table.clinicID).from(CreateClinicDB.class)
                .where(CreateClinicDB_Table.doctor_id.eq(doctorID)).querySingle();
        int clinicId = createclinic.getClinicID();
        for (int i = 0;i<clinics.size();i++) {
            Clinic clinic = clinics.get(i);
            ClinicDataBase clinicDataBase = new ClinicDataBase();
            clinicDataBase.setMobileNumber(clinic.getMobileNumber());
            clinicDataBase.setRequestsPerDay(clinic.getRequestsPerDay());
            clinicDataBase.setPrice(clinic.getPrice());
            clinicDataBase.setAddress(clinic.getAddress());
            clinicDataBase.setAddressAR(clinic.getAddressAR());
            clinicDataBase.setAreaID(clinic.getAreaID());
            clinicDataBase.setAreaName(clinic.getAreaName());
            clinicDataBase.setCityID(clinic.getCityID());
            clinicDataBase.setCityName(clinic.getCityName());
            clinicDataBase.setClinicID(clinic.getClinicID());
            clinicDataBase.setClinicName(clinic.getClinicName());
            clinicDataBase.setClinicNameAR(clinic.getClinicNameAR());
            clinicDataBase.setDiscount(clinic.getDiscount());
            clinicDataBase.setAreaID(clinic.getAreaID());
            clinicDataBase.setEditable(true);
            clinicDataBase.setAreaName(clinic.getAreaName());
            clinicDataBase.setLandLine(clinic.getLandLine());
            clinicDataBase.setRequestId(clinicId);
            clinicDataBase.save();
        }

    }

    private void save() {
        if (!validate())
            return;
        progressBar.setVisibility(View.VISIBLE);
        clinic.setAddressAR(addressAr.getText().toString());
        clinic.setAddress(address.getText().toString());
        clinic.setMobileNumber(mobileNumber.getText().toString());
        clinic.setClinicName(name.getText().toString());
        clinic.setClinicNameAR(nameAr.getText().toString());
        clinic.setRequestsPerDay(Integer.parseInt(requests.getText().toString()));
        clinic.setPrice(Integer.parseInt(price.getText().toString()));
        clinic.setDiscount(Integer.parseInt(discount.getText().toString()));
        clinic.setAreaID(areaID);
        clinic.setCityID(cityID);
        clinic.setCityName(city.getText().toString());
        clinic.setAreaName(area.getText().toString());
        if (isCreate){
            clinic.setClinicID(0);
            clinics.add(clinic);
            if (isOnline) {
                UpdateDoctorClinicsRequestBody clinicsRequestBody = new UpdateDoctorClinicsRequestBody();
                clinicsRequestBody.setDoctor_ID((int) doctorID);
                clinicsRequestBody.setLang(Locale.getDefault().getDisplayLanguage());
                clinicsRequestBody.setAllClinics(clinics);
                updateClinic(clinicsRequestBody);
            }else{
                saveInDB(clinics);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this,R.string.request_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {

            UpdateDoctorClinicsRequestBody clinicsRequestBody = new UpdateDoctorClinicsRequestBody();
            clinicsRequestBody.setDoctor_ID((int) doctorID);
            clinicsRequestBody.setLang(Locale.getDefault().getDisplayLanguage());
            clinicsRequestBody.setAllClinics(clinics);
            updateClinic(clinicsRequestBody);

            UpdateDoctorTimesRequestBody body = new UpdateDoctorTimesRequestBody()
                    .setDaysItems(days)
                    .setClinicID(clinic.getClinicID())
                    .setDoctorID(doctorID);
            updateTimes(body);
        }
    }

    private boolean validate() {
        boolean valid = true;
        if (isEmpty(name.getText())) {
            name.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            name.setError(null);
        }
        if (isEmpty(nameAr.getText())) {
            nameAr.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            nameAr.setError(null);
        }

        if (isEmpty(city.getText())) {
            city.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            city.setError(null);
        }
        if (isEmpty(area.getText())) {
            area.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            area.setError(null);
        }

        if (isEmpty(discount.getText()) || !isDigitsOnly(discount.getText())) {
            discount.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            discount.setError(null);
        }
        if (isEmpty(requests.getText()) || !isDigitsOnly(requests.getText())) {
            requests.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            requests.setError(null);
        }
        if (isEmpty(price.getText()) || !isDigitsOnly(price.getText())) {
            price.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            price.setError(null);
        }
        if (isEmpty(address.getText())) {
            address.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            address.setError(null);
        }
        if (isEmpty(addressAr.getText())) {
            addressAr.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            addressAr.setError(null);
        }
        if (isEmpty(mobileNumber.getText())) {
            mobileNumber.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            mobileNumber.setError(null);
        }

        return valid;
    }


    @OnClick(R.id.addSchedule)
    public void onViewClicked() {
        if (! isCreate) {
            new AddScheduleDialog(this, this).show();
        }
    }

    @Override
    public void onDismiss(AddScheduleDialog dialog) {
        if (dialog.isResultOk()) {
            Day item = new Day();
            item.setDay(dialog.getDayText());
            item.setDayId(dialog.getDay());
            item.setFromHour(dialog.getFromText());
            item.setToHour(dialog.getToText());
            days.add(item);
            adapter.notifyDataSetChanged();
            scheduleRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemRemoved() {

    }


    public Boolean verifyResponse(Response<DaysGeneralResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isSuccess()) {
                if (response.body().getResponse().getDays() != null) {
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


    public void fetchSchedule() {
        progressBar.setVisibility(View.VISIBLE);
        daysGeneralResponseCall = NetworkProvider.provideNetworkMethods(this).getSchedule(doctorID, clinic.getClinicID(), Locale.getDefault().getDisplayLanguage());
        daysGeneralResponseCall.enqueue(new Callback<DaysGeneralResponse>() {
            @Override
            public void onResponse(Call<DaysGeneralResponse> call, Response<DaysGeneralResponse> response) {
                if (verifyResponse(response)) {
                    progressBar.setVisibility(View.GONE);
                    days.addAll(response.body().getResponse().getDays());
                    adapter.setDays(days);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DaysGeneralResponse> call, Throwable t) {
                Toast.makeText(EditClinicActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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
                            cities.clear();
                            cities.addAll(response.body().getCities());
                            saveCitiesInDB(response.body().getCities());
                            if (citiesArrayAdapter != null) {
                                citiesArrayAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(EditClinicActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<CitiesGeneralResponse> call, Throwable t) {
                Toast.makeText(EditClinicActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    public void fetchAreas() {
        areaResponseCall = NetworkProvider.provideNetworkMethods(this).getAreas(cityID, 1, 1000);
        areaResponseCall.enqueue(new Callback<AreaGeneralResponse>() {
            @Override
            public void onResponse(Call<AreaGeneralResponse> call, Response<AreaGeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body().getAreas() != null) {
                            areas.clear();
                            areas.addAll(response.body().getAreas());
                            if (areasArrayAdapter != null) {
                                areasArrayAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(EditClinicActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AreaGeneralResponse> call, Throwable t) {
                Toast.makeText(EditClinicActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
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

    public void updateClinic(UpdateDoctorClinicsRequestBody updateClinicsRequest) {
        progressBar.setVisibility(View.VISIBLE);
        updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).updateClinic(updateClinicsRequest);
        updateDoctorResponseCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (verifyUpdateClinicResponse(response)) {
                    progressBar.setVisibility(View.GONE);
                    if (isCreate){
                        Toast.makeText(EditClinicActivity.this, R.string.clinic_added, Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                        Toast.makeText(EditClinicActivity.this, R.string.clinic_updated, Toast.LENGTH_SHORT).show();
                    }
                    isCreate = false;
                } else {
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                Toast.makeText(EditClinicActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });


    }

    public void updateTimes(UpdateDoctorTimesRequestBody updatetimesRequest) {
        progressBar.setVisibility(View.VISIBLE);
        updateDoctorTimesCall = NetworkProvider.provideNetworkMethods(this).updateClinicTimes(updatetimesRequest);
        updateDoctorTimesCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        finish();
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditClinicActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                Toast.makeText(EditClinicActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @OnTouch(R.id.city)
    public boolean selectCity(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (citiesPopupWindow == null) {
            citiesPopupWindow = new ListPopupWindow(this);
            citiesPopupWindow.setAnchorView(v);
            citiesPopupWindow.setWidth(v.getWidth());
            if (citiesArrayAdapter == null)
                citiesArrayAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        cities
                );
            citiesPopupWindow.setAdapter(citiesArrayAdapter);
            citiesPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    City city = cities.get(position);
                    EditClinicActivity.this.area.setText("");
                    EditClinicActivity.this.city.setText(city.getName());
                    citiesPopupWindow.dismiss();
                    cityID = city.getID();

                }
            });
        }
        citiesPopupWindow.show();
        return true;
    }

    @OnTouch(R.id.area)
    public boolean selectArea(View v, MotionEvent event) {
        if (!isOnline){
            fetchAreasFromDB(cityID);
        }else {
            fetchAreas();
        }
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (areasPopUpWindow == null) {
            areasPopUpWindow = new ListPopupWindow(this);
            areasPopUpWindow.setAnchorView(v);
            areasPopUpWindow.setWidth(v.getWidth());
            if (areasArrayAdapter == null)
                areasArrayAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        areas
                );
            areasPopUpWindow.setAdapter(areasArrayAdapter);
            areasPopUpWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Area area = areas.get(position);
                    EditClinicActivity.this.area.setText(area.getName());
                    areasPopUpWindow.dismiss();
                    areaID = area.getID();
                }
            });
        }
        areasPopUpWindow.show();
        return true;
    }


    @Override
    protected void onStop() {
        if (updateDoctorResponseCall != null) {
            if (!updateDoctorResponseCall.isCanceled()) {
                updateDoctorResponseCall.cancel();
            }
        }
        if (daysGeneralResponseCall != null) {
            if (!daysGeneralResponseCall.isCanceled()) {
                daysGeneralResponseCall.cancel();
            }
        }
        if (updateDoctorTimesCall != null) {
            if (!updateDoctorTimesCall.isCanceled()) {
                updateDoctorTimesCall.cancel();
            }
        }
        if (citiesResponseCall != null) {
            if (!citiesResponseCall.isCanceled()) {
                citiesResponseCall.cancel();
            }
        }
        if (areaResponseCall != null) {
            if (!areaResponseCall.isCanceled()) {
                areaResponseCall.cancel();
            }
        }
        super.onStop();
    }
}
