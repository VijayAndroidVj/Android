package com.android.mymedicine.java.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.mymedicine.R;
import com.android.mymedicine.java.customview.ResideMenu;
import com.android.mymedicine.java.customview.ResideMenu2;
import com.android.mymedicine.java.fragment.MedicineListFragment;
import com.android.mymedicine.java.fragment.MedicineListHistoryFragment;
import com.android.mymedicine.java.service.MyMedicineService;
import com.android.mymedicine.java.utils.BaseActivity;
import com.android.mymedicine.java.utils.PreferenceUtil;

/**
 * Created by razin on 23/11/17.
 */

public class Home extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FrameLayout fl_home;
    BottomNavigationView bottom_navigation;
    Fragment fragment1 = new MedicineListFragment();
    Fragment fragment2 = new MedicineListHistoryFragment();
    FragmentTransaction ft;
    //    ImageView img_actionbar_menu;
    ResideMenu2 resideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentActivity = this;

        initNavigationDrawer();

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.custom_container);
        View view = View.inflate(activity, R.layout.activity_home, parentLayout);
        setInitUI(view);
        setRegisterUI();
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        try {

            if (intent != null) {
                long min = intent.getLongExtra("min", 0);
                long mid = intent.getLongExtra("mid", 0);
                if (min > 0 && mid > 0) {
                    MyMedicineService.cancelAlarm(this, mid, mid + "_" + min);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setRegisterUI() {
//        img_actionbar_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setUpResideMenu();
//            }
//        });
        bottom_navigation.setOnNavigationItemSelectedListener(this);
    }

    private void setInitUI(View view) {
        fl_home = view.findViewById(R.id.fl_home);
        bottom_navigation = view.findViewById(R.id.bottom_navigation);
//        img_actionbar_menu = view.findViewById(R.id.img_actionbar_menu);
        ft = getFragmentManager().beginTransaction();
        ft.replace(fl_home.getId(), fragment1).commit();
    }

    private void setUpResideMenu() {
        resideMenu = new ResideMenu2(this);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.6f);
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_medicine_list:
                getFragmentManager().popBackStack();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fl_home, fragment1).commit();
                findViewById(R.id.fab_add_medicine).setVisibility(View.VISIBLE);
                PreferenceUtil preferenceUtil = new PreferenceUtil(this);
                preferenceUtil.setNotificationSettings(1);

                break;
            case R.id.menu_medicine_list_history:
                getFragmentManager().popBackStack();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fl_home, fragment2).commit();
                findViewById(R.id.fab_add_medicine).setVisibility(View.GONE);

                break;
            case R.id.img_actionbar_menu:
//                setUpResideMenu();
                break;
        }
        return true;
    }
}
