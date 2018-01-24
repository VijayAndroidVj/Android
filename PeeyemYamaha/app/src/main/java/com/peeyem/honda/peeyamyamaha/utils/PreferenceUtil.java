package com.peeyem.honda.peeyamyamaha.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.peeyem.honda.peeyamyamaha.R;

/**
 * This class used to store and get the preference value
 */

public class PreferenceUtil {
    SharedPreferences sharedpreferences;

    public PreferenceUtil(Context context) {
        sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getUserPassword() {
        return getString(Keys.PASSWORD, "");
    }

    public String getUserMailId() {
        return getString(Keys.EmailID, "");
    }

    public String getUserPhone() {
        return getString(Keys.PHONE, "");
    }

    public String getUserName() {
        return getString(Keys.USERNAME, "");
    }

    public String getString(String key, String defaultValue) {
        return sharedpreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedpreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedpreferences.getBoolean(key, defaultValue);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Keys.IS_ALREADY_REGISTERED, false);
        editor.putString(Keys.EmailID, "");
        editor.putString(Keys.USERNAME, "");
        editor.putString(Keys.PHONE, "");
        editor.putString(Keys.PASSWORD, "");
        editor.apply();
    }

}
