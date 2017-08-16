package com.example.ahmedessam.livehealthysales.model_dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abdelrhman Walid on 7/3/2017.
 */

public class GeoCoderResult  {
    @SerializedName("results")
    @Expose
    public List<Result> results = null;
    @SerializedName("status")
    @Expose
    public String status;
}
