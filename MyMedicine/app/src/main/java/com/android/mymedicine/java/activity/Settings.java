package com.android.mymedicine.java.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.utils.PreferenceUtil;

/**
 * Created by razin on 23/11/17.
 */

public class Settings extends AppCompatActivity implements View.OnClickListener {

    PreferenceUtil preferenceUtil;
    private RadioGroup rg_notification;
    private RadioButton rb_setting_five_minutes;
    private RadioButton rb_setting_fifteen_minutes;
    private RadioButton rb_setting_thirty_minutes;
    private RadioButton rb_setting_sixty_minutes;
    int settime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferenceUtil = new PreferenceUtil(this);
        settime = preferenceUtil.getNotificationSettings();
        setActionBar();
        setInitUI();
        setRegisterUI();
        setSettings();
    }

    private void setActionBar() {
        findViewById(R.id.img_actionbar_menu).setVisibility(View.GONE);
        findViewById(R.id.img_actionbar_back).setVisibility(View.VISIBLE);
        findViewById(R.id.img_actionbar_edit).setVisibility(View.GONE);
        TextView tv_actionbar_title = findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText(getResources().getString(R.string.settings));
        findViewById(R.id.img_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setSettings() {
        if (settime == 5){
            rb_setting_five_minutes.setChecked(true);
        } else if(settime == 15){
            rb_setting_fifteen_minutes.setChecked(true);
        } else if(settime == 30){
            rb_setting_thirty_minutes.setChecked(true);
        } else if(settime == 60){
            rb_setting_sixty_minutes.setChecked(true);
        }
    }

    private void setRegisterUI() {
        rb_setting_five_minutes.setOnClickListener(this);
                rb_setting_fifteen_minutes.setOnClickListener(this);
        rb_setting_thirty_minutes.setOnClickListener(this);
                rb_setting_sixty_minutes.setOnClickListener(this);
    }

    private void setInitUI() {
        rg_notification = findViewById(R.id.rg_notification);
        rb_setting_five_minutes = findViewById(R.id.rb_setting_five_minutes);
        rb_setting_fifteen_minutes = findViewById(R.id.rb_setting_fifteen_minutes);
        rb_setting_thirty_minutes = findViewById(R.id.rb_setting_thirty_minutes);
        rb_setting_sixty_minutes = findViewById(R.id.rb_setting_sixty_minutes);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_setting_five_minutes:
                preferenceUtil.setNotificationSettings(5);
                break;
            case R.id.rb_setting_fifteen_minutes:
                preferenceUtil.setNotificationSettings(15);
                break;
            case R.id.rb_setting_thirty_minutes:
                preferenceUtil.setNotificationSettings(30);
                break;
            case R.id.rb_setting_sixty_minutes:
                preferenceUtil.setNotificationSettings(60);
                break;
        }
    }
}
