package com.nuvei.nuveisdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.Nuvei;
import com.nuvei.nuvei_sdk.helpers.NuveiUtils;
import com.nuvei.nuvei_sdk.list_card.services.IListCardCallback;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;

import java.util.LinkedList;
import java.util.List;

public class CardActivity extends AppCompatActivity {
    List<CardModel> cardsList = new LinkedList<>();
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        context = this;
        getAllCards();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void getAllCards(){

        try {
            Log.v("Request", "entra aqui");
        Nuvei.getAllCards(context, "sda", new IListCardCallback(){
            @Override
            public void onError(ErrorResponse errorResponse) {
                Log.v("Error peticicon", errorResponse.getDescription());
                NuveiUtils.show(context, "Error", errorResponse.getDescription());
            }

            @Override
            public void onSuccess(List<CardModel> cardListResponseModel) {
                Log.v("Ok", "entra aqui");
            }
        });
        }catch (Exception e){
            Log.v("Error", e.toString());
        }
    }
}
