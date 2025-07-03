package com.nuvei.nuveisdk.fragments;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nuveisdk.R;
import com.google.android.material.button.MaterialButton;

public class AlertDialogFragment {
    public interface DialogActionListener{
        void  onOkClicked();
    }

    public  static void ShowErrorDialog(@NonNull Context context, @NonNull String title, @NonNull String message, @Nullable DialogActionListener listener){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_error_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
        dialogTitle.setText(title);
        dialogMessage.setText(message);


        MaterialButton buttonOk = dialog.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(view -> {
            if(listener != null){
                listener.onOkClicked();
            }
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

}
