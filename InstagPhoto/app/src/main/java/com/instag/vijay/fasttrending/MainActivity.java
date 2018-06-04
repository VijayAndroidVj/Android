package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.instag.vijay.fasttrending.activity.ComingSoon;
import com.instag.vijay.fasttrending.activity.CropActivity;
import com.instag.vijay.fasttrending.activity.SearchActivity;
import com.instag.vijay.fasttrending.adapter.PagerAdapter;
import com.instag.vijay.fasttrending.chat.ChatListActivity;
import com.instag.vijay.fasttrending.fragments.ChatFragment;
import com.instag.vijay.fasttrending.fragments.NewsfeedFragment;
import com.instag.vijay.fasttrending.fragments.PhotoFragment;
import com.instag.vijay.fasttrending.fragments.ProfileFragment;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instag.vijay.fasttrending.fragments.PhotoFragment.getFilePathFromURI;
import static com.instag.vijay.fasttrending.fragments.PhotoFragment.getRealPathFromURI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView searchEditText;
    public EditText edtsearchview;
    private Activity activity;
    private View iv_actionbar_settings;
    private TextView badge_noti;
    private View iv_actionbar_peoples;
    private View iv_actionbar_peoples_group;
    public static MainActivity mainActivity;
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private View flnewsfeed;
    private View flsearch;
    private View flvideo;
    private View flcamera;
    private View flheart;
    private View flprofile;
    private ImageView ivHome;
    private ImageView ivSearch;
    private ImageView ivVideo;
    private ImageView ivChat;
    private ImageView ivFav;
    private ImageView ivProfile;
    private ColorStateList selectedColorStateList;
    private ColorStateList unselectedColorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        mainActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        final View ivLogo = view.findViewById(R.id.ivLogo);
        final TextView name = view.findViewById(R.id.txtAppName);
        view.findViewById(R.id.iv_actionbar_noti).setOnClickListener(this);
        iv_actionbar_settings = view.findViewById(R.id.iv_actionbar_settings);
        badge_noti = view.findViewById(R.id.badge_noti);
        iv_actionbar_peoples = view.findViewById(R.id.iv_actionbar_peoples);
        iv_actionbar_peoples_group = view.findViewById(R.id.iv_actionbar_peoples_group);
        searchEditText = view.findViewById(R.id.searchview);
        edtsearchview = view.findViewById(R.id.edtsearchview);
