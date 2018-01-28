package com.pace.honda.pacehonda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pace.honda.pacehonda.R;
import com.pace.honda.pacehonda.data.ItemObject;
import com.pace.honda.pacehonda.holder.DownloadRecyclerViewHolders;

import java.util.List;

public class DownloadRecyclerViewAdapter extends RecyclerView.Adapter<DownloadRecyclerViewHolders> {

    private List<ItemObject> itemList;
    private Context context;

    public DownloadRecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public DownloadRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list, null);
        DownloadRecyclerViewHolders rcv = new DownloadRecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(DownloadRecyclerViewHolders holder, int position) {
        holder.countryName.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}