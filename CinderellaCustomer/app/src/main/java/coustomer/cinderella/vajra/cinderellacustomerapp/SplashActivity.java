package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import common.Config;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    private ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        LinearLayout line = (LinearLayout) findViewById(R.id.activity_splash);
        img_logo = (ImageView) findViewById(R.id.img_logo);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);

        //line.startAnimation(animation1);
        img_logo.startAnimation(animation2);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (Config.getDATA(SplashActivity.this, "mobile").equals("") && Config.getDATA(SplashActivity.this, "pwd").equals("")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
