package com.xr.vijay.tranzpost;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by vijay on 2/12/17.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
