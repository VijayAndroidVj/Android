package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul on 7/9/2017.
 */

public class OfferAdapter extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<>();

    private String str_currency = "";

    public OfferAdapter(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String fragment) {
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

            convertView = inflater.inflate(R.layout.offers_item, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txtOfferTitle.setText(hashMapArrayList.get(posistion).get("about_wall"));
//        mViewHolder.txtOfferCode.setText(hashMapArrayList.get(posistion).get("des_wall"));
        mViewHolder.txtOfferValid.setText(hashMapArrayList.get(posistion).get("des_wall"));
        mViewHolder.offer_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfferView.class);
                intent.putExtra("title", hashMapArrayList.get(posistion).get("about_wall"));
                intent.putExtra("des", hashMapArrayList.get(posistion).get("des_wall"));
                intent.putExtra("path", hashMapArrayList.get(posistion).get("image_path"));
                intent.putExtra("posted_by", hashMapArrayList.get(posistion).get("posted_by"));

                context.startActivity(intent);
            }
        });

        Picasso.with(context).load(hashMapArrayList.get(posistion).get("image_path")).into(mViewHolder.ivImageOffer);

//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txtOfferTitle, txtOfferCode;
        private final TextView txtOfferValid;
        private ImageView ivImageOffer;
        private View offer_item;

        public MyViewHolder(View item) {
            txtOfferTitle = (TextView) item.findViewById(R.id.txtOfferTitle);
            txtOfferCode = (TextView) item.findViewById(R.id.txtOfferCode);

            ivImageOffer = (ImageView) item.findViewById(R.id.ivImageOffer);
            txtOfferValid = (TextView) item.findViewById(R.id.txtOfferValid);

            offer_item = item.findViewById(R.id.offer_item);
        }
    }


}

