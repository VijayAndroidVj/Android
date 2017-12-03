package com.android.mymedicine.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.adapter.AppTourAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by razin on 23/11/17.
 */

public class AppTour extends AppCompatActivity {

    TextView tv_app_tour_skip;
    AppTourAdapter appTourAdapter;
    ViewPager viewpager;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour);
        setInitUI();
        setRegisterUI();
        setAppTour();
    }

    private void setRegisterUI() {
        tv_app_tour_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppTour.this,Login.class));
            }
        });
    }

    private void setInitUI() {
        tv_app_tour_skip = findViewById(R.id.tv_app_tour_skip);
        viewpager = findViewById(R.id.viewpager);
    }

    private void setAppTour() {
        appTourAdapter = new AppTourAdapter(this);
        viewpager.setAdapter(appTourAdapter);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

//                if (currentPage == jsonarray.length) {
                if (currentPage == 2) {
//                    currentPage = 0;
                    tv_app_tour_skip.setText("Next");
                }
                viewpager.setCurrentItem(currentPage++, true);
            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 1000);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
