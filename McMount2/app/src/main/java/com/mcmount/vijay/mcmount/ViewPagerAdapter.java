package com.mcmount.vijay.mcmount;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by vijay on 23/9/17.
 */

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    private ArrayList<BannerModel> bannerModelArrayList;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, ArrayList<BannerModel> bannerModelArrayList) {
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
    }

    @Override
    public int getCount() {
        return bannerModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        TextView txttitle;
        ImageView bannerImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.banner_item_layout, container,
                false);

        BannerModel bannerModel = bannerModelArrayList.get(position);

        // Locate the TextViews in viewpager_item.xml
        txttitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txttitle.setText(bannerModel.getTitle());
        bannerImage = (ImageView) itemView.findViewById(R.id.ivBanner);

        Glide.with(context).load(bannerModel.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bannerImage);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}