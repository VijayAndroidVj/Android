package com.xr.vijay.tranzpost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xr.vijay.tranzpost.model.DocumentModel;
import com.xr.vijay.tranzpost.model.EventResponse;
import com.xr.vijay.tranzpost.retrofit.ApiClient;
import com.xr.vijay.tranzpost.retrofit.ApiInterface;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by vijay on 9/12/17.
 */

public class Document_Preview extends AppCompatActivity {
    Activity activity;
    String imagePath;
    String uploadType;
    String user_Type, company;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_preview);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Manage Document");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        // MUST use app context to avoid memory leak!
// load with fresco
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

//// or load with glide
//        BigImageViewer.initialize(GlideImageLoader.with(this));

        if (getIntent() != null && getIntent().getExtras() != null) {
            imagePath = getIntent().getStringExtra("path");
            uploadType = getIntent().getStringExtra("uploadType");
            user_Type = getIntent().getStringExtra("user_Type");
            company = getIntent().getStringExtra("company");
            photoView.setImageURI(Uri.parse(imagePath));
            actionBar.setTitle(uploadType);

        }

        findViewById(R.id.saveDoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDoc();
            }
        });

    }

    private void uploadDoc() {

        try {
            if (Utils.isNetworkAvailable(activity) && !TextUtils.isEmpty(imagePath)) {
                findViewById(R.id.pbmanageDocument).setVisibility(View.VISIBLE);

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                RequestBody imageRequestBody =
                        RequestBody.create(
                                null,
                                new File(imagePath)
                        );
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part uploadimage =
                        MultipartBody.Part.createFormData("uploadimage", new File(imagePath).getName(), imageRequestBody);

                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                MultipartBody.Part mobile =
                        MultipartBody.Part.createFormData("mobile", preferenceUtil.getUserRegisteredNumber());

                MultipartBody.Part user_type =
                        MultipartBody.Part.createFormData("user_type", this.user_Type);
                MultipartBody.Part uploadDocumentType =
                        MultipartBody.Part.createFormData("uploadDocumentType", this.uploadType);

                MultipartBody.Part company =
                        MultipartBody.Part.createFormData("company", this.company);

                // finally, execute the request
                Call<EventResponse> call = apiService.upload_document(mobile, user_type, uploadDocumentType, company, uploadimage);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {

                        findViewById(R.id.pbmanageDocument).setVisibility(View.GONE);

                        EventResponse documentModel = response.body();
                        if (documentModel != null) {
                            if (documentModel.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(documentModel.getMessage()))
                                    Toast.makeText(activity, documentModel.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(activity, documentModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
