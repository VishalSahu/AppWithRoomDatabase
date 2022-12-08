package com.anr.appwithroomdatabase.fragments;

import android.content.Context;
import android.content.Intent;

public class MyBroadcastSender {

    public static void sendBroadcast(Context context, String message) {
        Intent intent = new Intent();
        intent.setAction("appWithRoomDatabase");
        intent.putExtra("clickEvents", message);
        context.sendBroadcast(intent);
    }
}
