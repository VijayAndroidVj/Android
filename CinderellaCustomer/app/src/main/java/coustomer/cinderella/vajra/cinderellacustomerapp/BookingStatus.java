package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Config;

import common.Response;

public class BookingStatus extends AppCompatActivity implements Response {

    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private BookingStatus activity;
    private AdapterPickupedCustomer adapterPickupedCustomer;
    private ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);
        activity = this;
        list_item = (ListView) findViewById(R.id.list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booking-Status");


    }

    @Override
    protected void onResume() {
        super.onResume();
        nameValuePairs.add(new BasicNameValuePair("customer_id", Config.getDATA(activity, "cid")));
        nameValuePairs.add(new BasicNameValuePair("pcid", getIntent().getStringExtra("pcid")));


        asyncPOST = new AsyncPOST(nameValuePairs, activity, Config.MAIN_URL + Config.GET_STATUS, activity);
        asyncPOST.execute();
        Log.w("namevalue", nameValuePairs.toString());
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

    @Override
    public void processFinish(String output) {

        Log.e("output", output);
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }

        try {
            HashMap<String, String> stringHashMap;
            JSONArray jsonArray = new JSONArray(output);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("tag_no", jsonArray.getJSONObject(i).getString("tag_no"));
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));


                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("shop_contact_name", jsonArray.getJSONObject(i).getString("shop_contact_name"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));

                stringHashMap.put("shop_contact", jsonArray.getJSONObject(i).getString("shop_contact"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                hashMapArrayList.add(stringHashMap);
            }
            adapterPickupedCustomer = new AdapterPickupedCustomer(activity, hashMapArrayList);
            list_item.setAdapter(adapterPickupedCustomer);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
