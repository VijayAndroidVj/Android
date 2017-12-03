package com.android.mymedicine.java.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mymedicine.R;

import java.util.ArrayList;


/**
 * Created by razin on 25/11/17.
 */

public class TimeSlotViewAdapter extends RecyclerView.Adapter<TimeSlotViewAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<String> list;

    public TimeSlotViewAdapter(Activity activity, ArrayList<String> list) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.adapter_time_slot_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_medicine_details_time_slot.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_medicine_details_time_slot;

        private MyViewHolder(View view) {
            super(view);
            tv_medicine_details_time_slot = view.findViewById(R.id.tv_medicine_details_time_slot);
        }
    }
}
