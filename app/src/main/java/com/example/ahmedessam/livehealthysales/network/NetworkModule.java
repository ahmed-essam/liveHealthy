package com.example.ahmedessam.livehealthysales.network;

import android.content.Context;

import com.example.ahmedessam.livehealthysales.application.myApp;
import com.example.ahmedessam.livehealthysales.util.UserHelper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class NetworkModule {

    private String BaseURL;
    private static myApp appContext = myApp.getInstance();

    public NetworkModule(String baseURL) {
        BaseURL = baseURL;
    }

    public HttpLoggingInterceptor getLoginInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
    public Interceptor getInterceptor(final Context context){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request requestBuilder = chain.request();
                HttpUrl originalHttpUrl = requestBuilder.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("UserID", UserHelper.getUserId(context))
                        .addQueryParameter("Token",UserHelper.getSecurity_token(context))
                        .build();
                return chain.proceed(requestBuilder.newBuilder().url(url).build());
            }
        };
        return interceptor;
    }
    public OkHttpClient.Builder getOkBuilder(Context context){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getLoginInterceptor());
        builder.addInterceptor(getInterceptor(context));
        return builder;
    }
    public Retrofit getClient(Context context){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkBuilder(context).build()).build();
        return retrofit;
    }


    public NetworkMethods getNetworkMethodes(Context context){
        NetworkMethods networkMethods = getClient(context).create(NetworkMethods.class);
        return networkMethods;
    }
}
