package com.instag.vijay.fasttrending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.BusinessPageModel;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;

/**
 * Created by vijay on 17/5/18.
 */

public class GroupPageListAdapter extends ArrayAdapter<BusinessPageModel> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        SelectableRoundedImageView ivProfileGroupListItem;
    }

    private Context context;
    private boolean isGroup;

    public GroupPageListAdapter(Context context, ArrayList<BusinessPageModel> users) {
        super(context, R.layout.page_list_item, users);
        this.context = context;
    }

    public GroupPageListAdapter(Context context, ArrayList<BusinessPageModel> users, boolean isGroup) {
        super(context, R.layout.page_list_item, users);
        this.context = context;
        this.isGroup = isGroup;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BusinessPageModel businessPageModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.page_list_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.tvName);
            viewHolder.ivProfileGroupListItem = convertView.findViewById(R.id.ivProfileGroupListItem);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(businessPageModel.getTitle());
        if (businessPageModel.getImage() != null && !businessPageModel.getImage().isEmpty()) {
            viewHolder.ivProfileGroupListItem.setBackgroundResource(0);
            Glide.with(context).load("http://www.xooads.com/FEELOUTADMIN/img/upload/" + businessPageModel.getImage()).centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivProfileGroupListItem);
        } else {
            if (isGroup) {
                viewHolder.ivProfileGroupListItem.setBackgroundResource(R.drawable.ic_group_black_24dp);
            } else {
                viewHolder.ivProfileGroupListItem.setBackgroundResource(R.drawable.ic_flag_black_24dp);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}