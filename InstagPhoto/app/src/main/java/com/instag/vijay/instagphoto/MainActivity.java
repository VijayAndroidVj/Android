package com.instag.vijay.instagphoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.instag.vijay.instagphoto.adapter.PagerAdapter;
import com.instag.vijay.instagphoto.fragments.SearchFragment;
import com.instag.vijay.instagphoto.retrofit.ApiClient;
import com.instag.vijay.instagphoto.retrofit.ApiInterface;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static EditText searchEditText;
    private Activity activity;

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

        searchEditText = (EditText) view.findViewById(R.id.searchview);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        searchEditText.clearFocus();

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
                if (position == 1) {
                    name.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.VISIBLE);
                } else {
                    name.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {

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
