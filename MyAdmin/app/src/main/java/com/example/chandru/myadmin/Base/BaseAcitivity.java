package com.example.chandru.myadmin.Base;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;


public abstract class BaseAcitivity extends AppCompatActivity {
    public static final int SCREEN_PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    public static final int SCREEN_HORIZONTAL = Configuration.ORIENTATION_LANDSCAPE;
    protected int leftRightSpace, topBottomSpace, orientation, height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        orientation = getOrientation();
        //initlizeview();

    }

   /* public void initlizeview() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            leftRightSpace = (int) (width * 0.0153);
            topBottomSpace = (int) (height * 0.0089);


        } else {
            leftRightSpace = (int) (height * 0.0153);
            topBottomSpace = (int) (width * 0.0089);

        }


    }*/

    public int getOrientation() {

        int orientationType = SCREEN_PORTRAIT;
        Display getOrient = getWindowManager().getDefaultDisplay();
        int rotation = getOrient.getRotation();
        int orientation = 0;
        Point size = new Point();
        getOrient.getSize(size);
        int orientType1 = SCREEN_PORTRAIT;
        int orientType2 = SCREEN_HORIZONTAL;
        if (size.x > 0 && size.y > 0) {
            if (size.x < size.y) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
                orientationType = orientType1;
            } else {
                orientationType = orientType2;
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }

        if (orientation == 0) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientationType = orientType1;
                    // text = "SCREEN_ORIENTATION_PORTRAIT";
                    break;
                case Surface.ROTATION_180:
                    orientationType = orientType1;
                    //   text = "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
                    break;
                case Surface.ROTATION_90:
                    orientationType = orientType2;
                    //  text = "SCREEN_ORIENTATION_LANDSCAPE";
                    break;
                case Surface.ROTATION_270:
                    orientationType = orientType2;
                    //  text = "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
                    break;
            }
        }

        return orientationType;


    }
}
