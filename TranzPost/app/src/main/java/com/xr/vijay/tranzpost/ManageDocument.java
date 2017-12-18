package com.xr.vijay.tranzpost;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.vijay.tranzpost.adapter.UploadPickerPopUp;
import com.xr.vijay.tranzpost.model.EventResponse;
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
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by vijay on 4/12/17.
 */

public class ManageDocument extends AppCompatActivity {
    RadioGroup radiotype;
    private android.widget.PopupWindow showPopup;
    private final int PERMISSION_REQUEST_CODE = 101;
    private final int STORAGE_PERMISSION_REQUEST_CODE = 102;
    public final static int CAMERA_REQUEST_PAN = 2;
    public final static int PICK_IMAGE_REQUEST_PAN = 3;
    public final static int CAMERA_REQUEST_VISITING = 6;
    public final static int PICK_IMAGE_REQUEST_VISITING = 7;
    public final static int CAMERA_REQUEST_RC = 8;
    public final static int PICK_IMAGE_REQUEST_RC = 9;

    public final static int CAMERA_REQUEST_DrivingLicense = 10;
    public final static int PICK_IMAGE_REQUEST_DrivingLicense = 11;
    public final static int CAMERA_REQUEST_VoterOrAdhaar = 12;
    public final static int PICK_IMAGE_REQUEST_VoterOrAdhaar = 13;
    public final static int CAMERA_REQUEST_address = 14;
    public final static int PICK_IMAGE_REQUEST_address = 15;
    public final static int CAMERA_REQUEST_Insurance = 16;
    public final static int PICK_IMAGE_REQUEST_Insurance = 17;
    public final static int CAMERA_REQUEST_Permit = 18;
    public final static int PICK_IMAGE_REQUEST_Permit = 19;
    public final static int CAMERA_REQUEST_Photo = 20;
    public final static int PICK_IMAGE_REQUEST_Photo = 21;


    public final int PAN_UPLOAD = 150;
    public final int ADDRESS_UPLOAD = 151;
    public final int RC_FRONT_UPLOAD = 152;
    public final int RC_BACK_UPLOAD = 153;
    public final int DRIVING_LICENSE_UPLOAD = 154;
    public final int INSURANCE_UPLOAD = 155;
    public final int PERMIT_UPLOAD = 156;
    public final int PHOTO_UPLOAD = 157;
    public final int VOTER_UPLOAD = 158;

    String imagePathPAN = "";
    String imagePathDriving = "";
    String imagePathVoterAadhaar = "";
    String imagePathInsurence = "";
    String imagePathPermit = "";
    String imagePathAddress = "";
    String imagePathPhoto = "";
    String imagePathRcFront = "";
    String imagePathRCBack = "";
    TextInputEditText et_transport_firm;
    ImageView ivloadCamDrivingLicense, ivloadCamVoterOrAdhaar, ivloadCamProof, ivloadCamInsurance, ivloadCamPermit, ivloadPhoto;
    ImageView ivloadRcFront, ivloadPan, ivloadRCBack;
    TextInputLayout til_transport_firm;
    String userType;
    Activity activity;
    PreferenceUtil preferenceUtil;

    LinearLayout llPanCard, llDrivingLicense, llVoterOrAdhaar, llAddressProof, llVisiting, llInsurance, llPermit, llPhoto;

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
        llPanCard = findViewById(R.id.llPanCard);
        llDrivingLicense = findViewById(R.id.llDrivingLicense);
        llVoterOrAdhaar = findViewById(R.id.llVoterOrAdhaar);
        llAddressProof = findViewById(R.id.llAddressProof);
        llInsurance = findViewById(R.id.llInsurance);
        llPermit = findViewById(R.id.llPermit);
        llPhoto = findViewById(R.id.llPhoto);
        llVisiting = findViewById(R.id.llVisiting);

        llVisiting.setVisibility(View.GONE);
        ivloadRcFront = findViewById(R.id.ivloadVisiting);
        ivloadPan = findViewById(R.id.ivloadCamPanCard);
        ivloadRCBack = findViewById(R.id.ivloadRcBook);

