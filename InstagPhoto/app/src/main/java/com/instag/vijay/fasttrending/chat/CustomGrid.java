package com.instag.vijay.fasttrending.chat;

/**
 * Created by razin on 06/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instag.vijay.fasttrending.R;


public class CustomGrid extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private String[] menuText;
    private int[] images;
    private static Integer id = 0;
    public final static int PICK_FILE_REQUEST = 1;
    public final static int CAMERA_REQUEST = 2;
    public final static int PICK_IMAGE_REQUEST = 3;
    public final static int PICK_AUDIO_REQUEST = 4;
    public final static int PICK_VIDEO_REQUEST = 5;

    public CustomGrid(Activity context, String[] menuText, int[] images) {
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
        ImageView packageImage;
        TextView tv_menu;
        int position;
    }

    @Override
    public void onClick(View v) {
        id = ((ViewHolder) v.getTag()).position;
        TrovaChat trovaChat = (TrovaChat) activity;
        switch (id) {
            case 0:
                trovaChat.launchDocumentPicker();
                break;
            case 1:
                trovaChat.launchCameraIntent();
                break;
            case 2:
                trovaChat.launchGalleryIntent();
                break;
            case 3:
                trovaChat.launchAudioIntent();
                break;
            case 4:
                trovaChat.launchVideoIntent();
                break;
        }
        if (trovaChat.dialog != null) {
            trovaChat.dialog.dismiss();
            trovaChat.dialog = null;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        //View grid;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_layout_chat, null);
            holder.packageImage = convertView.findViewById(R.id.menu_image);
            holder.tv_menu = convertView.findViewById(R.id.tv_menu);
            holder.packageImage.setImageResource(images[position]);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.tv_menu.setText(menuText[position]);
        holder.packageImage.setOnClickListener(this);
        holder.packageImage.setTag(holder);

        return convertView;
    }

}
