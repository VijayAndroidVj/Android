package com.example.chandru.laundry.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chandru.laundry.DeliveryActivity;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Pojo.delivery;
import com.example.chandru.laundry.R;

import java.util.List;




public class deliveryAdapter extends RecyclerView.Adapter<deliveryAdapter.MyViewHolderone> {
    private Context context;
    private List<delivery> list;
    private AdapterListener listener;
    public static final int LIST_TAG =5555;
    int selectedPosition=-1;


    public deliveryAdapter(List<delivery> list, DeliveryActivity mainActivity, AdapterListener listener) {
        this.list=list;
        this.context=mainActivity;
        this.listener=listener;
    }

    @Override
    public MyViewHolderone onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_list,parent,false);
        return new MyViewHolderone(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolderone holder, final int position) {
        delivery dboard =list.get(position);
        holder.tvTwo.setText(dboard.getItem_name());
        holder.tvThree.setText(dboard.getQuantity());
        holder.tvFour.setText(dboard.getPrice());
        holder.tvFive.setText(dboard.getTotal());



        if(selectedPosition==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#009688"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();

            }
        });

        holder.tvTwo.setTag(position);

        //  holder.btnAccept.setTag(position);



    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolderone extends RecyclerView.ViewHolder {
        private TextView tvTwo,tvThree,tvFour,tvFive;

        private LinearLayout linCat;
        public MyViewHolderone(View view) {
            super(view);

            tvTwo = (TextView)view.findViewById(R.id.tvTwo);
            tvThree = (TextView)view.findViewById(R.id.tvThree);
            tvFour = (TextView)view.findViewById(R.id.tvFour);
            tvFive = (TextView)view.findViewById(R.id.tvFive);



            tvTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    listener.adapterActionListener(LIST_TAG,pos);
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
