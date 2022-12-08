package com.anr.appwithroomdatabase.utilites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialog {
    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true);

        builder.setPositiveButton("Okay", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}
