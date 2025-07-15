package com.nuvei.nuvei_sdk.views;

import static com.nuvei.nuvei_sdk.models.CardModel.BRAND_RESOURCE_MAP;
import static com.nuvei.nuvei_sdk.views.ICardInputListener.FocusField.FOCUS_CARD;
import static com.nuvei.nuvei_sdk.views.ICardInputListener.FocusField.FOCUS_CARDHOLDERNAME;
import static com.nuvei.nuvei_sdk.views.ICardInputListener.FocusField.FOCUS_CVC;
import static com.nuvei.nuvei_sdk.views.ICardInputListener.FocusField.FOCUS_EXPIRY;
import static com.nuvei.nuvei_sdk.views.ICardInputListener.FocusField.FOCUS_POSTAL;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.VisibleForTesting;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;
import com.nuvei.nuvei_sdk.R;
import com.nuvei.nuvei_sdk.add_card.builders.ICardBrandChangeListener;
import com.nuvei.nuvei_sdk.add_card.builders.ICardNumberCompleteListener;
import com.nuvei.nuvei_sdk.add_card.builders.IExpireDateListener;
import com.nuvei.nuvei_sdk.add_card.view.CardNumberEditText;
import com.nuvei.nuvei_sdk.add_card.view.DateEditText;
import com.nuvei.nuvei_sdk.builders.CardBuilder;
import com.nuvei.nuvei_sdk.builders.IAfterChangeListener;
import com.nuvei.nuvei_sdk.builders.IErrorMessageListener;
import com.nuvei.nuvei_sdk.helpers.GlobalHelper;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardInformationWidget extends LinearLayout {

    static  final String CARD_TOKEN_WIDGET = "CardViewToken";
    static  final long CARD_NUMBER_HINT_DELAY = 120L;
    static  final long COMMON_HINT_DELAY = 90L;
    private int MY_SCAN_REQUEST_CODE = 10344;
    @Nullable
    private  ICardInputListener cardInputListener;
    private CardNumberEditText cardNumberEditText;
    private DateEditText dateEditText;
    private EditTextWidget cvcEditText, postalCodeEditText, cardHolderNameEditText, fiscalNumberEditText;
    private TextInputLayout cardNumberInputLayout, expireInputLayout, cvcInputLayout, nipInputLayout, fiscalNumberInputLayout, postalInputLayout, cardHolderInputLayout;
    private ImageButton imageButtonScanCard;
    private ImageView imageViewLogo;
    private LinearLayout secondRowLayout, thirdRowLayout, fourRowLayout;
    private boolean isEnabled, showPostalCode, showCardHolderName, showFiscalNumber, showScanCard, showLogo, adjustDrawable, isOTP;
    private @DrawableRes
    int cachedIconResource;
    String cardBrand, cardLogo;
    private @ColorInt
    int tintColorInt;



    public CardInformationWidget(Context context) {
        super(context);
        initView(null);
    }

    public CardInformationWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public CardInformationWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public CardInformationWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    private void initView(AttributeSet attrs) {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.dialog_add_card, this);
        final Context mContext = this.getContext();

        imageViewLogo = (ImageView) findViewById(R.id.logoNuvei);

        imageButtonScanCard = (ImageButton) findViewById(R.id.imageButtonScanCard);
        imageButtonScanCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                Fragment auxiliary = new Fragment() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        if (requestCode == MY_SCAN_REQUEST_CODE) {

                            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                                if(scanResult.cardNumber!=null) {
                                    cardNumberEditText.setText(scanResult.cardNumber);
                                }
                                if(scanResult.cardholderName!=null){
                                    cardHolderNameEditText.setText(scanResult.cardholderName);
                                }

                                if(scanResult.cvv!=null){
                                    cvcEditText.setText(scanResult.cvv);
                                }

                                if(scanResult.expiryMonth > 0 && scanResult.expiryYear > 0){

                                    dateEditText.setText(String.format(Locale.ENGLISH, "%02d", scanResult.expiryMonth)+"/"+ (""+scanResult.expiryYear).substring(2));
                                }

                                validateAllInputFields();

                            }

                        }

                        super.onActivityResult(requestCode, resultCode, data);
                        fm.beginTransaction().remove(this).commit();
                    }
                };
                fm.beginTransaction().add(auxiliary, "FRAGMENT_TAG").commit();
                fm.executePendingTransactions();

                Intent scanIntent = new Intent(mContext, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                auxiliary.startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

            }
        });




        cardNumberEditText = (CardNumberEditText) findViewById(R.id.et_add_source_card_number);

        fiscalNumberEditText = (EditTextWidget) findViewById(R.id.et_add_fiscal_number);



        dateEditText = (DateEditText) findViewById(R.id.et_add_source_expiry);
        cvcEditText = (EditTextWidget) findViewById(R.id.et_add_source_cvc);
        postalCodeEditText = (EditTextWidget) findViewById(R.id.et_add_source_zip_code);
        cardHolderNameEditText = (EditTextWidget) findViewById(R.id.et_add_source_cardholdername);
        tintColorInt = cardNumberEditText.getHintTextColors().getDefaultColor();

        cardBrand = CardModel.UNKNOWN;
        // This sets the value of mShouldShowPostalCode
        checkAttributeSet(attrs);


        cardNumberInputLayout = (TextInputLayout) findViewById(R.id.tl_add_source_card_number);
        expireInputLayout = (TextInputLayout) findViewById(R.id.tl_add_source_expiry);
        // We dynamically set the hint of the CVC field, so we need to keep a reference.
        cvcInputLayout = (TextInputLayout) findViewById(R.id.tl_add_source_cvc);
        fiscalNumberInputLayout = (TextInputLayout) findViewById(R.id.tl_add_fiscal_adress);
        postalInputLayout = (TextInputLayout) findViewById(R.id.tl_add_source_zip_code);
        cardHolderInputLayout = (TextInputLayout) findViewById(R.id.tl_card_holder_name);


        secondRowLayout = (LinearLayout) findViewById(R.id.row_expiry_cvc);
