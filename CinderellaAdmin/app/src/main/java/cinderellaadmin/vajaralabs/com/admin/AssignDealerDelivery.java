package cinderellaadmin.vajaralabs.com.admin;

import android.graphics.drawable.ColorDrawable;
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

public class AssignDealerDelivery extends AppCompatActivity implements Response {

    private AssignDealerDelivery activity;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterDeliveryPerson adapterdeliveryperson;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private AsyncPOST asyncPOST;
    private ListView list_delivery_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_dealer);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Assign-Delivery Person" + " " + "(" + getIntent().getStringExtra("unique_code") + ")");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        if (Configg.isNetworkAvailable(this)) {
            nameValuePairs.add(new BasicNameValuePair("sid", Configg.getDATA(this, "sid")));

            asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_DELIVERY_PERSON, this);
            asyncPOST.execute();
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
        }
        list_delivery_person = (ListView) findViewById(R.id.list_delivery_person);
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
                Configg.alert("Theres No Any Dealer's For" + Configg.getDATA(activity,"shop_code_and_name"), "Kindly Add Dealer For Your Shop", 2, activity);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("did", jsonArray.getJSONObject(i).getString("did"));
                stringHashMap.put("delivery_person", jsonArray.getJSONObject(i).getString("delivery_person"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));
                stringHashMap.put("shop_code", jsonArray.getJSONObject(i).getString("shop_code"));
                stringHashMap.put("sid", jsonArray.getJSONObject(i).getString("sid"));
                stringHashMap.put("vehicle", jsonArray.getJSONObject(i).getString("vehicle"));
                stringHashMap.put("license", jsonArray.getJSONObject(i).getString("license"));
                stringHashMap.put("email", jsonArray.getJSONObject(i).getString("email"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("password", jsonArray.getJSONObject(i).getString("password"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));

                hashMapArrayList.add(stringHashMap);


            }
            adapterdeliveryperson = new AdapterDeliveryPerson(activity, hashMapArrayList);
            list_delivery_person.setAdapter(adapterdeliveryperson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
