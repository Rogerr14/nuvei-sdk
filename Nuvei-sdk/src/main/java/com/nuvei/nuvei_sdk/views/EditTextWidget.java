package com.nuvei.nuvei_sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

public class EditTextWidget  extends TextInputEditText {

    @ColorInt
    private int errorColor;
    private boolean showError;


    private void intializateWidget(){

    }
    public EditTextWidget(@NonNull Context context) {
        super(context);
        intializateWidget();
    }

    public EditTextWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        intializateWidget();
    }

    public EditTextWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intializateWidget();
    }
}
