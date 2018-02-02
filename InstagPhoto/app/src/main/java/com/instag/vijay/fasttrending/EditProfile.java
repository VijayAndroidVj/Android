package com.instag.vijay.fasttrending;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.adapter.UploadPickerPopUp;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.joooonho.SelectableRoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 23/12/17.
 */

public class EditProfile extends AppCompatActivity {

    EditText input_username, input_usermail, input_userpassword, input_aboutme, input_state, input_country;
    SelectableRoundedImageView ivProfile1;
    Activity activity;
    PreferenceUtil preferenceUtil;
    public final static int CAMERA_REQUEST_PAN = 2;
    public final static int PICK_IMAGE_REQUEST_PAN = 3;
    String imagePath;
    boolean removePhoto;
    Spinner spinner;

    public void launchCameraIntent(int code) {
        if (showPopup != null) {
            showPopup.dismiss();
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, code);
    }

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
        preferenceUtil = new PreferenceUtil(activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar_cyclic);
        spinner = findViewById(R.id.spinner);
        input_username = findViewById(R.id.input_username);
        input_usermail = findViewById(R.id.input_useremail);
        input_userpassword = findViewById(R.id.input_userpassword);
        input_aboutme = findViewById(R.id.input_userbiography);
        input_state = findViewById(R.id.input_userstate);
        input_country = findViewById(R.id.input_usercountry);
        ivProfile1 = findViewById(R.id.ivProfile1);
        input_username.setText(preferenceUtil.getUserName());
        input_usermail.setText(preferenceUtil.getUserMailId());
        input_userpassword.setText(preferenceUtil.getUserPassword());

        input_aboutme.setText(preferenceUtil.getUserAboutMe());
        input_state.setText(preferenceUtil.getUserState());
        input_country.setText(preferenceUtil.getUserCountry());
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.spinner_item);
        spinner.setAdapter(adapter);

        if (preferenceUtil.getUserGender().equalsIgnoreCase("male")) {
//            radioButtonmale.setChecked(true);
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
//            radioButtonfemale.setChecked(true);
        }

        String profileImage = preferenceUtil.getMyProfile();
        if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
//            imagePath = profileImage;

            Glide.with(activity)
                    .load(profileImage)
                    .asBitmap()
                    .into(ivProfile1);
        }
        input_username.clearFocus();
        input_userpassword.clearFocus();

        ivProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 9)
                        .start(activity);
            }
        });
        findViewById(R.id.lblechanagephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 9)
                        .start(activity);
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        findViewById(R.id.btn_delete_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Delete Account", "Are you sure want to delete your account? Please note that this action cannot be undone and all your data(including photos, comments,likes) will be deleted.", true);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlert("Logout", "Are you sure want to logout?", false);
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

            final PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            MultipartBody.Part email =
                    MultipartBody.Part.createFormData("email", preferenceUtil.getUserMailId());
            MultipartBody.Part password =
                    MultipartBody.Part.createFormData("password", password1);
            MultipartBody.Part profile_image = null;
            if (!removePhoto)
                profile_image = MultipartBody.Part.createFormData("profile_image", preferenceUtil.getMyProfile());

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
                            preferenceUtil.putString(Keys.PASSWORD, password1);
                            preferenceUtil.putString(Keys.USERNAME, userName1);
                            preferenceUtil.putString(Keys.ABOUTME, aboutme);
                            preferenceUtil.putString(Keys.GENDER, gender);
                            preferenceUtil.putString(Keys.STATE, state);
                            preferenceUtil.putString(Keys.COUNTRY, country);
//                            preferenceUtil.putString(Keys.PROFILE_IMAGE, imagePath);
                            preferenceUtil.putString(Keys.PROFILE_IMAGE, sigInResponse.getServerimage());
                            if (!TextUtils.isEmpty(imagePath) && MainActivity.mainActivity != null) {
                                MainActivity.mainActivity.refresh();
                            }
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


    public void showAlert(final String title, final String message, final boolean isAccountDelete) {

        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
//                .setCustomImage(R.drawable.app_logo_back)
                .setCancelText("No").setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (isAccountDelete) {
                            deleteMyAccount();
                        } else {
                            preferenceUtil.logout();
                            Intent intent = new Intent(activity, SplashScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(activity.getString(R.string.app_name));
        txtMessage.setText(title);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (isAccountDelete) {
                    deleteMyAccount();
                } else {
                    preferenceUtil.logout();
                    Intent intent = new Intent(activity, SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        dialogView.findViewById(R.id.customDialogCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();*/
    }

    private void deleteMyAccount() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.delete_my_account(usermail);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        Toast.makeText(activity, patientDetails.getMessage(), Toast.LENGTH_SHORT).show();
                        preferenceUtil.logout();
                        Intent intent = new Intent(activity, SplashScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    progressBar.setVisibility(View.GONE);
                    String message = t.getMessage();
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else {
                        message = "Failed";
                    }
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    android.widget.PopupWindow showPopup;

    private void showAttachmentPopup(View view) {
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {

            String[] menuText = getResources().getStringArray(R.array.attachment);
            int[] images = {R.drawable.ic_camera_black_24dp, R.drawable.ic_photo_library_black_24dp, R.drawable.ic_delete_black_24dp};
            GridView menuGrid;
            showPopup = PopupWindow.getPopup(activity);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.upload_picker_layout, null);
            menuGrid = (GridView) popupView.findViewById(R.id.grid_layout);
            UploadPickerPopUp adapter = new UploadPickerPopUp(this, menuText, images);
            menuGrid.setAdapter(adapter);
            showPopup.setContentView(popupView);

            float widthPixels = getResources().getDisplayMetrics().widthPixels;

            showPopup.setWidth((int) widthPixels - 30);
            showPopup.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);
//        showPopup.showAsDropDown(view);
            showPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        } else {
            PermissionCheck.requestPermission(this, pendingPermissions, 111);
        }
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
                                .into(ivProfile1);
                        removePhoto = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                if (picturePath == null)
                    try {
                        picturePath = getRealPathFromURI(activity, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    imagePath = destination.getPath();

                    Glide.with(activity)
                            .load(new File(imagePath))
                            .asBitmap()
                            .into(ivProfile1);
                    removePhoto = false;
//                    Glide.with(activity).load(new File(imagePath)).centerCrop()
//                            .thumbnail(0.5f)
//                            .crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(ivProfile1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_PAN && resultCode == RESULT_OK && data != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
            myDir.mkdirs();
            File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                imagePath = destination.getPath();
//                ivloadPan.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(new File(imagePath))
                        .asBitmap()
                        .into(ivProfile1);

                removePhoto = false;
//                Glide.with(activity).load(new File(imagePath)).centerCrop()
//                        .thumbnail(0.5f)
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(ivProfile1);

//                Picasso.with(this).load(destination).into(ivProfile1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
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

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;

        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(column_index);
                    cursor.close();
                    return data;
                }
                cursor.close();
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void removePic() {
        try {
            if (showPopup != null) {
                showPopup.dismiss();
            }
            removePhoto = true;
            imagePath = null;
//                ivloadPan.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(R.drawable.user)
                    .asBitmap()
                    .into(ivProfile1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
