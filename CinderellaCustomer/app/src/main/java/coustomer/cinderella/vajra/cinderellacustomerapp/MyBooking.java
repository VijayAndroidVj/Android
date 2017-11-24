package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MyBooking extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterBooking adapterBooking;
    private ListView list_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My-Bookings");
        if (Config.isNetworkAvailable(this)) {
            nameValuePairs.add(new BasicNameValuePair("customer_id", Config.getDATA(this, "cid")));
            asyncPOST = new AsyncPOST(nameValuePairs, this, Config.MAIN_URL + Config.GET_BOOKING, this);
            asyncPOST.execute();
        } else {
            Config.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
        }
        list_booking = (ListView) findViewById(R.id.list_booking);
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
        System.out.println("booking" + output);
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            HashMap<String, String> stringHashMap;
            if (jsonArray.length() == 0) {
                Config.alert("Theres No Any Recent Bookings.", "Kindly Schedule Your Booking.", 2, MyBooking.this);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("customer_id", jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));

                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("pickup_time", jsonArray.getJSONObject(i).getString("pickup_time"));
                stringHashMap.put("pickup_date", jsonArray.getJSONObject(i).getString("pickup_date"));
                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));
                stringHashMap.put("bill_image", jsonArray.getJSONObject(i).getString("bill_image"));
                hashMapArrayList.add(stringHashMap);


            }
            adapterBooking = new AdapterBooking(MyBooking.this, hashMapArrayList);
            list_booking.setAdapter(adapterBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
