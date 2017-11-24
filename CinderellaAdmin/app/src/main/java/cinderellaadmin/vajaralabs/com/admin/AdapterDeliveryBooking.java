package cinderellaadmin.vajaralabs.com.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterDeliveryBooking extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterDeliveryBooking(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            convertView = inflater.inflate(R.layout.delivery_booking_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_date.setText(hashMapArrayList.get(posistion).get("pickup_date"));
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        mViewHolder.txt_time.setText(hashMapArrayList.get(posistion).get("pickup_time"));
        mViewHolder.txt_city.setText(hashMapArrayList.get(posistion).get("city"));
        mViewHolder.txt_locality.setText(hashMapArrayList.get(posistion).get("locality"));
        mViewHolder.txt_address.setText(hashMapArrayList.get(posistion).get("address"));
        mViewHolder.txt_mobile.setText(hashMapArrayList.get(posistion).get("mobile"));
        if (Configg.getDATA(context, "type").equals("shop")) {
            mViewHolder.txt_pickup.setText("Re-Schedule");
        }
        mViewHolder.txt_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configg.getDATA(context, "type").equals("delivery")) {
                    Intent intent = new Intent(context, DeliveryPickupCustomer.class);
                    intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                    intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                    intent.putExtra("cid", hashMapArrayList.get(posistion).get("cid"));
                    intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                    intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                    intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                    intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                    intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                    intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                    intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                    intent.putExtra("booking_id", hashMapArrayList.get(posistion).get("booking_id"));
                    intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                    intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                    intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));


                    context.startActivity(intent);
                }

            }
        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_date;
        private final TextView txt_unique_code;
        private final TextView txt_time, txt_pickup, txt_city, txt_locality, txt_address, txt_mobile;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_time = (TextView) item.findViewById(R.id.txt_time);
            txt_pickup = (TextView) item.findViewById(R.id.txt_pickup);
            txt_date = (TextView) item.findViewById(R.id.txt_date);
            txt_city = (TextView) item.findViewById(R.id.txt_city);
            txt_locality = (TextView) item.findViewById(R.id.txt_locality);
            txt_address = (TextView) item.findViewById(R.id.txt_address);
            txt_mobile = (TextView) item.findViewById(R.id.txt_mobile);


        }
    }


}
