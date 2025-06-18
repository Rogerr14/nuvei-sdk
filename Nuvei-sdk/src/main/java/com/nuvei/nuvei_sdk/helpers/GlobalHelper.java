package com.nuvei.nuvei_sdk.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kount.api.DataCollector;
import com.nuvei.nuvei_sdk.Nuvei;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.CardModel.CardBrand;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Utility class for common text-related operations in the Paymentez Android SDK.
 */
public  class GlobalHelper {
    private static final int LENGTH_COMMON_CARD = 16;
    private static final int LENGTH_AMERICAN_EXPRESS = 15;
    private static final int LENGTH_DINERS_CLUB = 14;
    private static String CARD_BRAND = null;

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Returns null if the input string is blank (null, empty, or all whitespace).
     *
     * @param value the input string
     * @return null if the string is blank, otherwise the input value
     */
    @Nullable
    public static String nullIfBlank(@Nullable String value) {
        return isBlank(value) ? null : value;
    }

    /**
     * Checks if the input string is null, empty, or all whitespace.
     *
     * @param value the input string
     * @return true if the string is blank, false otherwise
     */
    public static boolean isBlank(@Nullable String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Removes spaces and hyphens from a card number.
     *
     * @param cardNumber the card number, possibly with spaces or hyphens
     * @return the card number without spaces or hyphens, or null if the input is blank
     */
    @Nullable
    public static String removeSpacesAndHyphens(@Nullable String cardNumber) {
        return isBlank(cardNumber) ? null : cardNumber.replaceAll("[\\s-]", "");
    }

    /**
     * Checks if the input number starts with any of the given prefixes.
     *
     * @param number the number to test
     * @param prefixes the prefixes to check against
     * @return true if the number starts with any prefix, false otherwise
     */
    static boolean hasAnyPrefix(@Nullable String number, String... prefixes) {
        if (number == null) return false;
        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) return true;
        }
        return false;
    }

    /**
     * Computes the SHA-1 hash of the input string and returns it as a hexadecimal string.
     *
     * @param input the string to hash
     * @return the SHA-1 hash as a hex string, or null if the input is blank or hashing fails
     */
    @Nullable
    static String shaHashInput(@Nullable String input) {
        if (isBlank(input)) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            return bytesToHex(digest.digest(bytes));
        } catch (Exception e) {
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Returns a {@link CardModel.CardBrand} corresponding to a partial card number,
     * or {@link CardModel#UNKNOWN} if the card brand can't be determined from the input value.
     *
     * @param cardNumber a credit card number or partial card number
     * @return the {@link CardModel.CardBrand} corresponding to that number,
     * or {@link CardModel#UNKNOWN} if it can't be determined
     */
    @NonNull
    @CardModel.CardBrand
    public static String getPossibleCardType(@Nullable String cardNumber) {
        return getPossibleCardType(cardNumber, true);
    }
    public static void setPossibleCardType(String card_brand){
        CARD_BRAND = card_brand;
    }

    /**
     * Checks the input string to see whether or not it is a valid card number, possibly
     * with groupings separated by spaces or hyphens.
     *
     * @param cardNumber a String that may or may not represent a valid card number
     * @return {@code true} if and only if the input value is a valid card number
     */
    public static boolean isValidCardNumber(@Nullable String cardNumber) {
        String normalizedNumber = GlobalHelper.removeSpacesAndHyphens(cardNumber);
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(normalizedNumber);
    }

    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return {@code true} if and only if the input value is a valid Luhn number
     */
    static boolean isValidLuhnNumber(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        boolean isOdd = true;
        int sum = 0;

        for (int index = cardNumber.length() - 1; index >= 0; index--) {
            char c = cardNumber.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }

            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }

    /**
     * Checks to see whether the input number is of the correct length, after determining its brand.
     * This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return {@code true} if the card number is of known type and the correct length
     */
    static boolean isValidCardLength(@Nullable String cardNumber) {
        return cardNumber != null && isValidCardLength(cardNumber,
                getPossibleCardType(cardNumber, false));
    }

    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @param cardBrand a {@link CardBrand} used to get the correct size
     * @return {@code true} if the card number is the correct length for the assumed brand
     */
    static boolean isValidCardLength(
            @Nullable String cardNumber,
            @NonNull @CardBrand String cardBrand) {
        if(cardNumber == null || CardModel.UNKNOWN.equals(cardBrand)) {
            return false;
        }

        int length = cardNumber.length();
        switch (cardBrand) {
            case CardModel.AMERICAN_EXPRESS:
                return length == LENGTH_AMERICAN_EXPRESS;
            case CardModel.DINERS_CLUB:
                return length == LENGTH_DINERS_CLUB;
            default:
                return length == LENGTH_COMMON_CARD;
        }
    }

    @NonNull
    private static String getPossibleCardType(@Nullable String cardNumber,
                                              boolean shouldNormalize) {
        if(CARD_BRAND != null)
            return CARD_BRAND;

        if (GlobalHelper.isBlank(cardNumber)) {
            return CardModel.UNKNOWN;
        }

        String spacelessCardNumber = cardNumber;
        if (shouldNormalize) {
            spacelessCardNumber = GlobalHelper.removeSpacesAndHyphens(cardNumber);
        }




        if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_AMERICAN_EXPRESS)) {
            return CardModel.AMERICAN_EXPRESS;
        } else if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_DISCOVER)) {
            return CardModel.DISCOVER;
        } else if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_JCB)) {
            return CardModel.JCB;
        } else if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_DINERS_CLUB)) {
            return CardModel.DINERS_CLUB;
        } else if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_VISA)) {
            return CardModel.VISA;
        } else if (GlobalHelper.hasAnyPrefix(spacelessCardNumber, CardModel.PREFIXES_MASTERCARD)) {
            return CardModel.MASTERCARD;
        } else {
            return CardModel.UNKNOWN;
        }
    }


    /**
     * Check to see whether the input string is a whole, positive number.
     *
     * @param value the input string to test
     * @return {@code true} if the input value consists entirely of integers
     */
    public static boolean isWholePositiveNumber(@Nullable String value) {
        return value != null && TextUtils.isDigitsOnly(value);
    }

    /**
     * Determines whether the input year-month pair has passed.
     *
     * @param year the input year, as a two or four-digit integer
     * @param month the input month
     * @param now the current time
     * @return {@code true} if the input time has passed the specified current time,
     *  {@code false} otherwise.
     */
    @SuppressWarnings("WrongConstant")
    public static boolean hasMonthPassed(int year, int month, Calendar now) {
        if (hasYearPassed(year, now)) {
            return true;
        }

        // Expires at end of specified month, Calendar month starts at 0
        return normalizeYear(year, now) == now.get(Calendar.YEAR)
                && month < (now.get(Calendar.MONTH) + 1);
    }

    /**
     * Determines whether or not the input year has already passed.
     *
     * @param year the input year, as a two or four-digit integer
     * @param now, the current time
     * @return {@code true} if the input year has passed the year of the specified current time
     *  {@code false} otherwise.
     */
    public static boolean hasYearPassed(int year, Calendar now) {
        int normalized = normalizeYear(year, now);
        return normalized < now.get(Calendar.YEAR);
    }

    static int normalizeYear(int year, Calendar now)  {
        if (year < 100 && year >= 0) {
            String currentYear = String.valueOf(now.get(Calendar.YEAR));
            String prefix = currentYear.substring(0, currentYear.length() - 2);
            year = Integer.parseInt(String.format(Locale.US, "%s%02d", prefix, year));
        }
        return year;
    }



    public  static String getSessionId(Context context){
        String session_ID = UUID.randomUUID().toString();
        final String device_session_ID =session_ID.replace("-", "");
        final String MERCHANT_ID = "500005";
        final DataCollector dataCollector = com.kount.api.DataCollector.getInstance();
        dataCollector.setDebug(Nuvei.isTestMode());
        dataCollector.setContext(context);
        dataCollector.setMerchantID(MERCHANT_ID);
        if (Nuvei.isTestMode()) {
            dataCollector.setEnvironment(DataCollector.ENVIRONMENT_TEST);
        } else {
            dataCollector.setEnvironment(DataCollector.ENVIRONMENT_PRODUCTION);
        }
        dataCollector.setLocationCollectorConfig(DataCollector.LocationConfig.COLLECT);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                dataCollector.collectForSession(device_session_ID, new com.kount.api.DataCollector.CompletionHandler() {
                    @Override
                    public void completed(String s) {

                    }

                    @Override
                    public void failed(String s, final DataCollector.Error error) {

                    }

                });
            }
        });


    return device_session_ID;
    }

}