package com.xr.vijay.tranzpost;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 12-06-2017.
 */

public class PermissionCheck {

    public static ArrayList<String> getAllPermissions() {
        ArrayList<String> pendingPermission = new ArrayList<>();
        pendingPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        pendingPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        pendingPermission.add(Manifest.permission.CAMERA);


        return pendingPermission;
    }


    public static ArrayList<String> checkPermission(Activity activity, ArrayList<String> permissions) {
        ArrayList<String> pendingPermission = new ArrayList<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                pendingPermission.add(permission);
            }
        }
        return pendingPermission;
    }


    public static void requestPermission(Activity activity, ArrayList<String> permissions, int requestCode) {

        String[] permissionList = new String[permissions.size()];
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            permissionList[i] = permission;
        }

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions.get(0))) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            showPermissionExplanationDialog(activity, permissionList, requestCode);

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(activity, permissionList,
                    requestCode);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    private static void showPermissionExplanationDialog(final Activity activity, final String[] peermissions, final int requestCode) {


        AlertDialog.Builder alertDialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(activity);
        }
        alertDialogBuilder.setTitle("Permission needed");
        alertDialogBuilder.setMessage("You should allow the following permission in order to avoid app functional misbehave.");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    ActivityCompat.requestPermissions(activity, peermissions, requestCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= 21) {
                    activity.finishAndRemoveTask();
                } else {
                    activity.finish();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
