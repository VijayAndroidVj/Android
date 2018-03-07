package com.example.chandru.laundry;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.laundry.Adapter.deliveryAdapter;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Pojo.delivery;

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

public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener, AdapterListener {
    private List<delivery> maintain = new ArrayList<>();
    private deliveryAdapter bAdapter;
    private RecyclerView recycler_view;
    private String dataOne,dataTwos,Billno,bamt;
    private TextView cname, bill, phone, address, qty, amt, advance, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        getSupportActionBar().setTitle("Delivey Details");
        cname = (TextView) findViewById(R.id.cname);
        bill = (TextView) findViewById(R.id.bill);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        qty = (TextView) findViewById(R.id.qty);
        amt = (TextView) findViewById(R.id.amt);
        advance = (TextView) findViewById(R.id.advance);
        balance = (TextView) findViewById(R.id.balance);
        findViewById(R.id.btnDelivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        EditText re=(EditText)findViewById(R.id.receive) ;
                        EditText remar=(EditText)findViewById(R.id.remark) ;
                        String receive=re.getText().toString();
                        String remark=remar.getText().toString();
                        //http://demo.adityametals.com/api/update_delivery.php?bill="+Billno+"&paid="+bamt+"&summary="+remark+"&recieve="+receive
                        String Url = "http://demo.adityametals.com/api/update_delivery.php?bill="+Billno+"&paid="+bamt+"&summary="+remark+"&recieve="+receive;
                        new customerentry().execute(Url);

//                        Toast.makeText(DeliveryActivity.this, "Delivered successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                        findViewById(R.id.progress).setVisibility(View.GONE);
                    }
                }, 1500);
            }
        });

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        String Url = "http://demo.adityametals.com/api/view_order.php?bill_no=" + message;
        new serverUpload().execute(Url);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void adapterActionListener(int state, Object data) {

    }

    private class serverUpload extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DeliveryActivity.this);
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
                String[] separated = dataOne.split("\\}\\{");
                String one = separated[0] + "}";
                String two = "{"+ separated[1] ;
                Log.d("success", one);
                maintain.clear();
                JSONObject jsono = new JSONObject(one);
                JSONArray jsonOneArrayOne = jsono.getJSONArray("details");
                for (int i = 0; i < jsonOneArrayOne.length(); i++) {
                    JSONObject jssOne = jsonOneArrayOne.getJSONObject(i);
//cname,bill,phone,address,qty,amt,advance,balance
                    String Cname = jssOne.getString("customer_name");
                     Billno = jssOne.getString("order_id");
                    String phones = jssOne.getString("customer_phone");
                    String addresss = jssOne.getString("customer_address");
                    String amtt = jssOne.getString("total_amount");
                    String adamt = jssOne.getString("advance_amount");
                     bamt = jssOne.getString("balance_amount");
                    String qtyts = jssOne.getString("total_laundry");

                    cname.setText(Cname);
                    bill.setText(Billno);
                    phone.setText(phones);
                    address.setText(addresss);
                    qty.setText(qtyts);
                    amt.setText(amtt);
                    advance.setText(adamt);
                    balance.setText(bamt);


//                    deliverylist dboard = new deliverylist();
//                    dboard.setId(jss.getString("id"));
//                    dboard.setOrder_id(jss.getString("order_id"));
//                    dboard.setStore_ref(jss.getString("store_ref"));
//                    dboard.setOrder_date(jss.getString("order_date"));
//                    dboard.setCustomer_name(jss.getString("customer_name"));
//
//                    dboard.setCustomer_phone(jss.getString("customer_phone"));
//                    dboard.setCustomer_address(jss.getString("customer_address"));
//                    dboard.setDelivery_status(jss.getString("delivery_status"));
//                    dboard.setLocation(jss.getString("location"));
//                    dboard.setLaundry_for(jss.getString("laundry_for"));
//
//                    dboard.setDelivery_date(jss.getString("delivery_date"));
//                    dboard.setTotal_laundry(jss.getString("total_laundry"));
//                    dboard.setTotal_amount(jss.getString("total_amount"));
//                    dboard.setAdvance_amount(jss.getString("advance_amount"));
//                    dboard.setBalance_amount(jss.getString("balance_amount"));
//
//                    dboard.setPaid(jss.getString("paid"));
//                    dboard.setDelivered_on(jss.getString("delivered_on"));
//                    dboard.setLaundry_(jss.getString("laundry_"));
//                    dboard.setCheck_(jss.getString("check_"));
//                    dboard.setSummary(jss.getString("summary"));
                    // maintain.add(dboard);
                }

                JSONObject jsonoss = new JSONObject(two);
                JSONArray jsonOneArray = jsonoss.getJSONArray("items");
                for (int i = 0; i < jsonOneArray.length(); i++) {
                    JSONObject jss = jsonOneArray.getJSONObject(i);
                    delivery dboard = new delivery();
                    dboard.setId(jss.getString("id"));
                    dboard.setBill_id(jss.getString("bill_id"));
                    dboard.setService_name(jss.getString("service_name"));
                    dboard.setService_id(jss.getString("service_id"));
                    dboard.setItem_name(jss.getString("item_name"));
                    dboard.setItem_id(jss.getString("item_id"));
                    dboard.setPrice(jss.getString("price"));
                    dboard.setQuantity(jss.getString("quantity"));
                    dboard.setTotal(jss.getString("total"));
                    dboard.setDate(jss.getString("date"));
                    dboard.setCount(jss.getString("count"));
                    maintain.add(dboard);
                }


                recycler_view = (RecyclerView) findViewById(R.id.recycler_view_one);
                LinearLayoutManager lmanager = new LinearLayoutManager(DeliveryActivity.this);
                recycler_view.setLayoutManager(lmanager);
                bAdapter = new deliveryAdapter(maintain, DeliveryActivity.this, DeliveryActivity.this);
                recycler_view.setAdapter(bAdapter);
                bAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public void next(View view) {
        EditText re=(EditText)findViewById(R.id.receive) ;
        EditText remar=(EditText)findViewById(R.id.remark) ;
        String receive=re.getText().toString();
        String remark=remar.getText().toString();
        //http://demo.adityametals.com/api/update_delivery.php?bill="+Billno+"&paid="+bamt+"&summary="+remark+"&recieve="+receive
        String Url = "http://demo.adityametals.com/api/update_delivery.php?bill="+Billno+"&paid="+bamt+"&summary="+remark+"&recieve="+receive;
        new customerentry().execute(Url);
    }

    private class customerentry extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DeliveryActivity.this);
            dialog.setMessage("Loading in.., please wait");
            dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                dataTwos = (EntityUtils.toString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            try {
                JSONObject jsono = new JSONObject(dataTwos);
                String msg = jsono.getString("error");

                if (msg.equals("false")) {
                  //   Toast.makeText(DeliveryActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                  //  Intent myIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                  //  ActivityOptions options =
                   //      ActivityOptions.makeCustomAnimation(DeliveryActivity.this, R.anim.left_enter, R.anim.left_out);
                   // startActivity(myIntent, options.toBundle());

                    Toast.makeText(DeliveryActivity.this, "Delivered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    findViewById(R.id.progress).setVisibility(View.GONE);
                } else {
                    Toast.makeText(DeliveryActivity.this, "Could not Connect To the Server..", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
