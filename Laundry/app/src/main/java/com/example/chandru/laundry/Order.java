package com.example.chandru.laundry;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.laundry.Adapter.billAdapter;
import com.example.chandru.laundry.Adapter.catAdapter;
import com.example.chandru.laundry.Adapter.itemAdapter;
import com.example.chandru.laundry.Listener.AdapterListener;
import com.example.chandru.laundry.Pojo.bill;
import com.example.chandru.laundry.Pojo.cat;
import com.example.chandru.laundry.Pojo.item;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Order extends AppCompatActivity implements View.OnClickListener, AdapterListener {

    private static EditText edittext, editdate, edittextOne,  fullName, mobileNumber, location;
    private static Button signUpBtn;
    private static TextView forgotPassword, signUp, itemname,edittextTwo;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private catAdapter lAdapter;
    private itemAdapter kAdapter;
    private billAdapter bAdapter;
    private RecyclerView recycler_view, recycler_view_final;
    private String dataOne, dataOnes, itemData, dataTwos, name, customer, contact, address, datathree, serviceId, serviceName, Itemid;
    private List<cat> maintain = new ArrayList<>();
    private List<item> maintainlist = new ArrayList<>();
    private List<bill> billist = new ArrayList<>();
    private int qty = 0;
    private float unit = 0, mul = 0;
    int minteger = 0;
    String billno;
    Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        name = pref.getString("name", null);
        customer = pref.getString("cname", null);
        contact = pref.getString("contact", null);
        address = pref.getString("address", null);

        edittext = (EditText) findViewById(R.id.editText3);
        editdate = (EditText) findViewById(R.id.editText4);
        edittextOne = (EditText) findViewById(R.id.editText1);
        edittextOne.addTextChangedListener(watch);
        edittextTwo = (TextView) findViewById(R.id.editText2);
        itemname = (TextView) findViewById(R.id.itemname);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Order.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Order.this, ye, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        String Url = "http://demo.adityametals.com/api/service.php";
        new serverUpload().execute(Url);
        getDateTime();

        String Urlss = "http://demo.adityametals.com/api/items.php?service_id=5";
        new update().execute(Urlss);
        new getBillNo().execute("http://demo.adityametals.com/api/bill_no.php?");

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    DatePickerDialog.OnDateSetListener ye = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelOne();
        }

    };


    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelOne() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editdate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = (TextView) findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(this, "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }


    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;
        qty = minteger;
        display(minteger);

    }

    public void decreaseInteger(View view) {
        minteger = minteger - 1;
        qty = minteger;
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);

        TextView unitty = (TextView) findViewById(R.id.findUnit);
        TextView amt = (TextView) findViewById(R.id.findtotal);
        mul = unit * number;
        amt.setText("" + mul + "0");
    }

    public void adddata(View view) {

        if(qty ==0 || unit  ==0.0 || mul ==0.0 ){
            Toast.makeText(getApplicationContext(), "Please add Laundry item", Toast.LENGTH_SHORT).show();

        }else {

            bill dboard = new bill();
            dboard.setServiceId(serviceId);
            dboard.setItemid(Itemid);
            dboard.setServiceName(serviceName);
            dboard.setItem(itemData);
            dboard.setQty(String.valueOf(qty));
            dboard.setUnit("" + unit + "0");
            dboard.setAmt("" + mul + "0");
            billist.add(dboard);
            recycler_view_final = (RecyclerView) findViewById(R.id.recycler_view_one);
            LinearLayoutManager bmanager = new LinearLayoutManager(Order.this);
            recycler_view_final.setLayoutManager(bmanager);
            bAdapter = new billAdapter(billist, Order.this, Order.this);
            recycler_view_final.setAdapter(bAdapter);
            bAdapter.notifyDataSetChanged();

            int abc = 0;
            int cba = 0;
            for (int i = 0; i < billist.size(); i++) {

                abc = abc + (int) Float.parseFloat(billist.get(i).getQty());
                cba = cba + (int) Float.parseFloat(billist.get(i).getAmt());

            }
            TextView tq = (TextView) findViewById(R.id.tq);
            TextView tt = (TextView) findViewById(R.id.tt);

            tq.setText("Total qty:  " + abc);
            tt.setText("Total amt:Rs." + cba);
            TextView unitty = (TextView) findViewById(R.id.findUnit);
            TextView amt = (TextView) findViewById(R.id.findtotal);
            unitty.setText("0");
            amt.setText("0");
            itemData = "";
            qty = 0;
            unit = 0;
            mul = 0;
            display(0);
            serviceId = "";
            serviceName = "";
            Itemid = "";

        }


    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub
s=s.toString();

if(s.equals("")){

}else {
    float f1 = Float.parseFloat((String) s);
    int cba = 0;
    for (int i = 0; i < billist.size(); i++) {

        cba = cba + (int) Float.parseFloat(billist.get(i).getAmt());

    }
    f1=cba-f1;
    edittextTwo.setText(""+((int)f1));
    if(f1<0){
        Toast.makeText(getApplicationContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show();
    }

}





        }};

    public void next(View view) {
        // final String json =formatDataAsJSON();
        //Log.d("success",json);

        String picdate = edittext.getText().toString();
        String deldate = editdate.getText().toString();
        String adva = edittextOne.getText().toString();
        String bal = edittextTwo.getText().toString();

        editdate = (EditText) findViewById(R.id.editText4);
        edittextOne = (EditText) findViewById(R.id.editText1);
        edittextTwo = (TextView) findViewById(R.id.editText2);

        int abc = 0;
        int cba = 0;
        for (int i = 0; i < billist.size(); i++) {

            abc = abc + (int) Float.parseFloat(billist.get(i).getQty());
            cba = cba + (int) Float.parseFloat(billist.get(i).getAmt());

        }
        String Url = "http://demo.adityametals.com/api/add_joborder.php?bill=" + billno + "&user=" + name + "&customer=" + customer + "&contact=" + contact + "&address=" + contact + "&quantity=" + abc + "&amount=" + cba + "&advance=" + adva + "&balance=" + bal + "&delivery=" + deldate;
        new customerentry().execute(Url);

    }


    private class serverUpload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Order.this);
            dialog.setMessage("Loading in.., please wait");
            dialog.setTitle("");
            // dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            dialog.show();
            dialog.setCancelable(false);


        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String url = params[0].replace(" ", "%20");
                HttpGet httppost = new HttpGet(url);
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
                JSONArray jsonOneArray = jsono.getJSONArray("item");
                for (int i = 0; i < jsonOneArray.length(); i++) {
                    JSONObject jss = jsonOneArray.getJSONObject(i);
                    cat dboard = new cat();
                    dboard.setCategory_description(jss.getString("category_description"));
                    dboard.setCategory_name(jss.getString("category_name"));
                    dboard.setIcon_image(jss.getString("icon_image"));
                    dboard.setId(jss.getString("id"));
                    dboard.setRanduniq(jss.getString("randuniq"));
                    maintain.add(dboard);
                }
                recycler_view = (RecyclerView) findViewById(R.id.recycler_view_ten);
                LinearLayoutManager lmanager = new LinearLayoutManager(Order.this, LinearLayoutManager.HORIZONTAL, false);
                recycler_view.setLayoutManager(lmanager);
                lAdapter = new catAdapter(maintain, Order.this, Order.this);
                recycler_view.setAdapter(lAdapter);
                lAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void adapterActionListener(int state, Object data) {

        if (state == catAdapter.LIST_TAG && data != null) {
            int pois = (int) data;

            String Url = "http://demo.adityametals.com/api/items.php?service_id=" + maintain.get(pois).getId();
            new update().execute(Url);
        } else if (state == itemAdapter.LIST_TAGr && data != null) {

            int pos = (int) data;
            serviceId = maintainlist.get(pos).getService();
            for (int s = 0; s < maintain.size(); s++) {
                if (serviceId.equals(maintain.get(s).getId())) {
                    serviceName = maintain.get(s).getCategory_name();
                }
            }

            Itemid = maintainlist.get(pos).getId();

            itemData = maintainlist.get(pos).getLaundry_item();


            unit = Float.valueOf(maintainlist.get(pos).getLaundry_price());

            TextView unitty = (TextView) findViewById(R.id.findUnit);
            TextView amt = (TextView) findViewById(R.id.findtotal);
            unitty.setText(maintainlist.get(pos).getLaundry_price());
            amt.setText(maintainlist.get(pos).getLaundry_price());
        }
    }

    private String formatDataAsJSON() {
        JSONArray jsonOneArray = new JSONArray();
        try {
            for (int i = 0; i < billist.size(); i++) {
                JSONObject root = new JSONObject();
                root.put("service_id", billist.get(i).getServiceId());
                root.put("service_name", billist.get(i).getServiceName());
                root.put("item", billist.get(i).getItem());
                root.put("item_id", billist.get(i).getItemid());
                root.put("price", billist.get(i).getUnit());
                root.put("quantity", billist.get(i).getQty());
                root.put("total", billist.get(i).getAmt());
                jsonOneArray.put(root);
            }
            return jsonOneArray.toString();


        } catch (JSONException el) {
            el.printStackTrace();
        }

        return null;
    }

    private String getServerResponse(String json, String billno) {
        HttpPost post = new HttpPost("http://demo.adityametals.com/api/add_joborder_item.php?bill=" + billno + "");
        try {
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            DefaultHttpClient client = new DefaultHttpClient();
            BasicResponseHandler handler = new BasicResponseHandler();
            String response = client.execute(post, handler);
            return response;

        } catch (UnsupportedEncodingException es) {
            es.printStackTrace();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ;

    private void getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        edittext.setText(dateFormat.format(date) + "");
        //return dateFormat.format(date);
    }


    private void sendDatatoServer(final String billno) {
        final String json = formatDataAsJSON();
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {


                return getServerResponse(json, billno);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    System.out.println("data---" + s);
                    JSONObject js = new JSONObject(s);
                    if (js.getString("error").toString() == "false") {
                    }
                    Toast.makeText(Order.this, js.getString("msg").toString(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                }
            }
        }.execute();
    }

    private class update extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Order.this);
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
                dataOnes = (EntityUtils.toString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            try {
                JSONObject jsono = new JSONObject(dataOnes);
                maintainlist.clear();
                JSONArray jsonOneArray = jsono.getJSONArray("item");
                for (int i = 0; i < jsonOneArray.length(); i++) {
                    JSONObject jss = jsonOneArray.getJSONObject(i);
                    item dboards = new item();
                    dboards.setId(jss.getString("id"));
                    dboards.setLaundry_id(jss.getString("laundry_id"));
                    dboards.setLaundry_item(jss.getString("laundry_item"));
                    dboards.setLaundry_price(jss.getString("laundry_price"));
                    dboards.setLaundry_quantity(jss.getString("laundry_quantity"));
                    dboards.setService(jss.getString("service"));
                    maintainlist.add(dboards);
                }
                recycler_view = (RecyclerView) findViewById(R.id.recycler_view_two);
                LinearLayoutManager lmanager = new LinearLayoutManager(Order.this, LinearLayoutManager.HORIZONTAL, false);
                recycler_view.setLayoutManager(lmanager);
                kAdapter = new itemAdapter(maintainlist, Order.this, Order.this);
                recycler_view.setAdapter(kAdapter);
                lAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class customerentry extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Order.this);
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
                    senddata();
                    // sendDatatoServer();


                    // Toast.makeText(Order.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    //Intent myIntent = new Intent(Order.this, MainActivity.class);
                    //ActivityOptions options =
                    //      ActivityOptions.makeCustomAnimation(Order.this, R.anim.left_enter, R.anim.left_out);
                    //startActivity(myIntent, options.toBundle());
                } else {
                    Toast.makeText(Order.this, "Error", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void senddata() {

        JSONArray jsonOneArray = new JSONArray();
        try {
            for (int i = 0; i < billist.size(); i++) {
                JSONObject root = new JSONObject();
                root.put("service_id", billist.get(i).getServiceId());
                root.put("service_name", billist.get(i).getServiceName());
                root.put("item", billist.get(i).getItem());
                root.put("item_id", billist.get(i).getItemid());
                root.put("price", billist.get(i).getUnit());
                root.put("quantity", billist.get(i).getQty());
                root.put("total", billist.get(i).getAmt());
                jsonOneArray.put(root);
            }
            String jsonss = jsonOneArray.toString();
//            jsonss = "\"" + jsonss + "\"";
            jsonss = URLEncoder.encode(jsonss);
            String apiurl = "http://demo.adityametals.com/api/add_joborder_item.php?bill=" + billno + "&credintials=" + jsonss + "";
            new customerItem(billno).execute(apiurl);
        } catch (JSONException el) {
            el.printStackTrace();
        }


    }


    private class customerItem extends AsyncTask<String, Void, Boolean> {

        String billno;

        //ProgressDialog dialog;
        public customerItem(String billno) {
            this.billno = billno;
        }


        //ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog = new ProgressDialog(Order.this);
            //dialog.setMessage("Loading in.., please wait");
            //dialog.setTitle("");
            //dialog.show();
            //dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                datathree = (EntityUtils.toString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // dialog.dismiss();
            try {
                JSONObject jsono = new JSONObject(datathree);
                // Toast.makeText(Order.this, datathree, Toast.LENGTH_SHORT).show();
                String msg = jsono.getString("error");

                if (msg.equals("false")) {
                    // sendDatatoServer(billno);
                    Toast.makeText(Order.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Order.this, MainActivity.class);
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(Order.this, R.anim.left_enter, R.anim.left_out);
                    startActivity(myIntent, options.toBundle());
                } else {
                    Toast.makeText(Order.this, "Error", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getBillNo extends AsyncTask<String, Void, Boolean> {
//        String jsonss;
//
//        //ProgressDialog dialog;
//        public getBillNo(String jsonss) {
//            this.jsonss = jsonss;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog = new ProgressDialog(Order.this);
            //dialog.setMessage("Loading in.., please wait");
            //dialog.setTitle("");
            //dialog.show();
            //dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                datathree = (EntityUtils.toString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // dialog.dismiss();
            try {
                JSONObject jsono = new JSONObject(datathree);
                // Toast.makeText(Order.this, datathree, Toast.LENGTH_SHORT).show();
                String msg = jsono.getString("error");

                if (msg.equals("false")) {
                    billno = jsono.getString("bill_no");


                } else {
                    Toast.makeText(Order.this, "Error", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
