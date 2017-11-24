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
public class AdapterPickupedCustomer extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterPickupedCustomer(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);

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

            convertView = inflater.inflate(R.layout.pickuped_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if (hashMapArrayList.get(posistion).get("tag_no").equals("")) {
            mViewHolder.tag_no.setText("Processing");
        } else {
            mViewHolder.tag_no.setText(hashMapArrayList.get(posistion).get("tag_no"));

        }
        if (hashMapArrayList.get(posistion).get("items").equals("")) {
            mViewHolder.txt_clothes.setText("Processing");

        } else {
            mViewHolder.txt_clothes.setText(hashMapArrayList.get(posistion).get("items"));

        }
        if (hashMapArrayList.get(posistion).get("overall_total").equals("")) {
            mViewHolder.txt_oveal_total.setText("Processing");

        } else {
            mViewHolder.txt_oveal_total.setText(hashMapArrayList.get(posistion).get("overall_total"));

        }
        if (hashMapArrayList.get(posistion).get("shop_code_and_name").equals("")) {
            mViewHolder.txt_shop.setText("Processing");

        } else {
            mViewHolder.txt_shop.setText(hashMapArrayList.get(posistion).get("shop_code_and_name"));

        }
        if (hashMapArrayList.get(posistion).get("shop_contact").equals("")) {
            mViewHolder.txt_shop_contact.setText("Processing");

        } else {
            mViewHolder.txt_shop_contact.setText(hashMapArrayList.get(posistion).get("shop_contact"));

        }
        if (hashMapArrayList.get(posistion).get("completion").equals("")) {
            mViewHolder.txt_status.setText("0" + "%" + " Completed");

        }
        else{
            mViewHolder.txt_status.setText(hashMapArrayList.get(posistion).get("completion") + "%" + " Completed");

        }
            mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));


//        }
        return convertView;
    }


    private class MyViewHolder {
        private final TextView tag_no;
        private final TextView txt_unique_code;
        private final TextView txt_more, txt_clothes, txt_oveal_total, txt_assign_factory;
        private final TextView txt_shop_contact, txt_shop, txt_status;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_more = (TextView) item.findViewById(R.id.txt_more);
            tag_no = (TextView) item.findViewById(R.id.tag_no);
            txt_clothes = (TextView) item.findViewById(R.id.txt_clothes);
            txt_oveal_total = (TextView) item.findViewById(R.id.txt_oveal_total);
            txt_assign_factory = (TextView) item.findViewById(R.id.txt_assign_factory);
            txt_shop_contact = (TextView) item.findViewById(R.id.txt_shop_contact);
            txt_shop = (TextView) item.findViewById(R.id.txt_shop);
            txt_status = (TextView) item.findViewById(R.id.txt_status);


        }
    }


}
