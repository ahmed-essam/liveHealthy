package com.example.ahmedessam.livehealthysales.network;

import android.content.Context;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class NetworkProvider {
    private static String BaseUrl = "http://yakensolution.cloudapp.net:80/LiveHealthyAdmin/api/";
    private static NetworkMethods networkMethods;
    public static void initializeNetworkLayer(String baseURL,Context context) {
        networkMethods = new NetworkModule(baseURL).getNetworkMethodes(context);
    }
    public static NetworkMethods provideNetworkMethods(Context context) {
        networkMethods = new NetworkModule(BaseUrl).getNetworkMethodes(context);
        return networkMethods;
    }
}
