package com.android.mymedicine.java.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by razin on 24/11/17.
 */

public class MyMedicine extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Locale locale = Resources.getSystem().getConfiguration().locale;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}
