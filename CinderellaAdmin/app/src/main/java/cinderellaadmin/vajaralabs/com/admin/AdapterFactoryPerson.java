package cinderellaadmin.vajaralabs.com.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import common.Configg;
import util.CallBack;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterFactoryPerson extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterFactoryPerson(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            convertView = inflater.inflate(R.layout.factory_person, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_name.setText(hashMapArrayList.get(posistion).get("contact_person"));
        mViewHolder.txt_factory_name.setText(hashMapArrayList.get(posistion).get("factory_name"));
        if (Configg.getDATA(context, "type").equals("factory")) {
//            mViewHolder.mySwitch.setVisibility(View.VISIBLE);
        }
//        if (hashMapArrayList.get(posistion).get("completion").equals("75")){
////            mViewHolder.mySwitch.setChecked(false);
//        }
//        else if (hashMapArrayList.get(posistion).get("completion").equals("90")){
////            mViewHolder.mySwitch.setChecked(true);
//        }

        mViewHolder.btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volley_post_delivery_booking(
                        hashMapArrayList.get(posistion).get("fid"),
                        ((Activity) context).getIntent().getStringExtra("pcid"),
                        hashMapArrayList.get(posistion).get("factory_name"),
                        hashMapArrayList.get(posistion).get("contact_person"),
                        hashMapArrayList.get(posistion).get("mobile")
                );
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
        private final TextView txt_name;
        private final TextView txt_factory_name;

        public MyViewHolder(View item) {
            txt_name = (TextView) item.findViewById(R.id.txt_name);
            txt_factory_name = (TextView) item.findViewById(R.id.txt_factory_name);
//            txt_city = (TextView) item.findViewById(R.id.txt_city);
//            txt_locality = (TextView) item.findViewById(R.id.txt_locality);
//            txt_address = (TextView) item.findViewById(R.id.txt_address);

            btn_assign = (Button) item.findViewById(R.id.btn_assign);


        }
    }

    private void volley_post_delivery_booking(final String fid,
                                              final String tid,
                                              final String factory_name,
                                              final String factory_person,
                                              final String factory_contact) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.ASSIGN_FACTORY, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        CallBack callBack = new CallBack() {
                            @Override
                            public void onCallBack() {
                                Intent intent = new Intent();
                                intent.putExtra("factory_name", factory_name);
                                ((FactoryList) context).setResult(Activity.RESULT_OK, intent);
                                ((FactoryList) context).finish();
                            }
                        };
                        Configg.alert("Assign Factory", jsonObject.getString("message"), 2, context, callBack);
                    } else if (jsonObject.getString("success").equals("3")) {
                        Configg.alert("Assign Factory", jsonObject.getString("message") + "to" + '\n' + factory_name, 0, context);
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
                params.put("factory_id", fid);
                params.put("pcid", tid);
                params.put("factory_contact", factory_contact);
                params.put("factory_name", factory_name);
                params.put("factory_person", factory_person);


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
