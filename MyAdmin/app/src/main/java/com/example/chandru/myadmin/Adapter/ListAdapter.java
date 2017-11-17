package com.example.chandru.myadmin.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chandru.myadmin.Listener.AdapterListener;
import com.example.chandru.myadmin.MainActivity;
import com.example.chandru.myadmin.Pojo.DashBoard;
import com.example.chandru.myadmin.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private Context context;
    private List<DashBoard>list;
    private AdapterListener listener;
    public static final int LIST_TAG =143;

    public ListAdapter(List<DashBoard> list, MainActivity mainActivity, AdapterListener listener) {
        this.list=list;
        this.context=mainActivity;
        this.listener=listener;
    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View itemView = LayoutInflater.from(context).inflate(R.layout.row_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, int position) {

        DashBoard dboard =list.get(position);
        String rowNum = String.valueOf(position + 1);
        holder.tvOne.setText(rowNum);
        holder.tvTwo.setText(dboard.getMEMBER_NAME());
        if(dboard.getSTATUS().toString().equals("0")){
            holder.tvThree.setText("Active");
        }else {
            holder.tvThree.setText("De-active");
        }

      //  holder.tvFour.setText("Edit");
        holder.tvFour.setTag(position);


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOne,tvTwo,tvThree,tvFour;
        public MyViewHolder(View view) {
            super(view);
            tvOne = (TextView)view.findViewById(R.id.tvOne);
            tvTwo = (TextView)view.findViewById(R.id.tvTwo);
            tvThree = (TextView)view.findViewById(R.id.tvThree);
            tvFour = (TextView)view.findViewById(R.id.tvFour);
            tvFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    listener.adapterActionListener(LIST_TAG,pos);

                }
            });
        }
    }
}
