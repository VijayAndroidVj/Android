package com.android.mymedicine.java.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.view.View;

import com.android.mymedicine.R;
import com.android.mymedicine.java.model.MedicineModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by razin on 27/11/17.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void buttonTouch(final View view){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        view.setBackgroundResource(R.drawable.bg_button_pressed);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        view.setBackgroundResource(R.drawable.bg_button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public static String getFormatedDateTime(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static ArrayList<Long> setTimeSlot(MedicineModel medicineModel) {
        ArrayList<Long> arrList = new ArrayList<>();
        for (int i=0;i< medicineModel.getHowManyTimes();i++){
            switch (i){
                case 0:
                    arrList.add(medicineModel.getTimeSlot_1());
                    break;
                case 1:
                    arrList.add(medicineModel.getTimeSlot_2());
                    break;
                case 2:
                    arrList.add(medicineModel.getTimeSlot_3());
                    break;
                case 3:
                    arrList.add(medicineModel.getTimeSlot_4());
                    break;
                case 4:
                    arrList.add(medicineModel.getTimeSlot_5());
                    break;
                case 5:
                    arrList.add(medicineModel.getTimeSlot_6());
                    break;
                case 6:
                    arrList.add(medicineModel.getTimeSlot_7());
                    break;
                case 7:
                    arrList.add(medicineModel.getTimeSlot_8());
                    break;
                case 8:
                    arrList.add(medicineModel.getTimeSlot_9());
                    break;
                case 9:
                    arrList.add(medicineModel.getTimeSlot_10());
                    break;
                case 10:
                    arrList.add(medicineModel.getTimeSlot_11());
                    break;
                case 11:
                    arrList.add(medicineModel.getTimeSlot_12());
                    break;
                case 12:
                    arrList.add(medicineModel.getTimeSlot_13());
                    break;
                case 13:
                    arrList.add(medicineModel.getTimeSlot_14());
                    break;
                case 14:
                    arrList.add(medicineModel.getTimeSlot_15());
                    break;
                case 15:
                    arrList.add(medicineModel.getTimeSlot_16());
                    break;
                case 16:
                    arrList.add(medicineModel.getTimeSlot_17());
                    break;
                case 17:
                    arrList.add(medicineModel.getTimeSlot_18());
                    break;
                case 18:
                    arrList.add(medicineModel.getTimeSlot_19());
                    break;
                case 19:
                    arrList.add(medicineModel.getTimeSlot_20());
                    break;
                case 20:
                    arrList.add(medicineModel.getTimeSlot_21());
                    break;
                case 21:
                    arrList.add(medicineModel.getTimeSlot_22());
                    break;
                case 22:
                    arrList.add(medicineModel.getTimeSlot_23());
                    break;
                case 23:
                    arrList.add(medicineModel.getTimeSlot_24());
                    break;

            }
        }
        return arrList;
    }

    public static long convertDateToMilliSeconds(String date){
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

    public static long convertTimeToMilliSeconds(String time){
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date mDate = sdf.parse(time);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Time in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static long getMinimumValue(ArrayList<Long> sortedlist,MedicineModel medicineModel) {
        long currentTime;
        Calendar cal = Calendar.getInstance();
        long currentDate = convertDateToMilliSeconds(cal.get(Calendar.DAY_OF_MONTH)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR));
        if (currentDate >= medicineModel.getStartDate() && currentDate <= medicineModel.getEndDate()) {
            currentTime = convertTimeToMilliSeconds(cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE));
        } else {
            currentTime = 0;
        }
        ArrayList<Long> greaterList = new ArrayList<>();
        for (int i = 0; i<sortedlist.size();i++){
            if (sortedlist.get(i) > currentTime && sortedlist.get(i) != medicineModel.getNextNotificationTime()){
                greaterList.add(sortedlist.get(i));
            }
        }
        if (greaterList.size() == 0){
            greaterList.add(sortedlist.get(0));
        }

        return greaterList.get(0);
    }

    public static int getMedicineCount(ArrayList<Long> list, long nextNotificationTime,MedicineModel medicineModel) {
        int medicine_count = 0;
        for (int i = 0;i<list.size();i++){
            if (list.get(i) == nextNotificationTime){
                medicine_count = getMedicineFromModel(i,medicineModel);
            }
        }
        return medicine_count;
    }

    private static int getMedicineFromModel(int position,MedicineModel medicineModel) {
        int medicinecount = 0;
        switch (position){
            case 0:
                medicinecount = medicineModel.getMedicineCountSlot_1();
                break;
            case 1:
                medicinecount = medicineModel.getMedicineCountSlot_2();
                break;
            case 2:
                medicinecount = medicineModel.getMedicineCountSlot_3();
                break;
            case 3:
                medicinecount = medicineModel.getMedicineCountSlot_4();
                break;
            case 4:
                medicinecount = medicineModel.getMedicineCountSlot_5();
                break;
            case 5:
                medicinecount = medicineModel.getMedicineCountSlot_6();
                break;
            case 6:
                medicinecount = medicineModel.getMedicineCountSlot_7();
                break;
            case 7:
                medicinecount = medicineModel.getMedicineCountSlot_8();
                break;
            case 8:
                medicinecount = medicineModel.getMedicineCountSlot_9();
                break;
            case 9:
                medicinecount = medicineModel.getMedicineCountSlot_10();
                break;
            case 10:
                medicinecount = medicineModel.getMedicineCountSlot_11();
                break;
            case 11:
                medicinecount = medicineModel.getMedicineCountSlot_12();
                break;
            case 12:
                medicinecount = medicineModel.getMedicineCountSlot_13();
                break;
            case 13:
                medicinecount = medicineModel.getMedicineCountSlot_14();
                break;
            case 14:
                medicinecount = medicineModel.getMedicineCountSlot_15();
                break;
            case 15:
                medicinecount = medicineModel.getMedicineCountSlot_16();
                break;
            case 16:
                medicinecount = medicineModel.getMedicineCountSlot_17();
                break;
            case 17:
                medicinecount = medicineModel.getMedicineCountSlot_18();
                break;
            case 18:
                medicinecount = medicineModel.getMedicineCountSlot_19();
                break;
            case 19:
                medicinecount = medicineModel.getMedicineCountSlot_20();
                break;
            case 20:
                medicinecount = medicineModel.getMedicineCountSlot_21();
                break;
            case 21:
                medicinecount = medicineModel.getMedicineCountSlot_22();
                break;
            case 22:
                medicinecount = medicineModel.getMedicineCountSlot_23();
                break;
            case 23:
                medicinecount = medicineModel.getMedicineCountSlot_24();
                break;
        }
        return medicinecount;
    }
}
