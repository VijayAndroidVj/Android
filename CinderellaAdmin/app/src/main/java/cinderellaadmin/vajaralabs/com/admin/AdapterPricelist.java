package cinderellaadmin.vajaralabs.com.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
    private boolean boolean_chk;
     double  over_all_total=0.0;

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
//        mViewHolder.linear_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater inflater_custom = (LayoutInflater) context
//                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
//
//
//                HashMap<String, String> hashMap;
//
//                View convertView = inflater_custom.inflate(R.layout.price_tag, null);
//
//                final TextView txt_product = (TextView) convertView.findViewById(R.id.txt_product);
//                final TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
//                final TextView txt_total = (TextView) convertView.findViewById(R.id.txt_total);
//                EditText edt_count = (EditText) convertView.findViewById(R.id.edt_count);
//                edt_count.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        if (!s.toString().equals("")) {
//                            int count_value = Integer.parseInt(s.toString());
//                            double rate = Double.parseDouble(txt_price.getText().toString());
//                            double total = count_value * rate;
//                            txt_total.setText(String.valueOf(total));
////                            if (!DeliveryPickupCustomer.txt_oveal_total.getText().toString().equals("")) {
//                            over_all_total+=total;
////                                over_all_total = Double.parseDouble(DeliveryPickupCustomer.txt_oveal_total.getText().toString()) + total;
//                                DeliveryPickupCustomer.txt_oveal_total.setText(String.valueOf(over_all_total));
////                            }
//
//
////                            Toast.makeText(context,"to"+rate,Toast.LENGTH_SHORT).show();
//
//
//
//
////                            for (int i = 0; i < DeliveryPickupCustomer.list_itemLinearLayout.getChildCount(); i++) {
////                                final TextView txt_price_2 = (TextView) DeliveryPickupCustomer.list_itemLinearLayout.findViewById(R.id.txt_price);
////                                double rate_2 = Double.parseDouble(txt_price_2.getText().toString());
////
////                                over_all_total=over_all_total+rate_2;
////                                DeliveryPickupCustomer.txt_oveal_total.setText(String.valueOf(over_all_total));
////
////                            }
//                        } else {
//                            txt_total.setText("");
//                        }
//
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                txt_product.setText(hashMapArrayList.get(posistion).get("product_name"));
//                txt_price.setText(hashMapArrayList.get(posistion).get("product_price"));
////                        txt_total.setText(hashMapArrayList.get(i).get("product_name"));
////                for (int i = 0; i < DeliveryPickupCustomer.list_itemLinearLayout.getChildCount(); i++) {
////                    final TextView txt_product_2 = (TextView) DeliveryPickupCustomer.list_itemLinearLayout.findViewById(R.id.txt_product);
////
////                    if (hashMapArrayList.get(posistion).get("product_name").equals(txt_product_2.getText()
////                            .toString())) {
////                        boolean_chk=true;
////
////                    }
////                }
//
//                DeliveryPickupCustomer.list_itemLinearLayout.addView(convertView);
//
//                DeliveryPickupCustomer.dialog.dismiss();
//            }
//        });

//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_price;
        private final TextView txt_product_name;
        private final LinearLayout linear_click;

        public MyViewHolder(View item) {
            txt_price = (TextView) item.findViewById(R.id.txt_price);
            txt_product_name = (TextView) item.findViewById(R.id.txt_product);
            linear_click = (LinearLayout) item.findViewById(R.id.linear_click);


        }
    }


}
