package com.nuvei.nuvei_sdk.list_card.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nuvei.nuvei_sdk.models.CardModel;

import java.util.List;

public class CardListResponseModel {
    @SerializedName("cards")
    @Expose
    private final List<CardModel> cards = null;
    @SerializedName("result_size")
     @Expose
    private Integer resultSize;

    public List<CardModel> getCards(){return  cards;};

    public Integer getResultSize(){return resultSize;};

}
