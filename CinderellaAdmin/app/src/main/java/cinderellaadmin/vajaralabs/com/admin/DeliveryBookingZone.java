package cinderellaadmin.vajaralabs.com.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import common.Configg;
import common.Response;

public class DeliveryBookingZone extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterDeliveryBooking adapterDeliveryBooking;
    private ListView list_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_booking_zone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booking-Zone");
        if (Configg.isNetworkAvailable(this)) {
            if (Configg.getDATA(DeliveryBookingZone.this,"type").equals("shop")){
                nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(this, "sid")));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_DELIVERY_SHOP, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
            else if (Configg.getDATA(DeliveryBookingZone.this,"type").equals("delivery")) {
                nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(this, "did")));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_DELIVERY, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
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
                Configg.alert("Theres No Any Recent Bookings.", "", 2, DeliveryBookingZone.this);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("booking_id", jsonArray.getJSONObject(i).getString("booking_id"));
                stringHashMap.put("delivery_id", jsonArray.getJSONObject(i).getString("delivery_id"));

                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("cid", jsonArray.getJSONObject(i).getString("cid"));

                stringHashMap.put("locality_id", jsonArray.getJSONObject(i).getString("locality_id"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("pickup_time", jsonArray.getJSONObject(i).getString("pickup_time"));
                stringHashMap.put("pickup_date", jsonArray.getJSONObject(i).getString("pickup_date"));
                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));
                hashMapArrayList.add(stringHashMap);


            }
            adapterDeliveryBooking = new AdapterDeliveryBooking(DeliveryBookingZone.this, hashMapArrayList);
            list_booking.setAdapter(adapterDeliveryBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
