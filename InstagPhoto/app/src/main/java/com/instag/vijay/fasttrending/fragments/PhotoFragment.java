package com.instag.vijay.fasttrending.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.UpLoadImagePreview;
import com.instag.vijay.fasttrending.activity.CropActivity;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vijay on 21/11/17.
 */

public class PhotoFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    public static final int UPLOAD = 3;
    private Uri selectedImageUri;
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
                    //gelleryIntent();
                    startDialog();
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
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_VIDEO_REQUEST);
                }


            }
        });

    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("select image from?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        gelleryIntent();

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                selectedImageUri = FileProvider.getUriForFile(activity,
                                        "com.instag.vijay.fasttrending.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                                startActivityForResult(takePictureIntent, REQUEST_CHOOSE_CAM);
                            }
                        }

                       /* int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion <= Build.VERSION_CODES.KITKAT) {
                            String directory = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name) +
                                    File.separator + "Images" + File.separator;
                            if (new File(directory).exists() || new File(directory).mkdirs()) {
                                Log.i("Directory Created", directory);
                            }
                            String file = directory + "Image-" + System.currentTimeMillis() + ".jpg";
                            File newfile = new File(file);
                            try {
                                newfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            selectedImageUri = Uri.fromFile(newfile);

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                            startActivityForResult(cameraIntent, REQUEST_CHOOSE_CAM);
                        } else {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    selectedImageUri = FileProvider.getUriForFile(activity,
                                            "com.instag.vijay.fasttrending.provider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                                    startActivityForResult(takePictureIntent, REQUEST_CHOOSE_CAM);
                                }
                            }
                        }*/

                    }
                });
        myAlertDialog.show();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                // If request is cancelled, the result arrays are empty.
                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    startDialog();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_IMAGE_REQUEST);
                }

                break;
            case PICK_VIDEO_REQUEST:
                pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    cameraIntent();
                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, PICK_VIDEO_REQUEST);
                }
                break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void cameraIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);

    }

    private static final int REQUEST_CHOOSE_PHOTO = 1101;
    private static final int REQUEST_CHOOSE_CAM = 1102;

    private void gelleryIntent() {

       /* CropImage.activity().setAspectRatio(4, 4)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);*/

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, getString(R.string.title_choose_image));
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);

       /* Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
    }

    private Activity activity;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
                startActivity(CropActivity.callingIntent(getContext(), data.getData()));
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = result.getUri();

                    String filePath = getRealPathFromURI(activity, selectedImageUri);
                    if (filePath == null) {
                        filePath = getFilePathFromURI(activity, selectedImageUri);
                    }
                    if (!TextUtils.isEmpty(filePath)) {
                        File cFile = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name));
                        if (!cFile.exists()) {
                            cFile.mkdirs();
                        }
                        File destination = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
                        copy(new File(filePath), destination);

                        Intent intent = new Intent(activity, UpLoadImagePreview.class);
                        intent.putExtra("path", destination.getAbsolutePath());
                        startActivityForResult(intent, UPLOAD);
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == REQUEST_CHOOSE_CAM && resultCode == RESULT_OK) {
//            Intent intent = new Intent(activity, UpLoadImagePreview.class);
//            intent.putExtra("path", destination.getAbsolutePath());
//            startActivityForResult(intent, UPLOAD);

                startActivity(CropActivity.callingIntent(getContext(), selectedImageUri));
            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
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
            } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {

                Uri uri = data.getData();

                try {
                    onVideoCaptureSuccess(activity, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == UPLOAD && resultCode == RESULT_OK && data != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onVideoCaptureSuccess(Context context, final Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);
        if (sourceFilePath == null)
            sourceFilePath = getVidPath(uri);
        try {
            final String filePath;
            String dur = null;


            filePath = sourceFilePath;
            final File file = new File(filePath);
            // thumbFile = saveBitmap(thumbnail, "jpg", String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_thumb_folder));
            // thumbFilePath = Uri.fromFile(thumbFile).toString();
            dur = getDuration(context, uri);
            if (allowVideo) {
//                Toast.makeText(activity, "Video duration " + dur + " sec", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
                myAlertDialog.setTitle(getString(R.string.app_name));
                myAlertDialog.setMessage("Are sure want to post this video? \n " + filePath);
                myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        // do something when the OK button is clicked

                        String ext = ".mp4";
                        try {
                            ext = filePath.substring(filePath.lastIndexOf("."));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        File cFile = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name));
                        if (!cFile.exists()) {
                            cFile.mkdirs();
                        }

                        File destination = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name) + "/" + System.currentTimeMillis() + ext);
                        try {
                            copy(new File(filePath), destination);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        beginUpload(destination.getAbsolutePath());
                    }
                });
                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        // do something when the Cancel button is clicked
                    }
                });
                myAlertDialog.show();
            } else {
                Toast.makeText(activity, "Video duration should be below 30 sec", Toast.LENGTH_SHORT).show();
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
                initProgress("Uploading please wait...");
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestFile =
                        RequestBody.create(
                                null,
                                file
                        );
                MultipartBody.Part description =
                        MultipartBody.Part.createFormData("description", "");

                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                MultipartBody.Part user_mail =
                        MultipartBody.Part.createFormData("user_mail", preferenceUtil.getUserMailId());

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part image =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                MultipartBody.Part fileType =
                        MultipartBody.Part.createFormData("fileType", "2");


//                byte[] data = file.getAbsolutePath().getBytes("UTF-8");
//                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String base64 = Base64.encodeToString(b, Base64.DEFAULT);
                MultipartBody.Part videoThumb =
                        MultipartBody.Part.createFormData("videoThumb", base64);

                // finally, execute the request
                Call<EventResponse> call = apiService.insta_posts(description, image, fileType, videoThumb, user_mail);
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

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


    private boolean allowVideo = false;

    private String getDuration(Context context, Uri uri) {
        String duration = "";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        int hrs = (int) TimeUnit.MILLISECONDS.toHours(timeInMilliSec) % 24;
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(timeInMilliSec) % 60;
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(timeInMilliSec) % 60;
        if (hrs > 0)
            allowVideo = false;
        else {
            if (min <= 30) {
                allowVideo = true;
            } else {
                allowVideo = false;
            }
            duration = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
        }
        return duration;
    }

    // UPDATED!
    public String getVidPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String data = cursor.getString(column_index);
            cursor.close();
            return data;
        } else
            return null;
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

}