        ivloadCamDrivingLicense = findViewById(R.id.ivloadCamDrivingLicense);
        ivloadCamVoterOrAdhaar = findViewById(R.id.ivloadCamVoterOrAdhaar);
        ivloadCamProof = findViewById(R.id.ivloadCamProof);
        ivloadCamInsurance = findViewById(R.id.ivloadCamInsurance);
        ivloadCamPermit = findViewById(R.id.ivloadCamPermit);
        ivloadPhoto = findViewById(R.id.ivloadPhoto);

        final PreferenceUtil preferenceUtil = new PreferenceUtil(ManageDocument.this);
        imagePathPAN = preferenceUtil.getPan();
        imagePathAddress = preferenceUtil.getProof();
        imagePathRcFront = preferenceUtil.getRcFront();
        imagePathRCBack = preferenceUtil.getRCBack();

        imagePathVoterAadhaar = preferenceUtil.getVoter();
        imagePathPhoto = preferenceUtil.getProfile();
        imagePathPermit = preferenceUtil.getPermit();
        imagePathDriving = preferenceUtil.getDriveLicense();
        imagePathInsurence = preferenceUtil.getInsurance();

        til_transport_firm = findViewById(R.id.til_transport_firm);
        et_transport_firm = findViewById(R.id.et_transport_firm);
        final RadioButton radioTransporter = findViewById(R.id.radioTransporter);
        final RadioButton radioTruckOwner = findViewById(R.id.radioTruckOwner);
        final RadioButton radioTransporterandTruckOwner = findViewById(R.id.radioTransporterandTruckOwner);

        llDrivingLicense.setVisibility(View.VISIBLE);
        llVoterOrAdhaar.setVisibility(View.VISIBLE);
        llAddressProof.setVisibility(View.VISIBLE);
        llPhoto.setVisibility(View.VISIBLE);

