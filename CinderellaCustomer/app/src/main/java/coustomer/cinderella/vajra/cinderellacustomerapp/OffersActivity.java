package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Response;

/**
 * Created by Gokul on 7/9/2017.
 */

public class OffersActivity extends AppCompatActivity implements Response {

    private AsyncPOST asyncPOST;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ListView list_item;
    private Activity activity;
    private OfferAdapter offerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);

        activity = this;

        list_item = (ListView) findViewById(R.id.list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Offers");

//        nameValuePairs.add(new BasicNameValuePair("", ""));
        asyncPOST = new AsyncPOST(nameValuePairs, activity, "http://vajralabs.com/Cinderella/get_offer.php", this);
        asyncPOST.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void processFinish(String output) {
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            HashMap<String, String> stringHashMap;
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("notification_id", jsonArray.getJSONObject(i).getString("notification_id"));
                stringHashMap.put("about_wall", jsonArray.getJSONObject(i).getString("about_wall"));
                stringHashMap.put("des_wall", jsonArray.getJSONObject(i).getString("des_wall"));
                stringHashMap.put("posted_by", jsonArray.getJSONObject(i).getString("posted_by"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("image_path", jsonArray.getJSONObject(i).getString("image_path"));
                hashMapArrayList.add(stringHashMap);
            }
            offerAdapter = new OfferAdapter(activity, hashMapArrayList, "");
            list_item.setAdapter(offerAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
