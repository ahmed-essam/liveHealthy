package com.example.ahmedessam.livehealthysales.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ahmedessam.livehealthysales.application.myApp;

/**
 * Created by ahmed essam on 06/08/2017.
 */

public class UserHelper {
    public static final String AppLang = "Lang";
    public static final String UserId = "user_id";
    public static final String UserType = "user_type";
    public static final String security_token= "token";
    private static myApp appContext = myApp.getInstance();


    public static void saveStringInSharedPreferences(String tag, String value, Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(tag,value).apply();
    }
    public static String getAppLang(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(AppLang,null);
    }

    public static String getUserId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserId,null);
    }
    public static String getUserType(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserType,null);
    }
    public static String getSecurity_token(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(security_token,null);
    }

}
