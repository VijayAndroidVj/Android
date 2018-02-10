package com.vajralabs.uniroyal.uniroyal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.StaticValus;


public class AboutUs extends AppCompatActivity {
    TextView aboutUS;
    TextView about_us1;
    TextView about_us2;
    TextView about_us3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.nav_aboutus));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        aboutUS = findViewById(R.id.about_us);
        about_us1 = findViewById(R.id.about_us1);
        about_us2 = findViewById(R.id.about_us2);
        about_us3 = findViewById(R.id.about_us3);
        aboutUS.setText(Html.fromHtml(StaticValus.aboutUs));
        about_us1.setText(Html.fromHtml(StaticValus.aboutUs1));
        about_us2.setText(Html.fromHtml(StaticValus.aboutUs2));
        about_us3.setText(Html.fromHtml(StaticValus.aboutUs3));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
