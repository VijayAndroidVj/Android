package com.android.mymedicine.java.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.MedicineDetails;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.db.DbStructure;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by razin on 25/11/17.
 */

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<MedicineModel> medicineList;
    DbService dbService;

    public MedicineListAdapter(Activity activity,ArrayList<MedicineModel> medicineList) {
        this.activity = activity;
        this.medicineList = medicineList;
        layoutInflater = LayoutInflater.from(activity);
        dbService = new DbService(activity);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.adapter_home_medicine_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final MedicineModel medicineModel = medicineList.get(position);
        if (medicineModel.getLocalImagePath()!= null && !medicineModel.getLocalImagePath().equalsIgnoreCase("")) {
            File imgFile = new File(medicineModel.getLocalImagePath());
            Picasso.with(activity).load(imgFile).placeholder(R.drawable.placeholder).into(holder.img_medicine_adapter_med_image);
        }
        holder.tv_medicine_adapter_med_name.setText(medicineModel.getMedicineName());
        holder.tv_medicine_adapter_med_next_time.setText(Utils.getFormatedDateTime(medicineModel.getNextNotificationTime(),"hh:mm"));
        holder.tv_medicine_adapter_med_start_date.setText(Utils.getFormatedDateTime(medicineModel.getStartDate(),"dd-MMM-yyyy"));
        holder.tv_medicine_adapter_med_per_day.setText(medicineModel.getDosage()+" "+medicineModel.getDosageType()+" a day");
        holder.smContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent medicineDetailsActivity = new Intent(activity, MedicineDetails.class);
                medicineDetailsActivity.putExtra("medicine_id",medicineModel.getMedicineId());
                activity.startActivity(medicineDetailsActivity);
            }
        });

        holder.tv_left_swipe_taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbService.updateMedicine(DbStructure.FIELDMEDICINETAKEN,medicineModel.getMedicineId());
            }
        });

        holder.tv_left_swipe_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbService.updateMedicine(DbStructure.FIELDMEDICINESKIPPED,medicineModel.getMedicineId());
            }
        });


    }

    private long convertDate(String date){
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

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_medicine_adapter_med_name;
        TextView tv_medicine_adapter_med_next_time;
        TextView tv_medicine_adapter_med_start_date;
        TextView tv_medicine_adapter_med_per_day;
        TextView tv_left_swipe_taken;
        TextView tv_left_swipe_skip;
        View smContentView;
        CircleImageView img_medicine_adapter_med_image;

        private MyViewHolder(View view) {
            super(view);
            smContentView = view.findViewById(R.id.smContentView);
            img_medicine_adapter_med_image = view.findViewById(R.id.img_medicine_adapter_med_image);
            tv_medicine_adapter_med_name = view.findViewById(R.id.tv_medicine_adapter_med_name);
            tv_medicine_adapter_med_next_time = view.findViewById(R.id.tv_medicine_adapter_med_next_time);
            tv_medicine_adapter_med_start_date = view.findViewById(R.id.tv_medicine_adapter_med_start_date);
            tv_medicine_adapter_med_per_day = view.findViewById(R.id.tv_medicine_adapter_med_per_day);
            tv_left_swipe_taken = view.findViewById(R.id.tv_left_swipe_taken);
            tv_left_swipe_skip = view.findViewById(R.id.tv_left_swipe_skip);
           }
    }
}
