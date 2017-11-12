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

public class DoneJobs extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterDoneJobs adapterDoneJobs;
    private ListView list_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_zone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Done-Jobs");
        findViewById(R.id.flShareView).setVisibility(View.GONE);
        list_booking = (ListView) findViewById(R.id.list_booking);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.isNetworkAvailable(this)) {
            if (Configg.getDATA(DoneJobs.this, "type").equals("shop")) {
                nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(this, "sid")));
//                nameValuePairs.add(new BasicNameValuePair("locality_id", Configg.getDATA(this, "locality_id")));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.COMPLETED_LIST_SHOP, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            } else if (Configg.getDATA(DoneJobs.this, "type").equals("delivery")) {
                nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(this, "did")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.COMPLETED_LIST, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            } else if (Configg.getDATA(DoneJobs.this, "type").equals("factory")) {
                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.COMPLETED_LIST_FACTORY, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            } else if (Configg.getDATA(DoneJobs.this, "type").equals("admin")) {
                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.COMPLETED_LIST_ADMIN, this);
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
                Configg.alert("Theres No Any Recent Bookings.", "", 2, DoneJobs.this);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                stringHashMap.put("customer_id", jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));


                stringHashMap.put("locality_id", jsonArray.getJSONObject(i).getString("locality_id"));
                stringHashMap.put("delivery_contact", jsonArray.getJSONObject(i).getString("delivery_contact"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));
                stringHashMap.put("customer_name", jsonArray.getJSONObject(i).getString("customer_name"));
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
                stringHashMap.put("tag_no", jsonArray.getJSONObject(i).getString("tag_no"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("amount", jsonArray.getJSONObject(i).getString("amount"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));


                hashMapArrayList.add(stringHashMap);


            }
            adapterDoneJobs = new AdapterDoneJobs(DoneJobs.this, hashMapArrayList);
            list_booking.setAdapter(adapterDoneJobs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
