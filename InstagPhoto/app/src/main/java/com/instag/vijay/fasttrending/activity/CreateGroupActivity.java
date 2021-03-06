package com.instag.vijay.fasttrending.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.BusinessPageModel;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instag.vijay.fasttrending.EditProfile.getFilePathFromURI;
import static com.instag.vijay.fasttrending.EditProfile.getRealPathFromURI;

/**
 * Created by vijay on 19/4/18.
 */

public class CreateGroupActivity extends AppCompatActivity {

    private Activity activity;
    private EditText input_business_title;
    private ImageView iv_business_image;
    private MaterialSpinner sp_add_group_type;
    private String imagePath;
    private ArrayList<CategoryMain> categoryMains;
    private boolean view;
    TextView btnCreate;
    TextView tvTitleGroup;

    PreferenceUtil preferenceUtil;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Create Group");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        tvTitleGroup = findViewById(R.id.tvTitleGroup);
        input_business_title = findViewById(R.id.input_business_title);
        iv_business_image = findViewById(R.id.iv_business_image);
        sp_add_group_type = findViewById(R.id.sp_add_group_type);

        iv_business_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessPageModel.getEmail() != null && businessPageModel.getEmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                    ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                    if (pendingPermissions.size() > 0) {
                        PermissionCheck.requestPermission(activity, pendingPermissions, 301);
                        return;
                    }
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(4, 4)
                            .start(activity);
                }

            }
        });

        btnCreate = findViewById(R.id.btnGroupCreate);

        TextView btnCancel = findViewById(R.id.btnGroupCancel);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (businessPageModel != null) {
                    PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                    if (businessPageModel.getEmail() != null && businessPageModel.getEmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                        createBusinessPage();
                    } else {
                        Toast.makeText(activity, "Followed", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } else
                    createBusinessPage();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (businessPageModel != null) {
                    PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                    if (businessPageModel.getEmail() != null && businessPageModel.getEmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                        deleteGroupBusiness();
                    } else {
                        onBackPressed();
                    }
                } else
                    onBackPressed();
            }
        });

        categoryMains = new ArrayList<>();

        if (getIntent() != null) {
            businessPageModel = getIntent().getParcelableExtra("model");
            view = false;
            if (businessPageModel != null) {
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                if (businessPageModel.getEmail() != null && businessPageModel.getEmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                    btnCreate.setText("Update");
                    btnCancel.setText("Delete");
                    actionBar.setTitle("Update Group");
                    tvTitleGroup.setText("Update Group");
                } else {
                    view = true;
                    actionBar.setTitle("Group Details");
                    tvTitleGroup.setText("Group Details");
                    if (businessPageModel.isFollow()) {
                        btnCreate.setText("UnFollow");
                    } else
                        btnCreate.setText("Follow");
                    btnCancel.setText("Cancel");
                    input_business_title.setEnabled(false);

                    ArrayList<String> list = new ArrayList<>();
                    list.add(businessPageModel.getCategory());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_add_group_type.setAdapter(adapter);
                    sp_add_group_type.setPaddingSafe(0, 0, 0, 0);
                    sp_add_group_type.setSelection(1);
                    sp_add_group_type.setEnabled(false);

                }

                input_business_title.setText(businessPageModel.getTitle());
                if (businessPageModel.getImage() != null) {
                    Glide.with(activity)
                            .load("http://www.xooads.com/FEELOUTADMIN/img/upload/" + businessPageModel.getImage())
                            .asBitmap()
                            .into(iv_business_image);
                }
            }
        }

        if (!view)
            setCategoryList();

        //getBusinessCategory();
    }

    BusinessPageModel businessPageModel;

    private void getBusinessCategory() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                String usermail = preferenceUtil.getUserMailId();
                Call<ArrayList<CategoryMain>> call = service.getcategory(usermail);
                call.enqueue(new Callback<ArrayList<CategoryMain>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CategoryMain>> call, Response<ArrayList<CategoryMain>> response) {
                        categoryMains = response.body();
                        if (categoryMains != null) {
                            setCategoryList();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryMain>> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
                        } else {
                            message = "Failed";
                        }
                        setCategoryList();
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                setCategoryList();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = result.getUri();
                try {
                    String filePath = getRealPathFromURI(activity, selectedImageUri);
                    if (filePath == null) {
                        filePath = getFilePathFromURI(activity, selectedImageUri);
                    }
                    if (!TextUtils.isEmpty(filePath)) {
                        File destination = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
                        copy(new File(filePath), destination);
                        imagePath = destination.getPath();
                        Glide.with(activity)
                                .load(new File(imagePath))
                                .asBitmap()
                                .into(iv_business_image);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void setCategoryList() {

        try {
            ArrayList<String> list = new ArrayList<>();
            for (CategoryMain categoryMain : categoryMains) {
                list.add(categoryMain.getName());
            }
            list.add("Entertainment");
            list.add("Sports");
            list.add("Job");

            int sPos = -1;
            for (int i = 0; i < list.size(); i++) {
                String categoryMain = list.get(i);
                if (businessPageModel != null && businessPageModel.getCategory() != null && businessPageModel.getCategory().equalsIgnoreCase(categoryMain)) {
                    sPos = i;

                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_add_group_type.setAdapter(adapter);
            sp_add_group_type.setPaddingSafe(0, 0, 0, 0);
            sp_add_group_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (sPos != -1) {
                sp_add_group_type.setSelection(sPos + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void createBusinessPage() {
        try {
            String title = input_business_title.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                input_business_title.setError("Please enter group name");
                return;
            }
            input_business_title.setError(null);

            if (sp_add_group_type.getSelectedItemPosition() <= 0) {
                sp_add_group_type.setError("Please select group type");
                sp_add_group_type.requestFocus();
                return;
            }

            String category = sp_add_group_type.getSelectedItem().toString();
            File file = null;
            if (imagePath != null && !new File(imagePath).exists()) {
                Toast.makeText(activity, "Could not find the filepath of the selected file",
                        Toast.LENGTH_LONG).show();
                return;
            } else if (imagePath != null) {
                file = new File(imagePath);
            }

            if (CommonUtil.isNetworkAvailable(activity)) {
                initProgress("Uploading please wait...");
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestFile = null;
                if (file != null)
                    requestFile = RequestBody.create(
                            null,
                            file
                    );
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                MultipartBody.Part titlemul =
                        MultipartBody.Part.createFormData("title", title);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part uploaded_file = null;
                if (requestFile != null)
                    uploaded_file = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                MultipartBody.Part type =
                        MultipartBody.Part.createFormData("type", category);
                MultipartBody.Part txtEmail =
                        MultipartBody.Part.createFormData("email", new PreferenceUtil(activity).getUserMailId());
                MultipartBody.Part update = null;
                if (businessPageModel != null)
                    update = MultipartBody.Part.createFormData("update", businessPageModel.getTitle());

                Call<EventResponse> call = apiService.add_group(titlemul, uploaded_file, type, txtEmail, update);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                        closeProgress();
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(sigInResponse.getMessage()))
                                    Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                MainActivity.mainActivity.refresh(5);
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
                        closeProgress();
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteGroupBusiness() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Deleting....");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.delete_group_page(usermail, businessPageModel.getTitle(), "true");
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        closeProgress();
                        MainActivity.mainActivity.refresh(5);
                        onBackPressed();


                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    String message = t.toString();
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else {
                        message = "Failed";
                    }
                    closeProgress();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private ProgressDialog progressDoalog;

    private void initProgress(String title) {
        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage(title);
            progressDoalog.setTitle(activity.getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
            progressDoalog = null;
        }
    }

    private void closeProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.hide();
        progressDoalog = null;
    }
}
