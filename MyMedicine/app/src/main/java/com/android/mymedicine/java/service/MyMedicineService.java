package com.android.mymedicine.java.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.notification.MedicineBroadcast;
import com.android.mymedicine.java.utils.PreferenceUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.android.mymedicine.java.notification.MedicineBroadcast.handlerHashMap;
import static com.android.mymedicine.java.notification.MedicineBroadcast.runnableHashMap;

/**
 * Created by vijay on 30/11/17.
 */

public class MyMedicineService extends Service {

    private ServiceBroadCast receiver = new ServiceBroadCast();
    DbService dbService;
    AlarmManager alarmManager;
    AlarmManager snoozealarmManager1, snoozealarmManager2;
    PendingIntent pendingIntent;

    public static final long snoozeTime = 2 * 60000;
    public static final long snoozetotalCount = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            dbService = new DbService(this);
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, getIntentFilter());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SetAlarmSlot");
        intentFilter.addAction("cancelAlarm");
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void setMedicineAlarm(String medicineName, long mid, long timeToStart, long min, String localImagePath) {
        Intent intent = new Intent(this, MedicineBroadcast.class);
        intent.putExtra("timeToStart", timeToStart);
        intent.putExtra("mid", mid);
        intent.putExtra("min", min);
        intent.putExtra("medicineName", medicineName);
        intent.putExtra("localImagePath", localImagePath);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), (int) timeToStart, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToStart, pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToStart, pendingIntent);

    }

    public static void cancelAlarm(Context context, long mid, String min) {
        try {
            PreferenceUtil preferenceUtil = new PreferenceUtil(context);
            preferenceUtil.putExtra(mid + "_" + min, 4);

            Handler handler = handlerHashMap.get(mid + "_" + min);
            Runnable runnable = runnableHashMap.get(mid + "_" + min);
            handler.removeCallbacks(runnable);

            DbService dbService = new DbService(context);
            dbService.updateNotificationTime(mid);
            MyMedicineService.setAlarm(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAlarm(Context context) {
        Intent intent = new Intent();
        intent.setAction("SetAlarmSlot");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private class ServiceBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action)) {
                    switch (action) {
                        case "cancelAlarm":
                            if (alarmManager != null) {
                                alarmManager.cancel(pendingIntent);
                            }
                            if (snoozealarmManager1 != null) {
                                snoozealarmManager1.cancel(pendingIntent);
                            }
                            if (snoozealarmManager2 != null) {
                                snoozealarmManager2.cancel(pendingIntent);
                            }
                            break;
                        case "SetAlarmSlot":
                            ArrayList<MedicineModel> medicineModelArrayList = dbService.getNextMedicineList();
                            PreferenceUtil preferenceUtil = new PreferenceUtil(MyMedicineService.this);
                            for (MedicineModel medicineModel : medicineModelArrayList) {
                                Calendar calendar = Calendar.getInstance();
                                Calendar slotcalendar = Calendar.getInstance();
                                boolean show = false;
                                int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
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
//                                        if (count <= 0) {
                                        if (calendar.get(Calendar.HOUR_OF_DAY) < slotcalendar.get(Calendar.HOUR_OF_DAY) || (calendar.get(Calendar.HOUR_OF_DAY) == slotcalendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.MINUTE) <= slotcalendar.get(Calendar.MINUTE)))
                                            setMedicineAlarm(medicineModel.getMedicineName(), medicineModel.getMedicineId(), medicinetaketime - (5 * 60000), medicineModel.getNextNotificationTime(), medicineModel.getLocalImagePath());
//                                        } else {
//                                            setMedicineAlarmSnooze1(String.valueOf(medicineModel.getMedicineId()) + "_" + position, medicinetaketime - (5 * 60000), min);
//                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long convertDate(String date) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

}
