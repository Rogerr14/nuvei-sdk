package com.nuvei.nuvei_sdk.add_card.view;

import static com.nuvei.nuvei_sdk.views.ViewHelpers.separateCardNumberGroups;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.nuvei.nuvei_sdk.add_card.builders.ICardBrandChangeListener;
import com.nuvei.nuvei_sdk.add_card.builders.ICardNumberCompleteListener;
import com.nuvei.nuvei_sdk.helpers.GlobalHelper;
import com.nuvei.nuvei_sdk.helpers.NuveiUtils;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.views.EditTextWidget;
import com.nuvei.nuvei_sdk.views.ViewHelpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardNumberEditText extends EditTextWidget {
    Context cardNumberContext;
    // Note that AmEx and Diners Club have the same length
    // because Diners Club has one more space, but one less digit.
    private static final int MAX_LENGTH_COMMON = 19;
    private static final int MAX_LENGTH_AMEX_DINERS = 17;
    private static final Integer[] SPACES_ARRAY_COMMON = {4, 9, 14};
    private static final Set<Integer> SPACE_SET_COMMON =
            new HashSet<>(Arrays.asList(SPACES_ARRAY_COMMON));
    private static final Integer[] SPACES_ARRAY_AMEX = {4, 11};
    private static final Set<Integer> SPACE_SET_AMEX =
            new HashSet<>(Arrays.asList(SPACES_ARRAY_AMEX));
    @VisibleForTesting
    String cardBrand = CardModel.UNKNOWN;
    String cardLogo = CardModel.UNKNOWN;
    boolean isOTP = false;
    private ICardNumberCompleteListener cardNumberCompleteListener;
    private ICardBrandChangeListener cardBrandChangeListener;
    private int maxLength = 19;
    private boolean ignoreChangues =  false;
    private  boolean isCardNumberValid = false;
    public CardNumberEditText(@NonNull Context context) {
        super(context);
        this.cardNumberContext = context;
        listenForTextChanges();
    }

    public CardNumberEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.cardNumberContext = context;
        listenForTextChanges();
    }

    public CardNumberEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.cardNumberContext = context;
        listenForTextChanges();
    }


    @NonNull
    public String getCardBrand() {
        return cardBrand;
    }

    /**
     * Gets a usable form of the card number. If the text is "4242 4242 4242 4242", this
     * method will return "4242424242424242". If the card number is invalid, this returns
     * {@code null}.
     *
     * @return a space-free version of the card number, or {@code null} if the number is invalid
     */
    @Nullable
    public String getCardNumber() {
        return isCardNumberValid
                ? GlobalHelper.removeSpacesAndHyphens(getText().toString())
                : null;
    }

    public int getLengthMax() {
        return maxLength;
    }

    /**
     * Check whether or not the card number is valid
     *
     * @return the value of {@link #isCardNumberValid}
     */
    public boolean isCardNumberValid() {
        return isCardNumberValid;
    }
    public void setCardNumberCompleteListener(@NonNull ICardNumberCompleteListener listener) {
        cardNumberCompleteListener = listener;
    }

    public void setCardBrandChangeListener(@NonNull ICardBrandChangeListener listener) {
        cardBrandChangeListener = listener;
        // Immediately display the brand if known, in case this method is invoked when
        // partial data already exists.
        cardBrandChangeListener.onCardBrandChanged(cardBrand, cardLogo, isOTP);
    }


    public void updateLengthFilter() {
        setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
    }


    /**
     * Updates the selection index based on the current (pre-edit) index, and
     * the size change of the number being input.
     *
     * @param newLength the post-edit length of the string
     * @param editActionStart the position in the string at which the edit action starts
     * @param editActionAddition the number of new characters going into the string (zero for delete)
     * @return an index within the string at which to put the cursor
     */
    @VisibleForTesting
    int updateSelectionIndex(
            int newLength,
            int editActionStart,
            int editActionAddition) {
        int newPosition, gapsJumped = 0;
        Set<Integer> gapSet = CardModel.AMERICAN_EXPRESS.equals(cardBrand)
                ? SPACE_SET_AMEX
                : SPACE_SET_COMMON;
        boolean skipBack = false;
        for (Integer gap : gapSet) {
            if (editActionStart <= gap && editActionStart + editActionAddition > gap) {
                gapsJumped++;
            }

            // editActionAddition can only be 0 if we are deleting,
            // so we need to check whether or not to skip backwards one space
            if (editActionAddition == 0 && editActionStart == gap + 1) {
                skipBack = true;
            }
        }

        newPosition = editActionStart + editActionAddition + gapsJumped;
        if (skipBack && newPosition > 0) {
            newPosition--;
        }

        return newPosition <= newLength ? newPosition : newLength;
    }


    private void listenForTextChanges() {
        addTextChangedListener(new TextWatcher() {
            int latestChangeStart;
            int latestInsertionSize;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!ignoreChangues) {
                    latestChangeStart = start;
                    latestInsertionSize = after;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ignoreChangues) {
                    return;
                }

                if(start < 6){
                    //updateCardBrand(CardModel.UNKNOWN, CardModel.UNKNOWN, false);
                    updateCardBrand(GlobalHelper.getPossibleCardType(s.toString()), GlobalHelper.getPossibleCardType(s.toString()), false);
                }

                if (start >= 6 && start <= 10) {
                    updateCardBrandFromNumber(s.toString());
                }

                if (start > 16) {
                    // no need to do formatting if we're past all of the spaces.
                    return;
                }

                String spacelessNumber = GlobalHelper.removeSpacesAndHyphens(s.toString());
                if (spacelessNumber == null) {
                    return;
                }

                String[] cardParts = separateCardNumberGroups(
                        spacelessNumber, cardBrand);
                StringBuilder formattedNumberBuilder = new StringBuilder();
                for (int i = 0; i < cardParts.length; i++) {
                    if (cardParts[i] == null) {
                        break;
                    }

                    if (i != 0) {
                        formattedNumberBuilder.append(' ');
                    }
                    formattedNumberBuilder.append(cardParts[i]);
                }

                String formattedNumber = formattedNumberBuilder.toString();
                int cursorPosition = updateSelectionIndex(
                        formattedNumber.length(),
                        latestChangeStart,
                        latestInsertionSize);

                ignoreChangues = true;
                setText(formattedNumber);
                setSelection(cursorPosition);
                ignoreChangues = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == maxLength) {
                    boolean before = isCardNumberValid;
                    isCardNumberValid = GlobalHelper.isValidCardNumber(s.toString());
                    setShouldShowError(!isCardNumberValid);
                    if (!before && isCardNumberValid && cardNumberCompleteListener != null) {
                        cardNumberCompleteListener.onCardNumberComplete();
                    }
                } else {
                    isCardNumberValid = getText() != null
                            && GlobalHelper.isValidCardNumber(getText().toString());
                    // Don't show errors if we aren't full-length.
                    setShouldShowError(false);
                }
            }
        });
    }


    private void updateCardBrand(@NonNull String brand, String logo, boolean isOtp) {
        if (cardBrand.equals(brand)) {
            return;
        }

        cardBrand = brand;
        cardLogo = logo;
        isOTP = isOtp;

        if (cardBrandChangeListener != null) {
            cardBrandChangeListener.onCardBrandChanged(cardBrand, cardLogo, isOtp);
        }

        int oldLength = maxLength;
        maxLength = getLengthForBrand(cardBrand);
        if (oldLength == maxLength) {
            return;
        }

        updateLengthFilter();
    }


    private void updateCardBrandFromNumber(String partialNumber) {
        String spacelessCardNumber = GlobalHelper.removeSpacesAndHyphens(partialNumber);
        Log.v("sin espacion", spacelessCardNumber);
        if(partialNumber.length() >= 6 && partialNumber.length() <= 10){
            String brand = CardModel.getCardBrandPosible(spacelessCardNumber);
                updateCardBrand(brand, CardModel.asCardBrand(brand), false);
        }
    }

    private static int getLengthForBrand(String cardBrand) {
        if (CardModel.AMERICAN_EXPRESS.equals(cardBrand) || CardModel.DINERS_CLUB.equals(cardBrand)) {
            return MAX_LENGTH_AMEX_DINERS;
        } else {
            return MAX_LENGTH_COMMON;
        }
    }

}
