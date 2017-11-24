package cinderellaadmin.vajaralabs.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class AddCity extends AppCompatActivity implements Response{
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private Button btn_submit;
    private EditText edt_city, edt_locality;
    private AddCity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        activity=this;
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_city = (EditText) findViewById(R.id.edt_city);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_city.getText().toString().equals("")) {
                        nameValuePairs.add(new BasicNameValuePair("city", edt_city.getText().toString().trim()));
                        asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.ADD_CITY, activity);
                        asyncPOST.execute();



                } else {
                    Toast.makeText(getApplicationContext(), "City Should Not Empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("3")) {
                Configg.alert("Response From Server.",jsonObject.getString("message"),1,activity);


            } else if (jsonObject.getString("success").equals("1")) {

                Configg.alert("Response From Server.",jsonObject.getString("message"),2,activity);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
