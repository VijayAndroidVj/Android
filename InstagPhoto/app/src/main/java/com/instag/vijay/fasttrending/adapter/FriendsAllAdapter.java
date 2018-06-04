package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.UserModel;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.joooonho.SelectableRoundedImageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsAllAdapter extends RecyclerView.Adapter<FriendsAllAdapter.MyViewHolder> implements View.OnClickListener {

    private List<UserModel> originalList;
    private PreferenceUtil preferenceUtil;

    private void followUser(final UserModel meetingItem) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            String followermail = meetingItem.getEmail();

            if (!TextUtils.isEmpty(followermail)) {
                Call<EventResponse> call = service.add_follow(usermail, followermail, !meetingItem.isFollow());
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        EventResponse patientDetails = response.body();
                        Log.i("patientDetails", response.toString());
                        if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                            meetingItem.setFollow(!meetingItem.isFollow());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlivImage:
                Object object = v.getTag();
                if (object instanceof UserModel) {
                    UserModel userModel = (UserModel) object;
                    Intent intent = new Intent(activity, ProfileView.class);
                    intent.putExtra("profileId", userModel.getEmail());
                    activity.startActivity(intent);
                }
                break;
            case R.id.bfollow:
                object = v.getTag();
                if (object instanceof UserModel) {
                    UserModel userModel = (UserModel) object;
                    followUser(userModel);
                }
                break;
        }
    }


    private ProgressDialog progressDoalog;

    private void initProgress(String title) {
        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage(title);
            progressDoalog.setTitle(activity.getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
            progressDoalog = null;
        }
    }

    private void closeProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.hide();
        progressDoalog = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUsername;
        private SelectableRoundedImageView ivImage;
        private ImageView pimageg;
        private View rlnotification;
        private View llrightoption;
        private View rlivImage;
        private Button bfollow;


        private MyViewHolder(View view) {
            super(view);
            txtUsername = view.findViewById(R.id.username);
            ivImage = view.findViewById(R.id.ivImage);
            pimageg = view.findViewById(R.id.pimageg);
            rlnotification = view.findViewById(R.id.rlnotification);
            llrightoption = view.findViewById(R.id.llrightoption);
            rlivImage = view.findViewById(R.id.rlivImage);
            bfollow = view.findViewById(R.id.bfollow);
        }
    }

    private Activity activity;
    private String title;
    private LayoutInflater layoutInflater;

    public FriendsAllAdapter(Activity activity, List<UserModel> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        preferenceUtil = new PreferenceUtil(activity);
        layoutInflater = LayoutInflater.from(activity);
    }

    public FriendsAllAdapter(Activity activity, List<UserModel> moviesList, String title) {
        this.originalList = moviesList;
        this.activity = activity;
        this.title = title;
        preferenceUtil = new PreferenceUtil(activity);
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public FriendsAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.notification_item, parent, false);
        return new FriendsAllAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendsAllAdapter.MyViewHolder holder, int position) {
        UserModel post = originalList.get(position);
        holder.rlnotification.setOnClickListener(this);
        holder.rlivImage.setOnClickListener(this);
        holder.rlnotification.setTag(post);
        holder.rlivImage.setTag(post);
//        holder.txtUsername.setText(post.getUsername());
//        holder.txtTitle.setText(post.getTitle());
//        holder.txtPostDescription.setText(post.getDescription());
        holder.txtUsername.setText(Html.fromHtml("<b><font color='#000000'>" + post.getUsername() + "</font></b><n></n>"));
        holder.pimageg.setVisibility(View.GONE);

        if (title != null && !title.equalsIgnoreCase("friends")) {
            holder.bfollow.setVisibility(View.VISIBLE);
            holder.bfollow.setTag(post);
            if (post.isFollow()) {
                holder.bfollow.setText("Unfollow");
            } else {
                holder.bfollow.setText("follow");
            }
            holder.bfollow.setOnClickListener(this);
        } else {
            holder.bfollow.setVisibility(View.GONE);
        }


        if (post.getServerimage() != null && !post.getServerimage().isEmpty()) {
            Glide.with(activity).load(post.getServerimage())
                    .asBitmap()
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
