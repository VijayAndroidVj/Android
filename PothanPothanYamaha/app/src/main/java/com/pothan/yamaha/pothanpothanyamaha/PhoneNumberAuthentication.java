package com.pothan.yamaha.pothanpothanyamaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

/**
 * Created by vijay on 2/12/17.
 */

public class PhoneNumberAuthentication extends AppCompatActivity implements
        View.OnClickListener {


    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private AppCompatEditText mPhoneNumberField;

    private Button mVerifyButton;
    private EditText inputVehicleNumber;

    private ProgressBar progressbar;
    private CountryCodePicker ccp;
    private String vehicletype;
    private String vehiclenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ccp = findViewById(R.id.countrycode);
        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        vehicletype = getIntent().getStringExtra("vehicletype");
        vehiclenumber = getIntent().getStringExtra("vehiclenumber");

        // Assign views
        mPhoneNumberField = findViewById(R.id.field_phone_number);
        inputVehicleNumber = findViewById(R.id.input_username);

        progressbar = findViewById(R.id.progressbar);
        mVerifyButton = findViewById(R.id.button_verify);
        findViewById(R.id.selecttype).setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        if (vehiclenumber != null)
            inputVehicleNumber.setText(vehiclenumber);

        ccp.registerPhoneNumberTextView(mPhoneNumberField);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
//                Toast.makeText(Signin.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                progressbar.setVisibility(View.GONE);
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;

//                signInWithPhoneAuthCredential(credential);
                // [END_EXCLUDE]

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                progressbar.setVisibility(View.GONE);
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                progressbar.setVisibility(View.GONE);

                Intent intent = new Intent(PhoneNumberAuthentication.this, VerificationActivity.class);
                intent.putExtra("mobile", ccp.getSelectedCountryCode() + ccp.getPhoneNumber().getNationalNumber());
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("vehiclenumber", inputVehicleNumber.getText().toString().trim());
                intent.putExtra("vehicletype", vehicletype);
                intent.putExtra("country", ccp.getSelectedCountryName());

                startActivity(intent);
                finish();
            }
        };
        // [END phone_auth_callbacks]
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVERifier", "signInWithCredential:success");
                            startActivity(new Intent(PhoneNumberAuthentication.this, MainActivity.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseAuth.getInstance().signOut();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        progressbar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selecttype:
                Intent intent = new Intent(PhoneNumberAuthentication.this, SelectMyVehicle.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_verify:
                if (inputVehicleNumber.getText().toString().length() == 0) {
                    inputVehicleNumber.setError("Enter Vehicle Number");
                    inputVehicleNumber.requestFocus();
                    return;
                }
                if (!validatePhoneNumber()) {
                    return;
                }
                String mobile = ccp.getSelectedCountryCodeWithPlus() + ccp.getPhoneNumber().getNationalNumber();

//                intent = new Intent(PhoneNumberAuthentication.this, MmSignInActivity.class);
//                intent.putExtra("vehiclenumber", vehiclenumber);
//                intent.putExtra("vehicletype", vehicletype);
//                intent.putExtra("mobile", ccp.getPhoneNumber().getNationalNumber());
//                intent.putExtra("country", ccp.getSelectedCountryCodeWithPlus());
//                PreferenceUtil preferenceUtil = new PreferenceUtil(PhoneNumberAuthentication.this);
//                preferenceUtil.putString(Keys.PHONE, mobile);
//                preferenceUtil.putString(Keys.vehiclenumber, vehiclenumber);
//                preferenceUtil.putString(Keys.vehicletype, vehicletype);
//                startActivity(intent);

                startPhoneNumberVerification(mobile);
                break;
        }
    }
}
