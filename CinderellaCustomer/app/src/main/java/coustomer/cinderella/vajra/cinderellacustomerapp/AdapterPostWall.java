package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterPostWall extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterPostWall(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String fragment) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return hashMapArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hashMapArrayList.size();
    }

    @Override
    public View getView(final int posistion, View convertView, ViewGroup viewGroup) {

        MyViewHolder mViewHolder;
//
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.wall_post2, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_about.setText(hashMapArrayList.get(posistion).get("about_wall"));
//        mViewHolder.txt_des.setText(hashMapArrayList.get(posistion).get("des_wall"));
        mViewHolder.linear_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context,ViewProdutDetail.class);
//                intent.putExtra("title",hashMapArrayList.get(posistion).get("about_wall"));
//                intent.putExtra("des",hashMapArrayList.get(posistion).get("des_wall"));
//                intent.putExtra("path",hashMapArrayList.get(posistion).get("image_path"));
//
//                context.startActivity(intent);
            }
        });

        Picasso.with(context).load(hashMapArrayList.get(posistion).get("image_path")).into(mViewHolder.img_wall);

//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_about,txt_des;
        private final LinearLayout linear_5;
        //        private final TextView txt_vehicle_no,txt_driver;
//        private final Button btn_track;
        private ImageView img_wall;
        public MyViewHolder(View item) {
            txt_about = (TextView) item.findViewById(R.id.txt_about);
            txt_des = (TextView) item.findViewById(R.id.txt_des);

            img_wall=(ImageView) item.findViewById(R.id.img_wall);
            linear_5=(LinearLayout) item.findViewById(R.id.linear_5);
//            txt_vehicle_no = (TextView) item.findViewById(R.id.txt_vehicle_no);
//            txt_driver = (TextView) item.findViewById(R.id.txt_driver);
//            btn_track=(Button) item.findViewById(R.id.btn_track);





        }
    }


}
