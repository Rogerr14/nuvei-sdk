package com.nuvei.nuvei_sdk.add_card.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nuvei.nuvei_sdk.views.EditTextWidget;

public class CardNumberText extends EditTextWidget {
    public CardNumberText(@NonNull Context context) {
        super(context);
    }

    public CardNumberText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardNumberText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
