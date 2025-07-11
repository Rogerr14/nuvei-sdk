package com.nuvei.nuvei_sdk.delete_card.model;

import com.google.gson.annotations.SerializedName;

public class DeleteCardRequestModel {
    @SerializedName("card")
    private Card card;

    @SerializedName("user")
    private User user;

    public DeleteCardRequestModel(String token, String userId) {
        this.card = new Card(token);
        this.user = new User(userId);
    }

    public static class Card {
        @SerializedName("token")
        private String token;

        public Card(String token) {
            this.token = token;
        }
    }

    public static class User {
        @SerializedName("id")
        private String id;

        public User(String id) {
            this.id = id;
        }
    }
}
