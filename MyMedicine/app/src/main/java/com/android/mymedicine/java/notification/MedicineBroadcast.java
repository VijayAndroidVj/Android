package com.android.mymedicine.java.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.Home;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.service.MyMedicineService;
import com.android.mymedicine.java.utils.PreferenceUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.ALARM_SERVICE;
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
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("EveryDayCheck")) {
            try {
                DbService dbService = new DbService(context);
                ArrayList<MedicineModel> medicineModelArrayList = dbService.getNextMedicineList();
                PreferenceUtil preferenceUtil = new PreferenceUtil(context);
                for (MedicineModel medicineModel : medicineModelArrayList) {
                    Calendar calendar = Calendar.getInstance();
                    Calendar slotcalendar = Calendar.getInstance();
                    boolean show = false;
                    int currentDay = calendar.get(Calendar.DAY_OF_WEEK) + 1;
                    switch (currentDay) {
                        case 1:
                            show = medicineModel.getIsSelectedsunday();
                            break;
                        case 2:
                            show = medicineModel.getIsSelectedmonday();
                            break;
                        case 3:
                            show = medicineModel.getIsSelectedtuesday();
                            break;
                        case 4:
                            show = medicineModel.getIsSelectedwednesday();
                            break;
                        case 5:
                            show = medicineModel.getIsSelectedthursday();
                            break;
                        case 6:
                            show = medicineModel.getIsSelectedfriday();
                            break;
                        case 7:
                            show = medicineModel.getIsSelectedsaturday();
                            break;

                    }

                    if (show && medicineModel.getNextNotificationTime() > 0) {
                        int taken = preferenceUtil.getInt(String.valueOf(medicineModel.getMedicineId()) + "_" + medicineModel.getNextNotificationTime());
                        if (taken == 0) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                            SimpleDateFormat finalformatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            formatter.setLenient(false);
                            finalformatter.setLenient(false);

                            Date curDate = new Date();
                            String curTime = formatter.format(curDate);

                            Log.w("curTime", "" + curTime);

                            Date oldDate = formatter.parse(curTime);
                            long onlyCurrentDateMillis = oldDate.getTime();

                            Log.w("onlyCurrentDateMillis", "" + onlyCurrentDateMillis);
                            Log.w("elapsedRealtime", "" + SystemClock.elapsedRealtime());
                            long medicinetaketime = (onlyCurrentDateMillis + medicineModel.getNextNotificationTime());
                            slotcalendar.setTime(new Date(medicinetaketime));
                            Date notifitimeinString = new Date(medicinetaketime);
                            String notifitimeinString1 = finalformatter.format(notifitimeinString);
                            Log.w("notifitimeinString:", notifitimeinString1);
                            if (calendar.get(Calendar.HOUR_OF_DAY) < slotcalendar.get(Calendar.HOUR_OF_DAY) || (calendar.get(Calendar.HOUR_OF_DAY) == slotcalendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.MINUTE) <= slotcalendar.get(Calendar.MINUTE)))
                                setMedicineAlarm(context, medicineModel.getMedicineName(), medicineModel.getMedicineId(), medicinetaketime - (5 * 60000), medicineModel.getNextNotificationTime(), medicineModel.getLocalImagePath());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
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
    }

    private void setMedicineAlarm(Context context, String medicineName, long mid, long timeToStart, long min, String localImagePath) {
        Intent intent = new Intent(context, MedicineBroadcast.class);
        intent.putExtra("timeToStart", timeToStart);
        intent.putExtra("mid", mid);
        intent.putExtra("min", min);
        intent.putExtra("medicineName", medicineName);
        intent.putExtra("localImagePath", localImagePath);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), (int) timeToStart, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToStart, pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToStart, pendingIntent);
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
