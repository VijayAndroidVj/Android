package com.pothan.yamaha.pothanpothanyamaha.adapter;

/**
 * Created by vijay on 9/11/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pothan.yamaha.pothanpothanyamaha.R;
import com.pothan.yamaha.pothanpothanyamaha.WebViewActivity;
import com.pothan.yamaha.pothanpothanyamaha.models.CarModel;
import com.pothan.yamaha.pothanpothanyamaha.models.Group;

import java.util.ArrayList;


public class ExpandListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Group> groups;

    private Activity activity;

    public ExpandListAdapter(Activity activity, ArrayList<Group> groups) {
        this.activity = activity;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<CarModel> chList = groups.get(groupPosition).getLabreports();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {

        final CarModel reportsModel = (CarModel) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity
                    .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.card_view_list, null);
        }

        TextView country_title = (TextView) view.findViewById(R.id.country_title);
        View card_view = view.findViewById(R.id.card_view);
        card_view.setTag(reportsModel);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarModel carModel = (CarModel) view.getTag();
                if (!TextUtils.isEmpty(carModel.getUrl())) {
                    Intent in = new Intent(view.getContext(), WebViewActivity.class);
                    in.putExtra("loadUrl", carModel.getUrl());
                    view.getContext().startActivity(in);
                }
            }
        });
        TextView countryName = (TextView) view.findViewById(R.id.country_name);
        ImageView ivFileType = view.findViewById(R.id.car_photo);
        if (!TextUtils.isEmpty(reportsModel.getTopic())) {
            country_title.setVisibility(View.VISIBLE);
            country_title.setText(reportsModel.getTopic());
        } else {
            country_title.setVisibility(View.GONE);
        }
        countryName.setText(reportsModel.getModelname());
        ivFileType.setImageResource(reportsModel.getLocalImage());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<CarModel> chList = groups.get(groupPosition).getLabreports();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity
                    .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        TextView tv_report_adapter_item_notes_count = convertView.findViewById(R.id.tv_report_adapter_item_notes_count);
        tv.setText(group.getName());
        if (group.getLabreports().size() <= 0) {
            tv_report_adapter_item_notes_count.setVisibility(View.GONE);
        } else {
            tv_report_adapter_item_notes_count.setVisibility(View.VISIBLE);
            tv_report_adapter_item_notes_count.setText("" + group.getLabreports().size());
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
