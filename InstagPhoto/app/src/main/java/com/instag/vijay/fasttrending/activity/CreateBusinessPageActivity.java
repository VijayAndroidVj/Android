package com.instag.vijay.fasttrending.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.model.SubCategory;
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

public class CreateBusinessPageActivity extends AppCompatActivity {

    private Activity activity;
    private EditText input_business_title;
    private ImageView iv_business_image;
    private MaterialSpinner sp_add_business_category;
    private MaterialSpinner sp_add_business_sub_category;
    private String imagePath;
    private ArrayList<CategoryMain> categoryMains = new ArrayList<>();
    private ArrayList<SubCategory> subCategoryMains = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_business_page);
        activity = this;

        input_business_title = findViewById(R.id.input_business_title);
        iv_business_image = findViewById(R.id.iv_business_image);
        sp_add_business_category = findViewById(R.id.sp_add_business_category);
        sp_add_business_sub_category = findViewById(R.id.sp_add_business_sub_category);

        iv_business_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() > 0) {
                    PermissionCheck.requestPermission(activity, pendingPermissions, 301);
                    return;
                }
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 16)
                        .start(activity);
            }
        });
        findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBusinessPage();
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setCategoryList();
        getBusinessCategory();
        setSubCategoryList();
    }

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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_add_business_category.setAdapter(adapter);
            sp_add_business_category.setPaddingSafe(0, 0, 0, 0);
            sp_add_business_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        if (sp_add_business_category.getSelectedItemPosition() != 0) {
                            getSubCategory();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSubCategory() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<ArrayList<SubCategory>> call = service.get_business_subcategory_list(sp_add_business_category.getSelectedItem().toString());
                call.enqueue(new Callback<ArrayList<SubCategory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SubCategory>> call, Response<ArrayList<SubCategory>> response) {
                        subCategoryMains = response.body();
                        if (subCategoryMains != null) {
                            setSubCategoryList();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategory>> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                setSubCategoryList();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSubCategoryList() {

        try {
            ArrayList<String> list = new ArrayList<>();
            for (SubCategory subCategory : subCategoryMains) {
                list.add(subCategory.getCategory());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_add_business_sub_category.setAdapter(adapter);
            sp_add_business_sub_category.setPaddingSafe(0, 0, 0, 0);
            sp_add_business_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                input_business_title.setError("Please enter business title");
                return;
            }
            input_business_title.setError(null);

            if (sp_add_business_category.getSelectedItemPosition() <= 0) {
                sp_add_business_category.setError("Please select business category");
                sp_add_business_category.requestFocus();
                return;
            }
            if (sp_add_business_sub_category.getSelectedItemPosition() <= 0) {
                sp_add_business_sub_category.setError("Please select business sub category");
                sp_add_business_sub_category.requestFocus();
                return;
            }
            String category = sp_add_business_category.getSelectedItem().toString();
            String subcategory = sp_add_business_sub_category.getSelectedItem().toString();
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
                MultipartBody.Part categorymul =
                        MultipartBody.Part.createFormData("category", category);
                MultipartBody.Part subcategorymul =
                        MultipartBody.Part.createFormData("scategory", subcategory);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part uploaded_file = null;
                if (requestFile != null)
                    uploaded_file = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                MultipartBody.Part txtEmpPreAddress =
                        MultipartBody.Part.createFormData("txtEmpPreAddress", "");
                MultipartBody.Part state =
                        MultipartBody.Part.createFormData("state", preferenceUtil.getUserState());
                MultipartBody.Part city =
                        MultipartBody.Part.createFormData("city", "");
                MultipartBody.Part txtEmpContact =
                        MultipartBody.Part.createFormData("txtEmpContact", preferenceUtil.getUserContactNumber());
                MultipartBody.Part keyword =
                        MultipartBody.Part.createFormData("key", "");
                MultipartBody.Part txtEmail =
                        MultipartBody.Part.createFormData("email", new PreferenceUtil(activity).getUserMailId());

                // finally, execute the request
                Call<EventResponse> call = apiService.add_business_page(titlemul, uploaded_file, txtEmpPreAddress, state, city, txtEmpContact, categorymul, subcategorymul, keyword, txtEmail);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                        closeProgress();
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(sigInResponse.getMessage()))
                                    Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                MainActivity.mainActivity.refresh();
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
