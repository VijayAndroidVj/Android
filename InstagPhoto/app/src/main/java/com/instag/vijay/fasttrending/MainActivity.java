package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.instag.vijay.fasttrending.adapter.PagerAdapter;
import com.instag.vijay.fasttrending.fragments.SearchFragment;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static EditText searchEditText;
    private Activity activity;
    private View iv_actionbar_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        final TextView name = (TextView) view.findViewById(R.id.txtAppName);
        view.findViewById(R.id.iv_actionbar_noti).setOnClickListener(this);
        iv_actionbar_settings = view.findViewById(R.id.iv_actionbar_settings);
        searchEditText = (EditText) view.findViewById(R.id.searchview);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        iv_actionbar_settings.setOnClickListener(this);
        searchEditText.clearFocus();

        name.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        name.setText(getString(R.string.app_name));

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), 5);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position);
                iv_actionbar_settings.setVisibility(View.GONE);
                if (position == 1) {
                    name.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.VISIBLE);
                } else {
                    name.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.GONE);
                    name.setText(getString(R.string.app_name));
                    if (position == 4) {
                        PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                        name.setText(preferenceUtil.getUserName());
                        iv_actionbar_settings.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
                } else if (tabId == R.id.tab_cam) {
                    viewPager.setCurrentItem(2);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_fav) {
                    viewPager.setCurrentItem(3);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_account) {
                    viewPager.setCurrentItem(4);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
            }
        });
        bottomBar.selectTabAtPosition(0);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchFragment searchFragment = (SearchFragment) adapter.getItem(1);
                    searchFragment.refreshItems(searchEditText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        String token = FirebaseInstanceId.getInstance().getToken();
        registerFcmToken(token, activity);

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

    public static void registerFcmToken(String token, Context context) {
        if (CommonUtil.isNetworkAvailable(context)) {

            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(context);

            String usermail = preferenceUtil.getUserMailId();
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
                    String message = t.getMessage();
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
        Intent intent = new Intent(activity, NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_actionbar_noti:
                moveToNotification();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
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

        alertDialog.show();
    }


    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
        } else {
            back_pressed = System.currentTimeMillis();
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}