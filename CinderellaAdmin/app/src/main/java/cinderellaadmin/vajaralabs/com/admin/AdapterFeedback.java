package cinderellaadmin.vajaralabs.com.admin;

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
import android.widget.RatingBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterFeedback extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = null;
    ArrayList<HashMap<String, String>> hashMapArrayList2;


    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterFeedback(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.hashMapArrayList2 = new ArrayList<HashMap<String, String>>();
        this.hashMapArrayList2.addAll(hashMapArrayList);

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

            convertView = inflater.inflate(R.layout.activity_client_feedback, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }



        mViewHolder.ratebar_quality.setRating(Float.parseFloat(hashMapArrayList.get(posistion).
        get("washing_quality")));
        mViewHolder.ratebar_puntuality.setRating(Float.parseFloat(hashMapArrayList.get(posistion).
                get("puntuality")));
        mViewHolder.ratebar_behaviour.setRating(Float.parseFloat(hashMapArrayList.get(posistion).
                get("staff_behaviour")));
        mViewHolder.txt_review.setText(hashMapArrayList.get(posistion).
                get("review"));
        mViewHolder.txt_name.setText(hashMapArrayList.get(posistion).
                get("customer_name"));

        if (hashMapArrayList.get(posistion).
                get("review").equals("")){
            mViewHolder.txt_review.setVisibility(View.GONE);
        }

        else {
            mViewHolder.txt_review.setVisibility(View.VISIBLE);
        }

//        }
        return convertView;
    }

    private class MyViewHolder {

        private final RatingBar ratebar_quality;
        private final RatingBar ratebar_puntuality;
        private final RatingBar ratebar_behaviour;
        private final TextView txt_review;
        private final TextView txt_name;

        public MyViewHolder(View item) {
            ratebar_quality = (RatingBar) item.findViewById(R.id.ratebar_quality);
            ratebar_puntuality = (RatingBar)item.findViewById(R.id.ratebar_puntuality);
            ratebar_behaviour = (RatingBar)item.findViewById(R.id.ratebar_behaviour);
            txt_review = (TextView) item.findViewById(R.id.txt_review);
            txt_name=(TextView) item.findViewById(R.id.txt_name);


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
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.BOOKING_CANCEL, new com.android.volley.Response.Listener<String>() {
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        hashMapArrayList.clear();
        if (charText.length() == 0) {
            hashMapArrayList.addAll(hashMapArrayList2);
        } else {
            for (HashMap<String, String> wp : hashMapArrayList2) {
                if (wp.get("unique_code").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("mobile").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("completion").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void filter2(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        hashMapArrayList.clear();
        if (charText.length() == 0) {
            hashMapArrayList.addAll(hashMapArrayList2);
        } else {
            for (HashMap<String, String> wp : hashMapArrayList2) {

                if (wp.get("completion").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
