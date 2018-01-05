package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
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
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.Comments;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Comments> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuComments:
                Object object = v.getTag();
                if (object instanceof Comments) {
                    Comments comments = (Comments) object;
                    showMeetingtAlert(activity, comments);
                }
                break;
        }
    }


    public void showMeetingtAlert(Activity activity, final Comments comments) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(activity.getString(R.string.app_name));
        txtMessage.setText("Are you sure want to delete this comment?");
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                deleteComment(comments);
            }
        });

        dialogView.findViewById(R.id.customDialogCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void deleteComment(final Comments comments) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.delete_comment(usermail, comments.getComments_id());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        originalList.remove(comments);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMeetingName, txtMeetingComments;
        private Button btnMeetingJoin;
        private ImageView menuComments, ivProfile;

        private MyViewHolder(View view) {
            super(view);
            txtMeetingName = view.findViewById(R.id.txtMeetingName);
//            txtMeetingName.setTypeface(font, Typeface.BOLD);
            btnMeetingJoin = view.findViewById(R.id.btnMeetingJoin);
            ivProfile = view.findViewById(R.id.ivProfile);
            txtMeetingComments = view.findViewById(R.id.txtMeetingComments);
            txtMeetingComments.setVisibility(View.VISIBLE);
            menuComments = view.findViewById(R.id.menuComments);
            menuComments.setVisibility(View.VISIBLE);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Typeface font;
    private PreferenceUtil preferenceUtil;

    public CommentsAdapter(Activity activity, List<Comments> moviesList) {
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
        Comments comment = originalList.get(position);


        if (TextUtils.isEmpty(comment.getUsername())) {
            holder.txtMeetingName.setText("");
        } else {
            holder.txtMeetingName.setText(comment.getUsername());
        }

        if (TextUtils.isEmpty(comment.getComment())) {
            holder.txtMeetingComments.setText("");
        } else {
            holder.txtMeetingComments.setText(comment.getComment());
        }

        if (comment.getUser_email() != null && comment.getUser_email().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
            holder.menuComments.setOnClickListener(this);
            holder.menuComments.setTag(comment);
            holder.menuComments.setVisibility(View.VISIBLE);
        } else {
            holder.menuComments.setVisibility(View.GONE);
        }
        holder.btnMeetingJoin.setVisibility(View.GONE);

        if (comment.getProfile_image() != null && !comment.getProfile_image().isEmpty()) {

            Glide.with(activity).load(comment.getProfile_image()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivProfile);
        }


    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}