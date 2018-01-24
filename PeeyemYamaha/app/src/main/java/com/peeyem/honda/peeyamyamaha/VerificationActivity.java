package com.peeyem.honda.peeyamyamaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.peeyem.honda.peeyamyamaha.utils.Keys;
import com.peeyem.honda.peeyamyamaha.utils.PreferenceUtil;

/**
 * Created by vijay on 3/12/17.
 */

public class VerificationActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private EditText mPhoneNumberField;
    FirebaseAuth mAuth;
    private Button mVerifyButton;
    String verificationId, mobilenumber, country;

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");
        mobilenumber = getIntent().getStringExtra("mobile");
        country = getIntent().getStringExtra("country");
        // Assign views
        mPhoneNumberField = findViewById(R.id.edtCode);
        progressbar = findViewById(R.id.progressbar);
        mVerifyButton = findViewById(R.id.btnNext);
        mVerifyButton.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.txtnotrecieve).setOnClickListener(this);
        TextView textViewnumber = findViewById(R.id.txtSendTo);
        textViewnumber.setText("We have dent code to " + mobilenumber);
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
    }

    private void verifyPhoneNumberWithCode(String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent(VerificationActivity.this, MmSignUpActivity.class);
                            intent.putExtra("mobile", mobilenumber);
                            intent.putExtra("country", country);
                            PreferenceUtil preferenceUtil = new PreferenceUtil(VerificationActivity.this);
                            preferenceUtil.putString(Keys.PHONE, mobilenumber);
                            startActivity(intent);
                            finish();
                        } else {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(VerificationActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
        // [END verify_with_code]
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid verification code.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (!validatePhoneNumber()) {
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(mPhoneNumberField.getText().toString());
                break;
            case R.id.txtnotrecieve:
            case R.id.btnBack:
                Intent intent = new Intent(VerificationActivity.this, PhoneNumberAuthentication.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
