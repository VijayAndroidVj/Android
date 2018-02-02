package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.FavModel;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> implements View.OnClickListener {

    private List<FavModel> originalList;
    private PreferenceUtil preferenceUtil;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMeetingJoin:
                Object object = v.getTag();
                if (object instanceof FavModel) {
                    FavModel userModel = (FavModel) object;
                    followUser(userModel);
                }
                break;
            case R.id.rlParentMeeting:
                object = v.getTag();
                if (object instanceof FavModel) {
                    FavModel userModel = (FavModel) object;
                    PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                    String usermail = preferenceUtil.getUserMailId();
                    String followermail = usermail.equalsIgnoreCase(userModel.getWho()) ? userModel.getWhom() : userModel.getWho();
                    if (TextUtils.isEmpty(followermail)) {
                        followermail = userModel.getEmail();
                    }
                    Intent intent = new Intent(activity, ProfileView.class);
                    intent.putExtra("profileId", followermail);
                    activity.startActivity(intent);
                }
                break;
        }
    }


    public void showMeetingtAlert(Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(title);
        txtMessage.setText(message);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void followUser(final FavModel meetingItem) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            String followermail = usermail.equalsIgnoreCase(meetingItem.getWho()) ? meetingItem.getWhom() : meetingItem.getWho();
            if (TextUtils.isEmpty(followermail)) {
                followermail = meetingItem.getEmail();
            }
            if (!TextUtils.isEmpty(followermail)) {
                Call<EventResponse> call = service.add_follow(usermail, followermail, !meetingItem.isFollowing());
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        EventResponse patientDetails = response.body();
                        Log.i("patientDetails", response.toString());
                        if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                            meetingItem.setFollowing(!meetingItem.isFollowing());
                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMeetingName, txtMeetingComments;
        private Button btnMeetingJoin;
        private ImageView ivProfile;
        private View rlParentMeeting;

        private MyViewHolder(View view) {
            super(view);
            txtMeetingName = view.findViewById(R.id.txtMeetingName);
//            txtMeetingName.setTypeface(font, Typeface.BOLD);
            txtMeetingComments = view.findViewById(R.id.txtMeetingComments);
            btnMeetingJoin = view.findViewById(R.id.btnMeetingJoin);
            ivProfile = view.findViewById(R.id.ivProfile);
            rlParentMeeting = view.findViewById(R.id.rlParentMeeting);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Typeface font;

    public MeetingAdapter(Activity activity, List<FavModel> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        preferenceUtil = new PreferenceUtil(activity);
        layoutInflater = LayoutInflater.from(activity);
        font = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.meeting_list_item_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavModel userModel = originalList.get(position);
        holder.txtMeetingName.setVisibility(View.VISIBLE);
        holder.txtMeetingComments.setVisibility(View.GONE);
//        holder.txtMeetingName.setTypeface(font);
        holder.rlParentMeeting.setOnClickListener(this);
        holder.rlParentMeeting.setTag(userModel);
        if (TextUtils.isEmpty(userModel.getUserName())) {
            if (!TextUtils.isEmpty(userModel.getWhom()) && userModel.getWhom().contains("@")) {
                String[] name = userModel.getWhom().split("@");
                holder.txtMeetingName.setText(name[0]);
            } else {
                holder.txtMeetingName.setText("");
            }

        } else {
            holder.txtMeetingName.setText(userModel.getUserName());
        }

        if (preferenceUtil.getUserMailId().equalsIgnoreCase(userModel.getWho()) && preferenceUtil.getUserMailId().equalsIgnoreCase(userModel.getWhom())) {
            holder.btnMeetingJoin.setVisibility(View.GONE);
        } else {
            holder.btnMeetingJoin.setVisibility(View.VISIBLE);
            holder.btnMeetingJoin.setTypeface(font);
            if (userModel.isFollowing()) {
                holder.btnMeetingJoin.setText(activity.getString(R.string.unfollow));
            } else {
                holder.btnMeetingJoin.setText(activity.getString(R.string.follow));
            }
        }

        if (userModel.getProfile_image() != null && !userModel.getProfile_image().isEmpty()) {

            Glide.with(activity).load(userModel.getProfile_image()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivProfile);
        }
        holder.btnMeetingJoin.setTag(userModel);
        holder.btnMeetingJoin.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}