package com.anr.appwithroomdatabase.utilites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class ImageParser {
    public static Bitmap decodedImage(Context context, String current_email) {
        String imgBase64 = DatabaseHelper.getDB(context).entityDao().getProfilePicBase64(current_email);
        byte[] bytes = Base64.decode(imgBase64, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
    }
}