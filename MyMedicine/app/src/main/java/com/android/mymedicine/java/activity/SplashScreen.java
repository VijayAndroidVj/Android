package com.android.mymedicine.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.service.MyMedicineService;
import com.android.mymedicine.java.utils.PreferenceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

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

        Intent i = new Intent(SplashScreen.this, MyMedicineService.class);
        startService(i);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    File sd = Environment.getExternalStorageDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "/data/data/" + getPackageName() + "/databases/db_mymedicine";
                        String backupDBPath = "db_mymedicinetesting1.db";
                        File currentDB = new File(currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                    }
                } catch (Exception e) {

                }


                if (preferenceUtil.isLoggedin()) {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, Home.class));
                    finish();
                } else {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, AppTour.class));
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
