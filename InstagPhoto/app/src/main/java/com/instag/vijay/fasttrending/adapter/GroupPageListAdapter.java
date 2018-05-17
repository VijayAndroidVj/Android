package com.instag.vijay.fasttrending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.instag.vijay.fasttrending.R;

import java.util.ArrayList;

/**
 * Created by vijay on 17/5/18.
 */

public class GroupPageListAdapter extends ArrayAdapter<String> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
    }

    public GroupPageListAdapter(Context context, ArrayList<String> users) {
        super(context, R.layout.page_list_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.page_list_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.tvName);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(user);
        // Return the completed view to render on screen
        return convertView;
    }
}