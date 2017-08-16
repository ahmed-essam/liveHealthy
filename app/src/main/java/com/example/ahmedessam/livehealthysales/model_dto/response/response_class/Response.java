package com.example.ahmedessam.livehealthysales.model_dto.response.response_class;

import com.example.ahmedessam.livehealthysales.models.UserDetail;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class Response {
    @SerializedName("UserDetails")
   private UserDetail userDetail;

    public UserDetail getUserDetail() {
        return userDetail;
    }
}
