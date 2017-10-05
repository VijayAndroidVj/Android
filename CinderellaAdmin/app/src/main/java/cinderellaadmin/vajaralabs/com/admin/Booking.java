package cinderellaadmin.vajaralabs.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import common.Response;

public class Booking extends AppCompatActivity implements Response {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
    }

    @Override
    public void processFinish(String output) {

    }
}
