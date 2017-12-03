package com.android.mymedicine.java.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymedicine.R;
import com.android.mymedicine.java.model.CallEvent;
import com.android.mymedicine.java.retrofit.ApiClient;
import com.android.mymedicine.java.retrofit.ApiInterface;
import com.android.mymedicine.java.utils.PreferenceUtil;
import com.android.mymedicine.java.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by razin on 23/11/17.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView tv_login_signup_link;
    Button bt_login_signin;
    TextInputLayout til_login_username;
    TextInputLayout til_login_password;
    TextInputEditText et_login_username;
    TextInputEditText et_login_password;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceUtil = new PreferenceUtil(this);
        setInitUI();
        setRegisterUI();
        setTextWatcher();
    }

    private void setTextWatcher() {
        et_login_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_login_username.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_login_password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setRegisterUI() {
        tv_login_signup_link.setOnClickListener(this);
        bt_login_signin.setOnClickListener(this);
    }

    private void setInitUI() {
        tv_login_signup_link = findViewById(R.id.tv_login_signup_link);
        bt_login_signin = findViewById(R.id.bt_login_signin);
        til_login_username = findViewById(R.id.til_login_username);
        til_login_password = findViewById(R.id.til_login_password);
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
    }

    private void submit() {
        if (Utils.isNetworkAvailable(this)) {
            findViewById(R.id.pb_login).setVisibility(View.VISIBLE);
            String username = et_login_username.getText().toString();
            String password = et_login_password.getText().toString();
            HashMap<String, Object> hashmap = new HashMap<>();
            try {
                hashmap.put("username", username);
                hashmap.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ArrayList<CallEvent>> call = apiService.login(hashmap);
            call.enqueue(new Callback<ArrayList<CallEvent>>() {
                @Override
                public void onResponse(Call<ArrayList<CallEvent>> call, Response<ArrayList<CallEvent>> response) {

                    if (response.body() != null) {
                        CallEvent callEvent = response.body().get(0);
                        if (callEvent != null) {
                            if (callEvent.getStatus() == 1) {
                                preferenceUtil.setLogInData(callEvent);
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, callEvent.getError(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    findViewById(R.id.pb_login).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ArrayList<CallEvent>> call, Throwable t) {
                    findViewById(R.id.pb_login).setVisibility(View.GONE);
                    Toast.makeText(Login.this, getResources().getString(R.string.failed_to_connect_to_the_server), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_signup_link:
                startActivity(new Intent(Login.this, Registration.class));
                finish();
                break;
            case R.id.bt_login_signin:
                if (TextUtils.isEmpty(et_login_username.getText().toString())) {
                    til_login_username.setError(getResources().getString(R.string.please_provide_username));
                    et_login_username.requestFocus();
                } else if (TextUtils.isEmpty(et_login_password.getText().toString())) {
                    til_login_password.setError(getResources().getString(R.string.please_provide_password));
                    et_login_password.requestFocus();
                } else {
                    submit();
                }
                break;
        }
    }
}
