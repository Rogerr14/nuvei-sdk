package com.nuvei.nuveisdk.fragments;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.example.nuveisdk.R;

public class AlertLoadingFragment {
    private final Dialog dialog;

    // Constructor que recibe el contexto
    public AlertLoadingFragment(@NonNull Context context) {
        dialog = new Dialog(context);
        // Configurar el diálogo una sola vez
        dialog.setContentView(R.layout.alert_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
    }

    // Mostrar el diálogo
    public void showLoading() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    // Ocultar el diálogo
    public void dismissLoading() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // Verificar si el diálogo está visible
    public boolean isShowing() {
        return dialog.isShowing();
    }
}