        llPanCard.setVisibility(View.GONE);
        llInsurance.setVisibility(View.GONE);
        llVisiting.setVisibility(View.GONE);
        llPermit.setVisibility(View.GONE);
        userType = "Transporter";
        radiotype = (RadioGroup) findViewById(R.id.radiotype);
        radiotype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioTransporter) {
                    llDrivingLicense.setVisibility(View.VISIBLE);
                    llVoterOrAdhaar.setVisibility(View.VISIBLE);
                    llAddressProof.setVisibility(View.VISIBLE);
                    llPhoto.setVisibility(View.VISIBLE);
                    userType = "Transporter";
                    llPanCard.setVisibility(View.GONE);
                    llInsurance.setVisibility(View.GONE);
                    llVisiting.setVisibility(View.GONE);
                    llPermit.setVisibility(View.GONE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioTruckOwner) {
                    llDrivingLicense.setVisibility(View.VISIBLE);
                    llVoterOrAdhaar.setVisibility(View.VISIBLE);
                    llAddressProof.setVisibility(View.GONE);
                    llPhoto.setVisibility(View.VISIBLE);
                    userType = "TruckOwner";
                    llPanCard.setVisibility(View.VISIBLE);
                    llInsurance.setVisibility(View.VISIBLE);
                    llVisiting.setVisibility(View.VISIBLE);
                    llPermit.setVisibility(View.VISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioTransporterandTruckOwner) {
                    llDrivingLicense.setVisibility(View.VISIBLE);
                    llVoterOrAdhaar.setVisibility(View.VISIBLE);
                    llAddressProof.setVisibility(View.GONE);
                    llPhoto.setVisibility(View.VISIBLE);
                    userType = "TransporterandTruckOwner";
                    llPanCard.setVisibility(View.VISIBLE);
                    llInsurance.setVisibility(View.VISIBLE);
                    llVisiting.setVisibility(View.VISIBLE);
                    llPermit.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btnsaveandverify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allowVerify = false;
                if (userType.equalsIgnoreCase("Transporter")) {
                    if (TextUtils.isEmpty(imagePathDriving)) {
                        Toast.makeText(ManageDocument.this, "Add Driving License", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathVoterAadhaar)) {
                        Toast.makeText(ManageDocument.this, "Add Adhar card or Voter id document", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathAddress)) {
                        Toast.makeText(ManageDocument.this, "Add Address document", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathPhoto)) {
                        Toast.makeText(ManageDocument.this, "Add Profile", Toast.LENGTH_LONG).show();
                    } else {
                        allowVerify = true;
                    }
                } else {

                    if (TextUtils.isEmpty(imagePathPAN)) {
                        Toast.makeText(ManageDocument.this, "Add Pan document", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathVoterAadhaar)) {
                        Toast.makeText(ManageDocument.this, "Add Adhar card or Voter id document", Toast.LENGTH_LONG).show();
                    } else if (et_transport_firm.getText().toString().length() == 0 && llVisiting.getVisibility() == View.VISIBLE) {
                        til_transport_firm.setError("please provide valid transport firm");
                        et_transport_firm.requestFocus();
                    } else if (TextUtils.isEmpty(imagePathRcFront)) {
                        Toast.makeText(ManageDocument.this, "Add RcBook Front page", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathRCBack)) {
                        Toast.makeText(ManageDocument.this, "Add RcBook Back page", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathInsurence)) {
                        Toast.makeText(ManageDocument.this, "Add Insurance document", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathPermit)) {
                        Toast.makeText(ManageDocument.this, "Add Permit document", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(imagePathPhoto)) {
                        Toast.makeText(ManageDocument.this, "Add Profile", Toast.LENGTH_LONG).show();
                    } else if (userType.equalsIgnoreCase("TruckOwner")) {
                        allowVerify = true;
                    } else if (userType.equalsIgnoreCase("TransporterandTruckOwner")) {
                        if (TextUtils.isEmpty(imagePathDriving)) {
                            Toast.makeText(ManageDocument.this, "Add Driving License", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        allowVerify = true;
                    }
                }
                if (allowVerify) {
                    verifyAndUpload();
                }
            }
        });


        findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "PAN");
            }
        });

        findViewById(R.id.editPan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "PAN");
            }
        });


        findViewById(R.id.card_viewDrivingLicense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "DrivingLicense");
            }
        });

        findViewById(R.id.editDriveLicense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "DrivingLicense");
            }
        });


        findViewById(R.id.card_viewVoterOrAdhaar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "VoterOrAdhaar");
            }
        });

        findViewById(R.id.editAdhaar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "VoterOrAdhaar");
            }
        });


        findViewById(R.id.card_view_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "address");
            }
        });

        findViewById(R.id.editAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "address");
            }
        });

        findViewById(R.id.card_view_visiting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_transport_firm.getText().toString().length() == 0) {
                    et_transport_firm.setError("Please enter transport firm name");
                    et_transport_firm.requestFocus();
                } else {
                    showAttachmentPopup(view, "VISITING");
                    et_transport_firm.setError(null);
                }
            }
        });

        findViewById(R.id.editRcBok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_transport_firm.getText().toString().length() == 0) {
                    et_transport_firm.setError("Please enter transport firm name");
                    et_transport_firm.requestFocus();
                } else {
                    showAttachmentPopup(view, "VISITING");
                    et_transport_firm.setError(null);
                }
            }
        });

        findViewById(R.id.card_view_RcBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_transport_firm.getText().toString().length() == 0) {
                    et_transport_firm.setError("Please enter transport firm name");
                    et_transport_firm.requestFocus();
                } else {
                    showAttachmentPopup(view, "RC");
                    et_transport_firm.setError(null);
                }
            }
        });

        findViewById(R.id.card_viewInsurance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Insurance");
            }
        });

        findViewById(R.id.editInsurance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Insurance");
            }
        });


        findViewById(R.id.card_viewPermit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Permit");
            }
        });

        findViewById(R.id.editPermitCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Permit");
            }
        });


        findViewById(R.id.card_viewPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Photo");
            }
        });

        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachmentPopup(view, "Photo");
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

        if (!TextUtils.isEmpty(imagePathDriving)) {
            ivloadCamDrivingLicense.setVisibility(View.VISIBLE);
            if (imagePathDriving.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathDriving).into(ivloadCamDrivingLicense);
            } else {
                File destination = new File(imagePathDriving);
                Picasso.with(this).load(destination).into(ivloadCamDrivingLicense);
            }
        }

        if (!TextUtils.isEmpty(imagePathInsurence)) {
            ivloadCamInsurance.setVisibility(View.VISIBLE);
            if (imagePathInsurence.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathInsurence).into(ivloadCamInsurance);
            } else {
                File destination = new File(imagePathInsurence);
                Picasso.with(this).load(destination).into(ivloadCamInsurance);
            }
        }

        if (!TextUtils.isEmpty(imagePathPermit)) {
            ivloadCamPermit.setVisibility(View.VISIBLE);
            if (imagePathPermit.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathPermit).into(ivloadCamPermit);
            } else {
                File destination = new File(imagePathPermit);
                Picasso.with(this).load(destination).into(ivloadCamPermit);
            }
        }

        if (!TextUtils.isEmpty(imagePathPhoto)) {
            ivloadPhoto.setVisibility(View.VISIBLE);
            if (imagePathPhoto.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathPhoto).into(ivloadPhoto);
            } else {
                File destination = new File(imagePathPhoto);
                Picasso.with(this).load(destination).into(ivloadPhoto);
            }
        }

        if (!TextUtils.isEmpty(imagePathVoterAadhaar)) {
            ivloadCamVoterOrAdhaar.setVisibility(View.VISIBLE);
            if (imagePathVoterAadhaar.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathPAN).into(ivloadCamVoterOrAdhaar);
            } else {
                File destination = new File(imagePathVoterAadhaar);
                Picasso.with(this).load(destination).into(ivloadCamVoterOrAdhaar);
            }
        }

        if (!TextUtils.isEmpty(preferenceUtil.getfirm())) {
            et_transport_firm.setText(preferenceUtil.getfirm());
        }

        if (!TextUtils.isEmpty(imagePathAddress)) {
            ivloadCamProof.setVisibility(View.VISIBLE);
            if (imagePathAddress.contains("http://xrsoftwares.com/TranzPost")) {
                Picasso.with(this).load(imagePathAddress).into(ivloadCamProof);
            } else {
                File destination = new File(imagePathAddress);
                Picasso.with(this).load(destination).into(ivloadCamProof);
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
            if (preferenceUtil.gettype().equalsIgnoreCase("Transporter")) {
                radioTransporter.setChecked(true);
                llDrivingLicense.setVisibility(View.VISIBLE);
                llVoterOrAdhaar.setVisibility(View.VISIBLE);
                llAddressProof.setVisibility(View.VISIBLE);
                llPhoto.setVisibility(View.VISIBLE);

                llPanCard.setVisibility(View.GONE);
                llInsurance.setVisibility(View.GONE);
                llVisiting.setVisibility(View.GONE);
                llPermit.setVisibility(View.GONE);
            } else if (preferenceUtil.gettype().equalsIgnoreCase("TruckOwner")) {
                radioTruckOwner.setChecked(true);
            } else if (preferenceUtil.gettype().equalsIgnoreCase("TransporterandTruckOwner")) {
                radioTransporterandTruckOwner.setChecked(true);
            }
        }

    }

    private void verifyAndUpload() {

        try {
            if (Utils.isNetworkAvailable(activity)) {
                findViewById(R.id.pbmanageDocument).setVisibility(View.VISIBLE);

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                final PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                // finally, execute the request
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
                String currDate = df.format(calendar.getTime());

                Call<EventResponse> call = apiService.saveandverify(userType, preferenceUtil.getUserRegisteredNumber(), et_transport_firm.getText().toString().trim(), currDate);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {

                        findViewById(R.id.pbmanageDocument).setVisibility(View.GONE);

                        EventResponse documentModel = response.body();
                        if (documentModel != null) {
                            if (documentModel.getResult().equals("success")) {
                                if (!TextUtils.isEmpty(documentModel.getMessage()))
                                    Toast.makeText(activity, documentModel.getMessage(), Toast.LENGTH_SHORT).show();

                                if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporter) {
                                    preferenceUtil.setType("Transporter");
                                    preferenceUtil.setfirm("");
                                    preferenceUtil.setPan("");
                                    preferenceUtil.setInsurance("");
                                    preferenceUtil.setPermit("");
                                    preferenceUtil.setRCFront("");
                                    preferenceUtil.setRCBack("");
                                } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTruckOwner) {
                                    preferenceUtil.setType("TruckOwner");
                                    preferenceUtil.setDriveLicense("");
                                    preferenceUtil.setProof("");
                                    preferenceUtil.setfirm(et_transport_firm.getText().toString());
                                } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporterandTruckOwner) {
                                    preferenceUtil.setType("TransporterandTruckOwner");
                                    preferenceUtil.setProof("");
                                    preferenceUtil.setfirm(et_transport_firm.getText().toString());
                                }

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
                    imagePathPAN = destination.getPath();

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathPAN);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "PanCard");
                    startActivityForResult(intent, PAN_UPLOAD);
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
//                ivloadPan.setVisibility(View.VISIBLE);

                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathPAN);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "PanCard");
                startActivityForResult(intent, PAN_UPLOAD);

