package com.nuvei.nuveisdk;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.models.CardModel;

public class HomeActivity extends AppCompatActivity {
    CardModel cardModel;
    private CardView cardDebitView;
    private ImageView imageCard;
    private TextView titleCard;
    private Button buttonOrder;
    private TextView descriptionCard;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cardDebitView = (CardView) findViewById(R.id.card_debit_view);
        imageCard = (ImageView) findViewById(R.id.imageView);
        titleCard = (TextView) findViewById(R.id.tittle_card);
        descriptionCard = (TextView) findViewById(R.id.description_card);
        buttonOrder = (Button) findViewById(R.id.button_order);
        context = this;



        Log.v("create", "entra en el create");
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }

        };
        getOnBackPressedDispatcher().addCallback(this, callback);






        cardDebitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Resume", "entra en el resume");

        if(cardModel == null){
            imageCard.setImageResource(com.nuvei.nuvei_sdk.R.drawable.ic_unknown);
            titleCard.setText("Agregar Tarjeta");
            descriptionCard.setText("Debe agregar una tarjeta para continuar");
            buttonOrder.setBackgroundColor(getResources().getColor(R.color.gray, getTheme()));
            buttonOrder.setClickable(false);
        }
    }
}
