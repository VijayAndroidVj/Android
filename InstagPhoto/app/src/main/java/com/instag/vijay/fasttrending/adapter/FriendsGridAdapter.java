package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.UserModel;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsGridAdapter extends BaseAdapter {
    private Activity activity;
    private String title;
    private ArrayList<UserModel> timeSlotList;

    public FriendsGridAdapter(Activity context, ArrayList<UserModel> timeSlotList) {
        this.activity = context;
        this.timeSlotList = timeSlotList;
    }

    public FriendsGridAdapter(Activity context, ArrayList<UserModel> timeSlotList, String title) {
        this.activity = context;
        this.title = title;
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
        TextView txt_profile_follow;
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
            holder.txt_profile_follow = convertView.findViewById(R.id.txt_profile_follow);
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

            if (title != null && !title.equalsIgnoreCase("friends")) {
                holder.txt_profile_follow.setVisibility(View.VISIBLE);
                if (userModel.isFollow()) {
                    holder.txt_profile_follow.setText("Unfollow");
                } else {
                    holder.txt_profile_follow.setText("Follow");
                }
                holder.txt_profile_follow.setTag(userModel);
                holder.txt_profile_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserModel userModel1 = (UserModel) v.getTag();
                        followUser(userModel1);
                    }
                });
            } else {
                holder.txt_profile_follow.setVisibility(View.GONE);
            }


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private void followUser(final UserModel userModel) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            String followermail = userModel.getEmail();

            if (!TextUtils.isEmpty(followermail)) {
                Call<EventResponse> call = service.add_follow(usermail, followermail, !userModel.isFollow());
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        EventResponse patientDetails = response.body();
                        Log.i("patientDetails", response.toString());
                        if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                            userModel.setFollow(!userModel.isFollow());
                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "UserModel not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


}