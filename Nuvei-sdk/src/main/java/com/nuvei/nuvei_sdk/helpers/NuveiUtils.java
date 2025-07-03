package com.nuvei.nuvei_sdk.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class NuveiUtils {

    public  static String SERVER_DEV_URL = "https://ccapi-stg.paymentez.com/v2/";
    public  static String SERVER_PROD_URL= "https://ccapi.paymentez.com/v2";

    private static  String getUniqueToken( String client_key, String time_stamp_auth ){
        String unique_token = client_key +  time_stamp_auth;
        return new String(Hex.encodeHex(DigestUtils.sha256(unique_token)));
    }

    public static  String getAuthToken(String key, String code ){
        long time_stamp_time = System.currentTimeMillis()/1000;
        String time_stamp_string = String.valueOf(time_stamp_time);
        String auth_token = code + ";" + time_stamp_string + ";" + getUniqueToken(time_stamp_string, key);
        Log.v("token", Base64.encodeToString(auth_token.getBytes(), Base64.NO_WRAP));
        return Base64.encodeToString(auth_token.getBytes(), Base64.NO_WRAP);
    }


        public static void show(Context mContext, String title, String message){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setTitle(title);

            builder1.setMessage(message);
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


}