//                Picasso.with(this).load(destination).into(ivloadPan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_DrivingLicense && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathDriving = destination.getPath();
//                    ivloadCamDrivingLicense.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadCamDrivingLicense);
                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathDriving);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "DrivingLicense");
                    startActivityForResult(intent, DRIVING_LICENSE_UPLOAD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_DrivingLicense && resultCode == RESULT_OK && data != null) {

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
                imagePathDriving = destination.getPath();
//                ivloadCamDrivingLicense.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadCamDrivingLicense);

                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathDriving);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "DrivingLicense");
                startActivityForResult(intent, DRIVING_LICENSE_UPLOAD);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_VoterOrAdhaar && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathVoterAadhaar = destination.getPath();
//                    ivloadCamVoterOrAdhaar.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadCamVoterOrAdhaar);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathVoterAadhaar);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "VoterIdOrAdhar");
                    startActivityForResult(intent, VOTER_UPLOAD);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_VoterOrAdhaar && resultCode == RESULT_OK && data != null) {

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
                imagePathVoterAadhaar = destination.getPath();
//                ivloadCamVoterOrAdhaar.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadCamVoterOrAdhaar);
                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathVoterAadhaar);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "VoterIdOrAdhar");
                startActivityForResult(intent, VOTER_UPLOAD);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_address && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathAddress = destination.getPath();
