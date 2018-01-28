package com.pace.honda.pacehonda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pace.honda.pacehonda.models.EventResponse;
import com.pace.honda.pacehonda.retrofit.ApiClient;
import com.pace.honda.pacehonda.retrofit.ApiInterface;
import com.pace.honda.pacehonda.utils.Keys;
import com.pace.honda.pacehonda.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MmSignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText input_password, input_username;
    private static final String TAG = MmSignInActivity.class.getSimpleName();
    Button bt_clear_username;
    TextInputLayout til_signin_username;
    TextInputLayout til_signin_password;

    private String firstName, lastName, email, birthday, gender;
    private String userId;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_signin);

        input_password = (EditText) findViewById(R.id.input_password);
        input_username = (EditText) findViewById(R.id.input_username);
        bt_clear_username = (Button) findViewById(R.id.bt_clear_username);
        til_signin_username = (TextInputLayout) findViewById(R.id.til_signin_username);
        til_signin_password = (TextInputLayout) findViewById(R.id.til_signin_password);

        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_username.addTextChangedListener(new MyTextWatcher(input_username));
//        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.link_signup).setOnClickListener(this);
        findViewById(R.id.btn_sigin).setOnClickListener(this);
        findViewById(R.id.bt_clear_username).setOnClickListener(this);
        input_username.clearFocus();
        findViewById(R.id.link_Try_app).setOnClickListener(this);
        input_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (input_username.getText().toString().length() != 0) {
                    bt_clear_username.setVisibility(View.VISIBLE);
                } else {
                    bt_clear_username.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (input_username.getText().toString().length() != 0) {
                        bt_clear_username.setVisibility(View.VISIBLE);
                    } else {
                        bt_clear_username.setVisibility(View.GONE);
                    }
                } else {
                    bt_clear_username.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    /**
     * Validating form
     */

    private void submitForm() {

        if (!validUsername()) {
            return;
        }

        if (!validPassword()) {
            return;
        }

        final String username = input_username.getText().toString().trim();
        final String password = input_password.getText().toString().trim();

        if (CommonUtil.isNetworkAvailable(MmSignInActivity.this)) {
            MainActivity.showProgress(MmSignInActivity.this);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.login(username, password);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    Log.d(TAG, "response: " + response.body());
                    MainActivity.dismissProgress();
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                            Toast.makeText(MmSignInActivity.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            PreferenceUtil preferenceUtil = new PreferenceUtil(MmSignInActivity.this);
                            preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, true);
                            preferenceUtil.putString(Keys.EmailID, sigInResponse.getEmail());
                            preferenceUtil.putString(Keys.USERNAME, sigInResponse.getName());
                            preferenceUtil.putString(Keys.PHONE, sigInResponse.getMobile());
                            preferenceUtil.putString(Keys.PASSWORD, sigInResponse.getPassword());
                            Intent intent = new Intent(MmSignInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(MmSignInActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                    MainActivity.dismissProgress();
                    Toast.makeText(MmSignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MmSignInActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validPassword() {
        String pass = input_password.getText().toString().trim();
        if (pass.isEmpty()) {
            til_signin_password.setError("Invalid Password");
            requestFocus(input_password);
            return false;
        } else if (pass.length() < 6) {
            til_signin_password.setError("Password must be greater than 6 character");
            requestFocus(input_password);
            return false;
        } else {
            til_signin_password.setError(null);
        }

        return true;
    }

    private boolean validUsername() {
        String email = input_username.getText().toString().trim();

        if (email.isEmpty()) {
            til_signin_username.setError("Enter valid Email id");
            requestFocus(input_username);
            return false;
        } else if (email.isEmpty() || !isValidEmail(email)) {
            til_signin_username.setError("Enter valid Email id");
            requestFocus(input_username);
            return false;
        } else {
            til_signin_username.setError(null);
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_password:
//                    validPassword();
                    break;
                case R.id.input_username:
//                    validUsername();
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_sigin:
                submitForm();
                break;
            case R.id.link_signup:
            case R.id.btn_signup:
                PreferenceUtil preferenceUtil = new PreferenceUtil(MmSignInActivity.this);
                if (TextUtils.isEmpty(preferenceUtil.getUserPhone())) {
                    Intent intent = new Intent(MmSignInActivity.this, PhoneNumberAuthentication.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MmSignInActivity.this, MmSignUpActivity.class);
//                    intent.putExtra("mobile", mobilenumber);
                    intent.putExtra("mobile", preferenceUtil.getString(Keys.PHONE, ""));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
//                Intent intent = new Intent(MmSignInActivity.this, MmSignUpActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.bt_clear_username:
                input_username.setText("");
                break;
            case R.id.link_Try_app:
                Intent homeIntent = new Intent(MmSignInActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
                break;
        }
    }
}
