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

public class FactoryList extends AppCompatActivity implements Response {

    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterFactoryPerson adapterFactoryPerson;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private AsyncPOST asyncPOST;
    private ListView list_delivery_person;
    private FactoryList activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_list);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Factory-List" + " " + "(" + getIntent().getStringExtra("tag_no") + ")");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        if (Configg.isNetworkAvailable(this)) {
            nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(this, "sid")));

            asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_FACTORY_LIST, this);
            asyncPOST.execute();
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
        }
        list_delivery_person = (ListView) findViewById(R.id.list_item);
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
                Configg.alert("Theres No Any Dealer's For" + Configg.getDATA(activity, "shop_code_and_name"), "Kindly Add Dealer For Your Shop", 2, activity);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("fid", jsonArray.getJSONObject(i).getString("fid"));
                stringHashMap.put("contact_person", jsonArray.getJSONObject(i).getString("contact_person"));
                stringHashMap.put("factory_name", jsonArray.getJSONObject(i).getString("factory_name"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));

                hashMapArrayList.add(stringHashMap);


            }
            adapterFactoryPerson = new AdapterFactoryPerson(activity, hashMapArrayList);
            list_delivery_person.setAdapter(adapterFactoryPerson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
