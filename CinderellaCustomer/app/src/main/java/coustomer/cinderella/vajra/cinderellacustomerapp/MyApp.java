package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.Application;

import common.TypefaceUtil;

/**
 * Created by karthiks on 5/18/2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "roboto_regular.ttf");

    }
}
