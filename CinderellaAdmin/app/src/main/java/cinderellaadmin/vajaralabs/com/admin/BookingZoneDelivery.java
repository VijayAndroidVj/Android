package cinderellaadmin.vajaralabs.com.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class BookingZoneDelivery extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterBooking adapterBooking;
    private ListView list_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_zone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery-Zone");
        findViewById(R.id.flShareView).setVisibility(View.GONE);
        list_booking = (ListView) findViewById(R.id.list_booking);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.isNetworkAvailable(this)) {
            if (Configg.getDATA(BookingZoneDelivery.this, "type").equals("shop")) {
                nameValuePairs.add(new BasicNameValuePair("city_id", Configg.getDATA(this, "city_id")));
                nameValuePairs.add(new BasicNameValuePair("locality_id", Configg.getDATA(this, "locality_id")));
                nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(this, "sid")));
                nameValuePairs.add(new BasicNameValuePair("completion1", "90"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "95"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING2, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            } else if (Configg.getDATA(BookingZoneDelivery.this, "type").equals("delivery")) {
                nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(this, "did")));
                nameValuePairs.add(new BasicNameValuePair("completion1", "95"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "95"));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_DELIVERY2, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
//            else if (Configg.getDATA(BookingZonePickuped.this, "type").equals("factory")) {
//                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
////                nameValuePairs.add(new BasicNameValuePair("completion", "25"));
//
//                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_FACTORY, this);
//                asyncPOST.execute();
//                System.out.println("bookingzone" + nameValuePairs.toString());
//            }
            else if (Configg.getDATA(BookingZoneDelivery.this, "type").equals("admin")) {
                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));
                nameValuePairs.add(new BasicNameValuePair("completion1", "90"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "95"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_ADMIN2, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
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
                Configg.alert("Theres No Any Recent Bookings.", "", 2, BookingZoneDelivery.this);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("overall_count", jsonArray.getJSONObject(i).getString("overall_count"));

                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                stringHashMap.put("delivery_assign_person", jsonArray.getJSONObject(i).getString("delivery_assign_person"));
                stringHashMap.put("delivery_assign_contact", jsonArray.getJSONObject(i).getString("delivery_assign_contact"));

                stringHashMap.put("customer_name", jsonArray.getJSONObject(i).getString("customer_name"));
                stringHashMap.put("customer_id", jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));


                stringHashMap.put("locality_id", jsonArray.getJSONObject(i).getString("locality_id"));
                stringHashMap.put("delivery_contact", jsonArray.getJSONObject(i).getString("delivery_contact"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));


                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("pickup_time", jsonArray.getJSONObject(i).getString("pickup_time"));
                stringHashMap.put("pickup_date", jsonArray.getJSONObject(i).getString("pickup_date"));
                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));
                stringHashMap.put("factory_name", jsonArray.getJSONObject(i).getString("factory_name"));
                stringHashMap.put("factory_person", jsonArray.getJSONObject(i).getString("factory_person"));
                stringHashMap.put("factory_contact", jsonArray.getJSONObject(i).getString("factory_contact"));

                hashMapArrayList.add(stringHashMap);


            }
            adapterBooking = new AdapterBooking(BookingZoneDelivery.this, hashMapArrayList);
            list_booking.setAdapter(adapterBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
