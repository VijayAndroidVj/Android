package com.android.mymedicine.java.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.Home;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.service.MyMedicineService;
import com.android.mymedicine.java.utils.PreferenceUtil;

import java.io.File;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.android.mymedicine.java.service.MyMedicineService.snoozeTime;
import static com.android.mymedicine.java.service.MyMedicineService.snoozetotalCount;

/**
 * Created by vijay on 30/11/17.
 */

public class MedicineBroadcast extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // Create Intent for notification
        long timetostart = intent.getLongExtra("timeToStart", 0);
        long min = intent.getLongExtra("min", 0);
        long mid = intent.getLongExtra("mid", 0);
        String medicineName = intent.getStringExtra("medicineName");
        String localImagePath = intent.getStringExtra("localImagePath");
        PreferenceUtil preferenceUtil = new PreferenceUtil(context);
        int count = preferenceUtil.getInt(mid + "_" + min);
        if (count == 0) {
            preferenceUtil.putExtra(mid + "_" + min, 1);

            Intent intent1 = new Intent(context, Home.class);
            intent1.putExtra("timeToStart", timetostart);
            intent1.putExtra("mid", mid);
            intent1.putExtra("min", min);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT);

          /*  // Defining notification
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.app_icon_mymedicine)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(context.getString(R.string.notification_message)).setContentIntent(pi);

            // Allows notification to be cancelled when user clicks it
            nBuilder.setAutoCancel(true);*/
            Bitmap bitmap = null;
            if (!TextUtils.isEmpty(localImagePath)) {
                File file = new File(localImagePath);
                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(localImagePath);
                }
            }


            Notification nBuilder = null;
            if (bitmap != null) {
                Notification.Style style = new Notification.BigPictureStyle()
                        .bigPicture(bitmap);
                nBuilder = new Notification.Builder(context)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(medicineName + ":" + context.getString(R.string.notification_message))
                        .setSmallIcon(R.drawable.app_icon_mymedicine)
                        .setLargeIcon(bitmap).setContentIntent(pi).setAutoCancel(true)
                        .setStyle(style)
                        .build();

            } else {
                nBuilder = new Notification.Builder(context)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(medicineName + ":" + context.getString(R.string.notification_message))
                        .setSmallIcon(R.drawable.app_icon_mymedicine).setContentIntent(pi).setAutoCancel(true)
                        .build();
            }

            // Issuing notification
            int notifyid = (int) timetostart;
            NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.notify(notifyid, nBuilder);

            snoozeNotification(mid, medicineName, timetostart, min, localImagePath);


        }
    }

    public static HashMap<String, Handler> handlerHashMap = new HashMap<>();
    public static HashMap<String, Runnable> runnableHashMap = new HashMap<>();
    Handler handler;
    Runnable runnable;

    private void snoozeNotification(final long mid, final String medicineName, final long timetostart, final long min, final String localImagePath) {
        final PreferenceUtil preferenceUtil = new PreferenceUtil(context);
        final int count = preferenceUtil.getInt(mid + "_" + min);
        if (count <= snoozetotalCount) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    int changeeCount = count + 1;
                    preferenceUtil.putExtra(mid + "_" + min, changeeCount);
                    Intent intent1 = new Intent(context, Home.class);
                    intent1.putExtra("timeToStart", timetostart);
                    intent1.putExtra("mid", mid);
                    intent1.putExtra("min", min);
                    intent1.putExtra("localImagePath", localImagePath);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent1,
                            PendingIntent.FLAG_UPDATE_CURRENT);
//                    PendingIntent pi2 = PendingIntent.getActivity(context, 0, intent1,
//                            PendingIntent.FLAG_UPDATE_CURRENT);

                    // Defining notification
//                    NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
//                            context).setSmallIcon(R.drawable.app_icon_mymedicine)
//                            .setContentTitle(context.getString(R.string.notification_title))
//                            .setContentText(context.getString(R.string.notification_message)).setContentIntent(pi);

                    Bitmap bitmap = null;
                    if (!TextUtils.isEmpty(localImagePath)) {
                        File file = new File(localImagePath);
                        if (file.exists()) {
                            bitmap = BitmapFactory.decodeFile(localImagePath);
                        }
                    }


                    Notification nBuilder = null;
                    if (bitmap != null) {
                        Notification.Style style = new Notification.BigPictureStyle()
                                .bigPicture(bitmap);
                        nBuilder = new Notification.Builder(context)
                                .setContentTitle(context.getString(R.string.notification_title))
                                .setContentText(medicineName + ":" + context.getString(R.string.notification_message))
                                .setSmallIcon(R.drawable.app_icon_mymedicine).setAutoCancel(true)
                                .setLargeIcon(bitmap).setContentIntent(pi)
                                .setStyle(style)
                                .build();

                    } else {
                        nBuilder = new Notification.Builder(context)
                                .setContentTitle(context.getString(R.string.notification_title))
                                .setContentText(medicineName + ":" + context.getString(R.string.notification_message))
                                .setSmallIcon(R.drawable.app_icon_mymedicine).setContentIntent(pi).setAutoCancel(true)
                                .build();
                    }
//                    .addAction(R.drawable.icon_actionbar_share, "Take", pi)
//                    .addAction(R.drawable.ic_access_time_black_24dp, "Cancel", pi2);
                    // Allows notification to be cancelled when user clicks it
//                    nBuilder.setAutoCancel(true);
                    // Issuing notification
                    int notifyid = (int) timetostart;
                    NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notifyMgr.notify(notifyid, nBuilder);
                    snoozeNotification(mid, medicineName, timetostart, min, localImagePath);

                }
            };
            handlerHashMap.put(mid + "_" + min, handler);
            runnableHashMap.put(mid + "_" + min, runnable);
            handler.postDelayed(runnable, snoozeTime);
        } else {
            DbService dbService = new DbService(context);
            dbService.updateNotificationTime(mid);
            MyMedicineService.setAlarm(context);
        }
    }
}
