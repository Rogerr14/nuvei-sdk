package com.nuvei.nuvei_sdk.delete_card.service;

import com.nuvei.nuvei_sdk.delete_card.model.DeleteCardRequestModel;
import com.nuvei.nuvei_sdk.delete_card.model.DeleteCardResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDeleteCardService {
    @POST("card/delete")
    Call<DeleteCardResponseModel> deleteCard(@Body DeleteCardRequestModel request);
}
