package com.example.chandru.laundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.chandru.laundry.Adapter.deliverylistAdapter;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Pojo.deliverylist;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Delivery extends AppCompatActivity implements View.OnClickListener, AdapterListener {

    private List<deliverylist> maintain = new ArrayList<>();
    private deliverylistAdapter bAdapter;
    private RecyclerView recycler_view;
    private String dataOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }

        if (CommonUtil.isNetworkAvailable(Delivery.this)) {
            String Url = "http://demo.adityametals.com/api/order_list.php";
            new serverUpload().execute(Url);
        } else {
            Toast.makeText(Delivery.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void adapterActionListener(int state, Object data) {
        if (state == deliverylistAdapter.LIST_TAG && data != null) {
            int pois = (int) data;
            // String Url = "http://demo.adityametals.com/api/items.php?service_id="+maintain.get(pois).getId();
            // new Order.update().execute(Url);

            Intent detail = new Intent(this, DeliveryActivity.class);
            detail.putExtra("message", maintain.get(pois).getOrder_id());
            startActivity(detail);
        }

    }

    private class serverUpload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Delivery.this);
            dialog.setMessage("Loading in.., please wait");
            dialog.setTitle("");
            // dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            dialog.show();
            dialog.setCancelable(false);


        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //String url = params[0].replace(" ", "%20");
                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity entity = response.getEntity();
                dataOne = (EntityUtils.toString(entity));


            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            try {
                JSONObject jsono = new JSONObject(dataOne);
                maintain.clear();
                JSONArray jsonOneArray = jsono.getJSONArray("details");
                for (int i = 0; i < jsonOneArray.length(); i++) {
                    JSONObject jss = jsonOneArray.getJSONObject(i);
                    deliverylist dboard = new deliverylist();
                    dboard.setId(jss.getString("id"));
                    dboard.setOrder_id(jss.getString("order_id"));
                    dboard.setStore_ref(jss.getString("store_ref"));
                    dboard.setOrder_date(jss.getString("order_date"));
                    dboard.setCustomer_name(jss.getString("customer_name"));

                    dboard.setCustomer_phone(jss.getString("customer_phone"));
                    dboard.setCustomer_address(jss.getString("customer_address"));
                    dboard.setDelivery_status(jss.getString("delivery_status"));
                    dboard.setLocation(jss.getString("location"));
                    dboard.setLaundry_for(jss.getString("laundry_for"));

                    dboard.setDelivery_date(jss.getString("delivery_date"));
                    dboard.setTotal_laundry(jss.getString("total_laundry"));
                    dboard.setTotal_amount(jss.getString("total_amount"));
                    dboard.setAdvance_amount(jss.getString("advance_amount"));
                    dboard.setBalance_amount(jss.getString("balance_amount"));

                    dboard.setPaid(jss.getString("paid"));
                    dboard.setDelivered_on(jss.getString("delivered_on"));
                    dboard.setLaundry_(jss.getString("laundry_"));
                    dboard.setCheck_(jss.getString("check_"));
                    dboard.setSummary(jss.getString("summary"));
                    maintain.add(dboard);
                }
                recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
                LinearLayoutManager lmanager = new LinearLayoutManager(Delivery.this);
                recycler_view.setLayoutManager(lmanager);
                bAdapter = new deliverylistAdapter(maintain, Delivery.this, Delivery.this);
                recycler_view.setAdapter(bAdapter);
                bAdapter.notifyDataSetChanged();
                if (maintain.size() == 0) {
                    Toast.makeText(Delivery.this, "No list available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
