package com.instag.vijay.instagphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.instag.vijay.instagphoto.retrofit.ApiClient;
import com.instag.vijay.instagphoto.retrofit.ApiInterface;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by vijay on 25/11/17.
 */

public class UpLoadImagePreview extends AppCompatActivity {

    private Activity activity;
    private String uploadFile = "";
    private EditText edtDes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);
        activity = this;

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.ulpoadactionbar, null);
        final View uploadbtn = view.findViewById(R.id.iv_actionbar_noti);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);

        ScaleImageView imageView = findViewById(R.id.imageDialog);
        edtDes = findViewById(R.id.edtDescription);
        if (getIntent() != null && getIntent().getExtras() != null) {
            uploadFile = getIntent().getStringExtra("path");
            if (!TextUtils.isEmpty(uploadFile)) {
                Bitmap bitmap = BitmapFactory.decodeFile(uploadFile);
                if (bitmap != null)
                    imageView.setImageBitmap(bitmap);
            }
        }

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadbtn.startAnimation(buttonClick);
                if (!TextUtils.isEmpty(uploadFile)) {
//                    new UploadFileToServer(new File(uploadFile), edtDes.getText().toString().trim()).execute();
                    beginUpload(uploadFile);
                }
            }
        });

    }


        /*
  * Begins to upload the file specified by the file path.
  */

    private void beginUpload(final String locaPath) {
        if (locaPath == null || !new File(locaPath).exists()) {
            Toast.makeText(activity, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }

        File file = new File(locaPath);
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestFile =
                        RequestBody.create(
                                null,
                                file
                        );
                MultipartBody.Part description =
                        MultipartBody.Part.createFormData("description", edtDes.getText().toString().trim());

                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                MultipartBody.Part user_mail =
                        MultipartBody.Part.createFormData("user_mail", preferenceUtil.getUserMailId());

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part image =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                // finally, execute the request
                Call<EventResponse> call = apiService.insta_posts(description, image, user_mail);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {

                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(sigInResponse.getMessage()))
                                    Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                MainActivity.dismissProgress();
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
//        upload(file);
       /* TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, getString(R.string.app_name) + "/" + file.getName(),
                file);
        *//*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         *//*
        observer.setTransferListener(new UploadListener(reportsModel));*/
            } else {
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        File destination;
        String description;

        UploadFileToServer(File destination, String description) {
            this.destination = destination;
            this.description = description;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            MainActivity.showProgress(activity);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverAddress + "insta_posts.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                File sourceFile = new File(destination);

                // Adding file data to http body
                entity.addPart("image", new FileBody(destination));
//                entity.addPart("image", new FileBody(destination2));
//                entity.addPart("image3", new FileBody(destination3));


                System.out.println("image" + destination);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                entity.addPart("description", new StringBody(description));
                entity.addPart("user_mail", new StringBody(preferenceUtil.getUserMailId()));


                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Log.e(TAG, "Response from server: " + result);
            MainActivity.dismissProgress();
            System.out.println("Response from server: " + result);
            Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

            // showing the server response in an alert dialog


//            edt_date.getText().clear();
//            edt_time.getText().clear();
//            edt_title.getText().clear();
//            edt_content.getText().clear();
//            imf_set_image.setImageDrawable(null);
//            edt_title.requestFocus();

        }

    }*/
}
