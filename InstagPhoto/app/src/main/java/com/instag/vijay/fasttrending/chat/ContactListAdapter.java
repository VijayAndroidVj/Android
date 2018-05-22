package com.instag.vijay.fasttrending.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.Utils.Utils;
import com.instag.vijay.fasttrending.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vijay on 1/5/18.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private List<UserModel> contact_list;
    private ArrayList<UserModel> cloneContact_list;

    public void filter(String newText) {
        contact_list.clear();
        if (TextUtils.isEmpty(newText)) {
            contact_list = (List<UserModel>) cloneContact_list.clone();
        } else {
            for (int i = 0; i < cloneContact_list.size(); i++) {
                UserModel userModel = cloneContact_list.get(i);
                if ((userModel.getName() != null && userModel.getName().toLowerCase().contains(newText.trim().toLowerCase()) || (userModel.getUserId() != null && userModel.getUserId().toLowerCase().contains(newText.trim().toLowerCase()) || (userModel.getDisplayName() != null && userModel.getDisplayName().toLowerCase().contains(newText.trim().toLowerCase()))))) {
                    contact_list.add(userModel);
                }
            }
        }
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_contact_list_item_name;
        private TextView tv_contact_list_item_phone;
        private TextView tv_contact_list_item_alphabet;
        private CircleImageView iv_contact_list_item_alphabet;
        private TextView tv_contact_list_time;
        private TextView tv_contact_list_item_count;
        private View rl_contact_list_item_container;
        private ImageView iv_contact_list_item_phone;

        private MyViewHolder(View view) {
            super(view);
            tv_contact_list_item_name = view.findViewById(R.id.tv_contact_list_item_name);
            tv_contact_list_item_phone = view.findViewById(R.id.tv_contact_list_item_phone);
            tv_contact_list_item_alphabet = view.findViewById(R.id.tv_contact_list_item_alphabet);
            iv_contact_list_item_alphabet = view.findViewById(R.id.iv_contact_list_item_alphabet);
            rl_contact_list_item_container = view.findViewById(R.id.rl_contact_list_item_container);
            iv_contact_list_item_phone = view.findViewById(R.id.iv_contact_list_item_phone);
            tv_contact_list_time = view.findViewById(R.id.tv_contact_list_time);
            tv_contact_list_item_count = view.findViewById(R.id.tv_contact_list_item_count);

        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private ColorStateList deliverColors = Utils.getColorStateList("#808080");
    private ColorStateList seenColors = Utils.getColorStateList("#57C9FE");
    private String currDate;

    public ContactListAdapter(Activity activity, ArrayList<UserModel> contact_list) {
        this.contact_list = contact_list;
        this.cloneContact_list = (ArrayList<UserModel>) contact_list.clone();
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currDate = df.format(calendar.getTime());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserModel userModel = contact_list.get(position);

        try {
            if (activity instanceof ChatListActivity) {
                if (TextUtils.isEmpty(userModel.getDisplayName())) {
                    if (TextUtils.isEmpty(userModel.getName())) {
                        holder.tv_contact_list_item_name.setText(userModel.getUserId());
                        holder.tv_contact_list_item_phone.setVisibility(View.GONE);
                        holder.tv_contact_list_item_alphabet.setText(userModel.getUserId().substring(0, 1).toUpperCase());
                    } else {
                        holder.tv_contact_list_item_name.setText(userModel.getName());
                        holder.tv_contact_list_item_phone.setVisibility(View.VISIBLE);
                        holder.tv_contact_list_item_phone.setText(userModel.getMessage());
                        holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
                    }
                } else {
                    holder.tv_contact_list_item_name.setText(userModel.getDisplayName());
                    holder.tv_contact_list_item_phone.setVisibility(View.VISIBLE);
                    holder.tv_contact_list_item_phone.setText(userModel.getName());
                    holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
                }
            } else {
                if (TextUtils.isEmpty(userModel.getName())) {
                    holder.tv_contact_list_item_name.setText(userModel.getUserId());
                    holder.tv_contact_list_item_phone.setVisibility(View.GONE);
                    holder.tv_contact_list_item_alphabet.setText(userModel.getUserId().substring(0, 1).toUpperCase());
                } else {
                    holder.tv_contact_list_item_name.setText(userModel.getName());
                    holder.tv_contact_list_item_phone.setVisibility(View.VISIBLE);
                    holder.tv_contact_list_item_phone.setText(userModel.getMessage());
                    holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
                }
            }
//            holder.tv_contact_list_item_alphabet.setBackgroundColor(userModel.getColorState());
            if (userModel.getStatus() == 0) {
                holder.iv_contact_list_item_phone.setVisibility(View.VISIBLE);
                if (userModel.getMessageSeenStatus() == -1) {
                    holder.iv_contact_list_item_phone.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.iv_contact_list_item_phone.setImageTintList(deliverColors);
                    }
                } else if (userModel.getMessageSeenStatus() == 0) {
                    holder.iv_contact_list_item_phone.setImageResource(R.drawable.ic_done_black);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.iv_contact_list_item_phone.setImageTintList(deliverColors);
                    }
                } else if (userModel.getMessageSeenStatus() == 1) {
                    holder.iv_contact_list_item_phone.setImageResource(R.drawable.double_tick);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.iv_contact_list_item_phone.setImageTintList(deliverColors);
                    }
                } else if (userModel.getMessageSeenStatus() == 2) {
                    holder.iv_contact_list_item_phone.setImageResource(R.drawable.double_tick);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.iv_contact_list_item_phone.setImageTintList(seenColors);
                    }
                }
            } else {
                holder.iv_contact_list_item_phone.setVisibility(View.GONE);
            }

            if (userModel.getImage() != null && !userModel.getImage().isEmpty()) {
                holder.iv_contact_list_item_alphabet.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_alphabet.setVisibility(View.GONE);
                Glide.with(activity).load(userModel.getImage()).bitmapTransform(new LogListAdapter.CircleTransform(activity)).centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_contact_list_item_alphabet);
            } else {
                holder.iv_contact_list_item_alphabet.setVisibility(View.GONE);
                holder.tv_contact_list_item_alphabet.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
            }


            if (userModel.getDate() != null && !userModel.getDate().equalsIgnoreCase(currDate)) {
                holder.tv_contact_list_time.setText(userModel.getDate());
            } else {
                holder.tv_contact_list_time.setText(userModel.getTime());
            }

            if (userModel.getMimeType() != null && userModel.getMimeType().contains("text")) {
                holder.tv_contact_list_item_phone.setText(userModel.getMessage());
            } else {
                holder.tv_contact_list_item_phone.setText(userModel.getFileName());
            }
            if (userModel.getUnseenMessageCount() == 0) {
                holder.tv_contact_list_item_count.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_contact_list_item_count.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_count.setText("" + userModel.getUnseenMessageCount());
            }

            holder.rl_contact_list_item_container.setTag(userModel);
            holder.rl_contact_list_item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserModel userModel1 = (UserModel) v.getTag();
                    if (TextUtils.isEmpty(userModel1.getDisplayName()) && TextUtils.isEmpty(userModel1.getAgentKey())) {
                        Intent CallActivity = new Intent(activity, TrovaChat.class);
                        CallActivity.putExtra("otherUserID", userModel1.getUserId());
                        CallActivity.putExtra("otherUserName", userModel1.getName());
                        CallActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(CallActivity);
                    } else {
                        Intent CallActivity = new Intent(activity, TrovaChat.class);
                        CallActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CallActivity.putExtra("agentKey", userModel1.getAgentKey());
                        CallActivity.putExtra("isWidgetChat", true);
                        CallActivity.putExtra("incoming", 0);
                        CallActivity.putExtra("businessName", userModel1.getDisplayName());
                        activity.startActivity(CallActivity);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return contact_list == null ? 0 : contact_list.size();
    }

}