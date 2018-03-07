package com.instag.vijay.fasttrending;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vijay on 7/1/18.
 */

public class MyApplicaton extends Application {
    private static final String CONSUMER_KEY = "NG4gO82b8JCVmu0mzaMcH412V";
    private static final String CONSUMER_SECRET = "NbG5AfDrbiKMM3skKH8xciwfsxbvYyFoYUYvebsA0K6E7D9de3";

    private static MyApplicaton instance;


    public static MyApplicaton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
//        Twitter.initialize(base);
    }
}
