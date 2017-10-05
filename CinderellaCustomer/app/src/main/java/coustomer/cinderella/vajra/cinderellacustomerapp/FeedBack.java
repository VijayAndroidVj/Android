package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Config;
import common.Response;

public class FeedBack extends AppCompatActivity implements Response {

    private RatingBar ratebar_quality;
    private FeedBack activity;
    private RatingBar ratebar_behaviour;
    private RatingBar ratebar_puntuality;
    private EditText edt_review;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private String washing = "", puntuality = "", behaviour = "";
    private Button btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;
        getSupportActionBar().setTitle("FeedBack");
        ratebar_quality = (RatingBar) findViewById(R.id.ratebar_quality);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        ratebar_puntuality = (RatingBar) findViewById(R.id.ratebar_puntuality);
        ratebar_behaviour = (RatingBar) findViewById(R.id.ratebar_behaviour);
        edt_review = (EditText) findViewById(R.id.edt_review);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isNetworkConnected(activity)) {
                    if (!washing.equals("")) {
                        if (!puntuality.equals("")) {
                            if (!behaviour.equals("")) {


                                nameValuePairs.add(new BasicNameValuePair("washing_quality", washing));
                                nameValuePairs.add(new BasicNameValuePair("puntuality", puntuality));
                                nameValuePairs.add(new BasicNameValuePair("staff_behaviour", behaviour));
                                nameValuePairs.add(new BasicNameValuePair("review", edt_review.getText().toString()));
                                nameValuePairs.add(new BasicNameValuePair("customer_id", Config.getDATA(activity, "cid")));
                                nameValuePairs.add(new BasicNameValuePair("customer_name", Config.getDATA(activity, "name")));


                                asyncPOST = new AsyncPOST(nameValuePairs, activity, Config.MAIN_URL + Config.CUSTOMER_FEED, activity);
                                asyncPOST.execute();
                            } else {
                                Toast.makeText(activity, "Give Star Level For Staff Behaviour.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, "Give Star Level For Puntuality.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Give Star Level For Washing Quality.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Config.alert("Internet Lost.", "Please Check Your Internet Connection.", 0, activity);
                }

            }
        });

        ratebar_quality.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(activity, "" + rating, Toast.LENGTH_SHORT).show();
                washing = String.valueOf(rating);
            }
        });
        ratebar_behaviour.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                behaviour = String.valueOf(rating);
            }
        });
        ratebar_puntuality.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                puntuality = String.valueOf(rating);
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
            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("1")) {
                Config.alert("Feedback...", jsonObject.getString("message"), 0, activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
