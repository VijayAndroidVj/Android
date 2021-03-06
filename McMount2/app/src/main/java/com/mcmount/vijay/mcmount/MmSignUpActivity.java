package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mcmount.vijay.mcmount.retrofit.ApiClient;
import com.mcmount.vijay.mcmount.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MmSignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = MmSignUpActivity.class.getSimpleName();
    private Activity activity;

    private EditText input_name;
    private EditText input_email;
    //    private EditText input_Department;
    private EditText input_password;
    private EditText input_cpassword;
    private Button bt_clear_name;
    private Button bt_clear_email;
    private TextInputLayout til_signup_name;
    private TextInputLayout til_signup_email;
    private TextInputLayout til_signup_password;
    private TextInputLayout til_signup_cpassword;
    private EditText et_signup_phone;
    private TextInputLayout til_signup_phone;
    private CheckBox checkbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmsignup);

        activity = this;


        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);

        input_password = (EditText) findViewById(R.id.input_password);
        input_cpassword = (EditText) findViewById(R.id.input_cpassword);

        til_signup_name = (TextInputLayout) findViewById(R.id.til_signup_name);
        til_signup_email = (TextInputLayout) findViewById(R.id.til_signup_email);
        et_signup_phone = (EditText) findViewById(R.id.input_phone);
        til_signup_phone = (TextInputLayout) findViewById(R.id.til_signup_phone);
        til_signup_password = (TextInputLayout) findViewById(R.id.til_signup_password);
        til_signup_cpassword = (TextInputLayout) findViewById(R.id.til_signup_cpassword);
        bt_clear_name = (Button) findViewById(R.id.bt_clear_name);
        bt_clear_email = (Button) findViewById(R.id.bt_clear_email);
        checkbox = (CheckBox) findViewById(R.id.terms);
        checkbox.setText(Html.fromHtml("By signing up for an account you agree to our " +
                "<a href='http://mcmount.com//terms-and-conditions'>Terms and Conditions</a>"));
        checkbox.setClickable(true);
        checkbox.setMovementMethod(LinkMovementMethod.getInstance());


        bt_clear_name.setOnClickListener(this);
        bt_clear_email.setOnClickListener(this);

        input_name.setOnFocusChangeListener(this);
        input_email.setOnFocusChangeListener(this);

        input_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (input_name.getText().toString().length() != 0) {
                    bt_clear_name.setVisibility(View.VISIBLE);
                } else {
                    bt_clear_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (input_email.getText().toString().length() != 0) {
                    bt_clear_email.setVisibility(View.VISIBLE);
                } else {
                    bt_clear_email.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.link_signin).setOnClickListener(this);

        findViewById(R.id.btn_signup).setOnClickListener(this);
        input_name.clearFocus();
        input_email.clearFocus();
        input_password.clearFocus();
        input_cpassword.clearFocus();

//        spinner.setVisibility(View.INVISIBLE);
        //spinner.performClick();
//        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (spinner.isShown() && hasFocus) {
//            spinner.setVisibility(View.GONE);
//            //Do you work here
//        }
    }

    /**
     * Validating form
     */

    private void submitForm() {

        if (!validName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateCPassword()) {
            return;
        }


        final String email = input_email.getText().toString().trim();
        final String name = input_name.getText().toString().trim();

        final String phone = et_signup_phone.getText().toString().trim();
        final String password = input_password.getText().toString().trim();
        final String cpassword = input_cpassword.getText().toString().trim();

        if (!password.equalsIgnoreCase(cpassword)) {
            til_signup_cpassword.setError("Password mismatch");
            return;
        }

        if (!checkbox.isChecked()) {
            Toast.makeText(activity, "Accept terms ans conditions", Toast.LENGTH_LONG).show();
        }


        if (CommonUtil.isNetworkAvailable(activity)) {
            MainActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.register(name, email, password, phone, "", "", "", "");
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    Log.d(TAG, "Number of movies received: " + response.body());
                    MainActivity.dismissProgress();
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        if (sigInResponse.getError()) {
                            Toast.makeText(MmSignUpActivity.this, sigInResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                        } else {
                            PreferenceUtil preferenceUtil = new PreferenceUtil(MmSignUpActivity.this);
                            preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, true);
                            preferenceUtil.putString(Keys.NAME, sigInResponse.getUser().getName());
                            preferenceUtil.putString(Keys.EmailID, sigInResponse.getUser().getEmail());
                            preferenceUtil.putString(Keys.PHONE, sigInResponse.getUser().getMobile());
                            preferenceUtil.putString(Keys.PASSWORD, password);
                            Intent intent = new Intent(MmSignUpActivity.this, MmSignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(MmSignUpActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                    MainActivity.dismissProgress();
                    Toast.makeText(MmSignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent(MmSignUpActivity.this, MmSignInActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validatePhone() {
        String pass = et_signup_phone.getText().toString().trim();
        if (pass.isEmpty()) {
            til_signup_phone.setError("Invalid phone");
            requestFocus(et_signup_phone);
            return false;
        } else if (pass.length() < 3) {
            til_signup_phone.setError("Phone must be greater than 10 digit");
            requestFocus(et_signup_phone);
            return false;
        } else {
            til_signup_phone.setError(null);
        }
        return true;
    }

    private boolean validName() {
        String pass = input_name.getText().toString().trim();
        if (pass.isEmpty()) {
            til_signup_name.setError("Invalid name");
            requestFocus(input_name);
            return false;
        } else if (pass.length() < 3) {
            til_signup_name.setError("Name must be greater than 3 character");
            requestFocus(input_name);
            return false;
        } else {
            til_signup_name.setError(null);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = input_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            til_signup_email.setError("Enter valid Email id");
            requestFocus(input_email);
            return false;
        } else {
            til_signup_email.setError(null);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateCPassword() {
        String pass = input_cpassword.getText().toString().trim();

        if (pass.isEmpty()) {
            til_signup_cpassword.setError("Enter Confirm Password");
            requestFocus(input_cpassword);
            return false;
        } else {
            til_signup_cpassword.setError(null);
        }

        return true;
    }

    private boolean validatePassword() {
        String pass = input_password.getText().toString().trim();

        if (pass.isEmpty()) {
            til_signup_password.setError("Enter Password");
            requestFocus(input_password);
            return false;
        } else if (pass.length() < 6) {
            til_signup_password.setError("Minimum 6 characters");
            requestFocus(input_password);
            return false;
        } else {
            til_signup_password.setError(null);
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_signin:
                Intent intent = new Intent(MmSignUpActivity.this, MmSignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_signup:
                submitForm();
                break;
            case R.id.bt_clear_name:
                input_name.setText("");
                break;

            case R.id.bt_clear_email:
                input_email.setText("");
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.input_name:
                if (hasFocus) {
                    if (input_name.getText().toString().length() != 0) {
                        bt_clear_name.setVisibility(View.VISIBLE);
                    } else {
                        bt_clear_name.setVisibility(View.GONE);
                    }
                } else {
                    bt_clear_name.setVisibility(View.GONE);
                }
                break;
            case R.id.input_email:
                if (hasFocus) {
                    if (input_email.getText().toString().length() != 0) {
                        bt_clear_email.setVisibility(View.VISIBLE);
                    } else {
                        bt_clear_email.setVisibility(View.GONE);
                    }
                } else {
                    bt_clear_email.setVisibility(View.GONE);
                }
                break;
        }
    }
}
