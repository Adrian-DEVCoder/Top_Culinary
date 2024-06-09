package com.example.top_culinary.model;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.top_culinary.R;

public class Dialogo {
    public static void showDialog(Context context, String title, String message) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_custom);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        Button dialogButton = dialog.findViewById(R.id.dialogButton);

        dialogTitle.setText(title);
        dialogMessage.setText(message);

        dialogButton.setOnClickListener(v -> dialog.dismiss());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();
    }
}
