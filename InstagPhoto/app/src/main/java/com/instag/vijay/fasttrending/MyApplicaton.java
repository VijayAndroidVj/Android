package com.instag.vijay.fasttrending;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by vijay on 7/1/18.
 */

public class MyApplicaton extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
//        Twitter.initialize(base);
    }
}
