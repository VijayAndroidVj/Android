package com.mcmount.vijay.mcmount;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 27-06-2017.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public int[] drawer_icons = {R.string.fa_home, R.string.fa_share,
            R.string.fa_reports, R.string.fa_notes, R.string.fa_my_account,};
    protected Activity activity;
    protected Activity currentActivity;
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private ArrayList<String> navigation_items;
    private DrawerListAdapter drawerListAdapter;
    private ListView lv_drawer;
    private TextView txtProfileName;
    private boolean askPermission;
    public boolean permissionGranted;
    private ArrayList<String> pendingPermissions;
    private float lastTranslate = 0.0f;
    private ImageView iv_drawermenu;

    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        navigation_items = new ArrayList<>();
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);

        pendingPermissions = new ArrayList<>();
        if (pendingPermissions.size() == 0) {
            permissionGranted = true;
        } else {
            askPermission = true;
        }

        //adding menu items for naviations
        navigation_items.add(getString(R.string.home));
        navigation_items.add(getString(R.string.participants));
        navigation_items.add(getString(R.string.reports));
        navigation_items.add(getString(R.string.notes));
        navigation_items.add(getString(R.string.my_account));

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (askPermission) {
                askPermission = false;
                permissionGranted = false;
            }
            Log.d("onResume", "" + currentActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
//                pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (pendingPermissions.size() == 0) {
                    askPermission = false;
                    permissionGranted = true;
                } else {
                    askPermission = true;
                    permissionGranted = false;
                }

                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public ActionBar actionBar;

    public void setActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        TextView name = (TextView) view.findViewById(R.id.txtAppName);
        iv_drawermenu = (ImageView) view.findViewById(R.id.iv_drawermenu);
        iv_drawermenu.setVisibility(View.GONE);
        name.setText(getString(R.string.app_name));


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);

    }

    public void onlyActionbar() {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setActionBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initNavigationDrawer() {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setActionBar();

            lv_drawer = (ListView) findViewById(R.id.lv_drawer);

            txtProfileName = (TextView) findViewById(R.id.txtProfileName);
            TextView tv_navigation_drawer_appversion = (TextView) findViewById(R.id.tv_navigation_drawer_appversion);
            tv_navigation_drawer_appversion.setText("v" + BuildConfig.VERSION_NAME);
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
//                    drawerView.startAnimation(anim);
                    lastTranslate = moveFactor;

                }
            };
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            iv_drawermenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else
                        drawerLayout.openDrawer(GravityCompat.START);
                }
            });
//            toolbar.setNavigationIcon(R.drawable.home);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            });

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            navigation_items.clear();
            navigation_items.add(getString(R.string.profile));
            navigation_items.add(getString(R.string.comlaint_history));
            navigation_items.add(getString(R.string.upoad_bill));
            navigation_items.add(getString(R.string.mc_box));
            navigation_items.add(getString(R.string.warrenty_registration));
            navigation_items.add(getString(R.string.complaint_escalate));
            navigation_items.add(getString(R.string.raise_by_brand));
            navigation_items.add(getString(R.string.logout1));

            int[] drawer_icons = {
                    R.string.fa_my_account,
                    R.string.fa_appointment,
                    R.string.fa_notes,
                    R.string.fa_webview,
                    R.string.fa_reports,
                    R.string.fa_notes, R.string.fa_notes, R.string.fa_logout};

            drawerListAdapter = new DrawerListAdapter(BaseActivity.this, navigation_items, drawer_icons);


            lv_drawer.setAdapter(drawerListAdapter);

            lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {

                        drawerLayout.closeDrawer(GravityCompat.START);

                        if (navigation_items.get(position).equalsIgnoreCase("Profile")) {
//                            Intent in = new Intent(activity, MainActivity.class);
//                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(in);
//                            MainActivity.notesList = new ArrayList<Notes>();
//                            MainActivity.labReportList = new ArrayList<Labreport>();
//                            MainActivity.jsonForWebView = null;
////                        if (!(currentActivity instanceof MainActivity))
//                            finish();
                        } else if (navigation_items.get(position).equalsIgnoreCase("Complaint History")) {

                        } else if (navigation_items.get(position).equalsIgnoreCase("Upload Bill")) {
                        } else if (navigation_items.get(position).equalsIgnoreCase("MC Box")) {
                            if (CommonUtil.isNetworkAvailable(activity)) {
                            } else {
                                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (navigation_items.get(position).equalsIgnoreCase("Warrenty Registration")) {
                            if (CommonUtil.isNetworkAvailable(activity)) {
                            } else {
                                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                            }

                        } else if (navigation_items.get(position).equalsIgnoreCase("Complaint Escalate")) {
                            if (CommonUtil.isNetworkAvailable(activity)) {
                            } else {
                                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                            }

                        } else if (navigation_items.get(position).equalsIgnoreCase("Raise By Brand")) {
                            if (CommonUtil.isNetworkAvailable(activity)) {
                            } else {
                                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                            }

                        } else if (navigation_items.get(position).equalsIgnoreCase(getString(R.string.logout1))) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                            LayoutInflater inflater = activity.getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                            alertDialogBuilder.setView(dialogView);
                            TextView textViewtitle = (TextView) dialogView.findViewById(R.id.title);
                            TextView textViewmessage = (TextView) dialogView.findViewById(R.id.message);
                            textViewtitle.setText(activity.getString(R.string.app_name));
                            textViewmessage.setText("Are you sure want to logout?");

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Button dialogButtonCancel = (Button) dialogView.findViewById(R.id.customDialogCancel);
                            Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);
                            // Click cancel to dismiss android custom dialog box
                            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            // Action for custom dialog ok button click
                            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    // if this button is clicked, close
                                    // current activity
                                    PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                                    preferenceUtil.putBoolean(Keys.IS_ALREADY_REGISTERED, false);

                                    Intent intent = new Intent(activity, MmSignInActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            // show it
                            alertDialog.show();

                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                moveTaskToBack(true);
            } else {
                back_pressed = System.currentTimeMillis();
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onBackPressed();
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

            default:
                Log.i("Clicked", v.toString());
                break;
        }
    }
}
