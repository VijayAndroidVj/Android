package com.instag.vijay.fasttrending;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 2/12/17.
 */

public class PhoneNumberAuthentication extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";
    private AppCompatEditText mPhoneNumberField;
    //    private ProgressBar progressbar;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ccp = findViewById(R.id.countrycode);
        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Assign views
        mPhoneNumberField = findViewById(R.id.field_phone_number);
//        progressbar = findViewById(R.id.progressbar);
        Button mVerifyButton = findViewById(R.id.button_verify);
        mVerifyButton.setOnClickListener(this);

        ccp.registerPhoneNumberTextView(mPhoneNumberField);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
//                Toast.makeText(Signin.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // [END phone_auth_callbacks]
    }


    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_verify:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification();
                break;
//                String code = mVerificationField.getText().toString();
//                if (TextUtils.isEmpty(code)) {
//                    mVerificationField.setError("Cannot be empty.");
//                    return;
//                }

//                verifyPhoneNumberWithCode(mVerificationId, code);
//                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
        }
    }

    private void startPhoneNumberVerification() {
        final String mobile = "" + ccp.getPhoneNumber().getNationalNumber();
        if (CommonUtil.isNetworkAvailable(PhoneNumberAuthentication.this)) {
            if (!isClicked) {
                isClicked = true;
                MainActivity.showProgress(PhoneNumberAuthentication.this);
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<EventResponse> call = apiService.generateOtp(mobile);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        Log.d(TAG, "response: " + response.body());
                        MainActivity.dismissProgress();
                        updateClick();
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                Toast.makeText(PhoneNumberAuthentication.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PhoneNumberAuthentication.this, VerificationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("country", ccp.getSelectedCountryName());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(PhoneNumberAuthentication.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e(TAG, t.toString());
                        updateClick();
                        MainActivity.dismissProgress();
                        Toast.makeText(PhoneNumberAuthentication.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            updateClick();
            Toast.makeText(PhoneNumberAuthentication.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isClicked = false;

    private void updateClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isClicked = false;
            }
        }, 1500);
    }
}
