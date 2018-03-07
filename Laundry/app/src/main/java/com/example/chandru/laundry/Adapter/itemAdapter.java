package com.example.chandru.laundry.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Order;
import com.example.chandru.laundry.Pojo.item;
import com.example.chandru.laundry.R;
import java.util.List;


public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MyViewHolderone> {
    private Context context;
    private List<item> list;
    private AdapterListener listener;
    public static final int LIST_TAGr =6666;
    int selectedPosition=-1;


    public itemAdapter(List<item> list, Order mainActivity, AdapterListener listener) {
        this.list=list;
        this.context=mainActivity;
        this.listener=listener;
    }

    @Override
    public MyViewHolderone onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        return new MyViewHolderone(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolderone holder, final int position) {
        item dboard =list.get(position);
        holder.tvTwo.setText("Rs."+dboard.getLaundry_price());
        holder.img.setText(dboard.getLaundry_item());





        if(selectedPosition==position) {
            holder.catie.setBackgroundColor(Color.parseColor("#1578CB"));
            holder.img.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.catie.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.img.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // selectedPosition=position;
                notifyDataSetChanged();

            }
        });
        holder.itemlayout.setTag(position);





    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolderone extends RecyclerView.ViewHolder {
        private TextView tvTwo,img;
        private LinearLayout itemlayout,catie;

        public MyViewHolderone(View view) {
            super(view);

            tvTwo = (TextView)view.findViewById(R.id.tvTwo);
            img = (TextView)view.findViewById(R.id.img);
            itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
            catie=(LinearLayout)view.findViewById(R.id.catie);

            itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    selectedPosition=pos;
                    listener.adapterActionListener(LIST_TAGr,pos);
                    notifyDataSetChanged();
                }
            });
           // img=(ImageView)view.findViewById(R.id.img);

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
