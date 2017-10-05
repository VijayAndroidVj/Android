package cinderellaadmin.vajaralabs.com.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
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
import java.util.Map;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterAssignDelivery extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterAssignDelivery(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            convertView = inflater.inflate(R.layout.delivery_person, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_delivery_name.setText(hashMapArrayList.get(posistion).get("delivery_person"));
        mViewHolder.txt_shop.setText(hashMapArrayList.get(posistion).get("shop_code_and_name"));
        mViewHolder.txt_mobile.setText(hashMapArrayList.get(posistion).get("mobile"));

        mViewHolder.btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((Activity) context).getIntent().getStringExtra("completion").equals("50")
                        &&!((Activity) context).getIntent().getStringExtra("completion").trim().equals("90")) {
                    Toast.makeText(context,""+((Activity) context).getIntent().getStringExtra("completion"),1000).show();

                    volley_post_delivery_booking(hashMapArrayList.get(posistion).get("did"),
                            ((Activity) context).getIntent().getStringExtra("shop_id"),
                            ((Activity) context).getIntent().getStringExtra("pcid"),
                            ((Activity) context).getIntent().getStringExtra("unique_code"),
                            hashMapArrayList.get(posistion).get("delivery_person"),
                            ((Activity) context).getIntent().getStringExtra("shop_keeper"),
                            hashMapArrayList.get(posistion).get("mobile"),
                            ((Activity) context).getIntent().getStringExtra("mobile"),
                            ((Activity) context).getIntent().getStringExtra("shop_code_and_name")


                    );
                } else if (((Activity) context).getIntent().getStringExtra("completion").equals("90")) {
                    Toast.makeText(context,""+((Activity) context).getIntent().getStringExtra("pcid"),1000).show();

                    volley_post_job_done(((Activity) context).getIntent().getStringExtra("pcid"), "95",hashMapArrayList.get(posistion).get("did")
                    ,hashMapArrayList.get(posistion).get("delivery_person"),hashMapArrayList.get(posistion).get("mobile"));


                } else {
                    Configg.alert("Can't Assign", ((Activity) context).getIntent().getStringExtra("unique_code") + '\n' + "Already Pickuped By Delivery Person "
                            + '\n' + ((Activity) context).getIntent().getStringExtra("delivery_contact_name"), 2, context);
                }
            }
        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        //        private final TextView txt_date;
//        private final TextView txt_unique_code;
//        private final TextView txt_time, txt_assign, txt_city, txt_locality, txt_address, txt_mobile;
        private final Button btn_assign;
        private final TextView txt_delivery_name;
        private final TextView txt_shop;
        private final TextView txt_mobile;

        public MyViewHolder(View item) {
            txt_delivery_name = (TextView) item.findViewById(R.id.txt_delivery_name);
            txt_shop = (TextView) item.findViewById(R.id.txt_shop);
            txt_mobile = (TextView) item.findViewById(R.id.txt_mobile);
//            txt_city = (TextView) item.findViewById(R.id.txt_city);
//            txt_locality = (TextView) item.findViewById(R.id.txt_locality);
//            txt_address = (TextView) item.findViewById(R.id.txt_address);
            btn_assign = (Button) item.findViewById(R.id.btn_assign);


        }
    }

    private void volley_post_job_done(final String pcid, final String completion,
                                      final String id,
                                      final String name,
                                      final String mobile) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.FACTORY_JOB_DONE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        Configg.alert("JOB Completion", jsonObject.getString("message"), 2, context);
                    } else if (jsonObject.getString("success").equals("3")) {
                        Configg.alert("JOB Completion", jsonObject.getString("message"), 0, context);
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
                params.put("pcid", pcid);
                params.put("completion", completion);
                params.put("delivery_contact_name",name);
                params.put("delivery_contact",mobile);
                params.put("delivery_assign_id", id);


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

    private void volley_post_delivery_booking(final String delivery_id,
                                              final String shop_id,
                                              final String pcid,
                                              final String unique_code,
                                              final String delivery_person,
                                              final String shop_person,
                                              final String delivery_contact,
                                              final String shop_contact,
                                              final String shop_code_and_name
    ) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.POST_BOOKING_DELIVERY, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        Configg.alert("Assign Booking", jsonObject.getString("message"), 2, context);
                    } else if (jsonObject.getString("success").equals("3")) {
                        Configg.alert("Assign Booking", jsonObject.getString("message"), 0, context);
                    } else if (jsonObject.getString("success").equals("4")) {
                        Configg.alert("Assign Booking", jsonObject.getString("message"), 0, context);
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
                params.put("shop_id", shop_id);
                params.put("pcid", pcid);
                params.put("unique_code", unique_code);
                params.put("delivery_person", delivery_person);
                params.put("shop_person", shop_person);
                params.put("delivery_contact", delivery_contact);
                params.put("shop_contact", shop_contact);
                params.put("shop_code_and_name", shop_code_and_name);


//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                Log.w("params_out", params.toString());

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
