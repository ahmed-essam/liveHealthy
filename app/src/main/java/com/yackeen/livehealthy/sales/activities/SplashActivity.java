package com.yackeen.livehealthy.sales.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yackeen.livehealthy.sales.R;
import com.yackeen.livehealthy.sales.util.UserHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int secondsDelay=2;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (UserHelper.getUserId(SplashActivity.this) != null) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                }

                finish();
            }
        }, secondsDelay * 1000);
    }
}
