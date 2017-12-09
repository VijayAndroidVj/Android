package com.xr.vijay.tranzpost;

import android.content.Context;
import android.content.SharedPreferences;

import static com.xr.vijay.tranzpost.Keys.ATTACHED_TRUCKS;
import static com.xr.vijay.tranzpost.Keys.AlterNateNumber;
import static com.xr.vijay.tranzpost.Keys.CITY;
import static com.xr.vijay.tranzpost.Keys.EmailID;
import static com.xr.vijay.tranzpost.Keys.IS_ALREADY_REGISTERED;
import static com.xr.vijay.tranzpost.Keys.OWNED_TRUCKS;
import static com.xr.vijay.tranzpost.Keys.PASSWORD;
import static com.xr.vijay.tranzpost.Keys.USERNAME;
import static com.xr.vijay.tranzpost.Keys.mobile;

/**
 * Created by razin on 27/11/17.
 */

public class PreferenceUtil {
    public SharedPreferences sharedPreferences;
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


    public void setUserCity(String city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITY, city);
        editor.apply();

    }

    public String getUserCity() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(CITY, "");
    }

    public void setUserOwnedTruckes(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OWNED_TRUCKS, count);
        editor.apply();

    }

    public int getUserOwnedTruckes() {
        return sharedPreferences == null ? 0 : sharedPreferences.getInt(OWNED_TRUCKS, 0);
    }

    public void setUserRegisteredNumber(String mobile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Keys.mobile, mobile);
        editor.apply();

    }

    public String getUserRegisteredNumber() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(Keys.mobile, "");
    }


    public void setUserAlternateNumber(String count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AlterNateNumber, count);
        editor.apply();

    }

    public String getUserAlternateNumber() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(AlterNateNumber, "");
    }


    public void setUserAttachedTruckes(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ATTACHED_TRUCKS, count);
        editor.apply();

    }

    public int getUserAttachedTruckes() {
        return sharedPreferences == null ? 0 : sharedPreferences.getInt(ATTACHED_TRUCKS, 0);
    }

    public void setUserPassword(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, userEmail);
        editor.apply();

    }

    public String getUserPassword() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(PASSWORD, "");
    }

    public void setPan(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PANIMAGE", pan);
        editor.apply();

    }

    public String getPan() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("PANIMAGE", "");
    }


    public void setProof(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ProofIMAGE", pan);
        editor.apply();

    }

    public String getProof() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("ProofIMAGE", "");
    }

    public void setDriveLicense(String license) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("licenseIMAGE", license);
        editor.apply();

    }

    public String getDriveLicense() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("licenseIMAGE", "");
    }


    public void setRCFront(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VISITIMAGE", pan);
        editor.apply();
    }

    public String getRcFront() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("VISITIMAGE", "");
    }

    public void setRCBack(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RC", pan);
        editor.apply();
    }

    public String getRCBack() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("RC", "");
    }

    public void setProfile(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ProfileIMAGE", pan);
        editor.apply();
    }

    public String getProfile() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("ProfileIMAGE", "");
    }


    public void setVoter(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VoterIMAGE", pan);
        editor.apply();
    }

    public String getVoter() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("VoterIMAGE", "");
    }


    public void setPermit(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PermitIMAGE", pan);
        editor.apply();
    }

    public String getPermit() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("PermitIMAGE", "");
    }

    public void setInsurance(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("InsuranceIMAGE", pan);
        editor.apply();
    }

    public String getInsurance() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("InsuranceIMAGE", "");
    }


    public void setfirm(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firm", pan);
        editor.apply();
    }

    public String getfirm() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("firm", "");
    }

    public void setLoginType(String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logintype", type);
        editor.apply();
    }

    public String getLogintype() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("logintype", "");
    }

    public void setType(String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type", type);
        editor.apply();
    }

    public String gettype() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("type", "");
    }

    public void setProofType(String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ProofType", type);
        editor.apply();
    }

    public String getProofType() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("ProofType", "");
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
        editor.putString(USERNAME, userName);
        editor.apply();

    }


    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EmailID, userEmail);
        editor.apply();

    }

    public String getUserEmail() {
        return sharedPreferences == null ? "" : sharedPreferences.getString(EmailID, "");
    }

    public void setTruckPhoto1(String TruckPhoto1) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckPhoto1", TruckPhoto1);
        editor.apply();

    }

    public String getTruckPhoto1() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckPhoto1", "");
    }


    public void setTruckPhoto2(String TruckPhoto1) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckPhoto2", TruckPhoto1);
        editor.apply();

    }

    public String getTruckPhoto2() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckPhoto2", "");
    }


    public void setTruckPhoto3(String TruckPhoto) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckPhoto3", TruckPhoto);
        editor.apply();

    }

    public String getTruckPhoto3() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckPhoto3", "");
    }


    public void setTruckPhoto4(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckPhoto4", pan);
        editor.apply();

    }

    public String getTruckPhoto4() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckPhoto4", "");
    }

    public void setTruckModel(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckModel", pan);
        editor.apply();

    }

    public String getTruckModel() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckModel", "");
    }


    public void setTruckPermitType(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckPermitType", pan);
        editor.apply();

    }

    public String getTruckPermitType() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckPermitType", "");
    }


    public void setTruckBodyType(String pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TruckBodyType", pan);
        editor.apply();

    }

    public String getTruckBodyType() {
        return sharedPreferences == null ? "" : sharedPreferences.getString("TruckBodyType", "");
    }

    public void setTruckWeight(int pan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("TruckWeight", pan);
        editor.apply();

    }

    public int getTruckWeight() {
        return sharedPreferences == null ? 0 : sharedPreferences.getInt("TruckWeight", 0);
    }


    public void logoutAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mobile, "");
        editor.putString(EmailID, "");
        editor.putString(USERNAME, "");
        editor.putString(EmailID, "");
        editor.putString("logintype", "");
        editor.putString("type", "");
        editor.putBoolean(IS_ALREADY_REGISTERED, false);
        editor.apply();

    }
}
