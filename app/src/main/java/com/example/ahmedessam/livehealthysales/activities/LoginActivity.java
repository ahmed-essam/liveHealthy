package com.example.ahmedessam.livehealthysales.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.model_dto.request.LoginRequest;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.LoginResponce;
import com.example.ahmedessam.livehealthysales.network.NetworkProvider;
import com.example.ahmedessam.livehealthysales.util.TextHelper;
import com.example.ahmedessam.livehealthysales.util.UserHelper;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.langButton)
    Button langButton;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.login_progress_bar)
    ProgressBar progressBar;

    private String TAG = LoginActivity.class.getSimpleName();
    private String language;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        language = UserHelper.getAppLang(this);
        if (language != null) {
            String languageToLoad = null;
            if (language.equals("ar")) {
                langButton.setText(R.string.eng_language);
                languageToLoad = "ar";
            } else {
                languageToLoad = "en";
                langButton.setText(R.string.other_lang);
            }
//            UserHelper.saveStringInSharedPreferences(UserHelper.AppLang,languageToLoad,this);
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

        }

    }

    @OnClick(R.id.langButton)
    public void changeLang() {
        String languageToLoad = null;
        if (langButton.getText().toString().equals("ع")) {
            languageToLoad = "ar";
            langButton.setText(R.string.eng_language);
        } else {
            languageToLoad = "en";
            langButton.setText(R.string.other_lang);
        }
        if (languageToLoad == null) {
            return;
        }

        UserHelper.saveStringInSharedPreferences(UserHelper.AppLang,languageToLoad,this);
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());
        restartActivity();
    }

    public LoginRequest getLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(emailEditText.getText().toString());
        loginRequest.setPassword(passwordEditText.getText().toString());
        loginRequest.setLang(Locale.getDefault().getDisplayLanguage());
        return loginRequest;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        boolean realdata = verifyData();
        if (realdata) {
            addProgressBar();
            LoginRequest loginRequest = getLogin();
//       Call<LoginResponce> loginResponceCall=  NetworkCalls.Login(loginRequest);
            Call<LoginResponce> loginResponceCall = NetworkProvider.provideNetworkMethods(this).doLogin(loginRequest);
            loginResponceCall.enqueue(new Callback<LoginResponce>() {
                @Override
                public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                    if (response.isSuccessful()) {
                        removeProgressBar();
                        if (response.body() != null) {
                            LoginResponce loginResponce = response.body();
                            if (loginResponce.getIsSuccess()) {
                                String s = loginResponce.Response.getUserDetail().getUser_ID();
                                Log.d(TAG, "onResponse: " + s);
                                UserHelper.saveStringInSharedPreferences(UserHelper.UserId,s,LoginActivity.this);
                                UserHelper.saveStringInSharedPreferences(UserHelper.UserType,loginResponce.Response.getUserDetail().getType(),LoginActivity.this);
                                UserHelper.saveStringInSharedPreferences(UserHelper.security_token,response.headers().get("Token "),LoginActivity.this);
                                Log.d("token", ":"+response.headers().get("Token "));
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, ""+loginResponce.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "onResponse: empty response");
                            Toast.makeText(LoginActivity.this, ""+response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "onResponse: fail");
                        Toast.makeText(LoginActivity.this, "" + response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponce> call, Throwable t) {
                    Log.d(TAG, "onFailure: network error");
                    removeProgressBar();
                    Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, R.string.Empty_data, Toast.LENGTH_SHORT).show();
        }

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public boolean verifyData() {
        if (!TextHelper.isEditTextEmpty(new EditText[]{emailEditText, passwordEditText},this)) {
            return true;
        } else
            return false;
    }

    public void addProgressBar() {
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
    }

    public void removeProgressBar() {
        progressBar.setVisibility(View.GONE);
        progressBar.setEnabled(false);
        loginButton.setEnabled(true);
    }
}
