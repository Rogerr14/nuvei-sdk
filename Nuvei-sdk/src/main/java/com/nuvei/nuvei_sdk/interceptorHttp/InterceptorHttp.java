package com.nuvei.nuvei_sdk.interceptorHttp;

import static com.nuvei.nuvei_sdk.helpers.NuveiUtils.SERVER_DEV_URL;
import static com.nuvei.nuvei_sdk.helpers.NuveiUtils.SERVER_PROD_URL;

import android.content.Context;

import com.nuvei.nuvei_sdk.helpers.NuveiUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterceptorHttp {
    private static Retrofit retrofit = null;

    static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    public static  Retrofit getClient(Context mContext, boolean developer_mode, String CLIENT_CODE_APP, String CLIENT_KEY_APP){
        if(retrofit == null){
            String URL_BASE;
            if(developer_mode){
                URL_BASE = SERVER_DEV_URL;
            }else{
                URL_BASE = SERVER_PROD_URL;
            }

            builder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
                        .addHeader("Auth_Token", NuveiUtils.getAuthToken(CLIENT_KEY_APP, CLIENT_CODE_APP))
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
