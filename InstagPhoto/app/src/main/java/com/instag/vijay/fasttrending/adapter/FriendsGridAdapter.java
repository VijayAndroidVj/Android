package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.UserModel;

import java.util.ArrayList;

public class FriendsGridAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<UserModel> timeSlotList;

    public FriendsGridAdapter(Activity context, ArrayList<UserModel> timeSlotList) {
        this.activity = context;
        this.timeSlotList = timeSlotList;
    }


    @Override
    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txt_profile_name;
        ImageView iv_profile_pic;
        View cv_profile;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final UserModel userModel = timeSlotList.get(position);
        //View grid;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.profile_list_item, null);
            holder.txt_profile_name = convertView.findViewById(R.id.txt_profile_name);
            holder.iv_profile_pic = convertView.findViewById(R.id.iv_profile_pic);
            holder.cv_profile = convertView.findViewById(R.id.cv_profile);
            holder.txt_profile_name.setText(userModel.getUsername());
            holder.cv_profile.setTag(userModel);
            holder.cv_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserModel userModel1 = (UserModel) v.getTag();
                    Intent intent = new Intent(activity, ProfileView.class);
                    intent.putExtra("profileId", userModel1.getEmail());
                    activity.startActivity(intent);
                }
            });
            if (userModel.getServerimage() != null && !userModel.getServerimage().isEmpty()) {
                Glide.with(activity).load(userModel.getServerimage()).centerCrop()
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_profile_pic);
            } else {
                Glide.with(activity).load(R.drawable.default_profile_photo).centerCrop()
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_profile_pic);
            }


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}