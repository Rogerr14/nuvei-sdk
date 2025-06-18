package com.nuvei.nuvei_sdk.add_card.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nuvei.nuvei_sdk.models.CardModel;
public class CardResponseModel {
    @SerializedName("card")
    @Expose
    private CardModel cardModel;

    public CardModel getCard(){return cardModel;}
    public void setCardModel(CardModel cardModel){this.cardModel = cardModel;}


}
