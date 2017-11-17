package com.instag.vijay.instagphoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static ProgressDialog progressDoalog;

    public static void showProgress(Activity activity) {

        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Its loading....");
            progressDoalog.setTitle("ProgressDialog bar example");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        if (!progressDoalog.isShowing())
            progressDoalog.show();
    }

    public static void dismissProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.dismiss();
    }
}
