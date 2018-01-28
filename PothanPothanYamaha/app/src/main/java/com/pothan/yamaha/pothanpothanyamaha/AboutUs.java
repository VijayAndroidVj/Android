package com.pothan.yamaha.pothanpothanyamaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;


public class AboutUs extends AppCompatActivity {
    TextView aboutUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aboutUS = (TextView) findViewById(R.id.about_us);
        aboutUS.setText(Html.fromHtml(StaticValus.aboutUs));
    }

}
