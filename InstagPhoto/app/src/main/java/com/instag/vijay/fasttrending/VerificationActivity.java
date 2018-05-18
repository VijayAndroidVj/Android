package com.instag.vijay.fasttrending;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 3/12/17.
 */

public class VerificationActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private EditText mPhoneNumberField;
    private String mobileNumber, country;

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        mobileNumber = getIntent().getStringExtra("mobile");
        country = getIntent().getStringExtra("country");
        // Assign views
        mPhoneNumberField = findViewById(R.id.edtCode);
        progressbar = findViewById(R.id.progressbar);
        Button mVerifyButton = findViewById(R.id.btnNext);
        mVerifyButton.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.txtnotrecieve).setOnClickListener(this);
        TextView textViewNumber = findViewById(R.id.txtSendTo);
        textViewNumber.setText("We have dent code to " + mobileNumber);

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid verification code.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (!validatePhoneNumber()) {
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(mPhoneNumberField.getText().toString());
                break;
            case R.id.txtnotrecieve:
            case R.id.btnBack:
                Intent intent = new Intent(VerificationActivity.this, PhoneNumberAuthentication.class);
//                intent.putExtra("mobile", mobileNumber);
//                intent.putExtra("country", country);
                startActivity(intent);
                finish();
                break;
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

    private void verifyPhoneNumberWithCode(String cCode) {
        if (CommonUtil.isNetworkAvailable(VerificationActivity.this)) {
            // MainActivity.showProgress(VerificationActivity.this);
            if (!isClicked) {
                isClicked = true;
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<EventResponse> call = apiService.verifyOtp(mobileNumber, cCode);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        Log.d(TAG, "response: " + response.body());
                        progressbar.setVisibility(View.GONE);
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                Toast.makeText(VerificationActivity.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(VerificationActivity.this, MmSignUpActivity.class);
                                intent.putExtra("mobile", mobileNumber);
                                intent.putExtra("country", country);
                                PreferenceUtil preferenceUtil = new PreferenceUtil(VerificationActivity.this);
                                preferenceUtil.putString(Keys.COUNTRY, country);
                                preferenceUtil.putString(Keys.CONTACT_NUMBER, mobileNumber);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(VerificationActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }
                        updateClick();

                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e(TAG, t.toString());
                        updateClick();
//                    MainActivity.dismissProgress();
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(VerificationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            progressbar.setVisibility(View.GONE);
            Toast.makeText(VerificationActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
