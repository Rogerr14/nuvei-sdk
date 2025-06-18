package com.nuvei.nuvei_sdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("help")
    @Expose
    private String help;
    @SerializedName("description")
    @Expose
    private String description;

    public ErrorResponse(String type, String help, String description) {
        this.type = type;
        this.help = help;
        this.description = description;
    }

    public ErrorResponse(){

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
