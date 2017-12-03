package com.xr.vijay.tranzpost;

import android.content.Context;
import android.content.SharedPreferences;

import static com.xr.vijay.tranzpost.Keys.EmailID;
import static com.xr.vijay.tranzpost.Keys.IS_ALREADY_REGISTERED;
import static com.xr.vijay.tranzpost.Keys.USERNAME;
import static com.xr.vijay.tranzpost.Keys.mobile;

/**
 * Created by razin on 27/11/17.
 */

public class PreferenceUtil {
    public SharedPreferences sharedPreferences;
    public String USER_NAME = "user_name";
    public String USER_EMAIL = "user_email";
    Context context;


    public PreferenceUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyMedicineSP", Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public String getUserMailId() {
        return getString(EmailID, "");
    }

    public String getUserName() {
        return getString(USERNAME, "");
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }


    public void putExtra(String key, int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, count);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPreferences == null ? 0 : sharedPreferences.getInt(key, 0);
    }


    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();

    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, userEmail);
        editor.apply();

    }

    public String getUserEmail() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(USER_EMAIL, "");
    }

    public void logoutAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mobile, "");
        editor.putString(EmailID, "");
        editor.putString(USERNAME, "");
        editor.putString(USER_EMAIL, "");
        editor.putString(USER_NAME, "");
        editor.putBoolean(IS_ALREADY_REGISTERED, false);
        editor.apply();

    }
}
