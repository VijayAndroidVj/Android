package com.android.mymedicine.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.utils.BaseActivity;

/**
 * Created by razin on 23-08-2017.
 */

public class NotesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
            setContentView(R.layout.activity_main);
            currentActivity = this;

            initNavigationDrawer();

            LinearLayout parentLayout = (LinearLayout) findViewById(R.id.custom_container);
            View view = View.inflate(activity, R.layout.activity_medicine_details, parentLayout);
    }

    private TextView txtLabel;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }

        super.onClick(v);
    }
}