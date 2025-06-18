package com.nuvei.nuvei_sdk.models;

import com.google.gson.annotations.SerializedName;




public class ErrorModel{
    @SerializedName("error")
    private ErrorResponse errorResponse;

    public ErrorResponse getError(){return errorResponse;}

    public void setError(ErrorResponse errorResponse){
        this.errorResponse = errorResponse;
    }

}
