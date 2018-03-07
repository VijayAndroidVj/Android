package com.example.chandru.laundry.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Order;
import com.example.chandru.laundry.Pojo.cat;
import com.example.chandru.laundry.R;

import java.util.List;

public class catAdapter extends RecyclerView.Adapter<catAdapter.MyViewHolderone> {
    private Context context;
    private List<cat> list;
    private AdapterListener listener;
    public static final int LIST_TAG = 5555;
    int selectedPosition = -1;


    public catAdapter(List<cat> list, Order mainActivity, AdapterListener listener) {
        this.list = list;
        this.context = mainActivity;
        this.listener = listener;
    }

    @Override
    public MyViewHolderone onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cat_list, parent, false);
        return new MyViewHolderone(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolderone holder, final int position) {
        cat dboard = list.get(position);
        holder.tvTwo.setText(dboard.getCategory_name());


        Glide.with(context)
                .load("http://demo.adityametals.com/" + dboard.getIcon_image())
                .override(500, 400)
                .into(holder.img);

        if (selectedPosition == position) {
            holder.tvTwo.setTextColor(Color.parseColor("#FFFFFF"));
            holder.linCat.setBackgroundColor(Color.parseColor("#1578CB"));
            //  holder.catite.setBackgroundColor(Color.parseColor("#009688"));
        } else {
            holder.tvTwo.setTextColor(Color.parseColor("#000000"));
            holder.linCat.setBackgroundColor(Color.parseColor("#ffffff"));
            // holder.catite.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.catite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectedPosition=tpos;
                notifyDataSetChanged();

            }
        });

        holder.linCat.setTag(position);

        //  holder.btnAccept.setTag(position);


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class MyViewHolderone extends RecyclerView.ViewHolder {
        private TextView tvTwo;
        private ImageView img;
        private LinearLayout linCat, catite;

        public MyViewHolderone(View view) {
            super(view);

            tvTwo = (TextView) view.findViewById(R.id.tvTwo);
            img = (ImageView) view.findViewById(R.id.img);
            linCat = (LinearLayout) view.findViewById(R.id.catlayout);
            catite = (LinearLayout) view.findViewById(R.id.linCat);

            linCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    selectedPosition = pos;
                    listener.adapterActionListener(LIST_TAG, pos);

                }
            });


//            btnAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = (int) view.getTag();
//                    listener.adapterActionListener(LIST_TAGa,pos);
//
//                }
//            });
//            btnReject = (Button) view.findViewById(R.id.btnReject);
//            btnReject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = (int) view.getTag();
//                    listener.adapterActionListener(LIST_TAGr,pos);
//
//                }
//            });
        }
    }
}
