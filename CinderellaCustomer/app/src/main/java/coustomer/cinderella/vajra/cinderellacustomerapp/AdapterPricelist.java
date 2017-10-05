package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterPricelist extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterPricelist(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String fragment) {
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

            convertView = inflater.inflate(R.layout.price_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_product_name.setText(hashMapArrayList.get(posistion).get("product_name"));
        mViewHolder.txt_price.setText(hashMapArrayList.get(posistion).get("product_price"));

//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_price;
        private final TextView txt_product_name;

        public MyViewHolder(View item) {
            txt_price = (TextView) item.findViewById(R.id.txt_price);
            txt_product_name = (TextView) item.findViewById(R.id.txt_product);



        }
    }


}
