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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuveisdk.fragments.AlertDialogFragment;

public class HomeActivity extends AppCompatActivity {
    CardModel cardModel;
    private CardView cardDebitView;
    private ImageView imageCard;
    private TextView titleCard;
    private Button buttonOrder;
    private TextView descriptionCard;
    private Context context;
    String CARD_TOKEN = "";
    int SELECT_CARD_REQUEST = 1004;
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
//        imageCard.setImageResource(com.nuvei.nuvei_sdk.R.drawable.ic_unknown);
//            titleCard.setText("Agregar Tarjeta");
//            descriptionCard.setText("Debe agregar una tarjeta para continuar");
//            buttonOrder.setBackgroundColor(getResources().getColor(R.color.gray, getTheme()));
//            buttonOrder.setClickable(false);


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
                intent.putExtra("TOKEN", CARD_TOKEN);
                selectCardActivity.launch(intent);
//                registerForActivityResult(intent, SELECT_CARD_REQUEST);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Resume", "entra en el resume");

        if(CARD_TOKEN.isEmpty()){
            imageCard.setImageResource(com.nuvei.nuvei_sdk.R.drawable.ic_unknown);
            titleCard.setText("Agregar Tarjeta");
            descriptionCard.setText("Debe agregar una tarjeta para continuar");
            buttonOrder.setBackgroundColor(getResources().getColor(R.color.gray, getTheme()));
            buttonOrder.setClickable(false);
        }
    }



        private final ActivityResultLauncher<Intent> selectCardActivity = registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result->{
              Log.v("Resultado code", String.valueOf(result.getResultCode()));
              Log.i("Resultado code", String.valueOf(result.getData() != null));
              if(result.getResultCode() == RESULT_OK && result.getData() != null ){
                  Intent data = result.getData();
                  CARD_TOKEN = data.getStringExtra("CARD_TOKEN");
                String CARD_TYPE = data.getStringExtra("CARD_TYPE");
                String CARD_LAST4 = data.getStringExtra("CARD_LAST4");
                String CARD_HOLDER = data.getStringExtra("CARD_HOLDER");
                if (CARD_LAST4 != null  && !CARD_LAST4.equals("")){
                    imageCard.setImageResource(CardModel.getDrawableBrand(CARD_TYPE));
                    titleCard.setText("XXXX"+CARD_LAST4);
                    descriptionCard.setText(CARD_HOLDER);
                    buttonOrder.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                    buttonOrder.setClickable(true);

                }else {
                    AlertDialogFragment.ShowErrorDialog(this, "Atención",
                            "No se pudo obtener la información de la tarjeta", null);
                }
              }
              else if (result.getResultCode() == RESULT_CANCELED) {
                  Log.v("resultado cancelado", "dcaaaad");
                  imageCard.setImageResource(com.nuvei.nuvei_sdk.R.drawable.ic_unknown);
                  titleCard.setText("Agregar Tarjeta");
                  descriptionCard.setText("Debe agregar una tarjeta para continuar");
                  buttonOrder.setBackgroundColor(getResources().getColor(R.color.gray, getTheme()));
                  buttonOrder.setClickable(false);
              }
          }
        );
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == SELECT_CARD_REQUEST){
//            if(resultCode == RESULT_OK){
//                CARD_TOKEN = data.getStringExtra("CARD_TOKEN");
//                String CARD_TYPE = data.getStringExtra("CARD_TYPE");
//                String CARD_LAST4 = data.getStringExtra("CARD_LAST4");
//                String CARD_HOLDER = data.getStringExtra("CARD_HOLDER");
//                if (CARD_LAST4 != null  && !CARD_LAST4.equals("")){
//                    imageCard.setImageResource(CardModel.getDrawableBrand(CARD_TYPE));
//                    titleCard.setText("XXXX"+CARD_LAST4);
//                    descriptionCard.setText(CARD_HOLDER);
//
//                }
//            }
//        }
//    }
}
