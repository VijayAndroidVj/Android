package com.instag.vijay.fasttrending.adapter;

/**
 * Created by razin on 27/11/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instag.vijay.fasttrending.EditProfile;
import com.instag.vijay.fasttrending.R;

import static com.instag.vijay.fasttrending.EditProfile.CAMERA_REQUEST_PAN;
import static com.instag.vijay.fasttrending.EditProfile.PICK_IMAGE_REQUEST_PAN;


public class UploadPickerPopUp extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private String[] menuText;
    private int[] images;
    private static Integer id = 0;

    public UploadPickerPopUp(Activity context, String[] menuText, int[] images) {
        this.activity = context;
        this.menuText = menuText;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        LinearLayout actionLayout;
        ImageView packageImage;
        TextView menu_text;
        int position;
    }

    @Override
    public void onClick(View v) {
        id = ((ViewHolder) v.getTag()).position;

        switch (id) {
            case 0:
                EditProfile addMedicine = (EditProfile) activity;
                addMedicine.launchCameraIntent(CAMERA_REQUEST_PAN);
                break;
            case 1:
                addMedicine = (EditProfile) activity;
                addMedicine.launchGalleryIntent(PICK_IMAGE_REQUEST_PAN);
                break;
            case 2:
                addMedicine = (EditProfile) activity;
                addMedicine.removePic();
                break;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        //View grid;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_layout, null);
            holder.actionLayout = (LinearLayout) convertView.findViewById(R.id.actionLayout);
            holder.menu_text = (TextView) convertView.findViewById(R.id.menu_text);
            holder.menu_text.setText(menuText[position]);
            holder.packageImage = (ImageView) convertView.findViewById(R.id.menu_image);
            holder.packageImage.setImageResource(images[position]);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.actionLayout.setOnClickListener(this);
        holder.actionLayout.setTag(holder);

        return convertView;
    }

}
