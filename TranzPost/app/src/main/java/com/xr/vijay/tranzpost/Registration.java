package com.xr.vijay.tranzpost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.vijay.tranzpost.model.EventResponse;
import com.xr.vijay.tranzpost.retrofit.ApiClient;
import com.xr.vijay.tranzpost.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by razin on 23/11/17.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout til_registration_name;
    TextInputLayout til_registration_password;
    TextInputLayout til_registration_email;
    TextInputEditText et_registration_name;
    TextInputEditText et_registration_email;
    TextInputEditText et_registration_mobile;
    TextInputEditText et_registration_password;

    Button bt_registration_signup;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtil = new PreferenceUtil(this);
        setContentView(R.layout.activity_registration);
        setInitUI();
        setRegisterUI();
        setTextWatcher();
        et_registration_mobile.setText(getIntent().getStringExtra("mobile"));
    }

    private void setTextWatcher() {
        et_registration_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_registration_name.setError(null);
            }
        });


        et_registration_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidEmail(et_registration_email.getText().toString())) {
                    til_registration_email.setError(getResources().getString(R.string.please_provide_a_valid_email));
                } else {
                    til_registration_email.setError(null);
                }
            }
        });


    }

    private void setRegisterUI() {
        bt_registration_signup.setOnClickListener(this);
        findViewById(R.id.link_signin).setOnClickListener(this);
    }

    ProgressBar progressBar;

    private void setInitUI() {
        progressBar = findViewById(R.id.pb_registration);
        til_registration_name = findViewById(R.id.til_registration_name);
        til_registration_email = findViewById(R.id.til_registration_email);
        til_registration_password = findViewById(R.id.til_registration_password);
        et_registration_name = findViewById(R.id.et_registration_name);
        et_registration_email = findViewById(R.id.et_registration_email);
        bt_registration_signup = findViewById(R.id.bt_registration_signup);
        et_registration_mobile = findViewById(R.id.et_registration_mobile);
        et_registration_password = findViewById(R.id.et_registration_password);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Registration.this, PhoneNumberAuthentication.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registration_signup:
                if (TextUtils.isEmpty(et_registration_name.getText().toString())) {
                    til_registration_name.setError(getResources().getString(R.string.please_provide_name));
                    et_registration_name.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_email.getText().toString())) {
                    til_registration_email.setError(getResources().getString(R.string.please_provide_email));
                    et_registration_email.requestFocus();
                } else if (!isValidEmail(et_registration_email.getText().toString())) {
                    til_registration_email.setError(getResources().getString(R.string.please_provide_a_valid_email));
                    et_registration_email.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_password.getText().toString())) {
                    et_registration_password.setError(getResources().getString(R.string.please_provide_password));
                    et_registration_password.requestFocus();
                } else {
                    submit();
                }
                break;
            case R.id.link_signin:
                Intent intent = new Intent(Registration.this, Signin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mobile", et_registration_mobile.getText().toString());
                startActivity(intent);
                finish();
                break;
        }
    }

    private void submit() {
        if (Utils.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.pb_registration).setVisibility(View.VISIBLE);
            final String username = et_registration_name.getText().toString();
            final String mobile = et_registration_mobile.getText().toString();
            final String password = et_registration_password.getText().toString();
            final String email = et_registration_email.getText().toString();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.register(username, mobile, email, password);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    EventResponse callEvent = response.body();
                    if (callEvent != null) {
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                Toast.makeText(Registration.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                PreferenceUtil preferenceUtil = new PreferenceUtil(Registration.this);
                                preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, true);
                                preferenceUtil.putString(Keys.mobile, mobile);
                                preferenceUtil.putString(Keys.EmailID, email);
                                preferenceUtil.putString(Keys.USERNAME, username);
                                preferenceUtil.putString(Keys.PASSWORD, password);
                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(Registration.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, getResources().getString(R.string.failed_to_connect_to_the_server), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
