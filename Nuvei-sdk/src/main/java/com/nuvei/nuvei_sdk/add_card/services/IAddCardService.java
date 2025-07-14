package com.nuvei.nuvei_sdk.add_card.services;

import com.nuvei.nuvei_sdk.add_card.model.CardRequestModel;
import com.nuvei.nuvei_sdk.add_card.model.CardResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAddCardService {

    @POST("card/add")
    Call<CardResponseModel> addCard(@Body CardRequestModel cardRequestModel);



}
