package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.AsyncPOST;
import common.Config;
import common.Response;

public class MyProfile extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private MyProfile activity;
    private EditText edt_name, edt_mobile, edt_email, edt_address;
    private TextView txt_name;
    private Button btn_submit;
    private TextView txt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My-Profile");
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_logout=(TextView) findViewById(R.id.txt_logout);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signUpValidation()) {
                    volley_post_delivery_booking(Config.getDATA(activity, "cid"));
                }

            }
        });
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.removeDATA(activity,"mobile");
                Config.removeDATA(activity,"pwd");
                finish();
                MainActivity.activity.finish();
                Intent intent=new Intent(activity,LoginActivity.class);
                startActivity(intent);


            }
        });

    }

    private void volley_post_delivery_booking(final String cid) {

        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Config.MAIN_URL + Config.POST_EDIT_CUSTOMER_PROFILE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        Config.alert("My Profile", jsonObject.getString("message"), 2, activity);
                    } else if (jsonObject.getString("success").equals("3")) {
//                        Configg.alert("Assign Delivery", jsonObject.getString("message"), 0, context);
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
                params.put("cid", cid);
                params.put("name", edt_name.getText().toString());
                params.put("email", edt_email.getText().toString());
                params.put("address", edt_address.getText().toString());
                params.put("mobile", edt_mobile.getText().toString());


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

    private boolean signUpValidation() {
        if (edt_name.getText().toString().equals("")) {
            edt_name.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Fill Your First.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_mobile.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Mobile Number.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_email.getText().toString().equals("")) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your E-Mail.", Toast.LENGTH_SHORT).show();
            return true;

        }


        if (!Config.isValidEmaillId(edt_email.getText().toString().trim())) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (edt_address.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Address Should Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nameValuePairs.add(new BasicNameValuePair("cid", Config.getDATA(activity, "cid")));


        asyncPOST = new AsyncPOST(nameValuePairs, activity, Config.MAIN_URL + Config.GET_PROFILE, activity);
        asyncPOST.execute();
        Log.w("namevalue", nameValuePairs.toString());
    }

    @Override
    public void processFinish(String output) {

        Log.w("output", output);

        try {
            JSONArray jsonArray = new JSONArray(output);
            txt_name.setText(jsonArray.getJSONObject(0).getString("name"));
            edt_name.setText(jsonArray.getJSONObject(0).getString("name"));
            edt_address.setText(jsonArray.getJSONObject(0).getString("address"));
            edt_email.setText(jsonArray.getJSONObject(0).getString("email"));
            edt_mobile.setText(jsonArray.getJSONObject(0).getString("mobile"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
