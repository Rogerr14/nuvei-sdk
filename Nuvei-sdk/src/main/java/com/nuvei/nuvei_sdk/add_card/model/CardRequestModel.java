package com.nuvei.nuvei_sdk.add_card.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.UserModel;

public class CardRequestModel {
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("user")
    @Expose
    private UserModel user;
    @SerializedName("card")
    @Expose
    private CardModel card;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }
}
