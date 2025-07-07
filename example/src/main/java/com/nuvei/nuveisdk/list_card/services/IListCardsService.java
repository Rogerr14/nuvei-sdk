package com.nuvei.nuveisdk.list_card.services;

import com.nuvei.nuveisdk.list_card.model.CardListResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IListCardsService {
    @GET("card/list")
    Call<CardListResponseModel> getAllCards(@Query("uid") String uid);

}
