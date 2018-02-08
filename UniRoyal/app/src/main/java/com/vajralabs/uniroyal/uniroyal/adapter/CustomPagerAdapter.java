package com.vajralabs.uniroyal.uniroyal.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.BannerModel;

import java.util.List;

/**
 * Created by vijay on 5/2/18.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<BannerModel> imageList;

    public CustomPagerAdapter(Context context, List<BannerModel> imageList) {
        mContext = context;
        this.imageList = imageList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        TextView txtlabel = itemView.findViewById(R.id.txtlabel);
        if (TextUtils.isEmpty(imageList.get(position).getBanner_description())) {
            txtlabel.setText("");
            txtlabel.setVisibility(View.GONE);
        } else {
            txtlabel.setText(imageList.get(position).getBanner_description());
            txtlabel.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(imageList.get(position).getBanner_image())) {
            Glide.with(mContext).load(imageList.get(position).getBanner_image()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}