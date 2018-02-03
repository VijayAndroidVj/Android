package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MmSignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText input_password, input_username;
    private static final String TAG = MmSignInActivity.class.getSimpleName();
    Button bt_clear_username;
    private Activity activity;
    private FirebaseAuth mAuth;
    private String firstName, lastName, email, birthday, gender;
    private String userId;
    private CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;
    private ProgressDialog mProgressDialog;
    private LoginButton loginButton;
    private boolean isFbClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_signin);
        activity = this;
        mAuth = FirebaseAuth.getInstance();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            // If the login is successful...//
            public void success(final Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin" + result);
                handleTwitterSession(result.data, result);

            }


            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("l_name", "");
                isFbClicked = true;
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
//                            String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id = "";

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
//                                if (AccessToken.getCurrentAccessToken() != null) {
//                                    RequestData();
//                                    Profile profile = Profile.getCurrentProfile();
//                                    if (profile != null) {
//
//                                        facebook_id = profile.getId();
//                                        Log.e("facebook_id", facebook_id);
//                                        f_name = profile.getFirstName();
//                                        Log.e("f_name", f_name);
////                                        input_name.setText(f_name);
//                                        m_name = profile.getMiddleName();
//                                        Log.e("m_name", m_name);
//                                        l_name = profile.getLastName();
//                                        Log.e("l_name", l_name);
//                                        full_name = profile.getName();
//                                        Log.e("full_name", full_name);
//                                        profile_image = profile.getProfilePictureUri(400, 400).toString();
//                                        Log.e("profile_image", profile_image);
//                                    }
//                                }
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
//            }
//        });


        input_password = (EditText) findViewById(R.id.input_password);
        input_username = (EditText) findViewById(R.id.input_username);
        bt_clear_username = (Button) findViewById(R.id.bt_clear_username);

        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_username.addTextChangedListener(new MyTextWatcher(input_username));
//        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.link_signup).setOnClickListener(this);
        findViewById(R.id.btn_sigin).setOnClickListener(this);
        findViewById(R.id.bt_clear_username).setOnClickListener(this);
        input_username.clearFocus();
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

    private void handleFacebookAccessToken(AccessToken token) {
        if (isFbClicked) {
            Log.d(TAG, "handleFacebookAccessToken:" + token);

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                                if (user == null)
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Name, email address, and profile photo Url
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    LoginManager.getInstance().logOut();
                                    if (email != null) {
                                        submitForm(email);
                                    }
//                                Uri photoUrl = user.getPhotoUrl();

                                    // Check if user's email is verified
                                    boolean emailVerified = user.isEmailVerified();

                                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                                    // authenticate with your backend server, if you have one. Use
                                    // FirebaseUser.getToken() instead.
                                    String uid = user.getUid();
                                }
                                FirebaseAuth.getInstance().signOut();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(activity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                                LoginManager.getInstance().logOut();
                                FirebaseAuth.getInstance().signOut();
                            }

                            // ...
                        }
                    });
        } else {
            LoginManager.getInstance().logOut();

        }
    }


    private void handleTwitterSession(TwitterSession session, final Result<TwitterSession> twitterSession) {
        Log.d(TAG, "handleTwitterSession:" + session);
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            if (user == null)
                                user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                if (email == null) {
                                    TwitterAuthClient authClient = new TwitterAuthClient();
                                    authClient.requestEmail(twitterSession.data, new com.twitter.sdk.android.core.Callback<String>() {
                                        @Override
                                        public void success(Result<String> result) {
//                                            input_email.setText(result.data);
                                            String email = result.data;
                                            if (email != null) {
                                                submitForm(email);

                                            }
                                            Log.i("", email);
                                            // Do something with the result, which provides the email address
                                        }

                                        @Override
                                        public void failure(TwitterException exception) {
                                            // Do something on failure
                                            exception.printStackTrace();
                                        }
                                    });
                                } else {
                                    submitForm(email);
                                }

                                // Check if user's email is verified
                                boolean emailVerified = user.isEmailVerified();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getToken() instead.
                                String uid = user.getUid();
                            }
                            FirebaseAuth.getInstance().signOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            FirebaseAuth.getInstance().signOut();
                        }

                        // ...
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
//                        input_email.setText(json.getString("email"));
//                        input_userName.setText(json.getString("name"));
                        /*if (!json.getString("name").equalsIgnoreCase("male")) {
                            female.setChecked(true);
                        } else {
                            male.setChecked(true);
                        }*/
                    }
                    LoginManager.getInstance().logOut();

                } catch (Exception e) {
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
                if (isFbClicked) {
                    MainActivity.showProgress(MmSignInActivity.this);
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
            } else {
                MainActivity.showProgress(MmSignInActivity.this);
                twitterLoginButton.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void submitForm(String email) {

        if (TextUtils.isEmpty(email) && !validUsername()) {
            return;
        }

        if (TextUtils.isEmpty(email) && !validPassword()) {
            return;
        }

        final String username = input_username.getText().toString().trim();
        final String password = input_password.getText().toString().trim();

        if (CommonUtil.isNetworkAvailable(MmSignInActivity.this)) {
            if (TextUtils.isEmpty(email))
                MainActivity.showProgress(MmSignInActivity.this);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.login(username, password, email);
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
                            preferenceUtil.putString(Keys.NAME, sigInResponse.getName());

                            preferenceUtil.putString(Keys.GENDER, sigInResponse.getGender());
                            preferenceUtil.putString(Keys.STATE, sigInResponse.getState());
                            preferenceUtil.putString(Keys.COUNTRY, sigInResponse.getCountry());
                            preferenceUtil.putString(Keys.ABOUTME, sigInResponse.getAboutme());

                            preferenceUtil.putString(Keys.EmailID, sigInResponse.getEmail());
                            preferenceUtil.putString(Keys.USERNAME, sigInResponse.getUsername());
                            preferenceUtil.putString(Keys.PASSWORD, sigInResponse.getPassword());
                            preferenceUtil.putString(Keys.PROFILE_IMAGE, sigInResponse.getServerimage());
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
            input_password.setError("Invalid Password");
            requestFocus(input_password);
            return false;
        } else if (pass.length() < 6) {
            input_password.setError("Password must be greater than 6 character");
            requestFocus(input_password);
            return false;
        } else {
            input_password.setError(null);
        }

        return true;
    }

    private boolean validUsername() {
        String email = input_username.getText().toString().trim();

        if (email.isEmpty()) {
            input_username.setError("Enter valid Username id");
            requestFocus(input_username);
            return false;
        } else {
            input_username.setError(null);
        }

        return true;
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
                submitForm("");
                break;
            case R.id.link_signup:
            case R.id.btn_signup:
                PreferenceUtil preferenceUtil = new PreferenceUtil(MmSignInActivity.this);
                if (TextUtils.isEmpty(preferenceUtil.getUserCountry())) {
                    Intent intent = new Intent(MmSignInActivity.this, PhoneNumberAuthentication.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MmSignInActivity.this, MmSignUpActivity.class);
//                    intent.putExtra("mobile", mobilenumber);
                    intent.putExtra("country", preferenceUtil.getUserCountry());
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
        }
    }
}