//                    ivloadCamProof.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadCamProof);
                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathAddress);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "Address");
                    startActivityForResult(intent, ADDRESS_UPLOAD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_address && resultCode == RESULT_OK && data != null) {

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
//                ivloadCamProof.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadCamProof);

                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathAddress);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "Address");
                startActivityForResult(intent, ADDRESS_UPLOAD);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_VISITING && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathRcFront = destination.getPath();
//                    ivloadRcFront.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadRcFront);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathRcFront);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "RcBookFront");
                    startActivityForResult(intent, RC_FRONT_UPLOAD);

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
//                ivloadRcFront.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadRcFront);

                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathRcFront);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "RcBookFront");
                startActivityForResult(intent, RC_FRONT_UPLOAD);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_RC && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathRCBack = destination.getPath();
//                    ivloadRCBack.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadRCBack);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathRCBack);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "RcBookBack");
                    startActivityForResult(intent, RC_BACK_UPLOAD);

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
//                ivloadRCBack.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadRCBack);
                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathRCBack);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "RcBookBack");
                startActivityForResult(intent, RC_BACK_UPLOAD);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_Insurance && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathInsurence = destination.getPath();
//                    ivloadCamInsurance.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadCamInsurance);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathInsurence);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "Insurance");
                    startActivityForResult(intent, INSURANCE_UPLOAD);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_Insurance && resultCode == RESULT_OK && data != null) {

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
                imagePathInsurence = destination.getPath();
//                ivloadCamInsurance.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadCamInsurance);

                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathInsurence);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "Insurance");
                startActivityForResult(intent, INSURANCE_UPLOAD);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_Permit && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathPermit = destination.getPath();
