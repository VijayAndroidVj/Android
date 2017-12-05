package com.xr.vijay.tranzpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xr.vijay.tranzpost.model.EventResponse;
import com.xr.vijay.tranzpost.retrofit.ApiClient;
import com.xr.vijay.tranzpost.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 4/12/17.
 */

public class Signin extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout til_registration_mobile;
    TextInputLayout til_registration_password;
    TextInputEditText et_registration_mobile;
    TextInputEditText et_registration_password;

    Button bt_registration_signup;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtil = new PreferenceUtil(this);
        setContentView(R.layout.signin);
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {
        } else {
            PermissionCheck.requestPermission(this, pendingPermissions, 111);
        }

        setInitUI();
        setRegisterUI();
        setTextWatcher();
        et_registration_mobile.setText(getIntent().getStringExtra("mobile"));
    }

    private void setTextWatcher() {
        et_registration_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_registration_mobile.setError(null);
            }
        });


        et_registration_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_registration_password.setError(null);
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
        til_registration_mobile = findViewById(R.id.til_registration_mobile);
        til_registration_password = findViewById(R.id.til_registration_password);
        bt_registration_signup = findViewById(R.id.bt_registration_signup);
        et_registration_mobile = findViewById(R.id.et_registration_mobile);
        et_registration_password = findViewById(R.id.et_registration_password);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registration_signup:
                if (TextUtils.isEmpty(et_registration_mobile.getText().toString())) {
                    til_registration_mobile.setError(getResources().getString(R.string.please_provide_name));
                    et_registration_mobile.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_password.getText().toString())) {
                    til_registration_password.setError(getResources().getString(R.string.please_provide_email));
                    et_registration_password.requestFocus();
                } else {
                    submit();
                }
                break;
            case R.id.link_signin:
                Intent intent = new Intent(Signin.this, PhoneNumberAuthentication.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void submit() {
        if (Utils.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.pb_registration).setVisibility(View.VISIBLE);
            final String mobile = et_registration_mobile.getText().toString();
            final String password = et_registration_password.getText().toString();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.signin(mobile, password);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    EventResponse callEvent = response.body();
                    if (callEvent != null) {
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                Toast.makeText(Signin.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                PreferenceUtil preferenceUtil = new PreferenceUtil(Signin.this);
                                preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, true);
                                preferenceUtil.putString(Keys.USERNAME, sigInResponse.getName());
                                preferenceUtil.putString(Keys.EmailID, sigInResponse.getEmail());
                                preferenceUtil.putString(Keys.PASSWORD, sigInResponse.getPassword());
                                preferenceUtil.setUserRegisteredNumber(mobile);
                                preferenceUtil.setType(sigInResponse.getUser_type());
                                Intent intent = new Intent(Signin.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(Signin.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Signin.this, getResources().getString(R.string.failed_to_connect_to_the_server), Toast.LENGTH_SHORT).show();
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
