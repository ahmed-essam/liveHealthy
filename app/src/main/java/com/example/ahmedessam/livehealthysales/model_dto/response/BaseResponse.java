package com.example.ahmedessam.livehealthysales.model_dto.response;

/**
 * Created by ahmed essam on 08/08/2017.
 */

public class BaseResponse {

    private String errorMessage;
    private Boolean isSuccess;
    private String errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
