package com.yackeen.livehealthy.sales.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class UserDetail {
    @SerializedName("Type")
    private String Type;
    @SerializedName("User_ID")
    private String User_ID;



    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
}
