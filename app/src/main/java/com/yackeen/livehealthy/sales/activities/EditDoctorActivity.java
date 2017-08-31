package com.yackeen.livehealthy.sales.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yackeen.livehealthy.sales.R;
import com.yackeen.livehealthy.sales.database.UpdateDoctorDb;
import com.yackeen.livehealthy.sales.model_dto.request.UpdateDoctorRequest;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality.Speciality;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.speciality.SpecialityGeneralResponse;
import com.yackeen.livehealthy.sales.model_dto.response.response_class.updateDoctor.UpdateDoctorResponse;
import com.yackeen.livehealthy.sales.models.DoctorDetail;
import com.yackeen.livehealthy.sales.network.NetworkProvider;
import com.yackeen.livehealthy.sales.util.Connectivity;
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

import static android.text.TextUtils.isEmpty;

public class EditDoctorActivity extends AppCompatActivity {
    private static final String ARG_Detail = "doctor_detail";
    private static final String Detail = "Doctor_detail";
    private static final String TAG = "add_doctor_activity";
    @BindView(R.id.doctor_name_edit)
    EditText doctorNameEditText;
    @BindView(R.id.doctor_name_ar_edit)
    EditText doctorNameAREditText;
    @BindView(R.id.phone_number_edit_text)
    EditText pphoneEditText;
    @BindView(R.id.speciality_spinner)
    EditText specialitySpinner;
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
    @BindView(R.id.edit_clinics)
    Button editClinicButton;
    @BindView(R.id.progress_bar)
    ProgressBar progress;
    @BindView(R.id.nested_scroll)
    NestedScrollView nestedScroll;

    boolean internetConnection;
    Call<SpecialityGeneralResponse> specialityGeneralResponseCall;
    Call<UpdateDoctorResponse> updateDoctorResponseCall;
    ArrayAdapter<CharSequence> adapter;
    DoctorDetail doctorDetail;
    private List<Speciality> specialites;
    private ListPopupWindow specialtiesPopupWindow;
    private ArrayAdapter<Speciality> specialtiesArrayAdapter;
    private int specialtyID;

    public static Intent newEditDoctorIntent(Context context, DoctorDetail doctorDetail) {
        Intent intent = new Intent(context, EditDoctorActivity.class);
        intent.putExtra(ARG_Detail, doctorDetail);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);
        ButterKnife.bind(this);
        if (savedInstanceState!=null){
            doctorDetail = (DoctorDetail) savedInstanceState.getSerializable(Detail);
        }
        if (Connectivity.isConnected(this)){
            internetConnection = true;
            editClinicButton.setVisibility(View.VISIBLE);
            editClinicButton.setEnabled(true);
        }else{
            internetConnection = false;
            editClinicButton.setVisibility(View.GONE);
            editClinicButton.setEnabled(false);
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
            if (doctorDetail.getSpecialityID()!=null) {
                specialtyID = doctorDetail.getSpecialityID();
            }
        }
    }
    public DoctorDetail getDoctorDetail() {
        Intent intent = getIntent();
        DoctorDetail doctorDetail = (DoctorDetail) intent.getSerializableExtra(ARG_Detail);
        return doctorDetail;
    }
    public void addViewValues(DoctorDetail doctorDetail) {
        if (doctorDetail.getName()!=null) {
            doctorNameEditText.setText(doctorDetail.getName());
        }
        if (doctorDetail.getNameAR()!=null) {
            doctorNameAREditText.setText(doctorDetail.getNameAR());
        }
        if (doctorDetail.getMobileNumber()!=null) {
            pphoneEditText.setText(doctorDetail.getMobileNumber());
        }
        if (doctorDetail.getDescriptionAR()!=null) {
            arabicDescription.setText(doctorDetail.getDescriptionAR());
        }
        if (doctorDetail.getDescription()!=null) {
            englishDescription.setText(doctorDetail.getDescription());
        }
        if (doctorDetail.getSpeciality()!=null) {
            specialitySpinner.setText(doctorDetail.getSpeciality());
        }
        if (doctorDetail.getEmail()!=null) {
            emailEditText.setText(doctorDetail.getEmail().toString());
        }
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
        Intent intent = ClinicsActivity.newClinics(this, doctorDetail.getDoctorID(),true);
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
                    EditDoctorActivity.this.specialitySpinner.setText(speciality.getName());
                    specialtiesPopupWindow.dismiss();
                    specialtyID = speciality.getId();
                }
            });
        }
        specialtiesPopupWindow.show();
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
                Toast.makeText(EditDoctorActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
                getSpecialityFromDB();
                return;
            }
        });
    }

    public void updateDoctor() {

            if (! Connectivity.isConnected(this)){
                saveUpdateInDB(getUpdateDoctorRequest());
                Toast.makeText(this, R.string.network_error + getString(R.string.request_saved), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            updateDoctorResponseCall = NetworkProvider.provideNetworkMethods(this).updateDoctor(getUpdateDoctorRequest());

        updateDoctorResponseCall.enqueue(new Callback<UpdateDoctorResponse>() {
            @Override
            public void onResponse(Call<UpdateDoctorResponse> call, Response<UpdateDoctorResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        progress.setVisibility(View.GONE);
                        saveIcon.setEnabled(true);
                            Toast.makeText(EditDoctorActivity.this, R.string.doctor_updated, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        saveIcon.setEnabled(true);

                    }
                } else {
                    Toast.makeText(EditDoctorActivity.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    saveIcon.setEnabled(true);


                }
            }

            @Override
            public void onFailure(Call<UpdateDoctorResponse> call, Throwable t) {
                Toast.makeText(EditDoctorActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                saveIcon.setEnabled(true);


            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Detail,doctorDetail);
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

}
