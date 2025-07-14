package com.nuvei.nuvei_sdk.add_card.builders;

import androidx.annotation.NonNull;

public interface ICardBrandChangeListener {
    void onCardBrandChanged(@NonNull String brand, String cardLogo, boolean isOtp);
}
