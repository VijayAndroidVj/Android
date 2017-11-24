package cinderellaadmin.vajaralabs.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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

public class ClientFeedback extends AppCompatActivity implements Response{

    private ClientFeedback activity;
    private RatingBar ratebar_quality;
    private RatingBar ratebar_puntuality;
    private RatingBar ratebar_behaviour;
    private TextView txt_review;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private String washing = "", puntuality = "", behaviour = "";
    private ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String, String>>();
    private AdapterFeedback adapterFeedback;
    private ListView list_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        list_feedback=(ListView)findViewById(R.id.list_feedback);
        activity=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Client-Feedback");


        nameValuePairs.add(new BasicNameValuePair("customer_name", ""));


        asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_FEEDBACK, activity);
        asyncPOST.execute();

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

        if (!(hashMapArrayList.size() ==0)){
            hashMapArrayList.clear();
        }

        try {
            JSONArray jsonArray=new JSONArray(output);
            HashMap<String,String> stringHashMap;
            for (int i=0;i<jsonArray.length();i++){
                stringHashMap=new HashMap<String, String>();
                stringHashMap.put("customer_id",jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("washing_quality",jsonArray.getJSONObject(i).getString("washing_quality"));
                stringHashMap.put("puntuality",jsonArray.getJSONObject(i).getString("puntuality"));
                stringHashMap.put("staff_behaviour",jsonArray.getJSONObject(i).getString("staff_behaviour"));
                stringHashMap.put("review",jsonArray.getJSONObject(i).getString("review"));
                stringHashMap.put("customer_name",jsonArray.getJSONObject(i).getString("customer_name"));
                stringHashMap.put("created_at",jsonArray.getJSONObject(i).getString("created_at"));

                hashMapArrayList.add(stringHashMap);
            }
            adapterFeedback=new AdapterFeedback(activity,hashMapArrayList);
            list_feedback.setAdapter(adapterFeedback);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
