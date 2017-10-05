package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Config;
import common.Response;

public class GetNotifiction extends AppCompatActivity implements Response {

    private AsyncPOST asyncPOST;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private GetNotifiction activity;
    private AdapterPostWall adapterWall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiction);
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.getDATA(activity, "type").equals("admin")) {
            nameValuePairs.add(new BasicNameValuePair("", ""));
            asyncPOST = new AsyncPOST(nameValuePairs, activity, Config.MAIN_URL + Config.GET_OFFERS, activity);
            asyncPOST.execute();
        }
    }


    @Override
    public void processFinish(String output) {

        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            HashMap<String, String> stringHashMap;
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("wallid", jsonArray.getJSONObject(i).getString("wallid"));
                stringHashMap.put("about_wall", jsonArray.getJSONObject(i).getString("about_wall"));
                stringHashMap.put("des_wall", jsonArray.getJSONObject(i).getString("des_wall"));
                stringHashMap.put("posted_by", jsonArray.getJSONObject(i).getString("posted_by"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("image_path", jsonArray.getJSONObject(i).getString("image_path"));
                hashMapArrayList.add(stringHashMap);
            }
            adapterWall = new AdapterPostWall(activity, hashMapArrayList, "");
//            list_wall.setAdapter(adapterWall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
