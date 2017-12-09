package com.xr.vijay.tranzpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.vijay.tranzpost.adapter.UploadPickerPopUp;
import com.xr.vijay.tranzpost.model.DocumentModel;
import com.xr.vijay.tranzpost.retrofit.ApiClient;
import com.xr.vijay.tranzpost.retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by vijay on 6/12/17.
 */

public class TruckSettings extends AppCompatActivity implements View.OnClickListener {

    AppCompatEditText edtTruckModel;
    AppCompatEditText edtTruckWeight;
    CheckBox chkOpen, chkClosed, chkLocal, chkState, chkNational;
    CardView card_view1, card_view2, card_view3, card_view4;
    ImageView ivTruckPhoto, ivTruckPhoto2, ivTruckPhoto3, ivTruckPhoto4;
    String truckphoto1, truckphoto2, truckphoto3, truckphoto4;
    public final static int CAMERA_REQUEST_TRUCKS1 = 1;
    public final static int CAMERA_REQUEST_TRUCKS2 = 2;
    public final static int CAMERA_REQUEST_TRUCKS3 = 3;
    public final static int CAMERA_REQUEST_TRUCKS4 = 4;

    public final static int PICK_IMAGE_REQUEST_TRUCKS1 = 5;
    public final static int PICK_IMAGE_REQUEST_TRUCKS2 = 6;
    public final static int PICK_IMAGE_REQUEST_TRUCKS3 = 7;
    public final static int PICK_IMAGE_REQUEST_TRUCKS4 = 8;
    PreferenceUtil preferenceUtil;
    Activity activity;
    String body_type = "";
    String permit_type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_settings);
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Truck Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        edtTruckModel = findViewById(R.id.edtTruckModel);
        edtTruckWeight = findViewById(R.id.edtTruckWeight);
        chkOpen = findViewById(R.id.chkOpen);
        chkClosed = findViewById(R.id.chkClosed);
        chkLocal = findViewById(R.id.chkLocal);
        chkState = findViewById(R.id.chkState);
        chkNational = findViewById(R.id.chkNational);

        card_view1 = findViewById(R.id.card_view1);
        card_view2 = findViewById(R.id.card_view2);
        card_view3 = findViewById(R.id.card_view3);
        card_view4 = findViewById(R.id.card_view4);

        ivTruckPhoto = findViewById(R.id.ivTruckPhoto);
        ivTruckPhoto2 = findViewById(R.id.ivTruckPhoto2);
        ivTruckPhoto3 = findViewById(R.id.ivTruckPhoto3);
        ivTruckPhoto4 = findViewById(R.id.ivTruckPhoto4);
        findViewById(R.id.btnSaveTruckSettings).setOnClickListener(this);
        card_view1.setOnClickListener(this);
        card_view2.setOnClickListener(this);
        card_view3.setOnClickListener(this);
        card_view4.setOnClickListener(this);

        ivTruckPhoto.setOnClickListener(this);
        ivTruckPhoto2.setOnClickListener(this);
        ivTruckPhoto3.setOnClickListener(this);
        ivTruckPhoto4.setOnClickListener(this);


        card_view1.setOnClickListener(this);
        card_view2.setOnClickListener(this);
        card_view3.setOnClickListener(this);
        card_view4.setOnClickListener(this);

        chkOpen.setOnClickListener(this);
        chkClosed.setOnClickListener(this);

        chkLocal.setOnClickListener(this);
        chkState.setOnClickListener(this);
        chkNational.setOnClickListener(this);
