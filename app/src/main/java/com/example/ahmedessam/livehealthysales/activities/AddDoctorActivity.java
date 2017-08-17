package com.example.ahmedessam.livehealthysales.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.database.CreateDoctorDB;
import com.example.ahmedessam.livehealthysales.database.UpdateDoctorDb;
import com.example.ahmedessam.livehealthysales.model_dto.request.CreateDoctorRequest;
import com.example.ahmedessam.livehealthysales.model_dto.request.UpdateDoctorRequest;
import com.example.ahmedessam.livehealthysales.model_dto.response.GeoCoderResult;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality.Speciality;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.updateDoctor.UpdateDoctorResponse;
import com.example.ahmedessam.livehealthysales.models.DoctorDetail;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.example.ahmedessam.livehealthysales.util.Connectivity;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class AddDoctorActivity extends BaseLocationActivity {
    private static final String ARG_Detail = "doctor_detail";
    private static final String TAG = "add_doctor_activity";
    @BindView(R.id.doctor_name_edit)
    EditText doctorNameEditText;
    @BindView(R.id.doctor_name_ar_edit)
    EditText doctorNameAREditText;
    @BindView(R.id.tittle)
    EditText tittle;
    @BindView(R.id.tittle_ar)
    EditText tittleAR;
    @BindView(R.id.house_visit)
    EditText houseVisit;
    @BindView(R.id.gender)
    EditText gender;
    @BindView(R.id.nursing)
    EditText nursery;
    @BindView(R.id.consultant)
    EditText consultantEditText;
    @BindView(R.id.address_edit_text)
    EditText addressEditText;
    @BindView(R.id.phone_number_edit_text)
    EditText pphoneEditText;
    @BindView(R.id.speciality_spinner)
    EditText specialitySpinner;
    @BindView(R.id.location_edit_text)
    EditText location;
    @BindView(R.id.location_progress)
    ProgressBar locationProgress;
    @BindView(R.id.location_icon)
    ImageView locationIcon;
    @BindView(R.id.save_icon)
    ImageView saveIcon;
    @BindView(R.id.add_doctor_toolbar)
    Toolbar toolbar;
    @BindView(R.id.arabic_description)
    EditText arabicDescription;
    @BindView(R.id.english_description)
    EditText englishDescription;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.social_media)
    EditText socialMedia;
    @BindView(R.id.edit_clinics)
    Button editClinicButton;
    @BindView(R.id.progress_bar)
    ProgressBar progress;
    @BindView(R.id.nested_scroll)
    NestedScrollView nestedScroll;

    boolean internetConnection;
    Call<SpecialityGeneralResponse> specialityGeneralResponseCall;
    Call<UpdateDoctorResponse> updateDoctorResponseCall;
    Call<GeoCoderResult> geoCoderResultCall;
    boolean newDoctor;
    boolean houseVistBoolean;
    int conultant;
    boolean nurseing;
    String lonL, latL;
    ArrayAdapter<CharSequence> adapter;
    DoctorDetail doctorDetail;
    private String locationCor;
    private List<Speciality> specialites;
    private ListPopupWindow specialtiesPopupWindow;
    private ListPopupWindow genderListPopupWindow;
    private ListPopupWindow consultantListPopupWindow;
    private ListPopupWindow nurseryListPopupWindow;
    private ListPopupWindow housevisitListPopupWindow;
    private ArrayAdapter<Speciality> specialtiesArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;
    private ArrayAdapter<String> consultanArrayAdapter;
    private ArrayAdapter<String> houseVisitArrayAdapter;
    private ArrayAdapter<String> nursingArrayAdapter;
    private int specialtyID;

    public static Intent newAddDoctorIntent(Context context, DoctorDetail doctorDetail) {
        Intent intent = new Intent(context, AddDoctorActivity.class);
        intent.putExtra(ARG_Detail, doctorDetail);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        ButterKnife.bind(this);
        newDoctor = true;
        if (Connectivity.isConnected(this)){
            internetConnection = true;
        }else{
            internetConnection = false;
        }
        specialites = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().hasExtra(ARG_Detail)) {
            doctorDetail = getDoctorDetail();
            addViewValues(doctorDetail);
            getSpecialities();
            newDoctor = false;
            specialtyID = doctorDetail.getSpecialityID();
        } else {
            editClinicButton.setEnabled(false);
            editClinicButton.setVisibility(View.GONE);
        }

    }

    public DoctorDetail getDoctorDetail() {
        Intent intent = getIntent();
        DoctorDetail doctorDetail = (DoctorDetail) intent.getSerializableExtra(ARG_Detail);
        return doctorDetail;
    }

    @OnClick(R.id.location_icon)
    public void getlocationClick() {
        locationIcon.setVisibility(View.GONE);
        locationProgress.setVisibility(View.VISIBLE);
        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {

        lonL = String.valueOf(location.getLongitude());
        latL = String.valueOf(location.getLatitude());

        locationCor = String.format(Locale.US, "%s,%s", location.getLatitude(), location.getLongitude());
        String text = "";
        try {
            List<Address> list = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list.size() != 0) {
                for (int i = 0; i < list.get(0).getMaxAddressLineIndex(); i++) {
                    text += list.get(0).getAddressLine(i) + ",";
                }
                this.location.setText(text);
                locationProgress.setVisibility(View.GONE);
                locationIcon.setVisibility(View.VISIBLE);
            } else {
                getLocationFromNetwork();
            }

        } catch (IOException e) {
            e.printStackTrace();
            getLocationFromNetwork();
        }
    }

    private void getLocationFromNetwork() {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latL + "," + lonL + "&sensor=true";
        geoCoderResultCall = NetworkProvider.provideNetworkMethods(AddDoctorActivity.this).getLocationAddress(url);
        geoCoderResultCall.enqueue(new Callback<GeoCoderResult>() {
            @Override
            public void onResponse(Call<GeoCoderResult> call, Response<GeoCoderResult> response) {
                if (response.isSuccessful()){
                    GeoCoderResult result = response.body();
                    if (result != null && result.results != null && result.results.size() > 0)
                        location.setText(result.results.get(0).formattedAddress);
                    locationProgress.setVisibility(View.GONE);
                }else{
                    Toast.makeText(AddDoctorActivity.this, R.string.no_location, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeoCoderResult> call, Throwable t) {
                Toast.makeText(AddDoctorActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void addViewValues(DoctorDetail doctorDetail) {
        doctorNameEditText.setText(doctorDetail.getName());
        doctorNameAREditText.setText(doctorDetail.getNameAR());
        addressEditText.setText(doctorDetail.getLocation());
        pphoneEditText.setText(doctorDetail.getMobileNumber());
        arabicDescription.setText(doctorDetail.getDescriptionAR());
        englishDescription.setText(doctorDetail.getDescription());
        specialitySpinner.setText(doctorDetail.getSpeciality());
        emailEditText.setText(doctorDetail.getEmail().toString());
        location.setText(doctorDetail.getLocation());
    }

    public void saveCreateInDB(CreateDoctorRequest createDoctorRequest){
        CreateDoctorDB createDoctorDB = new CreateDoctorDB();
        createDoctorDB.setSocialMedia(createDoctorRequest.getSocialMedia());
        createDoctorDB.setConsultantID(createDoctorRequest.getConsultantID());
        createDoctorDB.setDescription(createDoctorRequest.getDescription());
        createDoctorDB.setDescriptionAR(createDoctorRequest.getDescriptionAR());
        createDoctorDB.setEmail(createDoctorRequest.getEmail());
        createDoctorDB.setGender(createDoctorRequest.getGender());
        createDoctorDB.setHouseVisit(createDoctorRequest.getHouseVisit());
        createDoctorDB.setLandLine(createDoctorRequest.getLandLine());
        createDoctorDB.setLang(createDoctorRequest.getLang());
        createDoctorDB.setLocation(createDoctorRequest.getLocation());
        createDoctorDB.setMobileNumber(createDoctorRequest.getMobileNumber());
        createDoctorDB.setName(createDoctorRequest.getName());
        createDoctorDB.setNameAR(createDoctorRequest.getNameAR());
        createDoctorDB.setNursery(createDoctorRequest.getNursery());
        createDoctorDB.setSpecialityID((int)specialtyID);
        createDoctorDB.setTitle(createDoctorRequest.getTitle());
        createDoctorDB.setTitleAR(createDoctorRequest.getTitleAR());
        createDoctorDB.save();
    }

    public void saveUpdateInDB(UpdateDoctorRequest updateDoctorRequest){
        UpdateDoctorDb updateDoctorDb = new UpdateDoctorDb();
        updateDoctorDb.setSpecialityID((int)specialtyID);
        updateDoctorDb.setNameAR(updateDoctorRequest.getNameAR());
        updateDoctorDb.setName(updateDoctorRequest.getName());
        updateDoctorDb.setDescrpition(updateDoctorRequest.getDescrpition());
        updateDoctorDb.setDescrpitionAR(updateDoctorRequest.getDescrpitionAR());
        updateDoctorDb.setDoctorID(updateDoctorRequest.getDoctorID());
        updateDoctorDb.setEmail(updateDoctorRequest.getEmail());
        updateDoctorDb.setLang(updateDoctorRequest.getLang());
        updateDoctorDb.setMobileNumber(updateDoctorDb.getMobileNumber());
        updateDoctorDb.save();
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

    public void getSpecialityFromDB(){
        List<Speciality> specialityList = SQLite.select().from(Speciality.class).queryList();
        if (specialityList.size() !=0){
            specialites.clear();
            specialites.addAll(specialityList);
        }
    }

    @OnClick(R.id.save_icon)
    public void save() {
        if (verify()) {
            nestedScroll.fullScroll(View.FOCUS_UP);
            saveIcon.setEnabled(false);
            progress.setVisibility(View.VISIBLE);

            updateDoctor();

        }else{
            Toast.makeText(this,R.string.empty_feild, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.edit_clinics)
    public void EditClinic() {
        Intent intent = ClinicsActivity.newClinics(this, doctorDetail.getDoctorID());
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean verify() {
        boolean valid = true;
        if (isEmpty(gender.getText())) {
            gender.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            gender.setError(null);
        }
        if (isEmpty(consultantEditText.getText())) {
            consultantEditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            consultantEditText.setError(null);
        }
        if (isEmpty(nursery.getText())) {
            nursery.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            nursery.setError(null);
        }
        if (isEmpty(tittle.getText())) {
            tittle.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            tittle.setError(null);
        }
        if (isEmpty(tittleAR.getText())) {
            tittleAR.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            tittleAR.setError(null);
        }
        if (isEmpty(doctorNameEditText.getText())) {
            doctorNameEditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            doctorNameEditText.setError(null);
        }
        if (isEmpty(doctorNameAREditText.getText())) {
            doctorNameAREditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            doctorNameAREditText.setError(null);
        }
        if (isEmpty(houseVisit.getText())) {
            houseVisit.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            houseVisit.setError(null);
        }

        if (isEmpty(emailEditText.getText())) {
            emailEditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        if (isEmpty(englishDescription.getText())) {
            englishDescription.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            englishDescription.setError(null);
        }
        if (isEmpty(arabicDescription.getText())) {
            arabicDescription.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            arabicDescription.setError(null);
        }
        if (isEmpty(specialitySpinner.getText())) {
            specialitySpinner.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            specialitySpinner.setError(null);
        }
        if (isEmpty(addressEditText.getText())) {
            addressEditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            addressEditText.setError(null);
        }
        if (isEmpty(socialMedia.getText())) {
            socialMedia.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            socialMedia.setError(null);
        }
        if (isEmpty(location.getText())) {
            location.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            location.setError(null);
        }
        return valid;
    }

    public UpdateDoctorRequest getUpdateDoctorRequest() {
        UpdateDoctorRequest updateDoctorRequest = new UpdateDoctorRequest();
        updateDoctorRequest.setDescrpition(englishDescription.getText().toString());
        updateDoctorRequest.setDescrpitionAR(arabicDescription.getText().toString());
        updateDoctorRequest.setDoctorID(doctorDetail.getDoctorID());
        updateDoctorRequest.setEmail(emailEditText.getText().toString());
        updateDoctorRequest.setMobileNumber(pphoneEditText.getText().toString());
        updateDoctorRequest.setName(doctorNameEditText.getText().toString());
        updateDoctorRequest.setNameAR(doctorNameAREditText.getText().toString());
        updateDoctorRequest.setLang(Locale.getDefault().getDisplayLanguage());
        updateDoctorRequest.setSpecialityID((int) specialtyID);
        return updateDoctorRequest;
    }

    public CreateDoctorRequest createDoctorRequest() {
        CreateDoctorRequest createDoctorRequest = new CreateDoctorRequest();
        createDoctorRequest.setLang(Locale.getDefault().getDisplayLanguage());
        createDoctorRequest.setSpecialityID((int) specialtyID);
        createDoctorRequest.setName(doctorNameEditText.getText().toString());
        createDoctorRequest.setMobileNumber(pphoneEditText.getText().toString());
        createDoctorRequest.setDescription(englishDescription.getText().toString());
        createDoctorRequest.setDescriptionAR(arabicDescription.getText().toString());
        createDoctorRequest.setEmail(emailEditText.getText().toString());
        createDoctorRequest.setNameAR(doctorNameAREditText.getText().toString());
        createDoctorRequest.setTitle(tittle.getText().toString());
        createDoctorRequest.setTitleAR(tittleAR.getText().toString());
        createDoctorRequest.setGender(gender.getText().toString());
        createDoctorRequest.setHouseVisit(houseVistBoolean);
        createDoctorRequest.setConsultantID(conultant);
        createDoctorRequest.setNursery(nurseing);
        createDoctorRequest.setLocation(location.getText().toString());
        createDoctorRequest.setSocialMedia(socialMedia.getText().toString());
        return createDoctorRequest;
    }

    @OnTouch(R.id.speciality_spinner)
    public boolean selectSpecialty(View v, MotionEvent event) {
        getSpecialities();
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (specialtiesPopupWindow == null) {
            specialtiesPopupWindow = new ListPopupWindow(this);
            specialtiesPopupWindow.setAnchorView(v);
            specialtiesPopupWindow.setWidth(v.getWidth());
            if (specialtiesArrayAdapter == null)
                specialtiesArrayAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        specialites
                );
            specialtiesPopupWindow.setAdapter(specialtiesArrayAdapter);
            specialtiesPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Speciality speciality = specialites.get(position);
                    AddDoctorActivity.this.specialitySpinner.setText(speciality.getName());
                    specialtiesPopupWindow.dismiss();
                    specialtyID = speciality.getId();
                }
            });
        }
        specialtiesPopupWindow.show();
        return true;
    }

    @OnTouch(R.id.gender)
    public boolean selectgender(View v, MotionEvent event) {
        final List<String> list = new ArrayList<String>();
        list.add(getString(R.string.male));
        list.add(getString(R.string.femal));
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (genderListPopupWindow == null) {
            genderListPopupWindow = new ListPopupWindow(this);
            genderListPopupWindow.setAnchorView(v);
            genderListPopupWindow.setWidth(v.getWidth());
            if (genderArrayAdapter == null)
                genderArrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list
                );
            genderListPopupWindow.setAdapter(genderArrayAdapter);
            genderListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gender.setText(list.get(position));
                    genderListPopupWindow.dismiss();

                }
            });
        }
        genderListPopupWindow.show();
        return true;
    }

    @OnTouch(R.id.consultant)
    public boolean selectConsultant(View v, MotionEvent event) {
        final List<String> list = new ArrayList<String>();
        list.add("Specialist");
        list.add("Consultant");
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (consultantListPopupWindow == null) {
            consultantListPopupWindow = new ListPopupWindow(this);
            consultantListPopupWindow.setAnchorView(v);
            consultantListPopupWindow.setWidth(v.getWidth());
            if (consultanArrayAdapter == null)
                consultanArrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list
                );
            consultantListPopupWindow.setAdapter(consultanArrayAdapter);
            consultantListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    consultantEditText.setText(list.get(position));
                    conultant = position + 1;
                    consultantListPopupWindow.dismiss();

                }
            });
        }
        consultantListPopupWindow.show();
        return true;
    }

    @OnTouch(R.id.house_visit)
    public boolean selectHoseVisit(View v, MotionEvent event) {
        final List<String> list = new ArrayList<String>();
        list.add("true");
        list.add("false");
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (housevisitListPopupWindow == null) {
            housevisitListPopupWindow = new ListPopupWindow(this);
            housevisitListPopupWindow.setAnchorView(v);
            housevisitListPopupWindow.setWidth(v.getWidth());
            if (houseVisitArrayAdapter == null)
                houseVisitArrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list
                );
            housevisitListPopupWindow.setAdapter(houseVisitArrayAdapter);
            housevisitListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    houseVisit.setText(list.get(position));
                    if (position == 0) {
                        houseVistBoolean = true;

                    } else if (position == 1) {
                        houseVistBoolean = false;
                    }
                    housevisitListPopupWindow.dismiss();

                }
            });
        }
        housevisitListPopupWindow.show();
        return true;
    }

    @OnTouch(R.id.nursing)
    public boolean selectNursing(View v, MotionEvent event) {
        final List<String> list = new ArrayList<String>();
        list.add("true");
        list.add("false");
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (nurseryListPopupWindow == null) {
            nurseryListPopupWindow = new ListPopupWindow(this);
            nurseryListPopupWindow.setAnchorView(v);
            nurseryListPopupWindow.setWidth(v.getWidth());
            if (nursingArrayAdapter == null)
                nursingArrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list
                );
            nurseryListPopupWindow.setAdapter(nursingArrayAdapter);
            nurseryListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    nursery.setText(list.get(position).toString());
                    if (position == 0) {
                        nurseing = true;

                    } else if (position == 1) {
                        nurseing = false;
                    }
                    nurseryListPopupWindow.dismiss();

                }
            });
        }
        nurseryListPopupWindow.show();
        return true;
    }


    public void getSpecialities() {
        if (! Connectivity.isConnected(this)){
            getSpecialityFromDB();
         return;
        }
        specialityGeneralResponseCall = NetworkProvider.provideNetworkMethods(this).getSpeciality(1, 10000);
        specialityGeneralResponseCall.enqueue(new Callback<SpecialityGeneralResponse>() {
            @Override
            public void onResponse(Call<SpecialityGeneralResponse> call, Response<SpecialityGeneralResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body() != null) {
                            List<Speciality> list = response.body().getResponse();
                            if (list != null) {
                                specialites.clear();
                                specialites.addAll(list);
                                saveSpeciality(list);
                            }
                            if (specialtiesArrayAdapter != null)
                                specialtiesArrayAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(AddDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(AddDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<SpecialityGeneralResponse> call, Throwable t) {
                Toast.makeText(AddDoctorActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void updateDoctor() {
        if (newDoctor) {
            if (! Connectivity.isConnected(this)){
                saveCreateInDB(createDoctorRequest());
                Toast.makeText(this, R.string.network_error + getString(R.string.request_saved), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).createNewDoctor(createDoctorRequest());

        } else {
            if (! Connectivity.isConnected(this)){
                saveUpdateInDB(getUpdateDoctorRequest());
                Toast.makeText(this, R.string.network_error + getString(R.string.request_saved), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).updateDoctor(getUpdateDoctorRequest());
        }
        updateDoctorResponseCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        progress.setVisibility(View.GONE);
                        saveIcon.setEnabled(true);
                        if (newDoctor) {
                            Toast.makeText(AddDoctorActivity.this, R.string.doctor_added, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddDoctorActivity.this, R.string.doctor_updated, Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    } else {
                        Toast.makeText(AddDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        saveIcon.setEnabled(true);

                    }
                } else {
                    Toast.makeText(AddDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    saveIcon.setEnabled(true);


                }
            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                Toast.makeText(AddDoctorActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                saveIcon.setEnabled(true);


            }
        });

    }

    @Override
    protected void onStop() {
        if (specialityGeneralResponseCall != null) {
            if (specialityGeneralResponseCall.isCanceled()) {
                specialityGeneralResponseCall.cancel();
            }
        }
        if (updateDoctorResponseCall != null) {
            if (updateDoctorResponseCall.isCanceled()) {
                updateDoctorResponseCall.cancel();
            }
        }
        super.onStop();
    }


    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
