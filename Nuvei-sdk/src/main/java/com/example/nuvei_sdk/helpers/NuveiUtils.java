package com.example.nuvei_sdk.helpers;

import android.util.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class NuveiUtils {

    public  static String SERVER_DEV_URL = "https://ccapi-stg.paymentez.com";
    public  static String SERVER_PROD_URL= "https://ccapi.paymentez.com";

    private static  String getUniqueToken( String client_key, String time_stamp_auth ){
        String unique_token = client_key +  time_stamp_auth;
        return new String(Hex.encodeHex(DigestUtils.sha256(unique_token)));
    }

    public static  String getAuthToken(String client_key, String client_code ){
        long time_stamp_time = System.currentTimeMillis()/1000;
        String time_stamp_string = Long.toString(time_stamp_time);
        String auth_token = client_code + ";" + time_stamp_string + ";" + getUniqueToken(time_stamp_string, client_key);
        return Base64.encodeToString(auth_token.getBytes(), Base64.NO_WRAP);
    }




}