/*
        edtTruckModel.setText(preferenceUtil.getTruckModel());
        edtTruckWeight.setText("" + preferenceUtil.getTruckWeight());
        if (preferenceUtil.getTruckBodyType().equalsIgnoreCase("Open")) {
            chkOpen.setChecked(true);
        } else if (preferenceUtil.getTruckBodyType().equalsIgnoreCase("Closed")) {
            chkClosed.setChecked(true);
        }

        if (preferenceUtil.getTruckPermitType().equalsIgnoreCase("Local")) {
            chkLocal.setChecked(true);
        } else if (preferenceUtil.getTruckPermitType().equalsIgnoreCase("State")) {
            chkState.setChecked(true);
        } else if (preferenceUtil.getTruckPermitType().equalsIgnoreCase("National")) {
            chkNational.setChecked(true);
        }

        truckphoto1 = preferenceUtil.getTruckPhoto1();
        truckphoto2 = preferenceUtil.getTruckPhoto2();
        truckphoto3 = preferenceUtil.getTruckPhoto3();
        truckphoto4 = preferenceUtil.getTruckPhoto4();

        if (!TextUtils.isEmpty(truckphoto1)) {
            ivTruckPhoto.setVisibility(View.VISIBLE);
            ivTruckPhoto.setImageResource(0);
            ivTruckPhoto.setColorFilter(null);
            if (truckphoto1.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(truckphoto1).into(ivTruckPhoto);
            } else {
                File destination = new File(truckphoto1);
                Picasso.with(this).load(destination).into(ivTruckPhoto);
            }
        }
        if (!TextUtils.isEmpty(truckphoto2)) {
            ivTruckPhoto2.setImageResource(0);
            ivTruckPhoto2.setColorFilter(null);
            ivTruckPhoto2.setVisibility(View.VISIBLE);
            if (truckphoto2.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(truckphoto2).into(ivTruckPhoto2);
            } else {
                File destination = new File(truckphoto2);
                Picasso.with(this).load(destination).into(ivTruckPhoto2);
            }
        }
        if (!TextUtils.isEmpty(truckphoto3)) {
            ivTruckPhoto3.setVisibility(View.VISIBLE);
            ivTruckPhoto3.setImageResource(0);
            ivTruckPhoto3.setColorFilter(null);
            if (truckphoto3.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(truckphoto3).into(ivTruckPhoto3);
            } else {
                File destination = new File(truckphoto3);
                Picasso.with(this).load(destination).into(ivTruckPhoto3);
            }
        }
        if (!TextUtils.isEmpty(truckphoto4)) {
            ivTruckPhoto4.setVisibility(View.VISIBLE);
            ivTruckPhoto4.setImageResource(0);
            ivTruckPhoto4.setColorFilter(null);
            if (truckphoto4.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(truckphoto4).into(ivTruckPhoto4);
            } else {
                File destination = new File(truckphoto4);
                Picasso.with(this).load(destination).into(ivTruckPhoto4);
            }
        }*/

    }


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.card_view1:
                case R.id.ivTruckPhoto:
                    showAttachmentPopup(view, "ivTruckPhoto1");
                    break;
                case R.id.card_view2:
                case R.id.ivTruckPhoto2:
                    showAttachmentPopup(view, "ivTruckPhoto2");
                    break;
                case R.id.card_view3:
                case R.id.ivTruckPhoto3:
                    showAttachmentPopup(view, "ivTruckPhoto3");
                    break;
                case R.id.card_view4:
                case R.id.ivTruckPhoto4:
                    showAttachmentPopup(view, "ivTruckPhoto4");
                    break;

                case R.id.chkOpen:
                    if (chkOpen.isChecked()) {
                        body_type = "Open";
                        chkClosed.setChecked(false);
                    }
                    break;
                case R.id.chkClosed:
                    if (chkClosed.isChecked()) {
                        body_type = "Closed";
                        chkOpen.setChecked(false);
                    }
                    break;

                case R.id.chkLocal:
                    if (chkLocal.isChecked()) {
                        permit_type = "Local";
                        chkState.setChecked(false);
                        chkNational.setChecked(false);
                    }
                    break;
                case R.id.chkState:
                    if (chkState.isChecked()) {
                        permit_type = "State";
                        chkLocal.setChecked(false);
                        chkNational.setChecked(false);
                    }
                    break;
                case R.id.chkNational:
                    if (chkNational.isChecked()) {
                        permit_type = "National";
                        chkState.setChecked(false);
                        chkLocal.setChecked(false);
                    }
                    break;
                case R.id.btnSaveTruckSettings:
                    if (edtTruckModel.getText().toString().length() == 0) {
                        edtTruckModel.setError("Enter Truck model");
                    } else if (edtTruckWeight.getText().toString().length() == 0) {
                        edtTruckWeight.setError("Enter Truck weight");
                    } else if (!chkOpen.isChecked() && !chkClosed.isChecked()) {
                        Toast.makeText(TruckSettings.this, "Please choose Body type", Toast.LENGTH_LONG).show();
                    } else if (!chkLocal.isChecked() && !chkNational.isChecked() && !chkState.isChecked()) {
                        Toast.makeText(TruckSettings.this, "Please choose permit", Toast.LENGTH_LONG).show();
                    } else {
                        updateTruckSettings();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTruckSettings() {
        try {
            if (Utils.isNetworkAvailable(activity)) {
                findViewById(R.id.pbmanageDocument).setVisibility(View.VISIBLE);
                MultipartBody.Part truck_photo1 = null;
                MultipartBody.Part truck_photo2 = null;
                MultipartBody.Part truck_photo3 = null;
                MultipartBody.Part truck_photo4 = null;

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                if (!TextUtils.isEmpty(truckphoto1)) {
                    RequestBody requestFileimagePathPAN =
                            RequestBody.create(
                                    null,
                                    new File(truckphoto1)
                            );
                    // MultipartBody.Part is used to send also the actual file name
                    truck_photo1 =
                            MultipartBody.Part.createFormData("truck_photo1", new File(truckphoto1).getName(), requestFileimagePathPAN);

                }

                if (!TextUtils.isEmpty(truckphoto2)) {
                    RequestBody requestFileimagePathAddress =
                            RequestBody.create(
                                    null,
                                    new File(truckphoto2)
                            );

                    truck_photo2 =
                            MultipartBody.Part.createFormData("truck_photo2", new File(truckphoto2).getName(), requestFileimagePathAddress);

                }


                if (!TextUtils.isEmpty(truckphoto3)) {
                    RequestBody requestFileimagePathVisiting =
                            RequestBody.create(
                                    null,
                                    new File(truckphoto3)
                            );
                    truck_photo3 =
                            MultipartBody.Part.createFormData("truck_photo3", new File(truckphoto3).getName(), requestFileimagePathVisiting);

                }
                if (!TextUtils.isEmpty(truckphoto4)) {
                    RequestBody requestFileimagePathRC =
                            RequestBody.create(
                                    null,
                                    new File(truckphoto4)
                            );
                    truck_photo4 =
                            MultipartBody.Part.createFormData("truck_photo4", new File(truckphoto4).getName(), requestFileimagePathRC);

                }
                MultipartBody.Part truck_model =
                        MultipartBody.Part.createFormData("truck_model", edtTruckModel.getText().toString().trim());
                MultipartBody.Part mobile =
                        MultipartBody.Part.createFormData("mobile", preferenceUtil.getUserRegisteredNumber());

                MultipartBody.Part body_type =
                        MultipartBody.Part.createFormData("body_type", this.body_type);

                MultipartBody.Part truck_weight =
                        MultipartBody.Part.createFormData("truck_weight", edtTruckWeight.getText().toString().trim());


                MultipartBody.Part permit_type =
                        MultipartBody.Part.createFormData("permit_type", this.permit_type);


                // finally, execute the request
                Call<DocumentModel> call = apiService.add_truck(truck_model, mobile, body_type, truck_weight, permit_type, truck_photo1, truck_photo2, truck_photo3, truck_photo4);
                call.enqueue(new Callback<DocumentModel>() {
                    @Override
                    public void onResponse(Call<DocumentModel> call, retrofit2.Response<DocumentModel> response) {

                        findViewById(R.id.pbmanageDocument).setVisibility(View.GONE);

                        DocumentModel documentModel = response.body();
                        if (documentModel != null) {
                            if (documentModel.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(documentModel.getMessage()))
                                    Toast.makeText(activity, documentModel.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, documentModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<DocumentModel> call, Throwable t) {
                        // Log error here since request failed
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

    private android.widget.PopupWindow showPopup;

    private void showAttachmentPopup(View view, String ivTruckPhoto3) {
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {

            String[] menuText = getResources().getStringArray(R.array.attachment);
            int[] images = {R.drawable.ic_camera_black_24dp, R.drawable.ic_photo_library_black_24dp};
            GridView menuGrid;
            showPopup = PopupWindow.getPopup(TruckSettings.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.upload_picker_layout, null);
            menuGrid = (GridView) popupView.findViewById(R.id.grid_layout);
            UploadPickerPopUp adapter = new UploadPickerPopUp(this, menuText, images, ivTruckPhoto3);
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
        if (requestCode == PICK_IMAGE_REQUEST_TRUCKS1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    truckphoto1 = destination.getPath();
                    ivTruckPhoto.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivTruckPhoto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_TRUCKS1 && resultCode == RESULT_OK && data != null) {

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
                truckphoto1 = destination.getPath();
                ivTruckPhoto.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivTruckPhoto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_TRUCKS2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    truckphoto2 = destination.getPath();
                    ivTruckPhoto2.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivTruckPhoto2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_TRUCKS2 && resultCode == RESULT_OK && data != null) {

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
                truckphoto2 = destination.getPath();
                ivTruckPhoto2.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivTruckPhoto2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_TRUCKS3 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    truckphoto3 = destination.getPath();
                    ivTruckPhoto3.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivTruckPhoto3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_TRUCKS3 && resultCode == RESULT_OK && data != null) {

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
                truckphoto3 = destination.getPath();
                ivTruckPhoto3.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivTruckPhoto3);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_TRUCKS4 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    truckphoto4 = destination.getPath();
                    ivTruckPhoto4.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivTruckPhoto4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_TRUCKS4 && resultCode == RESULT_OK && data != null) {

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
                truckphoto4 = destination.getPath();
                ivTruckPhoto4.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivTruckPhoto4);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
