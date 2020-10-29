package com.nepatyaz.appbootcamp.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.nepatyaz.appbootcamp.R;

public class DialogUtility {

    private static AlertDialog dlg;

    public static void showDialog(int animationId, String message, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        final View viewDialog = factory.inflate(R.layout.dialog_layout, null);
        dlg = alertDialog.create();

        TextView txtPromptTitle = viewDialog.findViewById(R.id.text_message);
        LottieAnimationView animation_view = viewDialog.findViewById(R.id.animation_view);

        animation_view.setAnimation(animationId);
        txtPromptTitle.setText(message);

        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dlg.setView(viewDialog);
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
        dlg.getWindow().setLayout(500, 500);
    }

    public static void showCustomDialog(int id, String message, Context context) {
        LayoutInflater factory = LayoutInflater.from(context);

        final View viewDialogErr = factory.inflate(R.layout.dialog_custom, null);
        setDataInformation(context, viewDialogErr, message, id);

    }


    public static void setDataInformation(Context context, View viewDialog, String message, int id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final AlertDialog dlg = alertDialog.create();

        TextView txtPromptMessage = (TextView) viewDialog.findViewById(R.id.txtPromptMessage);
        Button btnPromptOk = viewDialog.findViewById(R.id.btnPromptOk);
        LottieAnimationView lottieAnimationView = viewDialog.findViewById(R.id.animation_view);

        lottieAnimationView.setAnimation(id);

        txtPromptMessage.setText(message);

        btnPromptOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });


        dlg.setView(viewDialog);
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }


    public static void closeAllDialog() {
        dlg.dismiss();
    }

}
