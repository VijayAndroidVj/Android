package com.android.mymedicine.java.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.mymedicine.R;

/**
 * Created by razin on 2/8/17.
 */

public class AppTourAdapter extends PagerAdapter {
    Context context;

    public AppTourAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.adapter_app_tour, collection, false);

        ImageView img_app_tour = (ImageView) layout.findViewById(R.id.img_app_tour);
//        if (position == 0){
//            img_app_tour.setImageDrawable(context.getResources().getDrawable(R.drawable.banner_one));
//        } else if (position == 1){
//            img_app_tour.setImageDrawable(context.getResources().getDrawable(R.drawable.banner_two));
//        } else {
//            img_app_tour.setImageDrawable(context.getResources().getDrawable(R.drawable.banner_three));
//        }
        /*i have commented below a code where you can directly bring the path of the image and set into the imageView
        * read the comments below to understand picasso*/
//        try {
//            Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    exception.printStackTrace();
//                }
//            }).build();
//            picasso.with(context).load(/*"Banner image path"*/jsonArray.getJSONObject(position).getString("image_path + name on cloud")).placeholder(/*"if you want anything to show before image loads"*/ R.drawable.place_holder).into(imgBanner);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}