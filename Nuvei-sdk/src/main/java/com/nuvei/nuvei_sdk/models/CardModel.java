package com.nuvei.nuvei_sdk.models;

import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.StringDef;

import com.nuvei.nuvei_sdk.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nuvei.nuvei_sdk.helpers.GlobalHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A model object representing a Card in the Paymentez Android SDK.
 */
public class CardModel extends JsonModel  {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({AMERICAN_EXPRESS, DISCOVER, JCB, DINERS_CLUB, VISA, MASTERCARD, UNKNOWN, EXITO, ALKOSTO})
    public @interface CardBrand {}

    public static final String AMERICAN_EXPRESS = "ax";
    public static final String DISCOVER = "dc";
    public static final String JCB = "jc";
    public static final String DINERS_CLUB = "di";
    public static final String VISA = "vi";
    public static final String MASTERCARD = "mc";
    public static final String EXITO = "ex";
    public static final String ALKOSTO = "ak";
    public static final String UNKNOWN = "Unknown";

    public static final int CVC_LENGTH_AMERICAN_EXPRESS = 4;
    public static final int CVC_LENGTH_COMMON = 3;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FUNDING_CREDIT, FUNDING_DEBIT, FUNDING_PREPAID, FUNDING_UNKNOWN})
    public @interface FundingType {}

    public static final String FUNDING_CREDIT = "credit";
    public static final String FUNDING_DEBIT = "debit";
    public static final String FUNDING_PREPAID = "prepaid";
    public static final String FUNDING_UNKNOWN = "unknown";

    public static final Map<String, Integer> BRAND_RESOURCE_MAP = new HashMap<String, Integer>() {{
        put(AMERICAN_EXPRESS, R.drawable.ic_amex);
        put(DINERS_CLUB, R.drawable.ic_diners);
        put(DISCOVER, R.drawable.ic_discover);
        put(JCB, R.drawable.ic_jcb);
        put(MASTERCARD, R.drawable.ic_mastercard);
        put(VISA, R.drawable.ic_visa);
        put(UNKNOWN, R.drawable.ic_unknown);
        put(EXITO, R.drawable.ic_exito);
        put(ALKOSTO, R.drawable.ic_alkosto);
    }};

    public static final String[] PREFIXES_AMERICAN_EXPRESS = {"34", "37"};
    public static final String[] PREFIXES_DISCOVER = {"60", "62", "64", "65"};
    public static final String[] PREFIXES_JCB = {"35"};
    public static final String[] PREFIXES_DINERS_CLUB = {"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
    public static final String[] PREFIXES_VISA = {"4"};
    public static final String[] PREFIXES_MASTERCARD = {
            "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229",
            "223", "224", "225", "226", "227", "228", "229",
            "23", "24", "25", "26",
            "270", "271", "2720",
            "50", "51", "52", "53", "54", "55"
    };

    public static final int MAX_LENGTH_STANDARD = 16;
    public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
    public static final int MAX_LENGTH_DINERS_CLUB = 14;
    public static final String VALUE_CARD = "card";

    private static final String FIELD_OBJECT = "object";
    private static final String FIELD_ADDRESS_CITY = "address_city";
    private static final String FIELD_ADDRESS_COUNTRY = "address_country";
    private static final String FIELD_ADDRESS_LINE1 = "address_line1";
    private static final String FIELD_ADDRESS_LINE1_CHECK = "address_line1_check";
    private static final String FIELD_ADDRESS_LINE2 = "address_line2";
    private static final String FIELD_ADDRESS_STATE = "address_state";
    private static final String FIELD_ADDRESS_ZIP = "address_zip";
    private static final String FIELD_ADDRESS_ZIP_CHECK = "address_zip_check";
    private static final String FIELD_BRAND = "type";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_CURRENCY = "currency";
    private static final String FIELD_CUSTOMER = "customer";
    private static final String FIELD_CVC_CHECK = "cvc_check";
    private static final String FIELD_EXP_MONTH = "exp_month";
    private static final String FIELD_EXP_YEAR = "exp_year";
    private static final String FIELD_FINGERPRINT = "fingerprint";
    private static final String FIELD_FUNDING = "funding";
    private static final String FIELD_NAME = "holderName";
    private static final String FIELD_LAST4 = "last4";
    private static final String FIELD_ID = "id";

    @SerializedName("bin") @Expose private String bin;
    @SerializedName("status") @Expose private String status;
    @SerializedName("nip") @Expose private String nip;
    @SerializedName("card_auth") @Expose private String card_auth;
    @SerializedName("number") @Expose private String number;
    @SerializedName("token") @Expose private String token;
    @SerializedName("cvc") @Expose private String cvc;
    @SerializedName("holder_name") @Expose private String holderName;
    @SerializedName("transaction_reference") @Expose private String transactionReference;
    @SerializedName("message") @Expose private String message;
    @SerializedName("type") @Expose @CardBrand private String type;
    @SerializedName("expiry_month") @Expose private Integer expiryMonth;
    @SerializedName("expiry_year") @Expose private Integer expiryYear;
    private String termination;
    private String fiscal_number;
    private String addressLine1;
    private String addressLine2;
    private String addressCity;
    private String addressState;
    private String addressZip;
    private String addressCountry;
    @Size(4) private String last4;
    @FundingType private String funding;
    private String fingerprint;
    private String country;
    private String currency;
    private String customerId;
    private String cvcCheck;
    private String id;
    private String addressLine1Check;
    private String addressZipCheck;
    @NonNull private transient List<String> loggingTokens = new ArrayList<>();

    public static class Builder {
        private String number;
        private Integer expMonth;
        private Integer expYear;
        private String cvc;
        private String name;
        private String addressLine1;
        private String addressLine2;
        private String addressCity;
        private String addressState;
        private String addressZip;
        private String addressCountry;
        private String brand;
        private String funding;
        private String last4;
        private String fingerprint;
        private String country;
        private String currency;
        private String customer;
        private String cvcCheck;
        private String id;
        private String bin;
        private String status;
        private String token;
        private String transactionReference;
        private String message;

        public Builder(@Nullable String number, @IntRange(from = 1, to = 12) Integer expMonth,
                       @IntRange(from = 0) Integer expYear, @Nullable String cvc) {
            this.number = number;
            this.expMonth = expMonth;
            this.expYear = expYear;
            this.cvc = cvc;
        }

        public Builder name(String name) { this.name = name; return this; }
        public Builder addressLine1(String address) { this.addressLine1 = address; return this; }
        public Builder addressLine2(String address) { this.addressLine2 = address; return this; }
        public Builder addressCity(String city) { this.addressCity = city; return this; }
        public Builder addressState(String state) { this.addressState = state; return this; }
        public Builder addressZip(String zip) { this.addressZip = zip; return this; }
        public Builder addressCountry(String country) { this.addressCountry = country; return this; }
        public Builder brand(@CardBrand String brand) { this.brand = brand; return this; }
        public Builder funding(@FundingType String funding) { this.funding = funding; return this; }
        public Builder last4(String last4) { this.last4 = last4; return this; }
        public Builder fingerprint(String fingerprint) { this.fingerprint = fingerprint; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder customer(String customer) { this.customer = customer; return this; }
        public Builder cvcCheck(String cvcCheck) { this.cvcCheck = cvcCheck; return this; }
        public Builder id(String id) { this.id = id; return this; }
        public Builder bin(String bin) { this.bin = bin; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public Builder message(String message) { this.message = message; return this; }

        public CardModel build() {
            return new Card(this);
        }
    }

    private Card(Builder builder) {
        this.number = GlobalHelper.nullIfBlank(normalizeCardNumber(builder.number));
        this.expiryMonth = builder.expMonth;
        this.expiryYear = builder.expYear;
        this.cvc = GlobalHelper.nullIfBlank(builder.cvc);
        this.holderName = GlobalHelper.nullIfBlank(builder.name);
        this.addressLine1 = GlobalHelper.nullIfBlank(builder.addressLine1);
        this.addressLine2 = GlobalHelper.nullIfBlank(builder.addressLine2);
        this.addressCity = GlobalHelper.nullIfBlank(builder.addressCity);
        this.addressState = GlobalHelper.nullIfBlank(builder.addressState);
        this.addressZip = GlobalHelper.nullIfBlank(builder.addressZip);
        this.addressCountry = GlobalHelper.nullIfBlank(builder.addressCountry);
        this.last4 = GlobalHelper.nullIfBlank(builder.last4) == null ? getLast4() : builder.last4;
        this.type = asCardBrand(builder.brand) == null ? getType() : builder.brand;
        this.fingerprint = GlobalHelper.nullIfBlank(builder.fingerprint);
        this.funding = asFundingType(builder.funding);
        this.country = GlobalHelper.nullIfBlank(builder.country);
        this.currency = GlobalHelper.nullIfBlank(builder.currency);
        this.customerId = GlobalHelper.nullIfBlank(builder.customer);
        this.cvcCheck = GlobalHelper.nullIfBlank(builder.cvcCheck);
        this.id = GlobalHelper.nullIfBlank(builder.id);
        this.bin = GlobalHelper.nullIfBlank(builder.bin);
        this.status = GlobalHelper.nullIfBlank(builder.status);
        this.token = GlobalHelper.nullIfBlank(builder.token);
        this.transactionReference = GlobalHelper.nullIfBlank(builder.transactionReference);
        this.message = GlobalHelper.nullIfBlank(builder.message);
    }

    public static int getDrawableBrand(String brand) {
        return BRAND_RESOURCE_MAP.getOrDefault(brand, R.drawable.ic_unknown);
    }

    @Nullable
    @CardBrand
    public static String asCardBrand(@Nullable String possibleCardType) {
        if (TextUtils.isEmpty(possibleCardType)) return null;
        String type = possibleCardType.trim().toLowerCase();
        if (AMERICAN_EXPRESS.equals(type)) return AMERICAN_EXPRESS;
        if (MASTERCARD.equals(type)) return MASTERCARD;
        if (DINERS_CLUB.equals(type)) return DINERS_CLUB;
        if (DISCOVER.equals(type)) return DISCOVER;
        if (JCB.equals(type)) return JCB;
        if (VISA.equals(type)) return VISA;
        return UNKNOWN;
    }

    @Nullable
    @FundingType
    public static String asFundingType(@Nullable String possibleFundingType) {
        if (TextUtils.isEmpty(possibleFundingType)) return null;
        String type = possibleFundingType.trim().toLowerCase();
        if (FUNDING_CREDIT.equals(type)) return FUNDING_CREDIT;
        if (FUNDING_DEBIT.equals(type)) return FUNDING_DEBIT;
        if (FUNDING_PREPAID.equals(type)) return FUNDING_PREPAID;
        return FUNDING_UNKNOWN;
    }

    @Nullable
    public static CardModel fromString(String jsonString) {
        try {
            return fromJson(new JSONObject(jsonString));
        } catch (JSONException ignored) {
            return null;
        }
    }

    @Nullable
    public static CardModel fromJson(JSONObject jsonObject) {
        if (jsonObject == null || !VALUE_CARD.equals(jsonObject.optString(FIELD_OBJECT))) return null;
        Integer expMonth = optInteger(jsonObject, FIELD_EXP_MONTH);
        if (expMonth != null && (expMonth < 1 || expMonth > 12)) expMonth = null;
        Integer expYear = optInteger(jsonObject, FIELD_EXP_YEAR);
        if (expYear != null && expYear < 0) expYear = null;

        return new Builder(null, expMonth, expYear, null)
                .addressCity(optString(jsonObject, FIELD_ADDRESS_CITY))
                .addressLine1(optString(jsonObject, FIELD_ADDRESS_LINE1))
                .addressLine2(optString(jsonObject, FIELD_ADDRESS_LINE2))
                .addressCountry(optString(jsonObject, FIELD_ADDRESS_COUNTRY))
                .addressState(optString(jsonObject, FIELD_ADDRESS_STATE))
                .addressZip(optString(jsonObject, FIELD_ADDRESS_ZIP))
                .brand(asCardBrand(optString(jsonObject, FIELD_BRAND)))
                .country(optCountryCode(jsonObject, FIELD_COUNTRY))
                .customer(optString(jsonObject, FIELD_CUSTOMER))
                .currency(optCurrency(jsonObject, FIELD_CURRENCY))
                .cvcCheck(optString(jsonObject, FIELD_CVC_CHECK))
                .funding(asFundingType(optString(jsonObject, FIELD_FUNDING)))
                .fingerprint(optString(jsonObject, FIELD_FINGERPRINT))
                .id(optString(jsonObject, FIELD_ID))
                .last4(optString(jsonObject, FIELD_LAST4))
                .name(optString(jsonObject, FIELD_NAME))
                .bin(optString(jsonObject, "bin"))
                .status(optString(jsonObject, "status"))
                .token(optString(jsonObject, "token"))
                .transactionReference(optString(jsonObject, "transaction_reference"))
                .message(optString(jsonObject, "message"))
                .build();
    }

    public boolean validateCard() {
        return validateCard(Calendar.getInstance());
    }

    public boolean validateNumber() {
        return GlobalHelper.isValidCardNumber(number);
    }

    public boolean validateCVC() {
        if (GlobalHelper.isBlank(cvc)) return false;
        String cvcValue = cvc.trim();
        String cardType = getType();
        boolean validLength = (cardType == null && cvcValue.length() >= 3 && cvcValue.length() <= 4)
                || (AMERICAN_EXPRESS.equals(cardType) && cvcValue.length() == 4)
                || cvcValue.length() == 3;
        return ModelUtils.isWholePositiveNumber(cvcValue) && validLength;
    }

    public boolean validateExpiryDate() {
        return validateExpiryDate(Calendar.getInstance());
    }

    public boolean validateExpMonth() {
        return expiryMonth != null && expiryMonth >= 1 && expiryMonth <= 12;
    }

    boolean validateExpYear(Calendar now) {
        return expiryYear != null && !ModelUtils.hasYearPassed(expiryYear, now);
    }

    boolean validateCard(Calendar now) {
        return validateNumber() && validateExpiryDate(now) && (cvc == null || validateCVC());
    }

    boolean validateExpiryDate(Calendar now) {
        return validateExpMonth() && validateExpYear(now) && !ModelUtils.hasMonthPassed(expiryYear, expiryMonth, now);
    }

    @Nullable
    public String getNumber() { return number; }
    public void setNumber(String number) {
        this.number = number;
        this.type = null;
        this.last4 = null;
        getType();
        getLast4();
    }

    @Nullable
    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    @Nullable
    @IntRange(from = 1, to = 12)
    public Integer getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(@Nullable @IntRange(from = 1, to = 12) Integer expiryMonth) { this.expiryMonth = expiryMonth; }

    @Nullable
    public Integer getExpiryYear() { return expiryYear; }
    public void setExpiryYear(Integer expiryYear) { this.expiryYear = expiryYear; }

    @Nullable
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    @Nullable
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    @Nullable
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    @Nullable
    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }

    @Nullable
    public String getAddressState() { return addressState; }
    public void setAddressState(String addressState) { this.addressState = addressState; }

    @Nullable
    public String getAddressZip() { return addressZip; }
    public void setAddressZip(String addressZip) { this.addressZip = addressZip; }

    @Nullable
    public String getAddressCountry() { return addressCountry; }
    public void setAddressCountry(String addressCountry) { this.addressCountry = addressCountry; }

    @Nullable
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Nullable
    public String getLast4() {
        if (!GlobalHelper.isBlank(last4)) return last4;
        if (number != null && number.length() >= 4) {
            last4 = number.substring(number.length() - 4);
            return last4;
        }
        return null;
    }

    @Nullable
    @CardBrand
    public String getType() {
        if (GlobalHelper.isBlank(type) && !GlobalHelper.isBlank(number)) {
            type = CardUtils.getPossibleCardType(number);
        }
        return type;
    }

    @Nullable
    public String getFingerprint() { return fingerprint; }
    @Nullable
    @FundingType
    public String getFunding() { return funding; }
    @Nullable
    public String getCountry() { return country; }
    @Nullable
    @Override
    public String getId() { return id; }
    @Nullable
    public String getAddressLine1Check() { return addressLine1Check; }
    @Nullable
    public String getAddressZipCheck() { return addressZipCheck; }
    @Nullable
    public String getCustomerId() { return customerId; }
    @Nullable
    public String getCvcCheck() { return cvcCheck; }
    @Nullable
    public String getBin() { return bin; }
    public void setBin(String bin) { this.bin = bin; }
    @Nullable
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    @Nullable
    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
    @Nullable
    public String getCard_auth() { return card_auth; }
    public void setCard_auth(String card_auth) { this.card_auth = card_auth; }
    @Nullable
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    @Nullable
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    @Nullable
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    @Nullable
    public String getTermination() { return termination; }
    public void setTermination(String termination) { this.termination = termination; }
    @Nullable
    public String getFiscal_number() { return fiscal_number; }
    public void setFiscal_number(String fiscal_number) { this.fiscal_number = fiscal_number; }

    @NonNull
    public List<String> getLoggingTokens() { return loggingTokens; }

    @NonNull
    public Card addLoggingToken(@NonNull String loggingToken) {
        loggingTokens.add(loggingToken);
        return this;
    }

    @NonNull
    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        putStringIfNotNull(object, FIELD_NAME, holderName);
        putStringIfNotNull(object, FIELD_ADDRESS_CITY, addressCity);
        putStringIfNotNull(object, FIELD_ADDRESS_COUNTRY, addressCountry);
        putStringIfNotNull(object, FIELD_ADDRESS_LINE1, addressLine1);
        putStringIfNotNull(object, FIELD_ADDRESS_LINE2, addressLine2);
        putStringIfNotNull(object, FIELD_ADDRESS_STATE, addressState);
        putStringIfNotNull(object, FIELD_ADDRESS_ZIP, addressZip);
        putStringIfNotNull(object, FIELD_BRAND, type);
        putStringIfNotNull(object, FIELD_CURRENCY, currency);
        putStringIfNotNull(object, FIELD_COUNTRY, country);
        putStringIfNotNull(object, FIELD_CUSTOMER, customerId);
        putIntegerIfNotNull(object, FIELD_EXP_MONTH, expiryMonth);
        putIntegerIfNotNull(object, FIELD_EXP_YEAR, expiryYear);
        putStringIfNotNull(object, FIELD_FINGERPRINT, fingerprint);
        putStringIfNotNull(object, FIELD_FUNDING, funding);
        putStringIfNotNull(object, FIELD_CVC_CHECK, cvcCheck);
        putStringIfNotNull(object, FIELD_LAST4, last4);
        putStringIfNotNull(object, FIELD_ID, id);
        putStringIfNotNull(object, FIELD_OBJECT, VALUE_CARD);
        putStringIfNotNull(object, "bin", bin);
        putStringIfNotNull(object, "status", status);
        putStringIfNotNull(object, "token", token);
        putStringIfNotNull(object, "transaction_reference", transactionReference);
        putStringIfNotNull(object, "message", message);
        return object;
    }

    @NonNull
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_NAME, holderName);
        map.put(FIELD_ADDRESS_CITY, addressCity);
        map.put(FIELD_ADDRESS_COUNTRY, addressCountry);
        map.put(FIELD_ADDRESS_LINE1, addressLine1);
        map.put(FIELD_ADDRESS_LINE2, addressLine2);
        map.put(FIELD_ADDRESS_STATE, addressState);
        map.put(FIELD_ADDRESS_ZIP, addressZip);
        map.put(FIELD_BRAND, type);
        map.put(FIELD_CURRENCY, currency);
        map.put(FIELD_COUNTRY, country);
        map.put(FIELD_CUSTOMER, customerId);
        map.put(FIELD_EXP_MONTH, expiryMonth);
        map.put(FIELD_EXP_YEAR, expiryYear);
        map.put(FIELD_FINGERPRINT, fingerprint);
        map.put(FIELD_FUNDING, funding);
        map.put(FIELD_CVC_CHECK, cvcCheck);
        map.put(FIELD_LAST4, last4);
        map.put(FIELD_ID, id);
        map.put(FIELD_OBJECT, VALUE_CARD);
        map.put("bin", bin);
        map.put("status", status);
        map.put("token", token);
        map.put("transaction_reference", transactionReference);
        map.put("message", message);
        removeNullAndEmptyParams(map);
        return map;
    }

    public static void removeNullAndEmptyParams(@NonNull Map<String, Object> map) {
        for (String key : new HashSet<>(map.keySet())) {
            Object value = map.get(key);
            if (value == null || (value instanceof CharSequence && TextUtils.isEmpty((CharSequence) value))) {
                map.remove(key);
            } else if (value instanceof Map) {
                removeNullAndEmptyParams((Map<String, Object>) value);
            }
        }
    }

    private String normalizeCardNumber(String number) {
        return number == null ? null : number.trim().replaceAll("\\s+|-", "");
    }
}