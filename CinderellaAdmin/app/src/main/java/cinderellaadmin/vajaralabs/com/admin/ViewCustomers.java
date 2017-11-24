package cinderellaadmin.vajaralabs.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class ViewCustomers extends AppCompatActivity implements Response {

    private ViewCustomers activity;
    private AdapterCustomer adapterCustomer;
    private ListView list_customer;
    private ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String, String>>();
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
    private EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);
        activity = this;
        list_customer = (ListView) findViewById(R.id.list_item);
        edt_search=(EditText) findViewById(R.id.edt_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer-List");

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                adapterCustomer.filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (Configg.isNetworkAvailable(activity)){
            nameValuePairs.add(new BasicNameValuePair("customer_id", "customer_id"));
            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_CUSTOMER_LIST, activity);
            asyncPOST.execute();
        }
        else {
            Configg.alert("Internet...","Please Check Your Internet Connection",0,activity);
        }
    }

    @Override
    public void processFinish(String output) {
        if (!(hashMapArrayList.size() ==0)){
            hashMapArrayList.clear();
        }

        try {
            JSONArray jsonArray=new JSONArray(output);
            HashMap<String,String> stringHashMap;
            getSupportActionBar().setTitle("Customer-List"+" "+"Total : "+jsonArray.length());

            for (int i=0;i<jsonArray.length();i++){
                stringHashMap=new HashMap<String, String>();
                stringHashMap.put("cid",jsonArray.getJSONObject(i).getString("cid"));
                stringHashMap.put("name",jsonArray.getJSONObject(i).getString("name"));
                stringHashMap.put("mobile",jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("email",jsonArray.getJSONObject(i).getString("email"));
                stringHashMap.put("pwd",jsonArray.getJSONObject(i).getString("pwd"));
                stringHashMap.put("address",jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("created_at",jsonArray.getJSONObject(i).getString("created_at"));
                hashMapArrayList.add(stringHashMap);





            }
            adapterCustomer=new AdapterCustomer(activity,hashMapArrayList);
            list_customer.setAdapter(adapterCustomer);
        } catch (JSONException e) {


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
}
