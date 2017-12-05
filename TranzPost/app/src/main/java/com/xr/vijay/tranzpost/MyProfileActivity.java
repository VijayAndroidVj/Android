package com.xr.vijay.tranzpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xr.vijay.tranzpost.model.EventResponse;
import com.xr.vijay.tranzpost.retrofit.ApiClient;
import com.xr.vijay.tranzpost.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 4/12/17.
 */

public class MyProfileActivity extends AppCompatActivity {
    TextInputEditText et_registration_name, et_registration_registered_number, et_registration_alternate_number, et_registration_email, et_registration_password, et_registration_city, et_registration_noofownedtruck, et_registration_noofattachedtruck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layoutt);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        final TextInputLayout til_registration_name = findViewById(R.id.til_registration_name);
        final TextInputLayout til_registration_email = findViewById(R.id.til_registration_email);

        et_registration_name = findViewById(R.id.et_registration_name);
        et_registration_registered_number = findViewById(R.id.et_registration_registered_number);
        et_registration_alternate_number = findViewById(R.id.et_registration_alternate_number);
        et_registration_email = findViewById(R.id.et_registration_email);
        et_registration_password = findViewById(R.id.et_registration_password);
        et_registration_city = findViewById(R.id.et_registration_city);
        et_registration_noofownedtruck = findViewById(R.id.et_registration_noofownedtruck);
        et_registration_noofattachedtruck = findViewById(R.id.et_registration_noofattachedtruck);

        PreferenceUtil preferenceUtil = new PreferenceUtil(this);
        et_registration_name.setText(preferenceUtil.getUserName());
        et_registration_registered_number.setText(preferenceUtil.getUserRegisteredNumber());
        et_registration_alternate_number.setText(preferenceUtil.getUserAlternateNumber());
        et_registration_email.setText(preferenceUtil.getUserEmail());
        et_registration_password.setText(preferenceUtil.getUserPassword());
        et_registration_city.setText(preferenceUtil.getUserCity());

        et_registration_noofownedtruck.setText("" + preferenceUtil.getUserOwnedTruckes());

        et_registration_noofattachedtruck.setText("" + preferenceUtil.getUserAttachedTruckes());

        findViewById(R.id.btnprofilesave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_registration_name.getText().toString())) {
                    til_registration_name.setError(getResources().getString(R.string.please_provide_name));
                    et_registration_name.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_email.getText().toString())) {
                    til_registration_email.setError(getResources().getString(R.string.please_provide_email));
                    et_registration_email.requestFocus();
                } else if (!isValidEmail(et_registration_email.getText().toString())) {
                    til_registration_email.setError(getResources().getString(R.string.please_provide_a_valid_email));
                    et_registration_email.requestFocus();
                } else if (TextUtils.isEmpty(et_registration_password.getText().toString())) {
                    et_registration_password.setError(getResources().getString(R.string.please_provide_password));
                    et_registration_password.requestFocus();
                } else {
                    submit();
                }
            }
        });
        findViewById(R.id.btnmanagedocument).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, ManageDocument.class);
                startActivity(intent);
            }
        });

        if (preferenceUtil.gettype().equalsIgnoreCase("Customer")) {
            findViewById(R.id.documentsandTruckSettings).setVisibility(View.GONE);
        }

    }

    private void submit() {

        if (Utils.isNetworkAvailable(this)) {
            findViewById(R.id.pbProfile).setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<EventResponse> call = apiService.update_profile(et_registration_name.getText().toString().trim(), et_registration_registered_number.getText().toString().trim(), et_registration_email.getText().toString().trim(), et_registration_password.getText().toString().trim(), et_registration_alternate_number.getText().toString().trim(), et_registration_noofownedtruck.getText().toString().trim(), et_registration_noofattachedtruck.getText().toString().trim(), et_registration_city.getText().toString().trim());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse callEvent = response.body();
                    findViewById(R.id.pbProfile).setVisibility(View.GONE);
                    if (callEvent != null) {
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("failed")) {
                                Toast.makeText(MyProfileActivity.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyProfileActivity.this, "Successfully Saved", Toast.LENGTH_LONG).show();
                                PreferenceUtil preferenceUtil = new PreferenceUtil(MyProfileActivity.this);
                                preferenceUtil.setUserName(et_registration_name.getText().toString());
                                preferenceUtil.setUserRegisteredNumber(et_registration_registered_number.getText().toString());
                                preferenceUtil.setUserAlternateNumber(et_registration_alternate_number.getText().toString());
                                preferenceUtil.setUserEmail(et_registration_email.getText().toString());
                                preferenceUtil.setUserPassword(et_registration_password.getText().toString());
                                preferenceUtil.setUserCity(et_registration_city.getText().toString());
                                if (et_registration_noofownedtruck.getText().toString().length() > 0)
                                    preferenceUtil.setUserOwnedTruckes(Integer.valueOf(et_registration_noofownedtruck.getText().toString()));
                                if (et_registration_noofattachedtruck.getText().toString().length() > 0)
                                    preferenceUtil.setUserAttachedTruckes(Integer.valueOf(et_registration_noofattachedtruck.getText().toString()));

                            }
                        } else {
                            Toast.makeText(MyProfileActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    findViewById(R.id.pbProfile).setVisibility(View.GONE);
                    Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.failed_to_connect_to_the_server), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
