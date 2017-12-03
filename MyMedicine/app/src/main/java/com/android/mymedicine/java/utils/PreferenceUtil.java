package com.android.mymedicine.java.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.mymedicine.java.model.CallEvent;

/**
 * Created by razin on 27/11/17.
 */

public class PreferenceUtil {
    public SharedPreferences sharedPreferences;
    public String USER_ID = "user_id";
    public String USER_NAME = "user_name";
    public String PASSWORD = "user_password";
    public String FULL_NAME = "full_name";
    public String USER_EMAIL = "user_email";
    public String IMAGE_URL = "image_url";
    public String COUNTRY_ISO = "country_iso";
    public String PASSWORD_RESET_KEY = "password_reset_key";
    public String STATUS = "status";
    public String CREATED_DATE = "created_date";
    public String IS_LOGGEDIN = "user_number";
    public String NOTIFICATION_SETTINGS = "notification_settings";
    Context context;


    public PreferenceUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyMedicineSP", Context.MODE_PRIVATE);
    }

    public void setLogInData(CallEvent callEvent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, callEvent.getUserId());
        editor.putString(USER_NAME, callEvent.getUsername());
        editor.putString(PASSWORD, callEvent.getPassword());
        editor.putString(USER_EMAIL, callEvent.getEmail());
        editor.putString(IMAGE_URL, callEvent.getImageUrl());
        editor.putString(COUNTRY_ISO, callEvent.getCountryIso());
        editor.putString(PASSWORD_RESET_KEY, callEvent.getPasswordResetKey());
        editor.putString(CREATED_DATE, callEvent.getCreatedDate());
        editor.putInt(NOTIFICATION_SETTINGS, 5);
        editor.putBoolean(IS_LOGGEDIN, true);
        editor.commit();

    }

    public void setLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, "");
        editor.putString(USER_NAME, "");
        editor.putString(PASSWORD, "");
        editor.putString(USER_EMAIL, "");
        editor.putString(IMAGE_URL, "");
        editor.putString(COUNTRY_ISO, "");
        editor.putString(PASSWORD_RESET_KEY, "");
        editor.putString(CREATED_DATE, "");
        editor.putBoolean(IS_LOGGEDIN, false);
        editor.commit();

    }


    public void putExtra(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences == null ? false : sharedPreferences.getBoolean(key, false);
    }

    public void putExtra(String key, int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, count);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences == null ? 0 : sharedPreferences.getInt(key, 0);
    }

    public void setLoggedinStatus(boolean loginstatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGEDIN, loginstatus);
        editor.commit();

    }

    public void setNotificationSettings(int settime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_SETTINGS, settime);
        editor.commit();

    }

    public int getNotificationSettings() {
        return sharedPreferences == null ? 5 : sharedPreferences.getInt(NOTIFICATION_SETTINGS, 5);
    }

    public boolean isLoggedin() {
        return sharedPreferences == null ? false : sharedPreferences.getBoolean(IS_LOGGEDIN, false);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();

    }

    public String getUserName() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, userEmail);
        editor.commit();

    }

    public String getUserEmail() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(USER_EMAIL, "");
    }

}
