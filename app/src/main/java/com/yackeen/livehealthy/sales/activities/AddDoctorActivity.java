package com.yackeen.livehealthy.sales.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yackeen.livehealthy.sales.R;
import com.yackeen.livehealthy.sales.database.CreateDoctorDB;
import com.yackeen.livehealthy.sales.database.CreateDoctorDB_Table;
import com.yackeen.livehealthy.sales.model_dto.request.CreateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.response.GeoCoderResult;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.createDoctor.CreateDoctorResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality.Speciality;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.yackeen.livehealthy.sales.network.NetworkProvider;
import com.yackeen.livehealthy.sales.util.Connectivity;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
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
    ImageButton saveIcon;
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
    @BindView(R.id.progress_bar)
    ProgressBar progress;
    @BindView(R.id.nested_scroll)
    NestedScrollView nestedScroll;

    AlertDialog dialog;
    boolean internetConnection;
    Call<SpecialityGeneralResponse> specialityGeneralResponseCall;
    Call<CreateDoctorResponse> updateDoctorResponseCall;
    Call<GeoCoderResult> geoCoderResultCall;
    boolean newDoctor;
    boolean houseVistBoolean;
    int conultant;
    boolean nurseing;
    String lonL, latL;
    ArrayAdapter<CharSequence> adapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        ButterKnife.bind(this);
        conultant = 2;
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
            getSpecialities();

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
                for (int i = 0; i <= list.get(0).getMaxAddressLineIndex(); i++) {
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
                    locationIcon.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(AddDoctorActivity.this, R.string.no_location, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeoCoderResult> call, Throwable t) {
                Toast.makeText(AddDoctorActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                locationProgress.setVisibility(View.GONE);
                locationIcon.setVisibility(View.VISIBLE);
            }
        });
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

    public long getDoctorIDFromDB(){
        List<CreateDoctorDB> createDoctorDBs= SQLite.select(CreateDoctorDB_Table.id).from(CreateDoctorDB.class).orderBy(CreateDoctorDB_Table.id,false).queryList();
        CreateDoctorDB createDoctorDB = createDoctorDBs.get(0);
        long doctorId= createDoctorDB.getId();
        return doctorId;
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
            if (! Connectivity.isConnected(this)){
                saveCreateInDB(createDoctorRequest());
                progress.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.request_saved), Toast.LENGTH_SHORT).show();
                initiateDialog();
                return;
            }else {
                updateDoctor();
            }
        }else{
            saveIcon.setEnabled(true);
            Toast.makeText(this,R.string.empty_feild, Toast.LENGTH_SHORT).show();
        }
    }


    public void initiateDialog(){
        View view = LayoutInflater.from(this)
                .inflate(R.layout.save_popup, null, false);
        dialog = new AlertDialog.Builder(this).setTitle(R.string.add_clinic)
                .setView(view)
                .setPositiveButton(R.string.add_clinic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ClinicsActivity.newClinics(AddDoctorActivity.this,(int)getDoctorIDFromDB(),false);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.save_only, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create();
        dialog.show();
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
        if (conultant==0){
            consultantEditText.setError(getString(R.string.empty_feild));
            valid = false;
        }else{
            consultantEditText.setError(null);
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
        if (isEmpty(location.getText())) {
            location.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            location.setError(null);
        }
        if (isEmpty(pphoneEditText.getText())) {
            pphoneEditText.setError(getString(R.string.empty_feild));
            valid = false;
        } else {
            pphoneEditText.setError(null);
        }
        return valid;
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
                        getSpecialityFromDB();

                    }
                } else {
                    getSpecialityFromDB();

                }
            }


            @Override
            public void onFailure(Call<SpecialityGeneralResponse> call, Throwable t) {
                Toast.makeText(AddDoctorActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
                getSpecialityFromDB();

            }
        });
    }

    public void updateDoctor() {

            updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).createNewDoctor(createDoctorRequest());
        progress.setVisibility(View.VISIBLE);


        updateDoctorResponseCall.enqueue(new Callback<CreateDoctorResponse>() {
            @Override
            public void onResponse(Call<CreateDoctorResponse> call, Response<CreateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        progress.setVisibility(View.GONE);
                        saveIcon.setEnabled(true);
                            Toast.makeText(AddDoctorActivity.this, R.string.doctor_added, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CreateDoctorResponse> call, Throwable t) {
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
