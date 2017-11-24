package cinderellaadmin.vajaralabs.com.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterFactoryAssignedData extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterFactoryAssignedData(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

        mViewHolder.tag_no.setText(hashMapArrayList.get(posistion).get("tag_no"));
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        mViewHolder.txt_clothes.setText(hashMapArrayList.get(posistion).get("items"));
        mViewHolder.txt_oveal_total.setText(hashMapArrayList.get(posistion).get("overall_total"));
        mViewHolder.txt_assign_factory.setTag(hashMapArrayList.get(posistion));
        if (Configg.getDATA(context, "type").equals("shop")) {
            mViewHolder.txt_assign_factory.setText("Re-Schedule");
        }

        mViewHolder.txt_assign_factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configg.getDATA(context, "type").equals("shop")) {
                    ((AssignFactory) context).shashMapArrayList = (HashMap<String, String>) v.getTag();
                    Intent intent = new Intent(context, FactoryList.class);
                    intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                    intent.putExtra("tag_no", hashMapArrayList.get(posistion).get("tag_no"));
                    ((FactoryAssignedData) context).startActivityForResult(intent, 1111);

                } else if (Configg.getDATA(context, "type").equals("factory")) {
                    volley_post_job_done(hashMapArrayList.get(posistion).get("pcid"), "75");
                }

            }
        });


//        }
        return convertView;
    }


    private void volley_post_job_done(final String pcid, final String completion) {

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
                        Configg.alert("JOB Completion", jsonObject.getString("message"), 0, context);
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
                params.put("assignToFactory", "true");

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

    private class MyViewHolder {
        private final TextView tag_no;
        private final TextView txt_unique_code;
        private final TextView txt_more, txt_clothes, txt_oveal_total, txt_assign_factory;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_more = (TextView) item.findViewById(R.id.txt_more);
            tag_no = (TextView) item.findViewById(R.id.tag_no);
            txt_clothes = (TextView) item.findViewById(R.id.txt_clothes);
            txt_oveal_total = (TextView) item.findViewById(R.id.txt_oveal_total);
            txt_assign_factory = (TextView) item.findViewById(R.id.txt_assign_factory);


        }
    }


}
