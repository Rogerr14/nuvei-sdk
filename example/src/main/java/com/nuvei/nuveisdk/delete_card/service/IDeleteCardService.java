package com.nuvei.nuveisdk.delete_card.service;

import com.nuvei.nuveisdk.delete_card.model.DeleteCardResponseModel;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDeleteCardService {
    @FormUrlEncoded
    @POST("/card/delete")
    Call<DeleteCardResponseModel> deleteCard(@Query("userID") String user_ID, @Query("token") String token);
}
