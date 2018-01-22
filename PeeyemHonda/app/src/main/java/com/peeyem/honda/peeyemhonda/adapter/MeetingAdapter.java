package com.peeyem.honda.peeyemhonda.adapter;

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
import com.peeyem.honda.peeyemhonda.CarViewActivity;
import com.peeyem.honda.peeyemhonda.R;
import com.peeyem.honda.peeyemhonda.models.CarModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CarModel> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCarItem:
                Object object = v.getTag();
                if (object instanceof CarModel) {
                    CarModel userModel = (CarModel) object;
                    Intent in = new Intent(activity, CarViewActivity.class);
                    in.putExtra("model", userModel);
                    activity.startActivity(in);

                }
                break;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCarModel, txtCarkms, txtCarprice;
        private TextView txtCaryear;
        private ImageView ivCar;
        private View rlCarItem;

        private MyViewHolder(View view) {
            super(view);
            txtCarModel = view.findViewById(R.id.txtCarModel);
//            txtCarModel.setTypeface(font, Typeface.BOLD);
            txtCarkms = view.findViewById(R.id.txtCarkms);
            txtCaryear = view.findViewById(R.id.txtCaryear);
            ivCar = view.findViewById(R.id.ivCar);
            txtCarprice = view.findViewById(R.id.txtCarprice);
            rlCarItem = view.findViewById(R.id.rlCarItem);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    String string = "\u20B9";

    public MeetingAdapter(Activity activity, List<CarModel> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        byte[] utf8 = null;
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CarModel userModel = originalList.get(position);
        holder.rlCarItem.setOnClickListener(this);
        holder.rlCarItem.setTag(userModel);
        if (TextUtils.isEmpty(userModel.getModelname())) {
            holder.txtCarModel.setText("");
        } else {
            holder.txtCarModel.setText(userModel.getModelname());
        }

        if (TextUtils.isEmpty(userModel.getKmsRunned())) {
            holder.txtCarkms.setText("");
        } else {
            holder.txtCarkms.setText("KMS RUN : " + userModel.getKmsRunned());
        }

        if (TextUtils.isEmpty(userModel.getYear())) {
            holder.txtCaryear.setText("");
        } else {
            holder.txtCaryear.setText("YEAR : " + userModel.getYear());
        }

        if (TextUtils.isEmpty(userModel.getPrice())) {
            holder.txtCarprice.setText("");
        } else {
            holder.txtCarprice.setText("PRICE : " + string + " " + userModel.getPrice());
        }

        if (userModel.getCarImagePath() != null && !userModel.getCarImagePath().isEmpty()) {

            Glide.with(activity).load(userModel.getCarImagePath()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivCar);
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}