package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.PostView;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.Notification;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.joooonho.SelectableRoundedImageView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 24/12/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Notification> originalList;
    private PreferenceUtil preferenceUtil;

    private void followUser(final Notification meetingItem) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            String followermail = meetingItem.getFrom_email();

            if (!TextUtils.isEmpty(followermail)) {
                Call<EventResponse> call = service.add_follow(usermail, followermail, !meetingItem.getFollowing());
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        EventResponse patientDetails = response.body();
                        Log.i("patientDetails", response.toString());
                        if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                            meetingItem.setFollowing(!meetingItem.getFollowing());
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
                if (object instanceof Notification) {
                    Notification userModel = (Notification) object;
                    Intent intent = new Intent(activity, ProfileView.class);
                    intent.putExtra("profileId", userModel.getFrom_email());
                    activity.startActivity(intent);
                }
                break;
            case R.id.bfollow:
                object = v.getTag();
                if (object instanceof Notification) {
                    Notification userModel = (Notification) object;
                    followUser(userModel);
                }
                break;
            case R.id.btnpostDelete:
                object = v.getTag();
                if (object instanceof Notification) {
                    Notification post = (Notification) object;
                    showMeetingtAlert(activity, activity.getString(R.string.app_name), "Are you sure want to delete this post?", post);
                }
                break;
            case R.id.rlnotification:
                object = v.getTag();
                if (object instanceof Notification) {
                    Notification notification = (Notification) object;
                    if (notification.getType() != null && notification.getType().equalsIgnoreCase("follow")) {
//                        Intent intent = new Intent(activity, ProfileView.class);
//                        intent.putExtra("profileId", notification.get());
//                        activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, PostView.class);
                        intent.putExtra("postId", notification.getPost_id());
                        activity.startActivity(intent);
                    }
                }
                break;
        }
    }


    private void showMeetingtAlert(Activity activity, String title, String message, final Notification post) {

        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
//                .setCustomImage(R.drawable.app_logo_back)
                .setCancelText("No").setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        deletePost(post);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);
        Button dialogButtonCancel = (Button) dialogView.findViewById(R.id.customDialogCancel);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(title);
        txtMessage.setText(message);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post);
                alertDialog.dismiss();
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();*/
    }

    private void deletePost(final Notification post) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Deleting....");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.delete_post(usermail, post.getNotificationid());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        originalList.remove(post);
                        notifyDataSetChanged();
                        closeProgress();

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
                    closeProgress();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
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
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Activity activity, List<Notification> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        preferenceUtil = new PreferenceUtil(activity);
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification post = originalList.get(position);
        holder.rlnotification.setOnClickListener(this);
        holder.rlivImage.setOnClickListener(this);
        holder.rlnotification.setTag(post);
        holder.rlivImage.setTag(post);
//        holder.txtUsername.setText(post.getUsername());
//        holder.txtTitle.setText(post.getTitle());
//        holder.txtPostDescription.setText(post.getDescription());
        if (post.getType().equalsIgnoreCase("follow")) {
            if (TextUtils.isEmpty(post.getTo_email()) || post.getTo_email().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                holder.bfollow.setVisibility(View.VISIBLE);
                holder.bfollow.setTag(post);
                if (post.getFollowing()) {
                    holder.bfollow.setText("Unfollow");
                } else {
                    holder.bfollow.setText("follow");
                }
                holder.bfollow.setOnClickListener(this);
                holder.txtUsername.setText(Html.fromHtml("<b><font color='#000000'>" + post.getUsername() + "</font></b><n> started following you.</n>"));
            } else {
                holder.txtUsername.setText(Html.fromHtml("<b><font color='#000000'>" + post.getUsername() + "</font></b><n> started following " + post.getTousername() + ".</n>"));
                holder.bfollow.setVisibility(View.GONE);
            }

            holder.pimageg.setVisibility(View.GONE);

        } else {
            holder.bfollow.setVisibility(View.GONE);
            holder.pimageg.setVisibility(View.VISIBLE);
            String name = "your";
            if (TextUtils.isEmpty(post.getTo_email()) || post.getTo_email().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                name = "your";
            } else {
                name = post.getTousername() + "'s";
            }
            if (post.getType().equalsIgnoreCase("like")) {
                holder.txtUsername.setText(Html.fromHtml("<b><font color='#000000'>" + post.getUsername() + "</font></b><n> liked " + name + " post.</n>"));
            } else {
                holder.txtUsername.setText(Html.fromHtml("<b><font color='#000000'>" + post.getUsername() + "</font></b><n> commented " + name + " post.</n>"));
            }
            holder.pimageg.setImageBitmap(null);
            if (post.getPostimage() != null && !post.getPostimage().isEmpty()) {
                if (post.getFileType().equalsIgnoreCase("2")) {
                    byte[] decodedString = Base64.decode(post.getVideoThumb(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (decodedByte != null) {
                        holder.pimageg.setImageBitmap(decodedByte);
                    }
                } else {
                    Glide.with(activity).load(post.getPostimage()).centerCrop()
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.pimageg);
                }
            }
        }
        if (post.getProfile_image() != null && !post.getProfile_image().isEmpty()) {
            Glide.with(activity).load(post.getProfile_image())
                    .asBitmap()
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
