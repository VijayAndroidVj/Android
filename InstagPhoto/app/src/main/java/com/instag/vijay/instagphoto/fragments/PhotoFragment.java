package com.instag.vijay.instagphoto.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instag.vijay.instagphoto.PermissionCheck;
import com.instag.vijay.instagphoto.R;
import com.instag.vijay.instagphoto.UpLoadImagePreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vijay on 21/11/17.
 */

public class PhotoFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CAMRA_IMAGE_REQUEST = 2;
    private static final int UPLOAD = 3;

    private static PhotoFragment photoFragment;

    public static PhotoFragment getInstance() {
        if (photoFragment == null) {
            photoFragment = new PhotoFragment();
        }
        return photoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photofragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        view.findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    gelleryIntent();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_IMAGE_REQUEST);
                }


            }
        });

        view.findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    cameraIntent();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_CAMRA_IMAGE_REQUEST);
                }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                // If request is cancelled, the result arrays are empty.
                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    gelleryIntent();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_IMAGE_REQUEST);
                }

                break;
            case PICK_CAMRA_IMAGE_REQUEST:
                pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    cameraIntent();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_CAMRA_IMAGE_REQUEST);
                }
                break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_CAMRA_IMAGE_REQUEST);
    }

    private void gelleryIntent() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private Activity activity;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);

                    Intent intent = new Intent(activity, UpLoadImagePreview.class);
                    intent.putExtra("path", destination.getAbsolutePath());
                    startActivityForResult(intent, UPLOAD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_CAMRA_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
//                    beginUpload(destination.getAbsolutePath());
                Intent intent = new Intent(activity, UpLoadImagePreview.class);
                intent.putExtra("path", destination.getAbsolutePath());
                startActivityForResult(intent, UPLOAD);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == UPLOAD && resultCode == RESULT_OK && data != null) {
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
