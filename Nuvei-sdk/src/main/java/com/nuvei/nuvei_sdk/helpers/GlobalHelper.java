package com.nuvei.nuvei_sdk.helpers;

import androidx.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Utility class for common text-related operations in the Paymentez Android SDK.
 */
public final class GlobalHelper {

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
}