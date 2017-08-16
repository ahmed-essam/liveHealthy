package com.example.ahmedessam.livehealthysales.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.adapters.TimesAdapter;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.DoctorDetailResponse;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.schedule.DaysGeneralResponse;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
//import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    public static final String ARG_doctor = "doctor_id";
    private final String TAG = DemoObjectFragment.class.getSimpleName();

    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TimesAdapter timesAdapter;
    private int clinicId;
    Call<DaysGeneralResponse> daysGeneralResponseCall;
    int doctorId;

    boolean internetFlag = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            Bundle args = getArguments();
            clinicId = args.getInt(ARG_OBJECT);
            doctorId = args.getInt(ARG_doctor);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.reecyclerview_main);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timesAdapter = new TimesAdapter(getContext());
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(timesAdapter);
        fetchSchedule();
        return rootView;
    }

    public Boolean verifyResponse(Response<DaysGeneralResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isSuccess()) {
                if (response.body().getResponse().getDays() != null) {
                    return true;
                } else {
                    Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getContext(), response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getContext(), response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void fetchSchedule() {
        progressBar.setVisibility(View.VISIBLE);
        daysGeneralResponseCall = NetworkProvider.provideNetworkMethods(getContext()).getSchedule(doctorId,clinicId,Locale.getDefault().getDisplayLanguage());
        daysGeneralResponseCall.enqueue(new Callback<DaysGeneralResponse>() {
            @Override
            public void onResponse(Call<DaysGeneralResponse> call, Response<DaysGeneralResponse> response) {
                if (verifyResponse(response)) {
                    progressBar.setVisibility(View.GONE);
                    timesAdapter.addAll(response.body().getResponse().getDays());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DaysGeneralResponse> call, Throwable t) {
//                Toast.makeText(getContext(),getContext().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: "+t.getMessage() );
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public void onStop() {
        super.onStop();
        if (daysGeneralResponseCall != null) {
            if (!daysGeneralResponseCall.isCanceled()) {
                daysGeneralResponseCall.cancel();
            }
        }
    }
}
