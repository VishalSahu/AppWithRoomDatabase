package com.anr.appwithroomdatabase.utilites;

import com.google.android.material.textfield.TextInputEditText;

public class EditTextHandler {
    public static String getDataFromTextInput(TextInputEditText textInputEditText) {
        if (textInputEditText.getText() != null) {
            return textInputEditText.getText().toString().trim();
        }
        return "";
    }
}
