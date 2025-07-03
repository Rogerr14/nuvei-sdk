package com.nuvei.nuveisdk;

import static com.nuvei.nuveisdk.constants.Constants.CLIENT_APP_CODE;
import static com.nuvei.nuveisdk.constants.Constants.CLIENT_APP_KEY;
import static com.nuvei.nuveisdk.constants.Constants.SERVER_APP_CODE;
import static com.nuvei.nuveisdk.constants.Constants.SERVER_APP_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nuveisdk.R;
import com.nuvei.nuvei_sdk.Nuvei;


public class MainActivity extends AppCompatActivity {

    private Button simulationButton;
    private Context context;





        public  boolean handleBackPress(){
        return false;
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simulationButton = (Button)findViewById(R.id.simulationButton);
        context = this;

        Nuvei.configEnvironment(true, CLIENT_APP_CODE,CLIENT_APP_KEY,SERVER_APP_CODE,SERVER_APP_KEY);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }

        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        simulationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });
    }



}