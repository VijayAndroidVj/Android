package com.xr.vijay.tranzpost.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xr.vijay.tranzpost.R;
import com.xr.vijay.tranzpost.model.haltingPriceModel;

import java.util.ArrayList;


/**
 * Created by razin on 25/11/17.
 */

public class HaltingPriceAdapter extends RecyclerView.Adapter<HaltingPriceAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<haltingPriceModel> haltingPriceModelArrayList;

    public HaltingPriceAdapter(Activity activity, ArrayList<haltingPriceModel> medicineList) {
        this.activity = activity;
        this.haltingPriceModelArrayList = medicineList;
        layoutInflater = LayoutInflater.from(activity);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.halting_charge_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final haltingPriceModel medicineModel = haltingPriceModelArrayList.get(position);
        if (position == 0) {
            holder.trtitle.setVisibility(View.VISIBLE);
        } else {
            holder.trtitle.setVisibility(View.GONE);
        }
        holder.txtperday.setText(medicineModel.getDay());
        holder.txtonetothree.setText(medicineModel.getOnetothree());
        holder.txtfourtosix.setText(medicineModel.getFourtosix());
        holder.txtabovesix.setText(medicineModel.getAbovesix());
    }

    @Override
    public int getItemCount() {
        return haltingPriceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtperday;
        TextView txtonetothree;
        TextView txtfourtosix;
        TextView txtabovesix;
        View trtitle;

        private MyViewHolder(View view) {
            super(view);
            trtitle = view.findViewById(R.id.trtitle);
            txtperday = view.findViewById(R.id.txtperday);
            txtonetothree = view.findViewById(R.id.txtonetothree);
            txtfourtosix = view.findViewById(R.id.txtfourtosix);
            txtabovesix = view.findViewById(R.id.txtabovesix);
        }
    }
}
