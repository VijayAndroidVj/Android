package com.peeyemcar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.peeyem.app.R;

public class SplashActivity extends Activity
{

    String TAG = SplashActivity.class.getSimpleName();
    private ImageView mLogo;
    Context context;

    public static final int RQ_GOOGLE_HIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        context = this;
        mLogo = (ImageView) findViewById(R.id.splash_logo);
        Animation slideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        mLogo.startAnimation(slideup);



        /****************************************/
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                        Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish();

                    }


        }, 1000);
        /****************************************/





    }




    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }


}
