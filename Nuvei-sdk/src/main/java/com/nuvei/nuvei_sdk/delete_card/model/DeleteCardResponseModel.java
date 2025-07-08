package com.nuvei.nuvei_sdk.delete_card.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteCardResponseModel {
    @SerializedName("message")
    @Expose
    private String message;



    public String getMessage(){return this.message;};
}
