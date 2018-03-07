package com.example.chandru.laundry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chandru.laundry.Api.Api;
import com.example.chandru.laundry.Api.ApiInterface;
import com.example.chandru.laundry.Pojo.ServiceMain;
import com.example.chandru.laundry.Pojo.cat;
import com.example.chandru.laundry.Pojo.customer;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {
    File destination;
    private ImageView image_view;
    private static int RESULT_LOAD_IMG = 12345;
    private EditText edtName;
    private EditText edtQty;
    private EditText edtPrice;
    private Spinner spinner;
    ArrayList<cat> servicelist = new ArrayList<>();
    HashMap<String, String> servicelisthashmap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Laundry item");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        image_view = (ImageView) findViewById(R.id.image_view);
        edtName = (EditText) findViewById(R.id.edtName);
        edtQty = (EditText) findViewById(R.id.edtQty);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        spinner = (Spinner) findViewById(R.id.spinner);
        findViewById(R.id.btnAddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        getServiceList();

        // Spinner Drop down elements

    }

    private void getServiceList() {
        try {
            ApiInterface apiService =
                    Api.getClient().create(ApiInterface.class);

            Call<ServiceMain> call = apiService.getService();
            call.enqueue(new Callback<ServiceMain>() {
                @Override
                public void onResponse(Call<ServiceMain> call, Response<ServiceMain> response) {
                    List<String> languages = new ArrayList<String>();

                    servicelist = response.body().getItem();
                    // Creating adapter for spinner
                    for (cat catmodel : servicelist) {
                        servicelisthashmap.put(catmodel.getCategory_name(), catmodel.getId());
                        languages.add(catmodel.getCategory_name());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_spinner_item, languages);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinner.setVisibility(View.GONE);
                            String item = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
//                spinner.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ServiceMain> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("error", t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void choose(View view) {
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(AddProduct.this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        } else {
            PermissionCheck.requestPermission(AddProduct.this, pendingPermissions, 111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(AddProduct.this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        } else {
            PermissionCheck.requestPermission(AddProduct.this, pendingPermissions, 111);
        }

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    image_view.setImageBitmap(selectedImage);

                    try {
                        String filePath = getRealPathFromURI(AddProduct.this, imageUri);
                        if (filePath == null) {
                            filePath = getFilePathFromURI(AddProduct.this, imageUri);
                        }
                        if (!TextUtils.isEmpty(filePath)) {
                            File cFile = new File(Environment.getExternalStorageDirectory(),
                                    getString(R.string.app_name));
                            if (!cFile.exists()) {
                                cFile.mkdirs();
                            }
                            destination = new File(Environment.getExternalStorageDirectory(),
                                    getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
                            copy(new File(filePath), destination);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProduct.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(AddProduct.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
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
            Cursor cursor;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        String data = cursor.getString(column_index);
                        cursor.close();
                        return data;
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
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

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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

    private ProgressDialog progressDoalog;

    private void initProgress(String title) {
        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(AddProduct.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage(title);
            progressDoalog.setTitle(getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
            progressDoalog = null;
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


    private void closeProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.hide();
        progressDoalog = null;
    }

    //demo.adityametals.com/api/add_service.php

    public void addProduct() {

        String nme = edtName.getText().toString();
        String qty = edtQty.getText().toString();
        String price = edtPrice.getText().toString();
        if (TextUtils.isEmpty(nme)) {
            edtName.setError("Enter name.");
            return;
        }
        if (TextUtils.isEmpty(qty)) {
            edtQty.setError("Enter Quantity.");
            return;
        }
        if (TextUtils.isEmpty(price)) {
            edtPrice.setError("Enter Price.");
            return;
        }
        final String relation = (String) spinner.getSelectedItem();
        if (TextUtils.isEmpty(relation)) {
            Toast.makeText(AddProduct.this, "select service", Toast.LENGTH_SHORT).show();
            return;
        }
        String service = servicelisthashmap.get(relation);

        if (destination == null || !destination.exists()) {
            Toast.makeText(AddProduct.this, "add image", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (CommonUtil.isNetworkAvailable(AddProduct.this)) {
                initProgress("Uploading please wait...");
                ApiInterface apiService =
                        Api.getClient().create(ApiInterface.class);
                RequestBody requestFile =
                        RequestBody.create(
                                null,
                                destination
                        );
                MultipartBody.Part enme =
                        MultipartBody.Part.createFormData("name", nme);

                MultipartBody.Part eqty =
                        MultipartBody.Part.createFormData("quantity", qty);
                MultipartBody.Part eprice =
                        MultipartBody.Part.createFormData("price", price);
                MultipartBody.Part eservice =
                        MultipartBody.Part.createFormData("service_", service);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part image =
                        MultipartBody.Part.createFormData("icon_image", destination.getName(), requestFile);

                // finally, execute the request
                Call<customer> call = apiService.add_product(enme, eqty, eprice, eservice, image);
                call.enqueue(new Callback<customer>() {
                    @Override
                    public void onResponse(Call<customer> call, retrofit2.Response<customer> response) {
                        closeProgress();
                        customer sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getError().equals("false")) {
                                if (!TextUtils.isEmpty(sigInResponse.getError_msg()))
                                    Toast.makeText(AddProduct.this, sigInResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(AddProduct.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                }
                                onBackPressed();
                            } else {
                                Toast.makeText(AddProduct.this, sigInResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProduct.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<customer> call, Throwable t) {
                        // Log error here since request failed
                        closeProgress();
                        Toast.makeText(AddProduct.this, "Failed add laundry item..", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddProduct.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showResponse(String response) {
        // if(mResponseTv.getVisibility() == View.GONE) {
        //     mResponseTv.setVisibility(View.VISIBLE);
        // }
        //  mResponseTv.setText(response);
    }


}
