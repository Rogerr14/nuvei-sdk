package com.nuvei.nuvei_sdk.delete_card.service;

import com.nuvei.nuvei_sdk.delete_card.model.DeleteCardResponseModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;

public interface IDeleteCardCallback {
    void onError(ErrorResponse error);

    void onSuccess(DeleteCardResponseModel deleteCardResponseModel);
}
