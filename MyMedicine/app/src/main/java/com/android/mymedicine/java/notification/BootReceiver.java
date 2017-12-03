package com.android.mymedicine.java.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.mymedicine.java.service.MyMedicineService;

/**
 * Created by vijay on 3/12/17.
 */

public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();
        // Your code to execute when Boot Completd
        MyMedicineService.setAlarm(context);
    }
}