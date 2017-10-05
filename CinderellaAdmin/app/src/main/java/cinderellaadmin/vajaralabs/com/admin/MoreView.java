package cinderellaadmin.vajaralabs.com.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Response;
import uk.co.senab.photoview.PhotoView;

public class MoreView extends AppCompatActivity implements Response {
    private TextView txt_oveal_total, txt_oveal_count, txt_product, txt_balance, txt_payed, txt_total, txt_count;
    private EditText edt_type, edt_payment;
    private PhotoView img_1;
    private LinearLayout list_itemLinearLayout;
    private MoreView activity;
    private ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("More-Detail");
        activity = this;

        txt_payed = (TextView) findViewById(R.id.txt_payed);
        img_1 = (PhotoView) findViewById(R.id.img_1);
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_balance = (TextView) findViewById(R.id.txt_balance);
        txt_count = (TextView) findViewById(R.id.txt_count);

        list_item = (ListView) findViewById(R.id.list_item);

        txt_count.setText("no.of items:" + '\n' + getIntent().getStringExtra("overall_count"));
        txt_payed.setText("given amt:" + '\n' + getIntent().getStringExtra("given_amt"));
        txt_balance.setText(getIntent().getStringExtra("balance_amt"));
        txt_total.setText("total amt:" + '\n' + getIntent().getStringExtra("overall_total"));


        Picasso.with(activity).load(getIntent().getStringExtra("image_one")).into(img_1);

        List<String> listitems = arr(getIntent().getStringExtra("items"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1
                , listitems);

        list_item.setAdapter(arrayAdapter);


        System.out.println("items" + getIntent().getStringExtra("items"));
    }

    private List<String> arr(String s) {
        String replace = s.replace("[", "");
        System.out.println(replace);
        String replace1 = replace.replace("]", "");
        System.out.println(replace1);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        return myList;

    }

    @Override
    public void processFinish(String output) {

    }
}
