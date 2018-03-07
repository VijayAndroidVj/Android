package com.example.chandru.laundry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.laundry.Adapter.cutomerAdapter;
import com.example.chandru.laundry.Adapter.deliveryAdapter;
import com.example.chandru.laundry.Pojo.delivery;
import com.example.chandru.laundry.Pojo.landingcutomer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView count;
    private List<landingcutomer> maintain = new ArrayList<>();
    private cutomerAdapter cAdapter;
    private RecyclerView recycler_view;
    private String dataOne;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Dashboard");

        ImageView add=(ImageView)findViewById(R.id.btnADD);
        ImageView delivery=(ImageView)findViewById(R.id.btndelivery);
        ImageView addservice=(ImageView)findViewById(R.id.btnADDItem);
        ImageView additem=(ImageView)findViewById(R.id.btnaddproduct);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addscat=new Intent(MainActivity.this,AddCustomer.class);
                startActivity(addscat);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addscat=new Intent(MainActivity.this,Delivery.class);
                startActivity(addscat);

            }
        });
        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addscat=new Intent(MainActivity.this,AddService.class);
                startActivity(addscat);

            }
        });
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addscat=new Intent(MainActivity.this,AddProduct.class);
                startActivity(addscat);

            }
        });

        String Url = "http://demo.adityametals.com/api/dashboard.php" ;
        new serverUpload().execute(Url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            Intent intent = new Intent(MainActivity.this, AddService.class);

            startActivity(intent);
           // count=(TextView)findViewById(R.id.textView);
            //count.setText("Add is clicked");
            return(true);
        case R.id.reset:
            Intent intents = new Intent(MainActivity.this, AddProduct.class);

            startActivity(intents);
           // count=(TextView)findViewById(R.id.textView);
           // count.setText("Nothing is selected");
            return(true);
        case R.id.about:
            Intent intentd = new Intent(MainActivity.this, AddLocation.class);

            startActivity(intentd);
           // Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
           // showOptiontAlert(MainActivity.this, "Choose", "")
            ;
            return(true);
        case R.id.exit:
           // finish();
            showMeetingtAlert(MainActivity.this, "Logout", "Are you sure want to logout?")
;            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }


    public void showMeetingtAlert(Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);
        Button dialogButtonCancel = (Button) dialogView.findViewById(R.id.customDialogCancel);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(title);
        txtMessage.setText(message);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                PreferenceUtil preferenceUtil = new PreferenceUtil(MainActivity.this);
                preferenceUtil.logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }); dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }


    public void showOptiontAlert(Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custome_option, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Button addservices = (Button) dialogView.findViewById(R.id.btnaddservice);
        final Button additems = (Button) dialogView.findViewById(R.id.btnadditem);
        Button addlocations = (Button) dialogView.findViewById(R.id.btnaddlocation);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

       // txtTitle.setText(title);
       // txtMessage.setText(message);
        addservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, AddService.class);

                startActivity(intent);
//                PreferenceUtil preferenceUtil = new PreferenceUtil(MainActivity.this);
//                preferenceUtil.logout();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
            }
        });
        additems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, AddProduct.class);

                startActivity(intent);

            }
        });

        addlocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent intent = new Intent(MainActivity.this, AddLocation.class);

                startActivity(intent);

            }
        });
        alertDialog.show();
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder

                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    private class serverUpload extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {


            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading in.., please wait");
            dialog.setTitle("");
            // dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            dialog.show();
            dialog.setCancelable(false);
            }
            catch (Exception c)
            {
                Toast.makeText(MainActivity.this, "Network Error ,  try again..", Toast.LENGTH_SHORT)
                        .show();
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //String url = params[0].replace(" ", "%20");
                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity entity = response.getEntity();
                dataOne = (EntityUtils.toString(entity));


            } catch (IOException e) {
                e.printStackTrace();

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            try {
               // String[] separated = dataOne.split("\\}\\{");
               // String one = separated[0] + "}";
                //String two = "{"+ separated[1] ;
                //Log.d("success", one);
                //maintain.clear();
                JSONObject jsono = new JSONObject(dataOne);
                String Cname = jsono.getString("open_status");
                String CnameOne = jsono.getString("delivered_status");
                String CnameTwo = jsono.getString("customer_count");
                String CnameThree = jsono.getString("total_order");
                TextView tone=(TextView)findViewById(R.id.TextViewID);
                TextView ttwo=(TextView)findViewById(R.id.TextViewOne);
                TextView tthree=(TextView)findViewById(R.id.TextViewTwo);
                TextView tfour=(TextView)findViewById(R.id.TextViewThree);
                tone.setText(Cname);
                ttwo.setText(CnameOne);
                tthree.setText(CnameTwo);
                tfour.setText(CnameThree);

              //  JSONArray jsonOneArrayOne = jsono.getJSONArray("details");
              //  for (int i = 0; i < jsono.length(); i++) {
                   // JSONObject jssOne = jsono.getJSONObject(i);
                   // String Cname = jssOne.getString("customer_name");
                   // Billno = jssOne.getString("order_id");
                  //  String phones = jssOne.getString("customer_phone");
                  //  String addresss = jssOne.getString("customer_address");
                   // String amtt = jssOne.getString("total_amount");
                   // String adamt = jssOne.getString("advance_amount");
                   // bamt = jssOne.getString("balance_amount");
                   // String qtyts = jssOne.getString("total_laundry");


               // }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