//                    ivloadCamPermit.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadCamPermit);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathPermit);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "PermitCopy");
                    startActivityForResult(intent, PERMIT_UPLOAD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_Permit && resultCode == RESULT_OK && data != null) {

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
                imagePathPermit = destination.getPath();
//                ivloadCamPermit.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadCamPermit);
                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathPermit);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "PermitCopy");
                startActivityForResult(intent, PERMIT_UPLOAD);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_Photo && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
                    imagePathPhoto = destination.getPath();
//                    ivloadPhoto.setVisibility(View.VISIBLE);
//                    Picasso.with(this).load(destination).into(ivloadPhoto);

                    Intent intent = new Intent(activity, Document_Preview.class);
                    intent.putExtra("path", imagePathPhoto);
                    intent.putExtra("user_Type", userType);
                    intent.putExtra("company", et_transport_firm.getText().toString().trim());
                    intent.putExtra("uploadType", "Profile");
                    startActivityForResult(intent, PHOTO_UPLOAD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST_Photo && resultCode == RESULT_OK && data != null) {

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
                imagePathPhoto = destination.getPath();
//                ivloadPhoto.setVisibility(View.VISIBLE);
//                Picasso.with(this).load(destination).into(ivloadPhoto);
                Intent intent = new Intent(activity, Document_Preview.class);
                intent.putExtra("path", imagePathPhoto);
                intent.putExtra("user_Type", userType);
                intent.putExtra("company", et_transport_firm.getText().toString().trim());
                intent.putExtra("uploadType", "Profile");
                startActivityForResult(intent, PHOTO_UPLOAD);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PAN_UPLOAD:
                    preferenceUtil.setPan(imagePathPAN);
                    ivloadPan.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathPAN)).into(ivloadPan);

                    break;
                case ADDRESS_UPLOAD:
                    preferenceUtil.setProof(imagePathAddress);
                    ivloadCamProof.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathAddress)).into(ivloadCamProof);
                    break;
                case DRIVING_LICENSE_UPLOAD:
                    preferenceUtil.setDriveLicense(imagePathDriving);
                    ivloadCamDrivingLicense.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathDriving)).into(ivloadCamDrivingLicense);
                    break;
                case RC_BACK_UPLOAD:
                    preferenceUtil.setRCBack(imagePathRCBack);
                    ivloadRCBack.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathRCBack)).into(ivloadRCBack);
                    break;
                case RC_FRONT_UPLOAD:
                    preferenceUtil.setRCFront(imagePathRcFront);
                    ivloadRcFront.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathRcFront)).into(ivloadRcFront);
                    break;
                case PHOTO_UPLOAD:
                    preferenceUtil.setProfile(imagePathPhoto);
                    ivloadPhoto.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathPhoto)).into(ivloadPhoto);
                    break;
                case VOTER_UPLOAD:
                    preferenceUtil.setVoter(imagePathVoterAadhaar);
                    ivloadCamVoterOrAdhaar.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathVoterAadhaar)).into(ivloadCamVoterOrAdhaar);
                    break;
                case PERMIT_UPLOAD:
                    preferenceUtil.setPermit(imagePathPermit);
                    ivloadCamVoterOrAdhaar.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathPermit)).into(ivloadCamPermit);
                    break;
                case INSURANCE_UPLOAD:
                    ivloadCamInsurance.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(new File(imagePathInsurence)).into(ivloadCamInsurance);
                    preferenceUtil.setInsurance(imagePathInsurence);
                    break;

            }

            if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporter) {
                preferenceUtil.setType("Transporter");
                preferenceUtil.setfirm("");
            } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTruckOwner) {
                preferenceUtil.setType("TruckOwner");
                preferenceUtil.setfirm(et_transport_firm.getText().toString().trim());
            } else if (radiotype.getCheckedRadioButtonId() == R.id.radioTransporterandTruckOwner) {
                preferenceUtil.setType("TransporterandTruckOwner");
                preferenceUtil.setfirm(et_transport_firm.getText().toString().trim());
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
}
