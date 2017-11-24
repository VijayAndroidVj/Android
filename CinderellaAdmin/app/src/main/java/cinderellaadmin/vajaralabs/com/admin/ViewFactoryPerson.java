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

public class ViewFactoryPerson extends AppCompatActivity implements Response {

    private ViewFactoryPerson activity;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterFacotryList adapterFacotryList;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private AsyncPOST asyncPOST;
    private ListView list_delivery_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivery_person);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Factory List");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        if (Configg.isNetworkAvailable(this)) {
//            if (Configg.getDATA(activity, "type").equals("shop")) {
//                nameValuePairs.add(new BasicNameValuePair("sid", Configg.getDATA(this, "sid")));
//
//                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_DELIVERY_PERSON, this);
//                asyncPOST.execute();
//            } else if (Configg.getDATA(activity, "type").equals("admin")) {
            nameValuePairs.add(new BasicNameValuePair("sid", Configg.getDATA(this, "sid")));

            asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_FACTORY_LIST, this);
            asyncPOST.execute();
//            }
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
                Configg.alert("Theres No Any Shop", "Kindly Add Shop For Cinderella", 2, activity);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("fid", jsonArray.getJSONObject(i).getString("fid"));
                stringHashMap.put("factory_name", jsonArray.getJSONObject(i).getString("factory_name"));
                stringHashMap.put("contact_person", jsonArray.getJSONObject(i).getString("contact_person"));
                stringHashMap.put("email", jsonArray.getJSONObject(i).getString("email"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("password", jsonArray.getJSONObject(i).getString("password"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));



                hashMapArrayList.add(stringHashMap);


            }
            adapterFacotryList = new AdapterFacotryList(activity, hashMapArrayList);
            list_delivery_person.setAdapter(adapterFacotryList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

