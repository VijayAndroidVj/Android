package com.vajralabs.uniroyal.uniroyal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vajralabs.uniroyal.uniroyal.CommonUtil;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.EventResponse;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiClient;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiInterface;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by vijay on 23/12/17.
 */

public class DownloadCatelog extends AppCompatActivity {

    EditText input_username, input_companyname, input_usermail, input_mobile;
    Activity activity;

    ProgressBar progressBar1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_catelog);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Download Catalogue");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        input_username = findViewById(R.id.input_username);
        input_companyname = findViewById(R.id.input_companyname);
        progressBar1 = findViewById(R.id.progressBar_cyclic);
        input_usermail = findViewById(R.id.input_useremail);
        input_mobile = findViewById(R.id.input_userbiography);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    private boolean validName() {
        String pass = input_username.getText().toString().trim();
        if (pass.isEmpty()) {
            input_username.setError("Invalid name");
            requestFocus(input_username);
            return false;
        } else if (pass.length() < 3) {
            input_username.setError("Name must be greater than 3 character");
            requestFocus(input_username);
            return false;
        } else {
            input_username.setError(null);
        }

        return true;
    }

    private boolean validCompanyName() {
        String pass = input_companyname.getText().toString().trim();
        if (pass.isEmpty()) {
            input_companyname.setError("Invalid company");
            requestFocus(input_companyname);
            return false;
        } else {
            input_companyname.setError(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateMobile() {
        String pass = input_mobile.getText().toString().trim();

        if (pass.isEmpty()) {
            input_mobile.setError("Enter Password");
            requestFocus(input_mobile);
            return false;
        } else if (pass.length() < 6) {
            input_mobile.setError("Minimum 10 digit");
            requestFocus(input_mobile);
            return false;
        } else {
            input_mobile.setError(null);
        }

        return true;
    }


    private boolean validateEmail() {
        String pass = input_usermail.getText().toString().trim();

        if (TextUtils.isEmpty(pass)) {
            input_usermail.setError("Please add email");
            requestFocus(input_usermail);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(pass).matches()) {
            input_usermail.setError("Please enter valid email");
            requestFocus(input_usermail);
            return false;
        } else {
            input_usermail.setError(null);
        }

        return true;
    }

    private void updateProfile() {

        if (!validName()) {
            return;
        }
        if (!validCompanyName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
        if (!validateMobile()) {
            return;
        }


        final String userName1 = input_username.getText().toString().trim();
        final String email = input_usermail.getText().toString().trim();
        final String mobile = input_mobile.getText().toString().trim();
        final String company = input_companyname.getText().toString().trim();

        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar1.setVisibility(View.VISIBLE);
            showProgress();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);


            // finally, execute the request
            Call<EventResponse> call = apiService.download_catelog(userName1, company, email, mobile);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                    progressBar1.setVisibility(View.GONE);
                    hideProgress();
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        if (sigInResponse.getResult().equals("success")) {
                            new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Download Catalogue")
                                    .setContentText("Thanks for signing up. Our Latest Catalogue will be sent your mail Id. Happy Shopping.")
//                            .setCustomImage(R.drawable.app_logo_back)
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                            if (!TextUtils.isEmpty(sigInResponse.getMessage()))
                                Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    progressBar1.setVisibility(View.GONE);
                    hideProgress();
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar1.setVisibility(View.GONE);
            hideProgress();
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    ProgressDialog progressBar;

    private void showProgress() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Submitting ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar
    }

    private void hideProgress() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
            progressBar = null;
        }
    }


//    private void deleteMyAccount() {
//        if (CommonUtil.isNetworkAvailable(activity)) {
//            progressBar.setVisibility(View.VISIBLE);
//            ApiInterface service =
//                    ApiClient.getClient().create(ApiInterface.class);
//            String usermail = preferenceUtil.getUserMailId();
//            Call<EventResponse> call = service.delete_my_account(usermail);
//            call.enqueue(new Callback<EventResponse>() {
//                @Override
//                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
//                    progressBar.setVisibility(View.GONE);
//                    EventResponse patientDetails = response.body();
//                    Log.i("patientDetails", response.toString());
//                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
//                        Toast.makeText(activity, patientDetails.getMessage(), Toast.LENGTH_SHORT).show();
//                        preferenceUtil.logout();
//                        Intent intent = new Intent(activity, SplashScreen.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<EventResponse> call, Throwable t) {
//                    // Log error here since request failed
//                    progressBar.setVisibility(View.GONE);
//                    String message = t.getMessage();
//                    if (message.contains("Failed to")) {
//                        message = "Failed to Connect";
//                    } else {
//                        message = "Failed";
//                    }
//                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
//        }
//    }


    android.widget.PopupWindow showPopup;


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
