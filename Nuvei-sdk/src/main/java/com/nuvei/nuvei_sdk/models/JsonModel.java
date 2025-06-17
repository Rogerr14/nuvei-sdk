package com.nuvei.nuvei_sdk.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import com.nuvei.nuvei_sdk.helpers.GlobalHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class JsonModel {

    private static final String EMPTY = "";
    private static final String NULL = "null";
    @NonNull
    public  abstract Map<String, Object> toMap();

    @NonNull
    public abstract JSONObject toJson();

    @Override
    public String toString(){return  this.toJson().toString();}

    static void putStringIfNotNull(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName,
            @Nullable String value) {
        if (!GlobalHelper.isBlank(value)) {
            try {
                jsonObject.put(fieldName, value);
            } catch (JSONException ignored) {
            }
        }
    }

    static void putIntegerIfNotNull(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName,
            @Nullable Integer value) {
        if (value == null) {
            return;
        }
        try {
            jsonObject.put(fieldName, value.intValue());
        } catch (JSONException ignored) {
        }
    }

    @Nullable
    static String optString(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) {
        return nullIfNullOrEmpty(jsonObject.optString(fieldName));
    }


    @Nullable
    static String nullIfNullOrEmpty(@Nullable String possibleNull) {
        return NULL.equals(possibleNull) || EMPTY.equals(possibleNull)
                ? null
                : possibleNull;
    }


    @Nullable
    @Size(3)
    static String optCurrency(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) {
        String value = nullIfNullOrEmpty(jsonObject.optString(fieldName));
        if (value != null && value.length() == 3) {
            return value;
        }
        return null;
    }



    @Nullable
    @Size(2)
    static String optCountryCode(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) {
        String value = nullIfNullOrEmpty(jsonObject.optString(fieldName));
        if (value != null && value.length() == 2) {
            return value;
        }
        return null;
    }


    @Nullable
    static Integer optInteger(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) {
        if (!jsonObject.has(fieldName)) {
            return null;
        }
        return jsonObject.optInt(fieldName);
    }


}

