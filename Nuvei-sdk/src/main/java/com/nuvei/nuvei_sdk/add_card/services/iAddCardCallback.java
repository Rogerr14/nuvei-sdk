package com.nuvei.nuvei_sdk.add_card.services;

import com.nuvei.nuvei_sdk.add_card.model.CardResponseModel;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;


/**
 * An interface representing a callback to be notified about the results of
 * {@link CardResponseModel} creation or requests
 */
public interface iAddCardCallback {
    /**
     * PaymentezError callback method.
     * @param error the error that occurred.
     */
    void onError(ErrorResponse error);

    /**
     * Success callback method.
     * @param card the {@link CardModel} that was found or created.
     */
    void onSuccess(CardModel card);
}
