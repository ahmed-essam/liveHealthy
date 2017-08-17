package com.example.ahmedessam.livehealthysales.application;

import android.app.Application;

import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by ahmed essam on 06/08/2017.
 */

public class myApp extends Application {
    private String BaseUrl = "http://yakensolution.cloudapp.net:80/LiveHealthyAdmin/api/";
    private static myApp instance;
    @Override
    public void onCreate() {
        instance = this;
        NetworkProvider.initializeNetworkLayer(BaseUrl,this);
        FlowManager.init(this);
        super.onCreate();
    }


    public static synchronized myApp getInstance() {

        return instance;
    }
}
