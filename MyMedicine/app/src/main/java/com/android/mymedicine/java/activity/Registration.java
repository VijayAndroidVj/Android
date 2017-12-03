package com.android.mymedicine.java.activity;

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

public class Registration extends AppCompatActivity implements View.OnClickListener {

    TextView tv_registration_signin_link;
    TextInputLayout til_registration_name;
    TextInputLayout til_registration_username;
    TextInputLayout til_registration_email;
    TextInputLayout til_registration_password;
    TextInputLayout til_registration_cpassword;
    TextInputEditText et_registration_name;
    TextInputEditText et_registration_username;
    TextInputEditText et_registration_email;
    TextInputEditText et_registration_password;
    TextInputEditText et_registration_cpassword;
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

        et_registration_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_registration_username.setError(null);
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
                if (!isValidEmail(et_registration_email.getText().toString())){
                    til_registration_email.setError(getResources().getString(R.string.please_provide_a_valid_email));
                } else {
                    til_registration_email.setError(null);
                }
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
                    til_registration_cpassword.setError(null);
            }
        });

        et_registration_cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    til_registration_password.setError(null);
                    til_registration_cpassword.setError(null);
            }
        });
    }

    private void setRegisterUI() {
        tv_registration_signin_link.setOnClickListener(this);
        bt_registration_signup.setOnClickListener(this);
        findViewById(R.id.img_actionbar_back).setOnClickListener(this);
    }

    private void setInitUI() {
        findViewById(R.id.img_actionbar_menu).setVisibility(View.GONE);
        findViewById(R.id.img_actionbar_back).setVisibility(View.VISIBLE);
        TextView tv_actionbar_title = findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText(getResources().getString(R.string.registration));
        tv_registration_signin_link = findViewById(R.id.tv_registration_signin_link);
        til_registration_name = findViewById(R.id.til_registration_name);
        til_registration_username = findViewById(R.id.til_registration_username);
        til_registration_email = findViewById(R.id.til_registration_email);
        til_registration_password = findViewById(R.id.til_registration_password);
        til_registration_cpassword = findViewById(R.id.til_registration_cpassword);
        et_registration_name = findViewById(R.id.et_registration_name);
        et_registration_username = findViewById(R.id.et_registration_username);
        et_registration_email = findViewById(R.id.et_registration_email);
        et_registration_password = findViewById(R.id.et_registration_password);
        et_registration_cpassword = findViewById(R.id.et_registration_cpassword);
        bt_registration_signup = findViewById(R.id.bt_registration_signup);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Registration.this,Login.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_registration_signin_link:
                startActivity(new Intent(Registration.this,Login.class));
                finish();
                break;
            case R.id.img_actionbar_back:
                startActivity(new Intent(Registration.this,Login.class));
                finish();
                break;
            case R.id.bt_registration_signup:
                if (TextUtils.isEmpty(et_registration_name.getText().toString())){
                    til_registration_name.setError(getResources().getString(R.string.please_provide_name));
                    et_registration_name.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_username.getText().toString())){
                    til_registration_username.setError(getResources().getString(R.string.please_provide_username));
                    et_registration_username.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_email.getText().toString())){
                    til_registration_email.setError(getResources().getString(R.string.please_provide_email));
                    et_registration_email.requestFocus();
                } else if (!isValidEmail(et_registration_email.getText().toString())){
                    til_registration_email.setError(getResources().getString(R.string.please_provide_a_valid_email));
                    et_registration_email.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_password.getText().toString())){
                    til_registration_password.setError(getResources().getString(R.string.please_provide_password));
                    et_registration_password.requestFocus();
                } else if (!et_registration_cpassword.getText().toString().equals(et_registration_password.getText().toString())){
                    til_registration_cpassword.setError(getResources().getString(R.string.password_does_not_match));
                    til_registration_password.setError(getResources().getString(R.string.password_does_not_match));
                    et_registration_cpassword.requestFocus();
                } else {
                    submit();
                }
                break;
        }
    }

    private void submit() {
        if (Utils.isNetworkAvailable(this)) {
            findViewById(R.id.pb_registration).setVisibility(View.VISIBLE);
            String username = et_registration_username.getText().toString();
            String password = et_registration_password.getText().toString();
            String full_name = et_registration_name.getText().toString();
            String email = et_registration_email.getText().toString();
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = "";
            try {
                countryCode = tm.getSimCountryIso();
            } catch (Exception e){
                e.printStackTrace();
            }
            if (countryCode.equalsIgnoreCase("")){
                countryCode = "KW";
            }
            HashMap<String, Object> hashmap = new HashMap<>();
            try {
                hashmap.put("username",username);
                hashmap.put("password",password);
                hashmap.put("full_name",full_name);
                hashmap.put("email",email);
                hashmap.put("country_iso",countryCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ArrayList<CallEvent>> call = apiService.register(hashmap);
            call.enqueue(new Callback<ArrayList<CallEvent>>() {
                @Override
                public void onResponse(Call<ArrayList<CallEvent>> call, Response<ArrayList<CallEvent>> response) {

                    CallEvent callEvent = response.body().get(0);
                    if (callEvent != null) {
                        if (callEvent.getStatus() == 1) {
                            preferenceUtil.setLogInData(callEvent);
                            Intent intent = new Intent(Registration.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Registration.this, callEvent.getError(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    findViewById(R.id.pb_registration).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ArrayList<CallEvent>> call, Throwable t) {
                    findViewById(R.id.pb_registration).setVisibility(View.GONE);
                    Toast.makeText(Registration.this, getResources().getString(R.string.failed_to_connect_to_the_server), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            findViewById(R.id.pb_registration).setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
