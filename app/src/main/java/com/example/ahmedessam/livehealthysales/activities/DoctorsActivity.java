package com.example.ahmedessam.livehealthysales.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.adapters.DoctorsAdapter;
import com.example.ahmedessam.livehealthysales.model_dto.request.DoctorsRequest;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.AllDoctorsResponse;
import com.example.ahmedessam.livehealthysales.models.Doctor;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.example.ahmedessam.livehealthysales.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.doctors_toolbar)
    Toolbar doctorsToolbar;
    @BindView(R.id.doctors_recycler_view)
    RecyclerView doctorRecyclerView;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.add_floating_button)
    FloatingActionButton addFloatingActionButton;
    @BindView(R.id.no_data_text)
    TextView noDataText;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;


    //variables
    DoctorsAdapter doctorsAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static final int backageSize = 20;
    private int pageNum;
    Call<AllDoctorsResponse> responceCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        ButterKnife.bind(this);
        pageNum = 1;
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

                pageNum = pageNum + 1;

                fetchDoctors(searchEditText.getText().toString());

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
                mSwipeRefreshLayout.setRefreshing(true);
                fetchDoctors(charSequence.toString());
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

    public void fetchDoctors(String name) {
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
                            noDataText.setText(response.body().getErrorMessage());
                            noDataText.setVisibility(View.VISIBLE);
                        }
                    } else {

                        noDataText.setText(R.string.no_data);
                        noDataText.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DoctorsActivity.this, "" + response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AllDoctorsResponse> call, Throwable t) {
                noDataText.setText(R.string.network_error);
                noDataText.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
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
        fetchDoctors("");
    }

    @Override
    protected void onRestart() {
        fetchDoctors(searchEditText.getText().toString());
        super.onRestart();
    }

    @Override
    protected void onStop() {
        responceCall.cancel();
        super.onStop();
    }

}
