package com.yackeen.livehealthy.sales.model_dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdelrhman Walid on 5/29/2017.
 */

public class Result {
    @SerializedName("formatted_address")
    @Expose
    public String formattedAddress;
}
