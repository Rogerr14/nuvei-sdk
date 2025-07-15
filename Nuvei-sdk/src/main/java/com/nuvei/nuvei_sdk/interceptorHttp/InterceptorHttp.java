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


      public Retrofit getClient(Context mContext, String code, String key){
          String URL_BASE = Nuvei.isTestMode() ? SERVER_DEV_URL : SERVER_PROD_URL;

          OkHttpClient.Builder builder = new OkHttpClient.Builder();
          HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
          logging.setLevel(HttpLoggingInterceptor.Level.BODY);
          if (Nuvei.isTestMode()) {
              builder.addInterceptor(logging);
          }

          builder.addInterceptor(chain -> {
              String authToken = NuveiUtils.getAuthToken(key, code);
              Log.d("InterceptorHttp", "Generated Auth-Token for " + code + ": " + authToken);
              Request request = chain.request().newBuilder()
                      .addHeader("Content-Type", "application/json")
                      .addHeader("Auth-Token", authToken)
                      .build();
              return chain.proceed(request);
          });

          OkHttpClient client = builder.build();

          return new Retrofit.Builder()
                  .baseUrl(URL_BASE)
                  .addConverterFactory(GsonConverterFactory.create())
                  .client(client)
                  .build();
    }
}
