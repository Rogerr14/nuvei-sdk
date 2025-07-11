package com.nuvei.nuveisdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.models.CardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of payment cards in a RecyclerView.
 * Supports displaying card details (last 4 digits, holder name, brand logo) and handling
 * selection and deletion actions. Also supports an empty state view when no cards are available.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CARD = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private final List<CardModel> cards;
    private final CardActionListener listener;

    /**
     * Interface for handling card actions (selection or deletion).
     */
    public interface CardActionListener {
        /**
         * Called when a card is selected or deleted.
         *
         * @param card The card that was interacted with.
         * @param action The type of action performed.
         */
        void onCardAction(@NonNull CardModel card, @NonNull ActionType action);

        enum ActionType {
            SELECT,
            DELETE
        }
    }

    /**
     * Constructor for the adapter.
     *
     * @param cards The list of cards to display.
     * @param listener The listener for card selection and deletion actions.
     */
    public CardAdapter(@NonNull List<CardModel> cards, @NonNull CardActionListener listener) {
        this.cards = cards != null ? new ArrayList<>(cards) : new ArrayList<>();
        this.listener = listener;
    }

    /**
     * ViewHolder for a card item.
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCardNumber;
        private final TextView textViewCardHoldersName;
        private final ImageView imageViewBrandCard;
        private final ImageButton imageViewDeleteCard;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCardNumber = itemView.findViewById(R.id.textViewCardNumber);
            textViewCardHoldersName = itemView.findViewById(R.id.textViewCardHoldersName);
            imageViewBrandCard = itemView.findViewById(R.id.imageViewBrandCard);
            imageViewDeleteCard = itemView.findViewById(R.id.imageViewDeleteCard);
        }
    }

    /**
     * ViewHolder for an empty state view.
     */


    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return cards.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_CARD;}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_empty, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_info, parent, false);
            return new CardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder) {
            bindCardViewHolder((CardViewHolder) holder, position);
        }
    }

    private void bindCardViewHolder(CardViewHolder holder, int position) {
        CardModel card = cards.get(position);
        if (card == null) {
            return; // Skip invalid data
        }

        // Set card details
        holder.textViewCardNumber.setText(String.format("XXXX.%s - status: %s",
                safeGet(card.getLast4(), "****"),
                safeGet(card.getStatus(), "Unknown")));
        holder.textViewCardHoldersName.setText(safeGet(card.getHolderName(), "Unknown"));
        holder.imageViewBrandCard.setImageResource(CardModel.getDrawableBrand(card.getType()));

        // Set click listeners
        holder.itemView.setOnClickListener(v -> listener.onCardAction(card, CardActionListener.ActionType.SELECT));
        holder.imageViewDeleteCard.setOnClickListener(v -> listener.onCardAction(card, CardActionListener.ActionType.DELETE));
    }


    @Override
    public int getItemCount() {
        return cards.isEmpty() ? 1 : cards.size(); // Show empty view if no cards
    }

    /**
     * Updates the list of cards and notifies the adapter of changes.
     *
     * @param newCards The new list of cards.
     */
    public void updateCards(@NonNull List<CardModel> newCards) {
        this.cards.clear();
        this.cards.addAll(newCards != null ? newCards : new ArrayList<>());
        notifyDataSetChanged(); // Can be optimized with DiffUtil later
    }

    /**
     * Safely retrieves a string, returning a default value if null.
     */
    private String safeGet(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Gets the drawable resource for the card brand, with a fallback.
     */

}