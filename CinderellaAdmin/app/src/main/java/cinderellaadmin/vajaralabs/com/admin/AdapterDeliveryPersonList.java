package cinderellaadmin.vajaralabs.com.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterDeliveryPersonList extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = null;
    ArrayList<HashMap<String, String>> hashMapArrayList2;


    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterDeliveryPersonList(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.hashMapArrayList2 = new ArrayList<HashMap<String, String>>();
        this.hashMapArrayList2.addAll(hashMapArrayList);

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        hashMapArrayList.clear();
        if (charText.length() == 0) {
            hashMapArrayList.addAll(hashMapArrayList2);
        } else {
            for (HashMap<String, String> wp : hashMapArrayList2) {
                if (wp.get("delivery_person").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("shop_code_and_name").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }

                if (wp.get("mobile").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("address").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }

                if (wp.get("vehicle").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
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

            convertView = inflater.inflate(R.layout.delivery_list_update, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_name.setText(hashMapArrayList.get(posistion).get("delivery_person"));
        mViewHolder.txt_shop.setText(hashMapArrayList.get(posistion).get("shop_code_and_name"));
        mViewHolder.txt_mobile.setText(hashMapArrayList.get(posistion).get("mobile"));
        mViewHolder.txt_address.setText(hashMapArrayList.get(posistion).get("address"));
        mViewHolder.txt_vehicle_no.setText(hashMapArrayList.get(posistion).get("vehicle"));
        mViewHolder.txt_license.setText(hashMapArrayList.get(posistion).get("license"));
//        mViewHolder.txt_status.setText(hashMapArrayList.get(posistion).get("mobile"));
        mViewHolder.txt_pwd.setText(hashMapArrayList.get(posistion).get("password"));
        mViewHolder.btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hashMapArrayList.get(posistion).get("lattitude").equals("")) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("did", hashMapArrayList.get(posistion).get("did"));
                    intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));

                    intent.putExtra("lattitude", hashMapArrayList.get(posistion).get("lattitude"));
                    intent.putExtra("address_location", hashMapArrayList.get(posistion).get("address_location"));
                    intent.putExtra("longitude", hashMapArrayList.get(posistion).get("longitude"));

                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Delivery Person in Out Of Range", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        mViewHolder.btn_assign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                volley_post_delivery_booking(hashMapArrayList.get(posistion).get("did"),
//                        ((Activity) context).getIntent().getStringExtra("pcid")
//
//                );
//            }
//        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        //        private final TextView txt_date;
//        private final TextView txt_unique_code;
//        private final TextView txt_time, txt_assign, txt_city, txt_locality, txt_address, txt_mobile;
//        private final Button btn_assign;
        private final TextView txt_shop, txt_pwd, txt_name, txt_email, txt_address, txt_vehicle_no, txt_license, txt_status;
        private final TextView txt_mobile;
        private final Button btn_map;

        public MyViewHolder(View item) {
            txt_name = (TextView) item.findViewById(R.id.txt_name);
            txt_shop = (TextView) item.findViewById(R.id.txt_shop);
            txt_mobile = (TextView) item.findViewById(R.id.txt_mobile);
            txt_email = (TextView) item.findViewById(R.id.txt_email);
            txt_address = (TextView) item.findViewById(R.id.txt_address);
            txt_vehicle_no = (TextView) item.findViewById(R.id.txt_vehicle_no);
            txt_license = (TextView) item.findViewById(R.id.txt_license);
            txt_status = (TextView) item.findViewById(R.id.txt_status);
            txt_pwd = (TextView) item.findViewById(R.id.txt_pwd);
            btn_map = (Button) item.findViewById(R.id.btn_map);
//            btn_assign = (Button) item.findViewById(R.id.btn_assign);


        }
    }

    private void volley_post_delivery_booking(final String delivery_id, final String pcid) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.POST_DELIVERY_ID, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        Configg.alert("Assign Delivery", jsonObject.getString("message"), 0, context);
                    } else if (jsonObject.getString("success").equals("3")) {
                        Configg.alert("Assign Delivery", jsonObject.getString("message"), 0, context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("delivery_id", delivery_id);
                params.put("pcid", pcid);


//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


}
