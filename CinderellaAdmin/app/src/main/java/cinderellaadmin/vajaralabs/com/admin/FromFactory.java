package cinderellaadmin.vajaralabs.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import common.Configg;
import common.Response;

public class FromFactory extends AppCompatActivity implements Response{

    private ListView list_item;
    private FromFactory activity;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String, String>>();
    private AdapterFromFactory adapterfromfactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_factory);
        activity = this;
        list_item = (ListView) findViewById(R.id.list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("From-Factory");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.getDATA(activity,"type").equals("shop")) {
            nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));
            nameValuePairs.add(new BasicNameValuePair("completion", "75"));


            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_FROM_FACTORY, activity);
            asyncPOST.execute();
        }
        else if (Configg.getDATA(activity,"type").equals("delivery")) {
            nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
            nameValuePairs.add(new BasicNameValuePair("completion", "75"));


            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_FROM_FACTORY_DELIVERY, activity);
            asyncPOST.execute();
        }
        else if (Configg.getDATA(activity,"type").equals("factory")) {
            nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(activity, "fid")));
            nameValuePairs.add(new BasicNameValuePair("completion", "75"));


            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_FROM_FACTORY_FACTORY, activity);
            asyncPOST.execute();
        }

        Log.w("namevalue",nameValuePairs.toString());
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
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));



                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                hashMapArrayList.add(stringHashMap);
            }
            adapterfromfactory = new AdapterFromFactory(activity, hashMapArrayList);
            list_item.setAdapter(adapterfromfactory);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
