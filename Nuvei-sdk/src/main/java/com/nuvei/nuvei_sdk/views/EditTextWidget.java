package com.nuvei.nuvei_sdk.views;

import static com.nuvei.nuvei_sdk.views.ViewHelpers.isColorDark;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.textfield.TextInputEditText;
import com.nuvei.nuvei_sdk.R;
import com.nuvei.nuvei_sdk.builders.IAfterChangeListener;
import com.nuvei.nuvei_sdk.builders.IDeleteEmptyListener;
import com.nuvei.nuvei_sdk.builders.IErrorMessageListener;


import android.os.Handler;

public class EditTextWidget  extends TextInputEditText {


    //Listener
    private IErrorMessageListener errorMessageListener;
    @Nullable
    private IAfterChangeListener afterChangeListener;
    @Nullable
    private IDeleteEmptyListener deleteEmptyListener;

    @Nullable
    private ColorStateList colorStateList;
    //handlers
     Handler editTextHandler;
    //Error attributes
    private boolean showError;
    private String errorMessage;
    @ColorInt
    private int errorColor;
    private int defaultColorErrorId;

    public EditTextWidget(@NonNull Context context) {
        super(context);
        initView();
    }

    public EditTextWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EditTextWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        editTextHandler = new android.os.Handler();
        listenForTextChanges();
        listenForDeleteEmpty();
        determineDefaultErrorColor();
        colorStateList = getTextColors();
    }
    private void listenForDeleteEmpty() {
        // This method works for hard keyboards and older phones.
        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && deleteEmptyListener != null
                        && length() == 0) {
                    deleteEmptyListener.onDeleteEmpty();
                }
                return false;
            }
        });
    }

    @Nullable
    public ColorStateList getColorStateList() {
        return colorStateList;
    }

    public boolean getShowError() {
        return showError;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new SoftDeleteInputConnection(super.onCreateInputConnection(outAttrs), true);
    }


    /**
     * Sets a listener that can react to changes in text, but only by reflecting the new
     * text in the field.
     *
     * @param listener the {@link IAfterChangeListener} to attach to this view
     */
    void setAfterTextChangedListener(
            @Nullable IAfterChangeListener listener) {
        afterChangeListener = listener;
    }

    /**
     * Sets a listener that can react to the user attempting to delete the empty string.
     *
     * @param listener the {@link IDeleteEmptyListener} to attach to this view
     */
    protected void setDeleteEmptyListener(@Nullable IDeleteEmptyListener listener) {
        deleteEmptyListener = listener;
    }

    protected void setErrorMessageListener(@Nullable IErrorMessageListener listener) {
        errorMessageListener = listener;
    }
    protected void setErrorMessage(@Nullable String message) {
        errorMessage = message;
    }
    /**
     * Sets the error text color on this {@link EditTextWidget}.
     *
     * @param color a {@link ColorInt}
     */
    public void setErrorColor(@ColorInt int color) {
        errorColor = color;
    }

    /**
     * Change the hint value of this control after a delay.
     *
     * @param hintResource      the string resource for the hint to be set
     * @param delayMilliseconds a delay period, measured in milliseconds
     */
    public void setHintDelayed(@StringRes final int hintResource, long delayMilliseconds) {
        final Runnable hintRunnable = new Runnable() {
            @Override
            public void run() {
                setHint(hintResource);
            }
        };
        editTextHandler.postDelayed(hintRunnable, delayMilliseconds);
    }
    public void setShouldShowError(boolean shouldShowError) {
        if (errorMessage != null && errorMessageListener != null) {
            String message = shouldShowError ? errorMessage : null;
            errorMessageListener.displayErrorMessage(message);
        } else {
            showError = shouldShowError;
            if (showError) {
                setTextColor(errorColor);
            } else {
                setTextColor(colorStateList);
            }

            refreshDrawableState();
        }
    }

    private void determineDefaultErrorColor() {
        colorStateList = getTextColors();
        int color = colorStateList.getDefaultColor();
        if (isColorDark(color)) {
            // Note: if the _text_ color is dark, then this is a
            // light theme, and vice-versa.
            defaultColorErrorId = R.color.error_text_light_theme;
        } else {
            defaultColorErrorId = R.color.error_text_dark_theme;
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Passing a null token removes all callbacks and messages to the handler.
        editTextHandler.removeCallbacksAndMessages(null);
    }




    private void listenForTextChanges() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // purposefully not implemented.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // purposefully not implemented.
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (afterChangeListener != null) {
                    afterChangeListener.onTextChanged(s.toString());
                }
            }
        });
    }

    private class SoftDeleteInputConnection extends InputConnectionWrapper {

        public SoftDeleteInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // This method works on modern versions of Android with soft keyboard delete.
            if (getTextBeforeCursor(1, 0).length() == 0 && deleteEmptyListener != null) {
                deleteEmptyListener.onDeleteEmpty();
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }
}
