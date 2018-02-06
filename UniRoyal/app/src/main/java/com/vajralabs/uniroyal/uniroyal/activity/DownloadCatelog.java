package com.vajralabs.uniroyal.uniroyal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vajralabs.uniroyal.uniroyal.CommonUtil;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.EventResponse;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiClient;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiInterface;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by vijay on 23/12/17.
 */

public class DownloadCatelog extends AppCompatActivity {

    EditText input_username, input_usermail, input_userpassword, input_aboutme, input_state, input_country;
    Activity activity;
    public final static int CAMERA_REQUEST_PAN = 2;
    public final static int PICK_IMAGE_REQUEST_PAN = 3;
    String imagePath;
    boolean removePhoto;
    Spinner spinner;


    public void launchGalleryIntent(int code) {
        if (showPopup != null) {
            showPopup.dismiss();
        }
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), code);
    }

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.nav_download_catelog));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        input_username = findViewById(R.id.input_username);
        input_usermail = findViewById(R.id.input_useremail);
        input_aboutme = findViewById(R.id.input_userbiography);
        input_state = findViewById(R.id.input_userstate);
        input_country = findViewById(R.id.input_usercountry);


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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePassword() {
        String pass = input_userpassword.getText().toString().trim();

        if (pass.isEmpty()) {
            input_userpassword.setError("Enter Password");
            requestFocus(input_userpassword);
            return false;
        } else if (pass.length() < 6) {
            input_userpassword.setError("Minimum 6 characters");
            requestFocus(input_userpassword);
            return false;
        } else {
            input_userpassword.setError(null);
        }

        return true;
    }

    private void updateProfile() {

        if (!validName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        final String userName1 = input_username.getText().toString().trim();
        final String password1 = input_userpassword.getText().toString().trim();


        final String gender = spinner.getSelectedItemPosition() == 0 ? "male" : "female";

        final String aboutme = input_aboutme.getText().toString().trim();
        final String state = input_state.getText().toString().trim();
        final String country = input_country.getText().toString().trim();

        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            MultipartBody.Part aboutmemul =
                    MultipartBody.Part.createFormData("aboutme", aboutme);

            MultipartBody.Part statemul =
                    MultipartBody.Part.createFormData("state", state);

            MultipartBody.Part gendermul =
                    MultipartBody.Part.createFormData("gender", gender);


            MultipartBody.Part countrymul =
                    MultipartBody.Part.createFormData("country", country);


            MultipartBody.Part userName =
                    MultipartBody.Part.createFormData("username", userName1);

            MultipartBody.Part email =
                    MultipartBody.Part.createFormData("email", input_usermail.getText().toString());
            MultipartBody.Part password =
                    MultipartBody.Part.createFormData("password", password1);
            MultipartBody.Part profile_image = null;

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part uploadimage = null;
            if (imagePath != null) {
                File file = new File(imagePath);
                if (file.exists()) {
                    RequestBody requestFile =
                            RequestBody.create(
                                    null,
                                    file
                            );
                    uploadimage = MultipartBody.Part.createFormData("uploadimage", file.getName(), requestFile);
                }
            }


            // finally, execute the request
            Call<EventResponse> call = apiService.update_profile(uploadimage, userName, email, password, profile_image, aboutmemul, statemul, countrymul, gendermul);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        if (sigInResponse.getResult().equals("success")) {
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
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
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