//        searchEditText.setTextColor(getResources().getColor(R.color.black));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.grey1));
        iv_actionbar_peoples.setOnClickListener(this);
        iv_actionbar_peoples_group.setOnClickListener(this);
        iv_actionbar_settings.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        selectedColorStateList = CommonUtil.getColorStateList(activity, R.color.colorPrimary);
        unselectedColorStateList = CommonUtil.getColorStateList(activity, R.color.grey);
        name.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        name.setText(getString(R.string.app_name));

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);

        final BottomBar bottomBar = findViewById(R.id.bottomBar);
        viewPager = findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), 6);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        edtsearchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (viewPager.getCurrentItem() == 3) {
                    ChatFragment chatFragment = (ChatFragment) adapter.getItem(3);
                    chatFragment.filter(s.toString());
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                bottomBar.selectTabAtPosition(position);
                iv_actionbar_settings.setVisibility(View.GONE);
                iv_actionbar_peoples.setVisibility(View.GONE);
                iv_actionbar_peoples_group.setVisibility(View.GONE);
                edtsearchview.setVisibility(View.GONE);
                if (position == 1) {
//                    SearchFragment searchFragment = (SearchFragment) adapter.getItem(1);
//                    searchFragment.refreshItems();
                    name.setVisibility(View.GONE);
                    ivLogo.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.VISIBLE);
                    searchEditText.setClickable(true);
                    searchEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, SearchActivity.class);
                            activity.startActivity(intent);
                        }
                    });
                } else if (position == 3) {
//                    SearchFragment searchFragment = (SearchFragment) adapter.getItem(1);
//                    searchFragment.refreshItems();
                    name.setVisibility(View.GONE);
                    ivLogo.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.GONE);
                    edtsearchview.setVisibility(View.VISIBLE);
                    iv_actionbar_peoples.setVisibility(View.VISIBLE);
                    iv_actionbar_peoples_group.setVisibility(View.VISIBLE);
                    //edtsearchview.requestFocus();
                } else {
                    name.setVisibility(View.VISIBLE);
                    ivLogo.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.GONE);
                    name.setText(getString(R.string.app_name));
                    if (position == 5) {
//                        PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                        name.setText("My Profile");
                        iv_actionbar_settings.setVisibility(View.VISIBLE);
                    }

                }
                // updateSelectionItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        flnewsfeed = findViewById(R.id.flnewsfeed);
        flsearch = findViewById(R.id.flsearch);
        flvideo = findViewById(R.id.flvideo);
        flcamera = findViewById(R.id.flcamera);
        flheart = findViewById(R.id.flheart);
        flprofile = findViewById(R.id.flprofile);

        ivHome = findViewById(R.id.ivHome);
        ivSearch = findViewById(R.id.ivSearch);
        ivVideo = findViewById(R.id.ivVideo);
        ivChat = findViewById(R.id.ivChat);
        ivFav = findViewById(R.id.ivFav);
        ivProfile = findViewById(R.id.ivProfile);

        /*updateSelectionItem(0);

        flnewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        flsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        flvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        flcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        flheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });

        flprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(5);
            }
        });*/
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {

                    viewPager.setCurrentItem(0);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_search) {

                    viewPager.setCurrentItem(1);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_video) {

                    viewPager.setCurrentItem(2);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_cam) {
                    viewPager.setCurrentItem(3);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_fav) {
                    viewPager.setCurrentItem(4);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_account) {
                    viewPager.setCurrentItem(5);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
            }
        });
        bottomBar.selectTabAtPosition(0);

        String token = FirebaseInstanceId.getInstance().getToken();
        registerFcmToken(token, activity);

        updateNotiCount();

        Intent intent = getIntent();
        if (intent != null) {
            boolean moveToNotification = intent.getBooleanExtra("notification", false);
            if (moveToNotification) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        moveToNotification();
                    }
                }, 2500);
            }
        }

    }

    private void updateSelectionItem(int position) {
        try {

            flnewsfeed.setBackgroundColor(Color.parseColor("#FFFFFF"));
            flsearch.setBackgroundColor(Color.parseColor("#FFFFFF"));
            flvideo.setBackgroundColor(Color.parseColor("#FFFFFF"));
            flcamera.setBackgroundColor(Color.parseColor("#FFFFFF"));
            flheart.setBackgroundColor(Color.parseColor("#FFFFFF"));
            flprofile.setBackgroundColor(Color.parseColor("#FFFFFF"));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivHome.setImageTintList(unselectedColorStateList);
                ivSearch.setImageTintList(unselectedColorStateList);
                ivVideo.setImageTintList(unselectedColorStateList);
                ivChat.setImageTintList(unselectedColorStateList);
                ivFav.setImageTintList(unselectedColorStateList);
                ivProfile.setImageTintList(unselectedColorStateList);

                switch (position) {
                    case 0:
                        ivHome.setImageTintList(selectedColorStateList);
                        flnewsfeed.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 1:
                        ivSearch.setImageTintList(selectedColorStateList);
                        flsearch.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 2:
                        ivVideo.setImageTintList(selectedColorStateList);
                        flvideo.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 3:
                        ivChat.setImageTintList(selectedColorStateList);
                        flcamera.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 4:
                        ivFav.setImageTintList(selectedColorStateList);
                        flheart.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 5:
                        flprofile.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        ivProfile.setImageTintList(selectedColorStateList);
                        break;
                }
            } else {
                switch (position) {
                    case 1:
                        flsearch.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 2:
                        flvideo.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 3:
                        flcamera.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 4:
                        flheart.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                    case 5:
                        flprofile.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerFcmToken(String token, Context context) {
        PreferenceUtil preferenceUtil = new PreferenceUtil(context);
        String usermail = preferenceUtil.getUserMailId();
        if (CommonUtil.isNetworkAvailable(context) && !TextUtils.isEmpty(usermail)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<EventResponse> call = service.register_fcm(usermail, token);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        Log.w("Fcm token Register", patientDetails.getMessage());
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
                    Log.w("Fcm token Register", message);
                }
            });
        } else {
            Log.w("Fcm token Register", "Check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static ProgressDialog progressDoalog;

    public static void showProgress(Activity activity) {

        try {
            if (progressDoalog == null) {
                progressDoalog = new ProgressDialog(activity);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Please wait....");
                progressDoalog.setTitle(activity.getString(R.string.app_name));
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.dismiss();
        progressDoalog = null;
    }

    private void moveToNotification() {
        PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
        preferenceUtil.putInt(Keys.NOTI_COUNT, 0);
        updateNotiCount();
        Intent intent = new Intent(activity, NotificationActivity.class);
        startActivity(intent);
    }

    private Uri selectedImageUri;
    public final static int CAMERA_REQUEST = 201;
    public final static int REQUEST_CHOOSE_CAM = 202;

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
            case REQUEST_CHOOSE_CAM:
                // If request is cancelled, the result arrays are empty.
                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
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
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }

                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, REQUEST_CHOOSE_CAM);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
                        startActivityForResult(intent, PhotoFragment.UPLOAD);
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Intent intent = new Intent(activity, UpLoadImagePreview.class);
//            intent.putExtra("path", destination.getAbsolutePath());
//            startActivityForResult(intent, UPLOAD);

                startActivity(CropActivity.callingIntent(activity, selectedImageUri));
            } else if (requestCode == PhotoFragment.UPLOAD && resultCode == Activity.RESULT_OK) {
                viewPager.setCurrentItem(0);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLogo:
                ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    //gelleryIntent();
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
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }

                } else {
                    PermissionCheck.requestPermission(activity, pendingPermissions, REQUEST_CHOOSE_CAM);
                }

                break;
            case R.id.iv_actionbar_noti:
                moveToNotification();
                break;
            case R.id.iv_actionbar_peoples:
                Intent in = new Intent(activity, ChatListActivity.class);
                startActivity(in);
                break;
            case R.id.iv_actionbar_peoples_group:
                in = new Intent(activity, ComingSoon.class);
                startActivity(in);
                break;
            case R.id.iv_actionbar_settings:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_settings) {
                            Intent in = new Intent(activity, EditProfile.class);
                            startActivity(in);
                        } else if (item.getItemId() == R.id.menu_logout) {
                            showMeetingtAlert(activity);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;
        }
    }

    public void showMeetingtAlert(final Activity activity) {

        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Logout")
                .setContentText("Are you sure want to logout?")
//                .setCustomImage(R.drawable.app_logo_back)
                .setCancelText("No").setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                        preferenceUtil.logout();
                        Intent in = new Intent(activity, SplashScreen.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
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
        txtMessage.setText("Are you sure want to logout?");
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                preferenceUtil.logout();
                Intent in = new Intent(activity, SplashScreen.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
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


    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
            return;
        }
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
        } else {
            back_pressed = System.currentTimeMillis();
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void refresh(int movePos) {
        try {
            if (movePos > -1) {
                viewPager.setCurrentItem(movePos);
            }
            NewsfeedFragment newsfeedFragment = (NewsfeedFragment) adapter.getItem(0);
            ProfileFragment profileFragment = (ProfileFragment) adapter.getItem(5);
            newsfeedFragment.refreshItems();
            profileFragment.getMyPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshProfile() {
        try {
            ProfileFragment profileFragment = (ProfileFragment) adapter.getItem(5);
            profileFragment.getMyPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshChat() {
        try {
            ChatFragment chatFragment = (ChatFragment) adapter.getItem(3);
            chatFragment.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNotiCount() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                    int count = preferenceUtil.getInt(Keys.NOTI_COUNT, 0);
                    if (count > 0) {
                        badge_noti.setVisibility(View.VISIBLE);
                        badge_noti.setText(String.valueOf(count));
                    } else {
                        badge_noti.setVisibility(View.GONE);
                    }
                }
            }, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
