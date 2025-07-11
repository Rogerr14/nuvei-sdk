package com.nuvei.nuveisdk.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nuvei.nuvei_sdk.models.CardModel;

import java.util.List;

public class ListCardModel {
    @SerializedName("cards")
    @Expose
    private List<CardModel> cards = null;

    @SerializedName("result_size")
    @Expose
    private Integer resultSize;

    public List<CardModel> getCards() {
        return cards;
    }

    public void setCards(List<CardModel> cards) {
        this.cards = cards;
    }

    public Integer getResultSize() {
        return resultSize;
    }

    public void setResultSize(Integer resultSize) {
        this.resultSize = resultSize;
    }
}
