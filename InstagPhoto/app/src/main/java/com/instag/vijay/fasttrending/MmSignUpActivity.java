package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MmSignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = MmSignUpActivity.class.getSimpleName();
    private Activity activity;

    private TextInputEditText input_name;
    private TextInputEditText input_email;
    private TextInputEditText input_password;
    private Button bt_clear_name;
    private Button bt_clear_email;
    private TextInputEditText input_userName;
    private TextView txtusravailable;
    private CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;
    private RadioGroup radioSexGroup;
    private RadioButton male, female;
    private String country;
    private String mobile;
    private boolean isFbClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        activity = this;
        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");
        radioSexGroup = findViewById(R.id.radioSex);
        male = findViewById(R.id.radioMale);
        female = findViewById(R.id.radioFemale);
        txtusravailable = findViewById(R.id.txtusravailable);
        txtusravailable.setVisibility(View.GONE);
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

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            // If the login is successful...//
            public void success(final Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin" + result);
                Result<TwitterSession> twitterSession = result;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                Call<User> user = twitterApiClient.getAccountService().verifyCredentials(true, true);
                user.enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        input_name.setText(userResult.data.name);
                        input_userName.setText(result.data.getUserName());
                        input_email.setText(userResult.data.email);
//                        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();


                        String t_profile_image = userResult.data.profileImageUrl;

                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(twitterSession.data, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        input_email.setText(result.data);
                        // Do something with the result, which provides the email address
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        exception.printStackTrace();
                    }
                });

            }


            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        input_name = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_userName = findViewById(R.id.input_userName);
        bt_clear_name = findViewById(R.id.bt_clear_name);
        bt_clear_email = findViewById(R.id.bt_clear_email);
        input_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_userName.setError(null);
                if (input_userName.getText().toString().trim().length() > 0) {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<EventResponse> call = apiService.user_name_availability(input_userName.getText().toString().trim());
                    call.enqueue(new Callback<EventResponse>() {
                        @Override
                        public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                            Log.d(TAG, "Number of movies received: " + response.body());
                            EventResponse sigInResponse = response.body();
                            if (sigInResponse != null) {
                                if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                    txtusravailable.setVisibility(View.GONE);
                                    txtusravailable.setText(sigInResponse.getMessage());
                                    input_userName.setError("Username Not available");
                                    requestFocus(input_userName);
                                    isNameAailable = false;
                                } else {
                                    txtusravailable.setVisibility(View.VISIBLE);
                                    txtusravailable.setText(sigInResponse.getMessage());
                                    isNameAailable = true;
                                }
                            } else {
                                Toast.makeText(MmSignUpActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<EventResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e(TAG, t.toString());
                        }
                    });
                } else {
                    txtusravailable.setVisibility(View.GONE);
                }
            }
        });

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

        country = getIntent().getStringExtra("country");
        mobile = getIntent().getStringExtra("mobile");

        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFbClicked = true;
                // LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
                loginButton.registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id = "";

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                if (isFbClicked && AccessToken.getCurrentAccessToken() != null) {
                                    RequestData();
                                    Profile profile = Profile.getCurrentProfile();
                                    if (profile != null) {

                                        facebook_id = profile.getId();
                                        Log.e("facebook_id", facebook_id);
                                        f_name = profile.getFirstName();
                                        Log.e("f_name", f_name);
                                        input_name.setText(f_name);
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
                        if (json.has("gender")) {
                            if (!json.getString("gender").equalsIgnoreCase("male")) {
                                female.setChecked(true);
                            } else {
                                male.setChecked(true);
                            }
                        }
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
            if (requestCode == 64206) {
                if (isFbClicked)
                    callbackManager.onActivityResult(requestCode, resultCode, data);
            } else
                twitterLoginButton.onActivityResult(requestCode, resultCode, data);
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

        if (!validName()) {
            return;
        }

        if (!validateUsername()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!isNameAailable) {
            input_userName.setError("Username Not available");
            requestFocus(input_userName);
            return;
        }

        final String email = input_email.getText().toString().trim();
        final String name = input_name.getText().toString().trim();
        String userName = input_userName.getText().toString().trim();
        final String password = input_password.getText().toString().trim();
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        final String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);
        final String userName1 = userName.substring(0, 1).toUpperCase() + userName.substring(1);

        final String gender = selectedId == R.id.radioMale ? "male" : "female";
        if (CommonUtil.isNetworkAvailable(activity)) {
            MainActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.register(country, gender, name, userName1, email, password, mobile);
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
                            preferenceUtil.putString(Keys.NAME, name);
                            preferenceUtil.putString(Keys.EmailID, email);
                            preferenceUtil.putString(Keys.USERNAME, userName1);
                            preferenceUtil.putString(Keys.PASSWORD, password);
                            preferenceUtil.putString(Keys.COUNTRY, country);
                            preferenceUtil.putString(Keys.CONTACT_NUMBER, mobile);
                            preferenceUtil.putString(Keys.GENDER, gender);
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
            input_userName.setError("Invalid username");
            requestFocus(input_userName);
            return false;
        } else {
            input_userName.setError(null);
        }
        return true;
    }

    private boolean validName() {
        String pass = input_name.getText().toString().trim();
        if (pass.isEmpty()) {
            input_name.setError("Invalid name");
            requestFocus(input_name);
            return false;
        } else if (pass.length() < 3) {
            input_name.setError("Name must be greater than 3 character");
            requestFocus(input_name);
            return false;
        } else {
            input_name.setError(null);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = input_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_email.setError("Enter valid Email id");
            requestFocus(input_email);
            return false;
        } else {
            input_email.setError(null);
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
            input_password.setError("Enter Password");
            requestFocus(input_password);
            return false;
        } else if (pass.length() < 6) {
            input_password.setError("Minimum 6 characters");
            requestFocus(input_password);
            return false;
        } else {
            input_password.setError(null);
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_signin:
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                preferenceUtil.putString(Keys.COUNTRY, "");
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
