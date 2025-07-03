package com.nuvei.nuvei_sdk.interceptorHttp;

import static com.nuvei.nuvei_sdk.helpers.NuveiUtils.SERVER_DEV_URL;
import static com.nuvei.nuvei_sdk.helpers.NuveiUtils.SERVER_PROD_URL;

import android.content.Context;
import android.util.Log;

import com.nuvei.nuvei_sdk.Nuvei;
import com.nuvei.nuvei_sdk.helpers.NuveiUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterceptorHttp {
    private static Retrofit retrofit = null;

    static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    public static  Retrofit getClient(Context mContext, String code, String key){
        if(retrofit == null){
            String URL_BASE;
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            if(Nuvei.isTestMode()){
                URL_BASE = SERVER_DEV_URL;
                builder.addInterceptor(logging);
            }else{
                URL_BASE = SERVER_PROD_URL;
            }


            builder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
                        .addHeader("Auth-Token", NuveiUtils.getAuthToken(key, code))
                        .build();

                return chain.proceed(request);
            });

            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }
        return  retrofit;
    }
}
