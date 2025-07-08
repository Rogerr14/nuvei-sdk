package com.nuvei.nuvei_sdk.list_card.services;

import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;

import java.util.List;

public interface IListCardCallback {

    void onError(ErrorResponse errorResponse);

    void onSuccess(List<CardModel> cardListResponseModel);
}
