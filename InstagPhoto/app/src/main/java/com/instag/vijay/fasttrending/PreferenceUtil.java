package com.instag.vijay.fasttrending;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class used to store and get the preference value
 */

public class PreferenceUtil {
    SharedPreferences sharedpreferences;

    public PreferenceUtil(Context context) {
        sharedpreferences = context.getSharedPreferences("InstagPhoto", Context.MODE_PRIVATE);
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

    public String getUserAboutMe() {
        return getString(Keys.ABOUTME, "");
    }

    public String getUserGender() {
        return getString(Keys.GENDER, "");
    }

    public String getUserState() {
        return getString(Keys.STATE, "");
    }

    public String getUserCountry() {
        return getString(Keys.COUNTRY, "");
    }

    public String getUserContactNumber() {
        return getString(Keys.CONTACT_NUMBER, "");
    }


    public String getUserPassword() {
        return getString(Keys.PASSWORD, "");
    }

    public String getUserMailId() {
        return getString(Keys.EmailID, "");
    }

    public String getUserProfileStatus() {
        return getString(Keys.PROFILE_STATUS, "");
    }

    public String getWebInfo() {
        return getString(Keys.WEN_INFO, "");
    }

    public String getUserName() {
        return getString(Keys.USERNAME, "");
    }

    public String getProfileName() {
        return getString(Keys.NAME, "");
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
        editor.apply();
    }

    public String getMyCoverPhoto() {
        return getString(Keys.COVER_IMAGE, "");
    }

    public String getMyProfile() {
        return getString(Keys.PROFILE_IMAGE, "");
    }

    public boolean getMyPrivacySettings() {
        return getBoolean(Keys.PRIVACY_SETTINGS, false);
    }
}