//        third_row_layout = (LinearLayout) findViewById(R.id.third_row_layout);
//        fourRowLayout = (LinearLayout) findViewById(R.id.four_row_layout);


//        buttonHideNip = (Button) findViewById(R.id.buttonHideNip);
//        buttonHideNip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mNipTextInputLayout.getVisibility() == View.INVISIBLE){
//                    mShouldShowNip = true;
//                    mNipTextInputLayout.setVisibility(View.VISIBLE);
//                }else{
//                    mShouldShowNip = false;
//                    mNipTextInputLayout.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        if (showPostalCode) {
            // Set the label/hint to the shorter value if we have three things in a row.
            dateEditText.setHint(getResources().getString(R.string.expiry_label_short));
        }

        initTextInputLayoutErrorHandlers(
                cardHolderInputLayout,
                cardNumberInputLayout,
                expireInputLayout,
                cvcInputLayout,
                postalInputLayout,
                fiscalNumberInputLayout
        );

        initErrorMessages();
        initFocusChangeListeners();
        initDeleteEmptyListeners();


        cardNumberEditText.setCardBrandChangeListener(
                (brand, cardLogo, isOtp) -> {
                    Log.v("marca de tarjeta", brand);
                    updateBrand(brand, cardLogo, isOtp);
                });

        cardNumberEditText.setCardNumberCompleteListener(
                () -> {
                    if(cardBrand.equals("ex") || cardBrand.equals("ak")){
                        fiscalNumberEditText.requestFocus();
                        fiscalNumberInputLayout.setVisibility(View.VISIBLE);
                    }else{
                        dateEditText.requestFocus();
                        setEnabled(false);
                    }

                    if (cardInputListener != null) {
                        cardInputListener.onCardComplete();
                    }
                });



        dateEditText.setExpiryDateEditListener(
                new IExpireDateListener() {
                    @Override
                    public void onExpiryDateComplete() {
                        cvcEditText.requestFocus();
                        if (cardInputListener != null) {
                            cardInputListener.onExpirationComplete();
                        }
                    }
                });

        cvcEditText.setAfterTextChangedListener(
                new IAfterChangeListener() {
                    @Override
                    public void onTextChanged(String text) {
                        if (ViewHelpers.isCvcMaximalLength(cardBrand, text)) {
                            updateBrand(cardBrand, cardLogo, isOTP);
                            if (showPostalCode) {
                                postalCodeEditText.requestFocus();
                            }
                            if (cardInputListener != null) {
                                cardInputListener.onCvcComplete();
                            }
                        } else {
                            flipToCvcIconIfNotFinished();
                        }
                        cvcEditText.setShouldShowError(false);
                    }
                });



        fiscalNumberInputLayout.setVisibility(View.GONE);
        adjustViewForPostalCodeAttribute();
        adjustViewForCardHolderNameAttribute();
        adjustViewForScanCardAttribute();
        adjustViewForPaymentezLogoAttribute();

        postalCodeEditText.setAfterTextChangedListener(
                new IAfterChangeListener() {
                    @Override
                    public void onTextChanged(String text) {
                        if (isPostalCodeMaximalLength(true, text)
                                && cardInputListener != null) {
                            cardInputListener.onPostalCodeComplete();
                        }
                        postalCodeEditText.setShouldShowError(false);
                    }
                });

        cardNumberEditText.updateLengthFilter();
        updateBrand(CardModel.UNKNOWN, cardLogo, isOTP);
        setEnabled(true);
    }


    @VisibleForTesting
    CardInformationWidget(Context context, boolean activePostalCode, boolean activeCardHolderName, boolean activeScanCard, boolean activeLogo, boolean activeFiscalNumber ){
        super(context);
        showCardHolderName = activeCardHolderName;
        showPostalCode = activePostalCode;
        showScanCard = activeScanCard;
        showLogo = activeLogo;
        showFiscalNumber = activeFiscalNumber;
    }


    /**
     * @param listener A {@link ICardInputListener} to be notified of changes
     *                          to the user's focused field
     */
    public void setCardInputListener(@Nullable ICardInputListener listener) {
        cardInputListener = listener;
    }


    /**
     * Get a {@link CardModel} from inputs, if all fields
     */

    @Nullable
    public CardModel getCardInfo(){
        if(validateAllInputFields()){
            String cardNumber = cardNumberEditText.getCardNumber();
            int[] cardDate = dateEditText.getValidDateFields();
            String cvcValue = cvcEditText.getText().toString();
            int month = 0, year = 0;


            if(cardDate != null && cardDate.length>= 2){
                month =  cardDate[0];
                year = cardDate[1];
            }

            CardModel cardModel = new CardModel(new CardBuilder(cardNumber, month, year, cvcValue));
            if(cardBrand.equals("ex") || cardBrand.equals("ak")){
                cardModel.setCvc("");
                cardModel.setFiscal_number(fiscalNumberEditText.getText().toString());

            }

            if(showPostalCode){
                cardModel.setAddressZip(postalCodeEditText.getText().toString());

            }
            if(showCardHolderName){
                cardModel.setHolderName(cardHolderNameEditText.getText().toString());
            }
            return cardModel.addLoggingToken(CARD_TOKEN_WIDGET);
        }
        return  null;

    }



    public boolean validateAllInputFields(){

        boolean postalCodeIsValidOrGone = true, cardHolderNameIsValidOrGone = true, fiscalNumberIsValidOrGone = true;
        boolean isCardNumberValid = GlobalHelper.isValidCardNumber(cardNumberEditText.getCardNumber());
        Log.v("valid card number", String.valueOf(isCardNumberValid));
        boolean isDateExpiryValid = dateEditText.getValidDateFields() != null && dateEditText.isDateValid();
        Log.v("valid card date", String.valueOf(isDateExpiryValid));
        boolean isCvcValid = ViewHelpers.isCvcMaximalLength(cardBrand, cvcEditText.getText().toString());
        Log.v("valid card cvc", String.valueOf(isCvcValid));
        if(cardBrand.equals("ex") || cardBrand.equals("ak")){
            isDateExpiryValid = true;
            isCvcValid = true;
        }
        cardNumberEditText.setShouldShowError(!isCardNumberValid);
        dateEditText.setShouldShowError(!isDateExpiryValid);
        cvcEditText.setShouldShowError(!isCvcValid);

        if(showPostalCode){
            postalCodeIsValidOrGone = isPostalCodeMaximalLength(true, postalCodeEditText.getText().toString());
            postalCodeEditText.setShouldShowError(!postalCodeIsValidOrGone);
        }
        if(showCardHolderName){
            cardHolderNameIsValidOrGone = isCardHolderNameValid(cardHolderNameEditText.getText().toString());
            cardNumberEditText.setShouldShowError(!cardHolderNameIsValidOrGone);
        }

        if(showFiscalNumber){
            fiscalNumberIsValidOrGone = isFiscalNumberValid(fiscalNumberEditText.getText().toString());
            fiscalNumberEditText.setShouldShowError(!fiscalNumberIsValidOrGone);
        }

        return  isCardNumberValid && isDateExpiryValid && isCvcValid && postalCodeIsValidOrGone && fiscalNumberIsValidOrGone && cardHolderNameIsValidOrGone;


    }


    static boolean isCardHolderNameValid(@Nullable String text) {
        return text != null && text.length() >= 5;
    }

    static boolean isFiscalNumberValid(@Nullable String text) {
        return text != null && text.length() >= 6;
    }

    static boolean isNipValid(@Nullable String text) {
        return text != null && text.length() == 4;
    }

    static boolean isPostalCodeMaximalLength(boolean isZip, @Nullable String text) {
        return isZip && text != null && text.length() == 5;
    }

    @Override
    public void setEnabled(boolean enabled) {
        expireInputLayout.setEnabled(enabled);
        cardNumberInputLayout.setEnabled(enabled);
        cvcInputLayout.setEnabled(enabled);
        fiscalNumberInputLayout.setEnabled(enabled);
        postalInputLayout.setEnabled(enabled);
        cardHolderInputLayout.setEnabled(enabled);
        isEnabled = enabled;
    }



    private void updateBrand(@NonNull String brand, String cardLogo, boolean isOtp) {
        cardBrand = brand;
        cardLogo = cardLogo;
        isOTP = isOtp;

        updateCvc(cardBrand);
        int iconResourceId = BRAND_RESOURCE_MAP.get(CardModel.UNKNOWN);
        try{

            iconResourceId = BRAND_RESOURCE_MAP.get(GlobalHelper.getPossibleCardType(cardBrand));
        }catch(Exception e){}





        updateDrawable(iconResourceId, CardModel.UNKNOWN.equals(brand), cardLogo);
    }

    private void checkAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CardMultilineWidget,
                    0, 0);

            try {
                showScanCard =
                        a.getBoolean(R.styleable.CardMultilineWidget_shouldShowScanCard, false);
                showLogo =
                        a.getBoolean(R.styleable.CardMultilineWidget_shouldShowPaymentezLogo, true);
                showFiscalNumber = a.getBoolean(R.styleable.CardMultilineWidget_shouldFiscalNumber, false);
                showPostalCode =
                        a.getBoolean(R.styleable.CardMultilineWidget_shouldShowPostalCode, false);
                showCardHolderName =
                        a.getBoolean(R.styleable.CardMultilineWidget_shouldShowCardHolderName, true);
            } finally {
                a.recycle();
            }
        }
    }

    private void flipToCvcIconIfNotFinished() {
        if (ViewHelpers.isCvcMaximalLength(cardBrand, cvcEditText.getText().toString())) {
            return;
        }

        @DrawableRes int resourceId = CardModel.AMERICAN_EXPRESS.equals(cardBrand)
                ? R.drawable.ic_cvc_amex
                : R.drawable.ic_cvc;

        updateDrawable(resourceId, true, null);
    }

    @StringRes
    private int getCvcHelperText() {
        return CardModel.AMERICAN_EXPRESS.equals(cardBrand)
                ? R.string.cvc_multiline_helper_amex
                : R.string.cvc_multiline_helper;
    }

    private void updateDrawable(
            @DrawableRes int iconResourceId,
            final boolean needsTint, String brandLogoUrl) {


        final Drawable[] icon = new Drawable[1];
        icon[0] = getResources().getDrawable(iconResourceId, null);

        Drawable[] drawables = cardNumberEditText.getCompoundDrawables();
        Drawable original = drawables[0];
        if (original == null) {
            return;
        }

        final Rect copyBounds = new Rect();
        original.copyBounds(copyBounds);

        final int iconPadding = cardNumberEditText.getCompoundDrawablePadding();

        if (!adjustDrawable) {
            copyBounds.top = copyBounds.top - getDynamicBufferInPixels();
            copyBounds.bottom = copyBounds.bottom - getDynamicBufferInPixels();
            adjustDrawable = true;
        }

        icon[0].setBounds(copyBounds);
        final Drawable[] compatIcon = {DrawableCompat.wrap(icon[0])};
        if (needsTint) {
            DrawableCompat.setTint(compatIcon[0].mutate(), tintColorInt);
        }

        cardNumberEditText.setCompoundDrawablePadding(iconPadding);
        cardNumberEditText.setCompoundDrawables(compatIcon[0], null, null, null);

        if(brandLogoUrl != null && !brandLogoUrl.equals(CardModel.UNKNOWN)){
          Picasso.get().load(brandLogoUrl).into(new Target() {

                @Override
                public void onPrepareLoad(Drawable arg0) {


                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                    Drawable resource = new BitmapDrawable(getContext().getResources(),bitmap);
                    resource.setBounds(copyBounds);
                    Drawable compatIcon = DrawableCompat.wrap(resource);
                    if(needsTint){
                        DrawableCompat.setTint(compatIcon.mutate(), tintColorInt);
                    }
                    cardNumberEditText.setCompoundDrawablePadding(iconPadding);
                    cardNumberEditText.setCompoundDrawables(resource, null, null, null);

                }

              @Override
              public void onBitmapFailed(Exception e, Drawable errorDrawable) {

              }



            });

        }

    }
    private void updateCvc(@NonNull String brand) {
        if (CardModel.AMERICAN_EXPRESS.equals(brand)) {
            cvcEditText.setFilters(
                    new InputFilter[]{
                            new InputFilter.LengthFilter(CardModel.CVC_LENGTH_AMERICAN_EXPRESS)
                    });
            cvcInputLayout.setHint(getResources().getString(R.string.cvc_amex_hint));
        } else {
            cvcEditText.setFilters(
                    new InputFilter[]{
                            new InputFilter.LengthFilter(CardModel.CVC_LENGTH_COMMON)});
            cvcInputLayout.setHint(getResources().getString(R.string.cvc_number_hint));
        }
    }

    private int getDynamicBufferInPixels() {
        float pixelsToAdjust = getResources()
                .getDimension(R.dimen.card_icon_multiline_padding_bottom);
        BigDecimal bigDecimal = new BigDecimal(pixelsToAdjust);
        BigDecimal pixels = bigDecimal.setScale(0, RoundingMode.HALF_DOWN);
        return pixels.intValue();
    }

    private void initErrorMessages() {
        cardHolderNameEditText.setErrorMessage(getContext().getString(R.string.invalid_cardholdername));
        cardNumberEditText.setErrorMessage(getContext().getString(R.string.invalid_card_number));
        dateEditText.setErrorMessage(getContext().getString(R.string.invalid_expiry_year));
        cvcEditText.setErrorMessage(getContext().getString(R.string.invalid_cvc));
        postalCodeEditText.setErrorMessage(getContext().getString(R.string.invalid_zip));
        postalCodeEditText.setErrorMessage(getContext().getString(R.string.invalid_cardholdername));
        fiscalNumberEditText.setErrorMessage(getContext().getString(R.string.error_fiscal_number));

    }

    private void initFocusChangeListeners() {
        cardNumberEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardNumberEditText.setHintDelayed(
                            R.string.card_number_hint, CARD_NUMBER_HINT_DELAY);
                    if (cardInputListener != null) {
                        cardInputListener.onFocusChange(FOCUS_CARD);
                    }
                } else {
                    cardNumberEditText.setHint("");
                }
            }
        });

        dateEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateEditText.setHintDelayed(
                            R.string.expiry_date_hint, COMMON_HINT_DELAY);
                    if (cardInputListener != null) {
                        cardInputListener.onFocusChange(FOCUS_EXPIRY);
                    }
                } else {


                    dateEditText.setHint("");

                }
            }
        });

        cvcEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    flipToCvcIconIfNotFinished();
                    @StringRes int helperText = getCvcHelperText();
                    cvcEditText.setHintDelayed(helperText, COMMON_HINT_DELAY);
                    if (cardInputListener != null) {
                        cardInputListener.onFocusChange(FOCUS_CVC);
                    }
                } else {
                    updateBrand(cardBrand, cardLogo, isOTP);
                    cvcEditText.setHint("");
                }
            }
        });

        if (postalCodeEditText != null) {
            postalCodeEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!showPostalCode) {
                        return;
                    }
                    if (hasFocus) {
                        postalCodeEditText.setHintDelayed(R.string.zip_helper, COMMON_HINT_DELAY);
                        if (cardInputListener != null) {
                            cardInputListener.onFocusChange(FOCUS_POSTAL);
                        }
                    } else {
                        postalCodeEditText.setHint("");
                    }
                }
            });
        }


        if (cardHolderNameEditText != null) {
            cardHolderNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!showCardHolderName) {
                        return;
                    }
                    if (hasFocus) {
                        cardHolderNameEditText.setHintDelayed(R.string.name_helper, COMMON_HINT_DELAY);
                        if (cardInputListener != null) {
                            cardInputListener.onFocusChange(FOCUS_CARDHOLDERNAME);
                        }
                    } else {
                        cardHolderNameEditText.setHint("");
                        if(!isCardHolderNameValid(cardHolderNameEditText.getText().toString())){
                            cardHolderNameEditText.setShouldShowError(true);
                        }else{
                            cardHolderNameEditText.setShouldShowError(false);
                        }
                    }
                }
            });
        }


        if (fiscalNumberEditText != null) {
            fiscalNumberEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!showFiscalNumber) {
                        return;
                    }
                    if (hasFocus) {
                    } else {
                        fiscalNumberEditText.setHint("");
                        if(!isFiscalNumberValid(fiscalNumberEditText.getText().toString())){
                            fiscalNumberEditText.setShouldShowError(true);
                        }else{
                            fiscalNumberEditText.setShouldShowError(false);
                        }
                    }
                }
            });
        }
    }


    void adjustViewForPostalCodeAttribute() {
        // Set the label/hint to the shorter value if we have three things in a row.
//        @StringRes int expiryLabel = showPostalCode
//                ? R.string.expiry_label_short
//                : R.string.acc_label_expiry_date;
//        dateEditText.setHint(getResources().getString(expiryLabel));

        @IdRes int focusForward = showPostalCode
                ? R.id.et_add_source_zip_code
                : NO_ID;
        cvcEditText.setNextFocusForwardId(focusForward);
        cvcEditText.setNextFocusDownId(focusForward);

        int visibility = showPostalCode ? View.VISIBLE : View.GONE;
        postalInputLayout.setVisibility(visibility);

        int marginPixels = showPostalCode
                ? getResources().getDimensionPixelSize(R.dimen.add_card_expiry_middle_margin)
                : 0;
        LinearLayout.LayoutParams linearParams =
                (LinearLayout.LayoutParams) cvcInputLayout.getLayoutParams();
        linearParams.setMargins(0, 0, marginPixels, 0);
        linearParams.setMarginEnd(marginPixels);

        cvcInputLayout.setLayoutParams(linearParams);
    }


    void adjustViewForCardHolderNameAttribute() {
        int visibility = showCardHolderName ? View.VISIBLE : View.GONE;
        cardHolderInputLayout.setVisibility(visibility);
        if(showCardHolderName){
            cardHolderNameEditText.requestFocus();
        }else{
            cardNumberEditText.requestFocus();
        }
    }

    void adjustViewForPaymentezLogoAttribute() {

        int visibility = showLogo ? View.VISIBLE : View.GONE;
        imageViewLogo.setVisibility(visibility);
    }
    void adjustViewForScanCardAttribute() {

        int visibility = showScanCard ? View.VISIBLE : View.GONE;
        imageButtonScanCard.setVisibility(visibility);
    }
    private void initTextInputLayoutErrorHandlers(
            TextInputLayout cardholdernameInputLayout,
            TextInputLayout cardInputLayout,
            TextInputLayout expiryInputLayout,
            TextInputLayout cvcTextInputLayout,
            TextInputLayout postalInputLayout,
            TextInputLayout fiscalNumberInputLayout
    ) {


        cardNumberEditText.setErrorMessageListener(new ErrorListener(cardInputLayout));
        dateEditText.setErrorMessageListener(new ErrorListener(expiryInputLayout));



        cvcEditText.setErrorMessageListener(new ErrorListener(cvcTextInputLayout));
        if (postalCodeEditText != null) {
            postalCodeEditText.setErrorMessageListener(new ErrorListener(postalInputLayout));
        }


        if (cardHolderNameEditText != null) {
            cardHolderNameEditText.setErrorMessageListener(new ErrorListener(cardholdernameInputLayout));
        }



        if (fiscalNumberEditText != null) {
            fiscalNumberEditText.setErrorMessageListener(new ErrorListener(fiscalNumberInputLayout));
        }

    }
    private void initDeleteEmptyListeners() {

        dateEditText.setDeleteEmptyListener(
                new BackupFieldDeleteListener(cardNumberEditText));

        cvcEditText.setDeleteEmptyListener(
                new BackupFieldDeleteListener(dateEditText));

        // It doesn't matter whether or not the postal code is shown;
        // we can still say where you go when you delete an empty field from it.
        if (postalCodeEditText != null) {
            postalCodeEditText.setDeleteEmptyListener(
                    new BackupFieldDeleteListener(cvcEditText));
        }

    }


    private static class ErrorListener implements IErrorMessageListener {

        TextInputLayout textInputLayout;

        ErrorListener(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void displayErrorMessage(@Nullable String message) {
            if (message == null) {
                textInputLayout.setError(message);
                textInputLayout.setErrorEnabled(false);
            } else {
                textInputLayout.setError(message);
            }
        }
    }
}
