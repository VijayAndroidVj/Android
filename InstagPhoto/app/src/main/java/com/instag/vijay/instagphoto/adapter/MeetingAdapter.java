package com.instag.vijay.instagphoto.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.instagphoto.CommonUtil;
import com.instag.vijay.instagphoto.EventResponse;
import com.instag.vijay.instagphoto.FavModel;
import com.instag.vijay.instagphoto.PreferenceUtil;
import com.instag.vijay.instagphoto.R;
import com.instag.vijay.instagphoto.retrofit.ApiClient;
import com.instag.vijay.instagphoto.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> implements View.OnClickListener {

    private List<FavModel> originalList;

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
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private ProgressDialog progressDoalog;

    private void initProgress() {
        if (progressDoalog == null || !progressDoalog.isShowing()) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Deleting....");
            progressDoalog.setTitle(activity.getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMeetingName;
        private Button btnMeetingJoin;

        private MyViewHolder(View view) {
            super(view);
            txtMeetingName = (TextView) view.findViewById(R.id.txtMeetingName);
            txtMeetingName.setTypeface(font, Typeface.BOLD);
            btnMeetingJoin = view.findViewById(R.id.btnMeetingJoin);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Typeface font;

    public MeetingAdapter(Activity activity, List<FavModel> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
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

        if (userModel.isFollowing()) {
            holder.btnMeetingJoin.setText(activity.getString(R.string.unfollow));
        } else {
            holder.btnMeetingJoin.setText(activity.getString(R.string.follow));
        }
        holder.btnMeetingJoin.setTag(userModel);
        holder.btnMeetingJoin.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}