package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class PriceList extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private EditText edt_type;
    private EditText edt_product, edt_price;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Price-List");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        edt_type = (EditText) findViewById(R.id.edt_type);
        edt_product = (EditText) findViewById(R.id.edt_product_name);
        edt_price = (EditText) findViewById(R.id.edt_price);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PriceList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.product_list);
                Button btn_women = (Button) dialog.findViewById(R.id.btn_women);
                Button btn_men = (Button) dialog.findViewById(R.id.btn_men);
                Button btn_kids = (Button) dialog.findViewById(R.id.btn_kids);
                Button btn_home = (Button) dialog.findViewById(R.id.btn_home);

                btn_women.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Women");
                        dialog.dismiss();
                    }
                });
                btn_men.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Men");
                        dialog.dismiss();
                    }
                });
                btn_kids.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Kids");
                        dialog.dismiss();
                    }
                });

                btn_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Home");
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                if (!edt_type.getText().toString().equals("")) {
                    if (!edt_product.getText().toString().equals("")) {
                        if (!edt_price.getText().toString().equals("")) {
                            nameValuePairs.add(new BasicNameValuePair("product_type", edt_type.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("product_name", edt_product.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("product_price", edt_price.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));



                            asyncPOST = new AsyncPOST(nameValuePairs, PriceList.this, Configg.MAIN_URL + Configg.PRICE_LIST, PriceList.this);
                            asyncPOST.execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter The Product Price.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter The Product Name.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Select The Product Type First.", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
        try {
            JSONObject jsonObject=new JSONObject(output);
            if (jsonObject.getString("success").equals("1")){
                Configg.alert("Response From Server.",jsonObject.getString("message"),1,PriceList.this);

            }
            else if (jsonObject.getString("success").equals("3")){
                Configg.alert("Response From Server.",jsonObject.getString("message"),0,PriceList.this);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
