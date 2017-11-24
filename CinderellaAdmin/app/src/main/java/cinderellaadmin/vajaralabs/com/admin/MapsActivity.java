package cinderellaadmin.vajaralabs.com.admin;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Response {

    private GoogleMap mMap;
    private MapsActivity activity;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
    private String name="",lat="",longi="",address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Location-"+getIntent().getStringExtra("delivery_person"));
        activity=this;
        name=getIntent().getStringExtra("delivery_person");
        address=getIntent().getStringExtra("address_location");
        lat=getIntent().getStringExtra("lattitude");
        longi=getIntent().getStringExtra("longitude");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Configg.isNetworkAvailable(activity)){
            nameValuePairs.add(new BasicNameValuePair("did",getIntent().getStringExtra("did")));
            asyncPOST=new AsyncPOST(nameValuePairs,activity,Configg.MAIN_URL+
            Configg.GET_LOCATION,activity);
            asyncPOST.execute();

        }
        else{
            Configg.alert("Internet Lost...","Please Check Your Inernet Connection.",0,activity);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Delivery Person"+'\n'+
        name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONArray jsonArray=new JSONArray(output);
            for (int i=0;i<jsonArray.length();i++){
                name=jsonArray.getJSONObject(0).getString("delivery_person");
                lat=jsonArray.getJSONObject(0).getString("lattitude");
                address=jsonArray.getJSONObject(0).getString("address_location");
                longi=jsonArray.getJSONObject(0).getString("longitude");



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
