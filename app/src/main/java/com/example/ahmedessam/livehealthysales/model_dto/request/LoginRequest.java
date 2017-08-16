package com.example.ahmedessam.livehealthysales.model_dto.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class LoginRequest {
    @SerializedName("Email")
    private String Email;
    @SerializedName("Password")
    private String Password;
    @SerializedName("lang")
    private String lang;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
