package com.nuvei.nuvei_sdk.builders;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.nuvei.nuvei_sdk.models.CardModel;

public  class CardBuilder {
    public String number;
    public Integer expMonth;
    public Integer expYear;
    public String cvc;
    public String name;
    public String addressLine1;
    public String addressLine2;
    public String addressCity;
    public String addressState;
    public String addressZip;
    public String addressCountry;
    public String brand;
    public String funding;
    public String last4;
    public String fingerprint;
    public String country;
    public String currency;
    public String customer;
    public String cvcCheck;
    public String id;
    public String bin;
    public String status;
    public String token;
    public String transactionReference;
    public String message;

    public  CardBuilder(@Nullable String number, @IntRange(from = 1, to = 12) Integer expMonth,
                   @IntRange(from = 0) Integer expYear, @Nullable String cvc) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = cvc;
    }

    public CardBuilder name(String name) { this.name = name; return this; }
    public CardBuilder addressLine1(String address) { this.addressLine1 = address; return this; }
    public CardBuilder addressLine2(String address) { this.addressLine2 = address; return this; }
    public CardBuilder addressCity(String city) { this.addressCity = city; return this; }
    public CardBuilder addressState(String state) { this.addressState = state; return this; }
    public CardBuilder addressZip(String zip) { this.addressZip = zip; return this; }
    public CardBuilder addressCountry(String country) { this.addressCountry = country; return this; }
    public CardBuilder brand(@CardModel.CardBrand String brand) { this.brand = brand; return this; }
    public CardBuilder funding(@CardModel.FundingType String funding) { this.funding = funding; return this; }
    public CardBuilder last4(String last4) { this.last4 = last4; return this; }
    public CardBuilder fingerprint(String fingerprint) { this.fingerprint = fingerprint; return this; }
    public CardBuilder country(String country) { this.country = country; return this; }
    public CardBuilder currency(String currency) { this.currency = currency; return this; }
    public CardBuilder customer(String customer) { this.customer = customer; return this; }
    public CardBuilder cvcCheck(String cvcCheck) { this.cvcCheck = cvcCheck; return this; }
    public CardBuilder id(String id) { this.id = id; return this; }
    public CardBuilder bin(String bin) { this.bin = bin; return this; }
    public CardBuilder status(String status) { this.status = status; return this; }
    public CardBuilder token(String token) { this.token = token; return this; }
    public CardBuilder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
    public CardBuilder message(String message) { this.message = message; return this; }

    public CardModel build() {
        return new CardModel(this);
    }
}