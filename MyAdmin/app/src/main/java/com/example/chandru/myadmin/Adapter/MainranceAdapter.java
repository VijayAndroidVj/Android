package com.example.chandru.myadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chandru.myadmin.Listener.AdapterListener;
import com.example.chandru.myadmin.MaintenceActivity;
import com.example.chandru.myadmin.Pojo.Maintain;
import com.example.chandru.myadmin.R;

import java.util.List;

/**
 * Created by chandru on 10/13/2017.
 */

public class MainranceAdapter extends RecyclerView.Adapter<MainranceAdapter.MyViewHolderone> {
    private Context context;
    private List<Maintain> list;
    private AdapterListener listener;
    public static final int LIST_TAGs =5643;

    public MainranceAdapter(List<Maintain> list, MaintenceActivity mainActivity, AdapterListener listener) {
        this.list=list;
        this.context=mainActivity;
        this.listener=listener;
    }

    @Override
    public MainranceAdapter.MyViewHolderone onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_list,parent,false);
        return new MainranceAdapter.MyViewHolderone(itemView);
    }

    @Override
    public void onBindViewHolder(MainranceAdapter.MyViewHolderone holder, int position) {
        Maintain dboard =list.get(position);
        String rowNum = String.valueOf(position + 1);
        holder.tvOne.setText(rowNum);

        holder.tvTwo.setText(dboard.getMaintancename());

            holder.tvThree.setText(dboard.getAmount());


        //  holder.tvFour.setText("Edit");
        holder.tvFour.setTag(position);


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolderone extends RecyclerView.ViewHolder {
        private TextView tvOne,tvTwo,tvThree,tvFour;
        public MyViewHolderone(View view) {
            super(view);
            tvOne = (TextView)view.findViewById(R.id.tvOne);
            tvTwo = (TextView)view.findViewById(R.id.tvTwo);
            tvThree = (TextView)view.findViewById(R.id.tvThree);
            tvFour = (TextView)view.findViewById(R.id.tvFour);
            tvFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    listener.adapterActionListener(LIST_TAGs,pos);

                }
            });
        }
    }
}
