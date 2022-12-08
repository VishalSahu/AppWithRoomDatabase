package com.anr.appwithroomdatabase.utilites;

import android.util.Patterns;

import java.util.regex.Pattern;

public class MyValidator {
    public enum InputField {
        NAME, EMAIL, MOBILE_NO, PASSWORD
    }

    public static final Pattern onlyAlphabets = Pattern.compile("[a-zA-Z]+");

    public static boolean checkFieldData(InputField inputField, String inputData) {
        switch (inputField) {
            case NAME:
                return onlyAlphabets.matcher(inputData).matches();
            case EMAIL:
                return Patterns.EMAIL_ADDRESS.matcher(inputData).matches();
            case MOBILE_NO:
                return inputData.length() == 10;
            case PASSWORD:
                return inputData.length() >= 8;
        }
        return false;
    }
}
