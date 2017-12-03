package com.android.mymedicine.java.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.AddMedicine;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by razin on 25/11/17.
 */

public class TimeSlotListAdapter extends RecyclerView.Adapter<TimeSlotListAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    Activity activity;
    int timesPerDay;
    TimePickerDialog tpd;

    public TimeSlotListAdapter(Activity activity, int timesPerDay) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        this.timesPerDay = timesPerDay;
    }

    private void initDateTimePicker(final MyViewHolder holder, final int position) {
        tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String hh;
                        String mm;
                        if (hourOfDay < 10) {
                            hh = "0" + hourOfDay;
                        } else {
                            hh = "" + hourOfDay;
                        }
                        if (minute < 10) {
                            mm = "0" + minute;
                        } else {
                            mm = "" + minute;
                        }
                        String time = hh + ":" + mm;
                        holder.tv_adapter_time_slot_list_time.setText(time);
                        holder.tv_adapter_time_slot_list_qty.setText("take 1");

                        long millisecondOfDay =
                                TimeUnit.HOURS.toMillis(hourOfDay) +
                                        TimeUnit.MINUTES.toMillis(minute) +
                                        TimeUnit.SECONDS.toMillis(second);

                        setToMessageModel(millisecondOfDay, position);
                    }
                },
                0,
                0,
                false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_1);
        tpd.setAccentColor(ContextCompat.getColor(activity, R.color.bg_button));
        tpd.setOkColor(Color.BLACK);
        tpd.setCancelColor(Color.BLACK);
        tpd.vibrate(false);
        tpd.show(activity.getFragmentManager(), "Timepickerdialog");
    }

    private long convertTime(String time) {
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

    private void setToMessageModel(long timeinmillis, int position) {
        {

            switch (position) {
                case 0:
                    AddMedicine.medicineModel.setTimeSlot_1(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_1(1);
                    break;
                case 1:
                    AddMedicine.medicineModel.setTimeSlot_2(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_2(1);
                    break;
                case 2:
                    AddMedicine.medicineModel.setTimeSlot_3(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_3(1);
                    break;
                case 3:
                    AddMedicine.medicineModel.setTimeSlot_4(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_4(1);
                    break;
                case 4:
                    AddMedicine.medicineModel.setTimeSlot_5(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_5(1);
                    break;
                case 5:
                    AddMedicine.medicineModel.setTimeSlot_6(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_6(1);
                    break;
                case 6:
                    AddMedicine.medicineModel.setTimeSlot_7(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_7(1);
                    break;
                case 7:
                    AddMedicine.medicineModel.setTimeSlot_8(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_8(1);
                    break;
                case 8:
                    AddMedicine.medicineModel.setTimeSlot_9(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_9(1);
                    break;
                case 9:
                    AddMedicine.medicineModel.setTimeSlot_10(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_10(1);
                    break;
                case 10:
                    AddMedicine.medicineModel.setTimeSlot_11(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_11(1);
                    break;
                case 11:
                    AddMedicine.medicineModel.setTimeSlot_12(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_12(1);
                    break;
                case 12:
                    AddMedicine.medicineModel.setTimeSlot_13(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_13(1);
                    break;
                case 13:
                    AddMedicine.medicineModel.setTimeSlot_14(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_14(1);
                    break;
                case 14:
                    AddMedicine.medicineModel.setTimeSlot_15(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_15(1);
                    break;
                case 15:
                    AddMedicine.medicineModel.setTimeSlot_16(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_16(1);
                    break;
                case 16:
                    AddMedicine.medicineModel.setTimeSlot_17(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_17(1);
                    break;
                case 17:
                    AddMedicine.medicineModel.setTimeSlot_18(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_18(1);
                    break;
                case 18:
                    AddMedicine.medicineModel.setTimeSlot_19(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_19(1);
                    break;
                case 19:
                    AddMedicine.medicineModel.setTimeSlot_20(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_20(1);
                    break;
                case 20:
                    AddMedicine.medicineModel.setTimeSlot_21(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_21(1);
                    break;
                case 21:
                    AddMedicine.medicineModel.setTimeSlot_22(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_22(1);
                    break;
                case 22:
                    AddMedicine.medicineModel.setTimeSlot_23(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_23(1);
                    break;
                case 23:
                    AddMedicine.medicineModel.setTimeSlot_24(timeinmillis);
                    AddMedicine.medicineModel.setMedicineCountSlot_24(1);
                    break;
            }
        }
    }

    private void setToMessageModel(String time, int position) {

        switch (position) {
            case 0:
                AddMedicine.medicineModel.setTimeSlot_1(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_1(1);
                break;
            case 1:
                AddMedicine.medicineModel.setTimeSlot_2(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_2(1);
                break;
            case 2:
                AddMedicine.medicineModel.setTimeSlot_3(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_3(1);
                break;
            case 3:
                AddMedicine.medicineModel.setTimeSlot_4(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_4(1);
                break;
            case 4:
                AddMedicine.medicineModel.setTimeSlot_5(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_5(1);
                break;
            case 5:
                AddMedicine.medicineModel.setTimeSlot_6(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_6(1);
                break;
            case 6:
                AddMedicine.medicineModel.setTimeSlot_7(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_7(1);
                break;
            case 7:
                AddMedicine.medicineModel.setTimeSlot_8(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_8(1);
                break;
            case 8:
                AddMedicine.medicineModel.setTimeSlot_9(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_9(1);
                break;
            case 9:
                AddMedicine.medicineModel.setTimeSlot_10(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_10(1);
                break;
            case 10:
                AddMedicine.medicineModel.setTimeSlot_11(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_11(1);
                break;
            case 11:
                AddMedicine.medicineModel.setTimeSlot_12(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_12(1);
                break;
            case 12:
                AddMedicine.medicineModel.setTimeSlot_13(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_13(1);
                break;
            case 13:
                AddMedicine.medicineModel.setTimeSlot_14(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_14(1);
                break;
            case 14:
                AddMedicine.medicineModel.setTimeSlot_15(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_15(1);
                break;
            case 15:
                AddMedicine.medicineModel.setTimeSlot_16(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_16(1);
                break;
            case 16:
                AddMedicine.medicineModel.setTimeSlot_17(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_17(1);
                break;
            case 17:
                AddMedicine.medicineModel.setTimeSlot_18(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_18(1);
                break;
            case 18:
                AddMedicine.medicineModel.setTimeSlot_19(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_19(1);
                break;
            case 19:
                AddMedicine.medicineModel.setTimeSlot_20(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_20(1);
                break;
            case 20:
                AddMedicine.medicineModel.setTimeSlot_21(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_21(1);
                break;
            case 21:
                AddMedicine.medicineModel.setTimeSlot_22(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_22(1);
                break;
            case 22:
                AddMedicine.medicineModel.setTimeSlot_23(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_23(1);
                break;
            case 23:
                AddMedicine.medicineModel.setTimeSlot_24(convertTime(time));
                AddMedicine.medicineModel.setMedicineCountSlot_24(1);
                break;
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.adapter_time_slot_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.rl_adapter_time_slot_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDateTimePicker(holder, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return timesPerDay;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_adapter_time_slot_list_container;
        TextView tv_adapter_time_slot_list_time;
        TextView tv_adapter_time_slot_list_qty;

        private MyViewHolder(View view) {
            super(view);
            rl_adapter_time_slot_list_container = view.findViewById(R.id.rl_adapter_time_slot_list_container);
            tv_adapter_time_slot_list_time = view.findViewById(R.id.tv_adapter_time_slot_list_time);
            tv_adapter_time_slot_list_qty = view.findViewById(R.id.tv_adapter_time_slot_list_qty);
        }
    }
}
