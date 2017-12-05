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
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by vijay on 4/12/17.
 */

public class ManageDocument extends AppCompatActivity {
    RadioGroup radiotype;
    View llVisiting;
    private android.widget.PopupWindow showPopup;
    private final int PERMISSION_REQUEST_CODE = 101;
    private final int STORAGE_PERMISSION_REQUEST_CODE = 102;
    public final static int CAMERA_REQUEST_PAN = 2;
    public final static int PICK_IMAGE_REQUEST_PAN = 3;
    public final static int CAMERA_REQUEST_PROOF = 4;
    public final static int PICK_IMAGE_REQUEST_PROOF = 5;
    public final static int CAMERA_REQUEST_VISITING = 6;
    public final static int PICK_IMAGE_REQUEST_VISITING = 7;
    public final static int CAMERA_REQUEST_RC = 8;
    public final static int PICK_IMAGE_REQUEST_RC = 9;
    String imagePathPAN = "";
    String imagePathAddress = "";
    String imagePathRcFront = "";
    String imagePathRCBack = "";
    TextInputEditText et_transport_firm;
    ImageView ivloadRcFront, ivloadPan, ivloadProof, ivloadRCBack;
    TextInputLayout til_transport_firm;

    Activity activity;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_document_layout);
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Manage Document");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {
        } else {
            PermissionCheck.requestPermission(this, pendingPermissions, 111);
        }
        llVisiting = findViewById(R.id.llVisiting);
        llVisiting.setVisibility(View.GONE);
        ivloadRcFront = findViewById(R.id.ivloadVisiting);
        ivloadPan = findViewById(R.id.ivloadCamPanCard);
        ivloadProof = findViewById(R.id.ivloadCamProof);
        ivloadRCBack = findViewById(R.id.ivloadRcBook);
        final PreferenceUtil preferenceUtil = new PreferenceUtil(ManageDocument.this);
        imagePathPAN = preferenceUtil.getPan();
        imagePathAddress = preferenceUtil.getProof();
        imagePathRcFront = preferenceUtil.getVISIT();
        imagePathRCBack = preferenceUtil.getRC();
        til_transport_firm = findViewById(R.id.til_transport_firm);
        et_transport_firm = findViewById(R.id.et_transport_firm);
        final RadioButton radioTransporter = findViewById(R.id.radioTransporter);
        final RadioButton radioTruckOwner = findViewById(R.id.radioTruckOwner);
        final RadioButton radioTransporterandTruckOwner = findViewById(R.id.radioTransporterandTruckOwner);

        final RadioButton radioDrivingLicence = findViewById(R.id.radioDrivingLicence);
        final RadioButton radioAadharCard = findViewById(R.id.radioAadharCard);
        final RadioButton radioVoterId = findViewById(R.id.radioVoterId);
        final RadioButton radioPassport = findViewById(R.id.radioPassport);
        final RadioButton radioOthers = findViewById(R.id.radioOthers);

        radiotype = (RadioGroup) findViewById(R.id.radiotype);
        final RadioGroup radioAddresstype = (RadioGroup) findViewById(R.id.radioAddresstype);
        radiotype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioTransporter) {
                    llVisiting.setVisibility(View.GONE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioTruckOwner) {
                    llVisiting.setVisibility(View.VISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioTransporterandTruckOwner) {
                    llVisiting.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btnsaveandverify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(imagePathPAN)) {
                    Toast.makeText(ManageDocument.this, "Add PAN image", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(imagePathAddress)) {
                    Toast.makeText(ManageDocument.this, "Add Address image", Toast.LENGTH_LONG).show();
                } else if (et_transport_firm.getText().toString().length() == 0 && llVisiting.getVisibility() == View.VISIBLE) {
                    til_transport_firm.setError("please provide valid transport firm");
                    et_transport_firm.requestFocus();
                } else if (llVisiting.getVisibility() == View.VISIBLE && TextUtils.isEmpty(imagePathRcFront)) {
                    Toast.makeText(ManageDocument.this, "Add Visiting image", Toast.LENGTH_LONG).show();
                } else {
                    if (llVisiting.getVisibility() == View.GONE) {
                        imagePathRcFront = "";
                        imagePathRCBack = "";
                    }

                    if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporter) {
                        preferenceUtil.setType("Transporter");
                    } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTruckOwner) {
                        preferenceUtil.setType("TruckOwner");
                    } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporterandTruckOwner) {
                        preferenceUtil.setType("TransporterandTruckOwner");
                    }

                    if (radioAddresstype.getCheckedRadioButtonId() == R.id.radioDrivingLicence) {
                        preferenceUtil.setProofType("DrivingLicence");
                    } else if (radioAddresstype.getCheckedRadioButtonId() == R.id.radioAadharCard) {
                        preferenceUtil.setProofType("AadharCard");
                    } else if (radioAddresstype.getCheckedRadioButtonId() == R.id.radioVoterId) {
                        preferenceUtil.setProofType("VoterId");
                    } else if (radioAddresstype.getCheckedRadioButtonId() == R.id.radioOthers) {
                        preferenceUtil.setProofType("Others");
                    } else if (radioAddresstype.getCheckedRadioButtonId() == R.id.radioPassport) {
                        preferenceUtil.setProofType("Passport");
                    }

                    uploadDocuments();
                }
            }
        });

        findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "PAN");
            }
        });

        findViewById(R.id.card_view_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "PROOF");
            }
        });
        findViewById(R.id.card_view_visiting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "VISITING");
            }
        });

        findViewById(R.id.card_view_RcBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "RC");
            }
        });


        if (!TextUtils.isEmpty(imagePathPAN)) {
            ivloadPan.setVisibility(View.VISIBLE);
            if (imagePathPAN.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathPAN).into(ivloadPan);
            } else {
                File destination = new File(imagePathPAN);
                Picasso.with(this).load(destination).into(ivloadPan);
            }
        }

        if (!TextUtils.isEmpty(preferenceUtil.getfirm())) {
            et_transport_firm.setText(preferenceUtil.getfirm());
        }

        if (!TextUtils.isEmpty(imagePathAddress)) {
            ivloadProof.setVisibility(View.VISIBLE);
            if (imagePathAddress.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathAddress).into(ivloadProof);
            } else {
                File destination = new File(imagePathAddress);
                Picasso.with(this).load(destination).into(ivloadProof);
            }
        }

        if (!TextUtils.isEmpty(imagePathRcFront)) {
            ivloadRcFront.setVisibility(View.VISIBLE);
            if (imagePathRcFront.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathRcFront).into(ivloadRcFront);
            } else {
                File destination = new File(imagePathRcFront);
                Picasso.with(this).load(destination).into(ivloadRcFront);
            }

            if (!TextUtils.isEmpty(imagePathRCBack)) {
                ivloadRCBack.setVisibility(View.VISIBLE);
                if (imagePathRCBack.contains("http://xrsoftwares.com/TranzPost")) {
                    Picasso.with(this).load(imagePathRCBack).into(ivloadRCBack);
                } else {
                    File destination = new File(imagePathRCBack);
                    Picasso.with(this).load(destination).into(ivloadRCBack);
                }

            }
        }


        if (!TextUtils.isEmpty(preferenceUtil.gettype())) {
            if (preferenceUtil.gettype().equalsIgnoreCase("radioTransporter")) {
                radioTransporter.setChecked(true);
            } else if (preferenceUtil.gettype().equalsIgnoreCase("radioTruckOwner")) {
                radioTruckOwner.setChecked(true);
            } else if (preferenceUtil.gettype().equalsIgnoreCase("radioTransporterandTruckOwner")) {
                radioTransporterandTruckOwner.setChecked(true);
            }
        }

        if (!TextUtils.isEmpty(preferenceUtil.getProofType())) {
            if (preferenceUtil.getProofType().equalsIgnoreCase("radioDrivingLicence")) {
                radioDrivingLicence.setChecked(true);
            } else if (preferenceUtil.getProofType().equalsIgnoreCase("radioAadharCard")) {
                radioAadharCard.setChecked(true);
            } else if (preferenceUtil.getProofType().equalsIgnoreCase("radioVoterId")) {
                radioVoterId.setChecked(true);
            } else if (preferenceUtil.getProofType().equalsIgnoreCase("radioOthers")) {
                radioOthers.setChecked(true);
            } else if (preferenceUtil.getProofType().equalsIgnoreCase("radioPassport")) {
                radioPassport.setChecked(true);
            }
        }


    }


     /*
      * Begins to upload the file specified by the file path.
      */

    private void uploadDocuments() {

        try {
            if (Utils.isNetworkAvailable(activity)) {
                findViewById(R.id.pbmanageDocument).setVisibility(View.VISIBLE);
                MultipartBody.Part pan_card_path = null;
                MultipartBody.Part address_proof_path = null;
                MultipartBody.Part rcbook_front_page = null;
                MultipartBody.Part rcbook_back_page = null;


                MultipartBody.Part pan_image_serverepath = null;
                MultipartBody.Part address_proof_image_serverpath = null;
                MultipartBody.Part rcbook_front_image_serverpath = null;
                MultipartBody.Part rcbook_back_image_serverpath = null;


                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                if (imagePathPAN.contains("http://xrsoftwares.com/TranzPost")) {
                    pan_image_serverepath =
                            MultipartBody.Part.createFormData("pan_image_serverepath", imagePathPAN);

                } else {
                    RequestBody requestFileimagePathPAN =
                            RequestBody.create(
                                    null,
                                    new File(imagePathPAN)
                            );
                    // MultipartBody.Part is used to send also the actual file name
                    pan_card_path =
                            MultipartBody.Part.createFormData("pan_card_path", new File(imagePathPAN).getName(), requestFileimagePathPAN);

                }

                if (imagePathAddress.contains("http://xrsoftwares.com/TranzPost")) {
                    address_proof_image_serverpath =
                            MultipartBody.Part.createFormData("address_proof_image_serverpath", imagePathAddress);

                } else {
                    RequestBody requestFileimagePathAddress =
                            RequestBody.create(
                                    null,
                                    new File(imagePathAddress)
                            );

                    address_proof_path =
                            MultipartBody.Part.createFormData("address_proof_path", new File(imagePathAddress).getName(), requestFileimagePathAddress);

                }


                if (!TextUtils.isEmpty(imagePathRcFront) && !imagePathRcFront.contains("http://xrsoftwares.com/TranzPost")) {
                    RequestBody requestFileimagePathVisiting =
                            RequestBody.create(
                                    null,
                                    new File(imagePathRcFront)
                            );
                    rcbook_front_page =
                            MultipartBody.Part.createFormData("rcbook_front_page", new File(imagePathRcFront).getName(), requestFileimagePathVisiting);

                } else {
                    rcbook_front_image_serverpath =
                            MultipartBody.Part.createFormData("rcbook_front_image_serverpath", imagePathRcFront == null ? "" : imagePathRcFront);

                }
                if (!TextUtils.isEmpty(imagePathRCBack) && !imagePathRCBack.contains("http://xrsoftwares.com/TranzPost")) {
                    RequestBody requestFileimagePathRC =
                            RequestBody.create(
                                    null,
                                    new File(imagePathRCBack)
                            );
                    rcbook_back_page =
                            MultipartBody.Part.createFormData("rcbook_back_page", new File(imagePathRCBack).getName(), requestFileimagePathRC);

                } else {
                    rcbook_back_image_serverpath =
                            MultipartBody.Part.createFormData("rcbook_back_image_serverpath", imagePathRCBack == null ? "" : imagePathRCBack);

                }
                MultipartBody.Part number =
                        MultipartBody.Part.createFormData("number", preferenceUtil.getUserRegisteredNumber());

                MultipartBody.Part user_type =
                        MultipartBody.Part.createFormData("user_type", preferenceUtil.gettype());

                MultipartBody.Part address_proof_type =
                        MultipartBody.Part.createFormData("address_proof_type", preferenceUtil.getProofType());


                MultipartBody.Part company =
                        MultipartBody.Part.createFormData("company", et_transport_firm.getText().toString().trim());


                // finally, execute the request
                Call<DocumentModel> call = apiService.upload_document(number, user_type, pan_card_path, pan_image_serverepath, address_proof_type, address_proof_path, address_proof_image_serverpath, rcbook_front_page, rcbook_front_image_serverpath, rcbook_back_page, rcbook_back_image_serverpath, company);
                call.enqueue(new Callback<DocumentModel>() {
                    @Override
                    public void onResponse(Call<DocumentModel> call, retrofit2.Response<DocumentModel> response) {

                        if (llVisiting.getVisibility() == View.GONE) {
                            preferenceUtil.setfirm("");
                            preferenceUtil.setRCFront("");
                            preferenceUtil.setRCBack("");
                        } else {
                            preferenceUtil.setfirm(et_transport_firm.getText().toString());
                        }

                        Toast.makeText(ManageDocument.this, "Successfully Updated", Toast.LENGTH_LONG).show();
                        findViewById(R.id.pbmanageDocument).setVisibility(View.GONE);

                        DocumentModel documentModel = response.body();
                        if (documentModel != null) {
                            if (documentModel.getResult().equals("success")) {

                                preferenceUtil.setPan(documentModel.getPan_card_path());
                                preferenceUtil.setProof(documentModel.getAddress_proof_path());
                                preferenceUtil.setRCFront(documentModel.getRcbook_front_path());
                                preferenceUtil.setRCBack(documentModel.getRcbook_back_path());

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


    private void showAttachmentPopup(View view, String visiting) {
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {

            String[] menuText = getResources().getStringArray(R.array.attachment);
            int[] images = {R.drawable.ic_camera_black_24dp, R.drawable.ic_photo_library_black_24dp};
            GridView menuGrid;
            showPopup = PopupWindow.getPopup(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.upload_picker_layout, null);
            menuGrid = (GridView) popupView.findViewById(R.id.grid_layout);
            UploadPickerPopUp adapter = new UploadPickerPopUp(this, menuText, images, visiting);
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
        if (requestCode == PICK_IMAGE_REQUEST_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathPAN = destination.getPath();
                    ivloadPan.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivloadPan);
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
                imagePathPAN = destination.getPath();
                ivloadPan.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivloadPan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_PROOF && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathAddress = destination.getPath();
                    ivloadProof.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivloadProof);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_PROOF && resultCode == RESULT_OK && data != null) {

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
                imagePathAddress = destination.getPath();
                ivloadProof.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivloadProof);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_VISITING && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathRcFront = destination.getPath();
                    ivloadRcFront.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivloadRcFront);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_VISITING && resultCode == RESULT_OK && data != null) {

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
                imagePathRcFront = destination.getPath();
                ivloadRcFront.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivloadRcFront);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_RC && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathRCBack = destination.getPath();
                    ivloadRCBack.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(destination).into(ivloadRCBack);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_RC && resultCode == RESULT_OK && data != null) {

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
                imagePathRCBack = destination.getPath();
                ivloadRCBack.setVisibility(View.VISIBLE);
                Picasso.with(this).load(destination).into(ivloadRCBack);


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
