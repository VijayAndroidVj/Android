package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Response;
import uk.co.senab.photoview.PhotoView;

public class PriceList extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private ListView list_item;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> strings_product = new ArrayList<String>();
    EditText edt_type;
    private ArrayList<HashMap<String, String>> getHashMapArrayList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> stringArrayList = new ArrayList<String>();
    private AdapterPricelist adapterPricelist;


    private TextView txt_product;

    private PhotoView image;

    private DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_product = (TextView) findViewById(R.id.txt_product);

        getSupportActionBar().setTitle("Pricing-List");
        edt_type = (EditText) findViewById(R.id.edt_type);

        list_item = (ListView) findViewById(R.id.list_item);
        stringArrayList.add("Women");
        stringArrayList.add("Men");
        stringArrayList.add("Kids");
        stringArrayList.add("Home");

        getCustomerPriceListFromserver();
        // perform setOnTouchListener event on ImageView

        // perform setOnZoomInClickListener event on ZoomControls
        df = new DecimalFormat("#.##");

        image = (PhotoView) findViewById(R.id.img);

        //
        // Set the OnTouchImageViewListener which updates edit texts
        // with zoom and scroll diagnostics.
        //
//        image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
//
//            @Override
//            public void onMove() {
//                PointF point = image.getScrollPosition();
//                RectF rect = image.getZoomedRect();
//                float currentZoom = image.getCurrentZoom();
//                boolean isZoomed = image.isZoomed();
//                scrollPositionTextView.setText("x: " + df.format(point.x) + " y: " + df.format(point.y));
//                zoomedRectTextView.setText("left: " + df.format(rect.left) + " top: " + df.format(rect.top)
//                        + "\nright: " + df.format(rect.right) + " bottom: " + df.format(rect.bottom));
//                currentZoomTextView.setText("getCurrentZoom(): " + currentZoom + " isZoomed(): " + isZoomed);
//            }
//        });

//        strings_product.add("")
       /* edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PriceList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.product_list);
                ListView list_item2 = (ListView) dialog.findViewById(R.id.list_item2);
                ArrayAdapter arrayAdapter = new ArrayAdapter(PriceList.this, R.layout.product_name, R.id.txt_product, stringArrayList);
                list_item2.setAdapter(arrayAdapter);
                list_item2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        nameValuePairs.add(new BasicNameValuePair("product_type", stringArrayList.get(i)));
                        dialog.dismiss();
                        txt_product.setText(stringArrayList.get(i) + " " + "List");
                        if (common.Config.isNetworkAvailable(PriceList.this)) {
                            asyncPOST = new AsyncPOST(nameValuePairs, PriceList.this, common.Config.MAIN_URL + common.Config.PRICE_LIST, PriceList.this);
                            asyncPOST.execute();

                        } else {
                            common.Config.alert("Internet Disconnected....", "Please Check Your Internet Connection", 0, PriceList.this);
                        }
                    }
                });
                dialog.show();

            }
        });*/


    }

    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private void getCustomerPriceListFromserver() {
        try {
            nameValuePairs.add(new BasicNameValuePair("", ""));

            asyncPOST = new AsyncPOST(nameValuePairs, PriceList.this, common.Config.MAIN_URL + common.Config.GET_CUSTOMER_PRICE, PriceList.this);
            asyncPOST.execute();

        } catch (Exception e) {
            e.printStackTrace();
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


    @Override
    public void processFinish(String output) {
        try {
            if (!(hashMapArrayList.size() == 0)) {
                hashMapArrayList.clear();
            }

            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.has("result") && jsonObject.getString("result").equals("success")) {
                String path = jsonObject.getString("image");
                Picasso.with(PriceList.this).load(path).into(image);
            }
           /* JSONArray jsonArray = new JSONArray(output);
            HashMap<String, String> stringHashMap;
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("pid", jsonArray.getJSONObject(i).getString("pid"));
                stringHashMap.put("product_type", jsonArray.getJSONObject(i).getString("product_type"));
                stringHashMap.put("product_name", jsonArray.getJSONObject(i).getString("product_name"));
                stringHashMap.put("product_price", jsonArray.getJSONObject(i).getString("product_price"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));

                hashMapArrayList.add(stringHashMap);
            }
            ArrayList<HashMap<String, String>> arrayList = hashMapArrayList;
            Collections.sort(arrayList, new Comparator<HashMap<String, String>>() {

                @Override
                public int compare(HashMap<String, String> lhs,
                                   HashMap<String, String> rhs) {
                    // Do your comparison logic here and retrn accordingly.

                    return lhs.get("product_name").compareToIgnoreCase(rhs.get("product_name"));
                }
            });
            adapterPricelist = new AdapterPricelist(PriceList.this, hashMapArrayList, "");
            list_item.setAdapter(adapterPricelist);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
