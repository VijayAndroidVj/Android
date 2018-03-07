package com.example.chandru.laundry;

/**
 * Created by chandru on 2/15/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

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


    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }



    public void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
      editor.putBoolean("Loggedin", false);
       /*   editor.putString(Keys.EmailID, "");
        editor.putString(Keys.USERID, "");
        editor.putString(Keys.NAME, "");
        editor.putString(Keys.USERNAME, "");
        editor.putString(Keys.PASSWORD, "");
        editor.putString(Keys.STATE, "");
        editor.putString(Keys.ABOUTME, "");
        editor.putString(Keys.COUNTRY, "");
        editor.putString(Keys.PROFILE_IMAGE, "");
        editor.putString(Keys.COVER_IMAGE, "");
        editor.putBoolean(Keys.PRIVACY_SETTINGS, false);
       */
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedpreferences.getBoolean(key, defaultValue);
    }
    public boolean isLoggined() {
        return getBoolean("Loggedin", false);
    }
}

