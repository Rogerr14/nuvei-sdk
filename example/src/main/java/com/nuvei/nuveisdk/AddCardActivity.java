package com.nuvei.nuveisdk;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.Nuvei;
import com.nuvei.nuvei_sdk.add_card.services.iAddCardCallback;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;
import com.nuvei.nuvei_sdk.views.CardInformationWidget;
import com.nuvei.nuveisdk.constants.Constants;
import com.nuvei.nuveisdk.fragments.AlertDialogFragment;
import com.nuvei.nuveisdk.fragments.AlertLoadingFragment;

public class AddCardActivity extends AppCompatActivity {
    Button buttonSave;
    CardInformationWidget cardInformationWidget;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);
        context = this;
        final String uid = "4";
        final String email = "dev@paymentez.com";
        cardInformationWidget = (CardInformationWidget) findViewById(R.id.card_info_widget);
        buttonSave = (Button) findViewById(R.id.button_save_card);
        buttonSave.setOnClickListener((V)->{
            buttonSave.setEnabled(false);
            CardModel cardModel = cardInformationWidget.getCardInfo();
            if(cardModel == null){
                buttonSave.setEnabled(true);
                   AlertDialogFragment.ShowErrorDialog(context, "Error to get card info", "Invalid Card Data",null);
            }else{
                final AlertLoadingFragment loading = new AlertLoadingFragment(context);
                loading.showLoading();
                Nuvei.addCard(context, uid, email, cardModel, new iAddCardCallback() {
                    @Override
                    public void onError(ErrorResponse error) {
                        loading.dismissLoading();
                        buttonSave.setEnabled(true);
                        AlertDialogFragment.ShowErrorDialog(context, "Error: "+ error.getType(),error.getDescription(), null);
                    }

                    @Override
                    public void onSuccess(CardModel card) {
                        buttonSave.setEnabled(true);
                        loading.dismissLoading();
//                        if(card != null){
//                            if(card.getStatus().equals("valid")){
//                                AlertDialogFragment.ShowErrorDialog(context, "Card Successfully Added", "status: " + card.getStatus() + "\n" +
//                                        "Card Token: " + card.getToken() + "\n" +
//                                        "transaction_reference: " + card.getTransactionReference(),null);
//
//                            }else if(card.getStatus().equals("review")){
//                                AlertDialogFragment.ShowErrorDialog(
//                                        context,
//                                        "Card Under Review",
//                                        "status: " + card.getStatus() + "\n" +
//                                                "Card Token: " + card.getToken() + "\n" +
//                                                "transaction_reference: " + card.getTransactionReference(), null);
//
//                            }else{
//                                AlertDialogFragment.ShowErrorDialog(
//                                        context,
//                                        "Error",
//                                        "status: " + card.getStatus() + "\n" +
//                                                "message: " + card.getMessage(), null);
//                            }
//                        }
                    }
                });

            }
        });

    }
}
