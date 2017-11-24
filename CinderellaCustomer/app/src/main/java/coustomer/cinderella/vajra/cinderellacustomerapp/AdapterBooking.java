package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

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

import common.Config;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterBooking extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterBooking(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            convertView = inflater.inflate(R.layout.booking_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_date.setText(hashMapArrayList.get(posistion).get("pickup_date"));
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        mViewHolder.txt_time.setText(hashMapArrayList.get(posistion).get("pickup_time"));
        mViewHolder.txt_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookingStatus.class);
                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                intent.putExtra("customer_id", hashMapArrayList.get(posistion).get("customer_id"));
                context.startActivity(intent);


            }
        });

        mViewHolder.txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmation);
                Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        if (hashMapArrayList.get(posistion).get("completion").equals("50")){
                            volley_cancel_booking("yes",hashMapArrayList.get(posistion).get("pcid"));

//                        }
//                        else {
//                            Config.alert("Booking Cancel...","Ciderella Already Pickuped Your Cloths.",0,context);
//                        }


                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
//                Intent intent=new Intent(context,BookingStatus.class);
//                intent.putExtra("pcid",hashMapArrayList.get(posistion).get("pcid"));
//                intent.putExtra("customer_id",hashMapArrayList.get(posistion).get("customer_id"));
//                context.startActivity(intent);


            }
        });
        if (hashMapArrayList.get(posistion).get("completion").equals("")||
                hashMapArrayList.get(posistion).get("completion").equals("25")){
            mViewHolder.txt_cancel.setVisibility(View.VISIBLE);

        }
        else {
            mViewHolder.txt_cancel.setVisibility(View.GONE);

        }


            if (hashMapArrayList.get(posistion).get("completion").equals("100")){
            mViewHolder.txt_bill.setVisibility(View.VISIBLE);
        }
        else {
            mViewHolder.txt_bill.setVisibility(View.GONE);

        }
        mViewHolder.txt_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BillView.class);
                if (!hashMapArrayList.get(posistion).get("bill_image").equals("")) {
                    intent.putExtra("bill_image", hashMapArrayList.get(posistion).get("bill_image"));
                }
                else {
                    intent.putExtra("bill_image", "vvbvc");
                }
                intent.putExtra("unique_code",hashMapArrayList.get(posistion).get("unique_code"));
                context.startActivity(intent);


            }
        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_date;
        private final TextView txt_unique_code;
        private final TextView txt_time, txt_call;
        private final TextView txt_status, txt_cancel;
        private final TextView txt_bill;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_time = (TextView) item.findViewById(R.id.txt_time);
            txt_call = (TextView) item.findViewById(R.id.txt_call);
            txt_date = (TextView) item.findViewById(R.id.txt_date);
            txt_status = (TextView) item.findViewById(R.id.txt_status);
            txt_cancel = (TextView) item.findViewById(R.id.txt_cancel);
            txt_bill=(TextView) item.findViewById(R.id.txt_bill);


        }
    }

    private void volley_cancel_booking(final String cancel,
                                              final String pcid

    ) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Config.MAIN_URL + Config.BOOKING_CANCEL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
//                        ((Activity) context).finish();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage("Booking Cancel").setTitle(jsonObject.getString("message"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // do nothing
                                            ((Activity) context).finish();
                                            ((Activity) context).overridePendingTransition(0, 0);
                                            context.startActivity(((Activity) context).getIntent());
                                            ((Activity) context).overridePendingTransition(0, 0);


                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        alert.setCancelable(false);
                    } else if (jsonObject.getString("success").equals("0")) {
                        ((Activity) context).finish();
//                        Config.alert("Booking Cancel", jsonObject.getString("message"), 0, context);
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

                params.put("cancel", cancel);
                params.put("pcid", pcid);


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
