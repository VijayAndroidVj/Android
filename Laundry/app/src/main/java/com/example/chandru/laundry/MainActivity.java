package com.example.chandru.laundry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        ImageView add=(ImageView)findViewById(R.id.btnADD);
        ImageView delivery=(ImageView)findViewById(R.id.btndelivery);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
//        case R.id.add:
//            count=(TextView)findViewById(R.id.textView);
//            count.setText("Add is clicked");
//            return(true);
//        case R.id.reset:
//            count=(TextView)findViewById(R.id.textView);
//            count.setText("Nothing is selected");
//            return(true);
//        case R.id.about:
//            Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
//            return(true);
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
}
