package com.example.ahmedessam.livehealthysales.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmedessam.livehealthysales.R;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.yayandroid.locationmanager.listener.LocationListener;

/**
 * Created by ahmed essam on 15/08/2017.
 */

public abstract class BaseLocationActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;

    public LocationConfiguration getLocationConfiguration() {
        return Configurations
                .defaultConfiguration(getString(R.string.location_permission), getString(R.string.turn_gps));
    }

    protected LocationManager getLocationManager() {
        return locationManager;
    }

    protected void getLocation() {
        if (locationManager != null) {
            locationManager.get();
        } else {
            throw new IllegalStateException("locationManager is null. "
                    + "Make sure you call super.initialize before attempting to getLocation");
        }
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new LocationManager.Builder(getApplicationContext())
                .configuration(getLocationConfiguration())
                .activity(this)
                .notify(this)
                .build();
        LocationManager.enableLog(true);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        locationManager.onDestroy();
        super.onDestroy();
    }

    @CallSuper
    @Override
    protected void onPause() {
        locationManager.onPause();
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.onResume();
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationManager.onActivityResult(requestCode, resultCode, data);
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        // override if needed
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onLocationFailed(int type) {

    }

    @Override
    public void onPermissionGranted(boolean alreadyHadPermission) {
        // override if needed
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // override if needed
    }


}