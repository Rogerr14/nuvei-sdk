package com.nuvei.nuveisdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nuveisdk.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nuvei.nuvei_sdk.Nuvei;
import com.nuvei.nuvei_sdk.delete_card.service.IDeleteCardCallback;
import com.nuvei.nuvei_sdk.list_card.services.IListCardCallback;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;
import com.nuvei.nuveisdk.adapter.CardAdapter;
import com.nuvei.nuveisdk.fragments.AlertDialogFragment;
import com.nuvei.nuveisdk.fragments.AlertLoadingFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CardActivity extends AppCompatActivity {
    List<CardModel> cardsList = new LinkedList<>();

    private CardModel cardSelected  ;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManagerRecycler;
    private FloatingActionButton addCardButton;
    Context context;
    private String cardToken;
    String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        context = this;
        getAllCards();




        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (cardSelected != null) {

                  boolean exist = cardsList.stream().anyMatch(cardModel -> cardModel.getToken().equals(cardSelected.getToken()));
                  boolean isSelect = cardSelected.getToken().equals(token);
                  Log.v("eciste", String.valueOf(exist));
                  if(!exist && isSelect ){
                      setResult(RESULT_CANCELED);
                  }else{
                      setResult(RESULT_OK);
                  }
                }else{
                    setResult(RESULT_CANCELED);
                }
                finish();
            }

        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        addCardButton = (FloatingActionButton) findViewById(R.id.button_add_card);
        addCardButton.setOnClickListener((view -> {
            Intent intent = new Intent(context, AddCardActivity.class);
            startActivity(intent);
        }));
        recyclerView = (RecyclerView) findViewById(R.id.list_cards);
        recyclerView.setHasFixedSize(true);
        layoutManagerRecycler = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerRecycler);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    public void getAllCards(){

        AlertLoadingFragment loading = new AlertLoadingFragment(context);
        loading.showLoading();
        try {
            Log.v("Request", "entra aqui");
        Nuvei.getAllCards(context, "4", new IListCardCallback(){
            @Override
            public void onError(ErrorResponse errorResponse) {
                loading.dismissLoading();
                Log.v("Error peticicon", errorResponse.getDescription());
                AlertDialogFragment.ShowErrorDialog(context, "Atención", "Ocurrio un error al realizar esta petición", null);
            }

            @Override
            public void onSuccess(List<CardModel> cardListResponseModel) {
                loading.dismissLoading();
                cardsList = cardListResponseModel;
                Intent intent = getIntent();
                token =  intent.getStringExtra("TOKEN");

                if( token != null && !cardsList.isEmpty()){
                    cardSelected = cardsList.stream()
                            .filter(card -> card.getToken() != null && card.getToken().equals(token))
                            .findFirst()
                            .orElse(null);


                }
                mAdapter = new CardAdapter(cardsList, new CardAdapter.CardActionListener() {


                    @Override
                    public void onCardAction(@NonNull CardModel card, @NonNull ActionType action) {
                        if(action == ActionType.DELETE){
                            Nuvei.deleteCard(context, "4", card.getToken(),new IDeleteCardCallback(){

                                @Override
                                public void onError(ErrorResponse errorResponse) {
                                    loading.dismissLoading();
                                    Log.v("Ocurrió un error", errorResponse.getDescription());
                                    AlertDialogFragment.ShowErrorDialog(context, "Atención", "Ocurrio un error al realizar esta petición", null);
                                }

                                @Override
                                public void onSuccess(String message) {
                                    getAllCards();

                                    AlertDialogFragment.ShowErrorDialog(context, "Atención", "Se eliminó la tarjeta correctamente", null);
                                }
                            });
                        }

                        if(action == ActionType.SELECT){
                            cardSelected = card;
                            cardToken = card.getToken();
                            Intent intentReturn = new Intent();
                            intentReturn.putExtra("CARD_TOKEN", card.getToken());
                            intentReturn.putExtra("CARD_TYPE", card.getType());
                            intentReturn.putExtra("CARD_LAST4", card.getLast4());
                            intentReturn.putExtra("CARD_HOLDER", card.getHolderName());
                            setResult(Activity.RESULT_OK, intentReturn);
                            finish();

                        }
                    }
                });
                recyclerView.setAdapter(mAdapter);
            }
        });
        }catch (Exception e){
            Log.v("Error", e.toString());
        }
    }
}
