package com.peeyemcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peeyem.app.R;

import java.util.ArrayList;


/**
 * Created by ramees on 8/16/2016.
 */
public class DrawerListAdapter extends BaseAdapter {
    private Activity activity;
    private int[] imageId;
    private static LayoutInflater inflater = null;
    private ArrayList<String> titles;
//    Typeface font;

    public DrawerListAdapter(Activity activity, ArrayList<String> titles, int[] icons) {
// TODO Auto-generated constructor stub
        this.titles = titles;
        this.activity = activity;
        imageId = icons;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        font = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
// TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView txtTitle;
        ImageView ivIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        Holder holder = new Holder();
        View view;
        view = inflater.inflate(R.layout.layout_drawer_item, null);

        holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        holder.ivIcon = view.findViewById(R.id.ivIcon);
//        holder.ivIcon.setTypeface(font);
        holder.txtTitle.setText(titles.get(position));
        holder.ivIcon.setImageResource(imageId[position]);
        //Picasso.with(activity.getApplicationContext()).load(imageId[position]).into(holder.ivIcon);

        return view;
    }

}
