package com.xr.vijay.tranzpost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 27-06-2017.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Activity activity;
    protected Activity currentActivity;
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private TextView txtProfileName;
    private boolean askPermission;
    public boolean permissionGranted;
    private ArrayList<String> pendingPermissions;
    private float lastTranslate = 0.0f;
    private ImageView iv_drawermenu;
    Animation animFadein;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public ActionBar actionBar;

    public void setActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.LEFT);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar_home, null);
        TextView tv_actionbar_title = (TextView) view.findViewById(R.id.tv_actionbar_title);
        iv_drawermenu = (ImageView) view.findViewById(R.id.iv_actionbar_menu);

        tv_actionbar_title.setText("Home");
//        if (currentActivity instanceof ParticipantsActivity) {
//            title.setText(getString(R.string.consultation));
//            hideActionBar();
//        } else if (currentActivity instanceof ReportsActivity) {
//            title.setText(getString(R.string.consultation));
//        } else if (currentActivity instanceof NotesActivity) {
//            title.setText(getString(R.string.consultation));
//        } else if (currentActivity instanceof MainActivity || currentActivity instanceof AddAppointmentActivity) {
//            if (isDoctor) {
//                title.setText(R.string.doctor);
//            } else {
//                title.setText(R.string.participants);
//            }
//            iv_actionbar_mic.setVisibility(View.GONE);
//            iv_actionbar_video.setVisibility(View.GONE);
//            iv_actionbar_speaker.setVisibility(View.GONE);
//            iv_actionbar_switchcamera.setVisibility(View.GONE);
//        }

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);

    }

    public void initNavigationDrawer() {
        try {
            animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setActionBar();

            txtProfileName = (TextView) findViewById(R.id.txtProfileName);
            txtProfileName.setText(preferenceUtil.getUserName());
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

                @Override
                public void onDrawerClosed(View v) {
                    super.onDrawerClosed(v);
                }

                @Override
                public void onDrawerOpened(View v) {
                    super.onDrawerOpened(v);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);

                    float moveFactor = (drawerView.getWidth() * slideOffset);
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    lastTranslate = moveFactor;

                }
            };
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

            findViewById(R.id.ll_residemmenu_home).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else
                        drawerLayout.openDrawer(GravityCompat.START);

                    view.startAnimation(animFadein);
                }
            });

            iv_drawermenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else
                        drawerLayout.openDrawer(GravityCompat.START);

                }
            });
            findViewById(R.id.ll_residemmenu_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animFadein);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    preferenceUtil.logoutAll();
                    Intent intent = new Intent(activity, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.ll_residemmenu_charge).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animFadein);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(activity, HaltingPriceActivity.class);
                    startActivity(intent);
                }
            });

            findViewById(R.id.ll_residemmenu_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animFadein);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(activity, MyProfileActivity.class);
                    startActivity(intent);
                }
            });

            findViewById(R.id.ll_residemmenu_settings).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animFadein);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(activity, TruckSettings.class);
                    startActivity(intent);
                }
            });

            findViewById(R.id.ll_residemmenu_contactus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animFadein);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(activity, ContactUs.class);
                    startActivity(intent);
                }
            });

            if (preferenceUtil.getLogintype().equalsIgnoreCase("Customer")) {
                findViewById(R.id.ll_residemmenu_available_offers).setVisibility(View.GONE);
                findViewById(R.id.ll_residemmenu_my_offers).setVisibility(View.GONE);
                findViewById(R.id.ll_residemmenu_charge).setVisibility(View.GONE);
                findViewById(R.id.ll_residemmenu_my_booking).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.ll_residemmenu_available_offers).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_residemmenu_my_offers).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_residemmenu_charge).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_residemmenu_my_booking).setVisibility(View.GONE);
            }


//            toolbar.setNavigationIcon(R.drawable.home);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            });

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    int mToolbarHeight, mAnimDuration = 0/* milliseconds */;

    ValueAnimator mVaActionBar;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
        } else {
            back_pressed = System.currentTimeMillis();
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_actionbar_menu:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else
                    drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                Log.i("Clicked", v.toString());
                break;
        }
    }
}
