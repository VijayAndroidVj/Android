package com.android.mymedicine.java.customview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by iyara_rajan on 10-07-2017.
 */

public class PopupWindow {

    public static android.widget.PopupWindow getPopup(Activity activity) {
        final android.widget.PopupWindow window = new android.widget.PopupWindow(activity);

        window.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    window.dismiss();
                    return true;
                }
                return false;
            }
        });

        window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);

        window.setBackgroundDrawable(new BitmapDrawable());

        return window;
    }
}
