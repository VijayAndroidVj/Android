package com.instag.vijay.fasttrending.chat;

import android.app.Activity;
import android.content.Intent;
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
import com.instag.vijay.fasttrending.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vijay on 1/5/18.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private List<UserModel> contact_list;
    private List<UserModel> cloneContact_list;

    public void filter(String newText) {
        contact_list.clear();
        if (TextUtils.isEmpty(newText)) {
            contact_list = cloneContact_list;
        } else {
            for (int i = 0; i < cloneContact_list.size(); i++) {
                UserModel userModel = cloneContact_list.get(i);
                if ((userModel.getName() != null && newText.trim().toLowerCase().equalsIgnoreCase(userModel.getName()) || (userModel.getUserId() != null && newText.trim().toLowerCase().equalsIgnoreCase(userModel.getUserId())))) {
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
        private View rl_contact_list_item_container;

        private MyViewHolder(View view) {
            super(view);
            tv_contact_list_item_name = view.findViewById(R.id.tv_contact_list_item_name);
            tv_contact_list_item_phone = view.findViewById(R.id.tv_contact_list_item_phone);
            tv_contact_list_item_alphabet = view.findViewById(R.id.tv_contact_list_item_alphabet);
            iv_contact_list_item_alphabet = view.findViewById(R.id.iv_contact_list_item_alphabet);
            rl_contact_list_item_container = view.findViewById(R.id.rl_contact_list_item_container);

        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public ContactListAdapter(Activity activity, ArrayList<UserModel> contact_list) {
        this.contact_list = contact_list;
        this.cloneContact_list = (List<UserModel>) contact_list.clone();
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
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
            if (TextUtils.isEmpty(userModel.getName())) {
                holder.tv_contact_list_item_name.setText(userModel.getUserId());
                holder.tv_contact_list_item_phone.setVisibility(View.GONE);
            } else {
                holder.tv_contact_list_item_name.setText(userModel.getName());
                holder.tv_contact_list_item_phone.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_phone.setText(userModel.getUserId());
                holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
            }
            if (userModel.getImage() != null && !userModel.getImage().isEmpty()) {
                holder.iv_contact_list_item_alphabet.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_alphabet.setVisibility(View.GONE);
                Glide.with(activity).load(userModel.getImage()).transform(new LogListAdapter.CircleTransform(activity)).centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_contact_list_item_alphabet);
            } else {
                holder.iv_contact_list_item_alphabet.setVisibility(View.GONE);
                holder.tv_contact_list_item_alphabet.setVisibility(View.VISIBLE);
                holder.tv_contact_list_item_alphabet.setText(userModel.getName().substring(0, 1).toUpperCase());
            }
            holder.rl_contact_list_item_container.setTag(userModel);
            holder.rl_contact_list_item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserModel userModel1 = (UserModel) v.getTag();
                    Intent CallActivity = new Intent(activity, TrovaChat.class);
                    CallActivity.putExtra("otherUserID", userModel1.getUserId());
                    CallActivity.putExtra("otherUserName", userModel1.getName());
                    CallActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(CallActivity);
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