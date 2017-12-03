package com.android.mymedicine.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.adapter.TimeSlotViewAdapter;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.utils.Utils;
import com.android.mymedicine.java.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by razin on 26/11/17.
 */

public class MedicineDetails extends AppCompatActivity {

    DbService dbService;
    long medicine_id;
    MedicineModel medicineModel;

    TextView tv_medicine_details_medication_name;
    TextView tv_medicine_details_dosage;
    TextView tv_medicine_details_unit;
    TextView tv_medicine_details_start_date;
    TextView tv_medicine_details_end_date;
    TextView tv_medicine_details_frequency;
    TextView tv_medicine_details_how_many_times_per_day;
    TextView tv_medicine_details_total_taken;
    TextView tv_medicine_details_total_skipped;
    TextView tv_medicine_details_day_sunday;
    TextView tv_medicine_details_day_monday;
    TextView tv_medicine_details_day_tuesday;
    TextView tv_medicine_details_day_wednesday;
    TextView tv_medicine_details_day_thursday;
    TextView tv_medicine_details_day_friday;
    TextView tv_medicine_details_day_saturday;
    RecyclerView rv_medicine_details_time_slot;
    ImageView img_medicine_details_med_image;

    TimeSlotViewAdapter timeSlotViewAdapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        list = new ArrayList<>();
        dbService = new DbService(this);
        medicine_id = getIntent().getLongExtra("medicine_id",0);
        medicineModel = dbService.getMedicineDetail(medicine_id);
        setActionBar();
        setInitUI();
        setContent();
        setTimeSlot();
    }

    private void setTimeSlotAdapter() {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(MedicineDetails.this);
        rv_medicine_details_time_slot.setLayoutManager(LayoutManager);
        rv_medicine_details_time_slot.setItemAnimator(new DefaultItemAnimator());
        rv_medicine_details_time_slot.addItemDecoration(new VerticalSpaceItemDecoration(1));
        timeSlotViewAdapter = new TimeSlotViewAdapter(MedicineDetails.this,list);
        rv_medicine_details_time_slot.setAdapter(timeSlotViewAdapter);
        rv_medicine_details_time_slot.setVisibility(View.VISIBLE);
    }

    private void setContent() {
        tv_medicine_details_medication_name.setText(medicineModel.getMedicineName());
                tv_medicine_details_dosage.setText(medicineModel.getDosage()+"");
        tv_medicine_details_unit.setText(medicineModel.getDosageType());
                tv_medicine_details_start_date.setText(Utils.getFormatedDateTime(medicineModel.getStartDate(),"dd-MMM-yyyy"));
        tv_medicine_details_end_date.setText(Utils.getFormatedDateTime(medicineModel.getEndDate(),"dd-MMM-yyyy"));
                tv_medicine_details_frequency.setText(medicineModel.getFrequency());
        tv_medicine_details_how_many_times_per_day.setText(medicineModel.getHowManyTimes()+" times");
                tv_medicine_details_total_taken.setText(medicineModel.getMedicineTaken()+"");
        tv_medicine_details_total_skipped.setText(medicineModel.getMedicineSkipped()+"");
        if (medicineModel.getIsSelectedsunday()){
            tv_medicine_details_day_sunday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_sunday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_sunday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_sunday.setBackgroundResource(R.color.bg_unselected_day);
        }

        if (medicineModel.getIsSelectedmonday()){
            tv_medicine_details_day_monday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_monday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_monday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_monday.setBackgroundResource(R.color.bg_unselected_day);
        }

        if (medicineModel.getIsSelectedtuesday()){
            tv_medicine_details_day_tuesday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_tuesday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_tuesday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_tuesday.setBackgroundResource(R.color.bg_unselected_day);
        }
        if (medicineModel.getIsSelectedwednesday()){
            tv_medicine_details_day_wednesday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_wednesday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_wednesday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_wednesday.setBackgroundResource(R.color.bg_unselected_day);
        }
        if (medicineModel.getIsSelectedthursday()){
            tv_medicine_details_day_thursday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_thursday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_thursday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_thursday.setBackgroundResource(R.color.bg_unselected_day);
        }
        if (medicineModel.getIsSelectedfriday()){
            tv_medicine_details_day_friday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_friday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_friday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_friday.setBackgroundResource(R.color.bg_unselected_day);
        }
        if (medicineModel.getIsSelectedsaturday()){
            tv_medicine_details_day_saturday.setBackgroundResource(R.color.bg_selected_day);
            tv_medicine_details_day_saturday.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_check_black_24dp),null);
        } else {
            tv_medicine_details_day_saturday.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            tv_medicine_details_day_saturday.setBackgroundResource(R.color.bg_unselected_day);
        }
        if (medicineModel.getLocalImagePath()!= null && !medicineModel.getLocalImagePath().equalsIgnoreCase("")) {
            File imgFile = new File(medicineModel.getLocalImagePath());
            Picasso.with(this).load(imgFile).placeholder(R.drawable.placeholder).into(img_medicine_details_med_image);
        }
    }

    private void setActionBar() {
        findViewById(R.id.img_actionbar_menu).setVisibility(View.GONE);
        findViewById(R.id.img_actionbar_back).setVisibility(View.VISIBLE);
        findViewById(R.id.img_actionbar_edit).setVisibility(View.VISIBLE);
        TextView tv_actionbar_title = findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText(getResources().getString(R.string.medicine_details));
        findViewById(R.id.img_actionbar_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editMedicineActivity = new Intent(MedicineDetails.this,AddMedicine.class);
                startActivity(editMedicineActivity);
            }
        });

        findViewById(R.id.img_actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setInitUI() {
        tv_medicine_details_medication_name = findViewById(R.id.tv_medicine_details_medication_name);
        tv_medicine_details_dosage = findViewById(R.id.tv_medicine_details_dosage);
        tv_medicine_details_unit = findViewById(R.id.tv_medicine_details_unit);
        tv_medicine_details_start_date = findViewById(R.id.tv_medicine_details_start_date);
        tv_medicine_details_end_date = findViewById(R.id.tv_medicine_details_end_date);
        tv_medicine_details_frequency = findViewById(R.id.tv_medicine_details_frequency);
        tv_medicine_details_how_many_times_per_day = findViewById(R.id.tv_medicine_details_how_many_times_per_day);
        tv_medicine_details_total_taken = findViewById(R.id.tv_medicine_details_total_taken);
        tv_medicine_details_total_skipped = findViewById(R.id.tv_medicine_details_total_skipped);
        tv_medicine_details_day_sunday = findViewById(R.id.tv_medicine_details_day_sunday);
        tv_medicine_details_day_monday = findViewById(R.id.tv_medicine_details_day_monday);
        tv_medicine_details_day_tuesday = findViewById(R.id.tv_medicine_details_day_tuesday);
        tv_medicine_details_day_wednesday = findViewById(R.id.tv_medicine_details_day_wednesday);
        tv_medicine_details_day_thursday = findViewById(R.id.tv_medicine_details_day_thursday);
        tv_medicine_details_day_friday = findViewById(R.id.tv_medicine_details_day_friday);
        tv_medicine_details_day_saturday = findViewById(R.id.tv_medicine_details_day_saturday);
        rv_medicine_details_time_slot = findViewById(R.id.rv_medicine_details_time_slot);
        img_medicine_details_med_image = findViewById(R.id.img_medicine_details_med_image);
    }
    private void setTimeSlot() {
        for (int i=0;i<medicineModel.getHowManyTimes();i++){
            switch (i){
                case 0:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_1(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_1()+")");
                    break;
                case 1:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_2(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_2()+")");
                    break;
                case 2:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_3(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_3()+")");
                    break;
                case 3:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_4(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_4()+")");
                    break;
                case 4:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_5(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_5()+")");
                    break;
                case 5:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_6(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_6()+")");
                    break;
                case 6:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_7(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_7()+")");
                    break;
                case 7:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_8(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_8()+")");
                    break;
                case 8:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_9(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_9()+")");
                    break;
                case 9:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_10(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_10()+")");
                    break;
                case 10:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_11(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_11()+")");
                    break;
                case 11:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_12(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_12()+")");
                    break;
                case 12:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_13(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_13()+")");
                    break;
                case 13:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_14(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_14()+")");
                    break;
                case 14:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_15(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_15()+")");
                    break;
                case 15:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_16(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_16()+")");
                    break;
                case 16:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_17(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_17()+")");
                    break;
                case 17:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_18(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_18()+")");
                    break;
                case 18:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_19(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_19()+")");
                    break;
                case 19:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_20(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_20()+")");
                    break;
                case 20:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_21(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_21()+")");
                    break;
                case 21:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_22(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_22()+")");
                    break;
                case 22:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_23(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_23()+")");
                    break;
                case 23:
                    list.add(Utils.getFormatedDateTime(medicineModel.getTimeSlot_24(),"hh:mm")+" (take "+medicineModel.getMedicineCountSlot_24()+")");
                    break;

            }
        }
        setTimeSlotAdapter();
    }
}
