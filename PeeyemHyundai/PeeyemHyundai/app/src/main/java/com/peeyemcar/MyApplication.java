package com.peeyemcar;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by vijay on 31/1/18.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
