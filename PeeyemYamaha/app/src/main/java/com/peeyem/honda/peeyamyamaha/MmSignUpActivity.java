package com.peeyem.honda.peeyamyamaha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.peeyem.honda.peeyamyamaha.models.EventResponse;
import com.peeyem.honda.peeyamyamaha.retrofit.ApiClient;
import com.peeyem.honda.peeyamyamaha.retrofit.ApiInterface;
import com.peeyem.honda.peeyamyamaha.utils.Keys;
import com.peeyem.honda.peeyamyamaha.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MmSignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = MmSignUpActivity.class.getSimpleName();
    private Activity activity;

    //Tags to send the username and image url to next activity using intent
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";
    private TextInputEditText input_email;
    private TextInputEditText input_password;
    private Button bt_clear_email;
    private TextInputLayout til_signup_email;
    private TextInputLayout til_signup_password;
    private TextInputEditText input_userName;
    private TextInputLayout til_signup_userName;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        activity = this;
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");
        /*mTwitterAuthClient = new TwitterAuthClient();
        mTwitterAuthClient.authorize(activity, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> session) {
                TwitterAuthToken authToken = session.data.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Twitter Login Failure", e.getMessage());
            }
        });*/


        input_email = (TextInputEditText) findViewById(R.id.input_email);
        input_password = (TextInputEditText) findViewById(R.id.input_password);
        til_signup_email = (TextInputLayout) findViewById(R.id.til_signup_email);
        input_userName = (TextInputEditText) findViewById(R.id.input_userName);
        til_signup_userName = (TextInputLayout) findViewById(R.id.til_signup_userName);
        til_signup_password = (TextInputLayout) findViewById(R.id.til_signup_password);
        bt_clear_email = (Button) findViewById(R.id.bt_clear_email);

        bt_clear_email.setOnClickListener(this);

        input_email.setOnFocusChangeListener(this);


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
        input_email.clearFocus();
        input_password.clearFocus();

        mobile = getIntent().getStringExtra("mobile");

        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
                loginButton.registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id = "";

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                if (AccessToken.getCurrentAccessToken() != null) {
                                    RequestData();
                                    Profile profile = Profile.getCurrentProfile();
                                    if (profile != null) {

                                        facebook_id = profile.getId();
                                        Log.e("facebook_id", facebook_id);
                                        f_name = profile.getFirstName();
                                        m_name = profile.getMiddleName();
                                        Log.e("m_name", m_name);
                                        l_name = profile.getLastName();
                                        Log.e("l_name", l_name);
                                        full_name = profile.getName();
                                        Log.e("full_name", full_name);
                                        profile_image = profile.getProfilePictureUri(400, 400).toString();
                                        Log.e("profile_image", profile_image);
                                    }
                                }
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });
            }
        });


    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);
                try {
                    if (json != null) {
                        input_email.setText(json.getString("email"));
                        input_userName.setText(json.getString("name"));
                    }
                    LoginManager.getInstance().logOut();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


//        spinner.setVisibility(View.INVISIBLE);
    //spinner.performClick();
//        spinner.setVisibility(View.GONE);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 64206)
                callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (spinner.isShown() && hasFocus) {
//            spinner.setVisibility(View.GONE);
//            //Do you work here
//        }
    }

    boolean isNameAailable = false;

    /**
     * Validating form
     */

    private void submitForm() {

        if (!validateUsername()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }


        final String email = input_email.getText().toString().trim();
        final String userName = input_userName.getText().toString().trim();
        final String password = input_password.getText().toString().trim();

        if (CommonUtil.isNetworkAvailable(activity)) {
            MainActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.register("1", mobile, userName, email, password);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    Log.d(TAG, "Number of movies received: " + response.body());
                    MainActivity.dismissProgress();
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        LoginManager.getInstance().logOut();
                        if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                            Toast.makeText(MmSignUpActivity.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            PreferenceUtil preferenceUtil = new PreferenceUtil(MmSignUpActivity.this);
                            preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, true);
                            preferenceUtil.putString(Keys.EmailID, email);
                            preferenceUtil.putString(Keys.USERNAME, userName);
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

    private boolean validateUsername() {
        String username = input_userName.getText().toString().trim();
        if (username.isEmpty()) {
            til_signup_userName.setError("Invalid username");
            requestFocus(input_userName);
            return false;
        } else {
            til_signup_userName.setError(null);
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

            case R.id.bt_clear_email:
                input_email.setText("");
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
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
