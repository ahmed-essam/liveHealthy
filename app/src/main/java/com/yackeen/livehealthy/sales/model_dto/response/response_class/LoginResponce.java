package com.yackeen.livehealthy.sales.model_dto.response.response_class;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed essam on 07/08/2017.
 */

public class LoginResponce {
    private String errorMessage;
    private Boolean isSuccess;
    private String errorCode;
    @SerializedName("Response")
    public com.yackeen.livehealthy.sales.model_dto.response.response_class.Response Response;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public com.yackeen.livehealthy.sales.model_dto.response.response_class.Response getResponse() {
        return Response;
    }

    public void setResponse(com.yackeen.livehealthy.sales.model_dto.response.response_class.Response response) {
        Response = response;
    }
}
