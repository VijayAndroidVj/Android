package com.xr.vijay.tranzpost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by razin on 23/11/17.
 */

public class SplashScreen extends AppCompatActivity {

    private TextView tv_splash_screen_appname;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferenceUtil = new PreferenceUtil(this);
        tv_splash_screen_appname = findViewById(R.id.tv_splash_screen_appname);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean result = preferenceUtil.getBoolean(Keys.IS_ALREADY_REGISTERED, false);
                if (result) {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, Signin.class));
                    finish();
                }


            }

        }, 1500);

        tv_splash_screen_appname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Locale locale = new Locale("en");
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//                recreate();
            }
        });
    }
}
