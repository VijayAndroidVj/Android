package cinderellaadmin.vajaralabs.com.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private LinearLayout list_item;

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

        list_item = (LinearLayout) findViewById(R.id.llorderlist);

        txt_count.setText("no.of items:" + '\n' + getIntent().getStringExtra("overall_count"));
        String total = getIntent().getStringExtra("overall_total");
        String given = getIntent().getStringExtra("given_amt");
        txt_payed.setText("given amt:" + '\n' + given);
        txt_total.setText("total amt:" + '\n' + total);
        try {
            double ttl = Double.valueOf(total);
            double gvn = Double.valueOf(given);
            double blnce = ttl - gvn;
            txt_balance.setText("balance amt:" + blnce);

        } catch (Exception e) {
            e.printStackTrace();
            txt_balance.setText(getIntent().getStringExtra("balance_amt"));
        }

        Picasso.with(activity).load(getIntent().getStringExtra("image_one")).into(img_1);

        List<String> listitems = arr(getIntent().getStringExtra("items"));
        List<String> costList = arr(getIntent().getStringExtra("count"));

       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1
                , listitems);

        list_item.setAdapter(arrayAdapter);*/


        System.out.println("items" + getIntent().getStringExtra("items"));

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < listitems.size(); i++) {

            final View convertView = inflater.inflate(R.layout.price_list, null);
            final TextView txt_product = (TextView) convertView.findViewById(R.id.txt_product);
            final TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
            convertView.setId(i);

            txt_product.setText(listitems.get(i));
            txt_price.setText(costList.get(i));
            convertView.setTag(i);
            list_item.addView(convertView);


        }
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
