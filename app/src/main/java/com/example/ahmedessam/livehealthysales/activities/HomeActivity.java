package com.example.ahmedessam.livehealthysales.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedessam.livehealthysales.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.doctor_icon)
    public void doctorClicked(){
        startActivity(new Intent(HomeActivity.this,DoctorsActivity.class));

    }
}
