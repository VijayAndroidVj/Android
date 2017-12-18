package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;


public class DeliveryPickupCustomer extends AppCompatActivity implements Response {
    private LinearLayout list_itemLinearLayout;
    private AsyncPOST asyncPOST;
    private ProgressDialog progressBar;
    private ArrayList<String> strings = new ArrayList<String>();
    private HashMap<String, String> itemCountlist = new HashMap<>();

    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<String> strings_product = new ArrayList<String>();
    EditText edt_type;
    private ArrayList<HashMap<String, String>> getHashMapArrayList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> stringArrayList = new ArrayList<String>();
    private ArrayList<String> strings_payment = new ArrayList<String>();
    private EditText edt_payment;

    private AdapterPricelist adapterPricelist;
    private TextView txt_product;
    private DeliveryPickupCustomer activity;
    public Dialog dialog;
    public ArrayList<String> strings_items = new ArrayList<String>();
    public static ArrayList<String> strings_rate = new ArrayList<String>();
    public static ArrayList<String> strings_count = new ArrayList<String>();
    public static ArrayList<String> strings_total = new ArrayList<String>();
    public static ArrayList<String> strings_comment = new ArrayList<String>();
    public boolean boolean_count;

    private Button btn_submit;
    private TextView txt_oveal_total1, txt_oveal_total2, txt_oveal_total, txt_oveal_count;
    private double over_all_total = 0.0;
    private ImageView img_1, img_2, img_3;
    private File destination, destination2, destination3;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private LayoutInflater inflater;
    private LayoutInflater inflater2;
    private double sum = 0;
    private int item_sum = 0;
    private double initialTotalSum = 0;
    private boolean allowChange = true;

    private boolean bool_exist;
    private ArrayList<Double> doubleArrayList = new ArrayList<Double>();
    private ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
    private String currentDateTime;
    private String img_str = "";
    private String taken_amt = "", balance_amt = "";
    private boolean boolean_count2;
    private EditText edtDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list_get);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_product = (TextView) findViewById(R.id.txt_product);
        txt_oveal_total1 = (TextView) findViewById(R.id.txt_oveal_total1);
        txt_oveal_total2 = (TextView) findViewById(R.id.txt_oveal_total2);
        txt_oveal_total = (TextView) findViewById(R.id.txt_oveal_total3);
        edtDiscount = (EditText) findViewById(R.id.edtDiscount);
        getSupportActionBar().setTitle("Pricing-List");
        edt_type = (EditText) findViewById(R.id.edt_type);
        edt_payment = (EditText) findViewById(R.id.edt_payment);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        img_1 = (ImageView) findViewById(R.id.img_1);
        img_2 = (ImageView) findViewById(R.id.img_2);
        img_3 = (ImageView) findViewById(R.id.img_3);


        list_itemLinearLayout = (LinearLayout) findViewById(R.id.list_item);
        txt_oveal_count = (TextView) findViewById(R.id.txt_oveal_count);
        stringArrayList.add("Women");
        stringArrayList.add("Men");
        stringArrayList.add("Kids");
        stringArrayList.add("Home");

        strings_payment.add("Pay Now");
        strings_payment.add("POD");

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_str = "1";
                selectImage();
            }
        });
        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_str = "2";

                selectImage();
            }
        });
        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_str = "3";

                selectImage();
            }
        });

        edtDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (allowChange)
                    try {
                        String sdiscount = edtDiscount.getText().toString();
                        int discount = 0;
                        if (sdiscount.length() > 0) {
                            discount = Integer.valueOf(sdiscount);
                        }
                        if (discount > initialTotalSum) {
                            allowChange = false;
                            edtDiscount.setText(sdiscount.substring(0, edtDiscount.getText().toString().length() - 1));
                            edtDiscount.setSelection(edtDiscount.getText().toString().length());
                            allowChange = true;
                        } else {
                            sum = initialTotalSum - discount;
                            double percentage = ((sum * 18) / 100);
                            txt_oveal_total2.setText(percentage + " GST 18%");
                            sum = sum + percentage;
                            txt_oveal_total.setText("" + sum);
                            txt_oveal_count.setText(item_sum + "");

                            getSupportActionBar().setTitle("Pricing-List " + "Amt:" + sum + '\n' +
                                    "Qty:" + item_sum);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });


        if (!(list_itemLinearLayout.getChildCount() == 0)) {
            for (int k = 0; k < list_itemLinearLayout.getChildCount(); k++) {
                final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(k);

                TextView view1 = (TextView) linearLayout_parent.getChildAt(0);
                final EditText edt_count = (EditText) linearLayout_parent.findViewById(R.id.edt_count);
//                final ImageView img_remove = (ImageView) linearLayout_parent.findViewById(R.id.img_remove);
//                img_remove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        System.out.println("hello....");
//                    }
//                });

            }
        }

//        strings_product.add("")
        edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflater = (LayoutInflater) activity
                        .getSystemService(LAYOUT_INFLATER_SERVICE);

                dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.get_price2);
                final LinearLayout linear_item = (LinearLayout) dialog.findViewById(R.id.linear_item);
                EditText edt_search = (EditText) dialog.findViewById(R.id.edt_search);


                for (int i = 0; i < hashMapArrayList.size(); i++) {

                    final View convertView = inflater.inflate(R.layout.price_list, null);
                    final TextView txt_product = (TextView) convertView.findViewById(R.id.txt_product);
                    final TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
                    convertView.setId(i);

                    txt_product.setText(hashMapArrayList.get(i).get("product_name"));
                    txt_price.setText(hashMapArrayList.get(i).get("product_price"));
                    convertView.setTag(i);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getTag() != null) {
                                int tags = (int) view.getTag();
                                String stags = hashMapArrayList.get(tags).get("product_name");
                                String amount = hashMapArrayList.get(tags).get("product_price");
                                if (stags.equalsIgnoreCase("Other")) {
                                    addOthers();
                                    return;
                                }
                                String pcs = hashMapArrayList.get(tags).get("pcs");
                                addSelectedProduct(stags, amount, pcs);
                            }

                        }
                    });
                    linear_item.addView(convertView);


                }
                edt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        for (int j = 0; j < linear_item.getChildCount(); j++) {
                            LinearLayout linearLayout_parent = (LinearLayout) linear_item.getChildAt(j);
                            LinearLayout linearLayout_child = (LinearLayout) linearLayout_parent.getChildAt(0);
                            TextView view1 = (TextView) linearLayout_child.getChildAt(0);
                            TextView textView_rate = (TextView) linearLayout_child.getChildAt(1);

                            String s = charSequence.toString();

                            if (view1.getText().toString().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
                                linearLayout_parent.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout_parent.setVisibility(View.GONE);

                            }

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                dialog.show();

//                adapterPricelist = new AdapterPricelist(activity, hashMapArrayList, "");


            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!(strings_items.size() == 0)) {
                    strings_items.clear();
                    strings_rate.clear();
                    strings_count.clear();
                    strings_total.clear();
                    strings_comment.clear();
                }
                if (!(strings_rate.size() == 0)) {
                    strings_rate.clear();

                }
                if (!(strings_count.size() == 0)) {
                    strings_count.clear();

                }
                if (!(strings_total.size() == 0)) {
                    strings_total.clear();
                }
                if (!(strings_comment.size() == 0)) {
                    strings_comment.clear();
                }


                if (txt_oveal_total.getText().toString().equals("") || txt_oveal_total.getText().toString().equals("0.0")) {
                    Toast.makeText(activity, "First Choose The Items.", Toast.LENGTH_SHORT).show();

                } else if (edt_payment.getText().toString().equals("")) {
                    Toast.makeText(activity, "Select The Payment Mode.", Toast.LENGTH_SHORT).show();

                } else if (destination == null && destination2 == null && destination3 == null) {
                    Toast.makeText(activity, "Capture Atleast One Image.", Toast.LENGTH_SHORT).show();

                } else {

                    for (int i = 0; i < list_itemLinearLayout.getChildCount(); i++) {
                        final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(i);

                        TextView txt_product = (TextView) linearLayout_parent.findViewById(R.id.txt_product);
                        TextView txt_price = (TextView) linearLayout_parent.findViewById(R.id.txt_price);
                        TextView txt_total = (TextView) linearLayout_parent.findViewById(R.id.txt_total);
                        EditText edt_count = (EditText) linearLayout_parent.findViewById(R.id.edt_count);
                        EditText edt_comment = (EditText) linearLayout_parent.findViewById(R.id.edt_comment);

                        if (edt_count.getText().toString().equals("")) {
                            edt_count.requestFocus();
                            boolean_count = true;
                            Toast.makeText(activity, "Add Item Or Remove it.", Toast.LENGTH_SHORT).show();
                            break;

                        }

                        strings_items.add(txt_product.getText().toString() + "");
                        strings_rate.add(txt_price.getText().toString() + "");
                        strings_count.add(edt_count.getText().toString() + "");
                        strings_total.add(txt_total.getText().toString() + "");
                        strings_comment.add(edt_comment.getText().toString() + "");
                        Log.e("strings_items", txt_product.getText().toString());


                    }
                    if (!boolean_count) {
                        progressBar = new ProgressDialog(activity);

                        UploadFileToServer toServer = new UploadFileToServer();
                        toServer.execute();
                        nameValuePairs.add(new BasicNameValuePair("image", destination.toString()));
//                        nameValuePairs.add(new BasicNameValuePair("image1", destination2.toString()));
//                        nameValuePairs.add(new BasicNameValuePair("image2", destination3.toString()));

                        nameValuePairs.add(new BasicNameValuePair("instruction", strings_comment.toString()));

                        nameValuePairs.add(new BasicNameValuePair("items", strings_items.toString()));
                        nameValuePairs.add(new BasicNameValuePair("unique_code", getIntent().getStringExtra("unique_code")));
                        nameValuePairs.add(new BasicNameValuePair("rate", strings_rate.toString()));

                        nameValuePairs.add(new BasicNameValuePair("count", strings_count.toString()));
                        nameValuePairs.add(new BasicNameValuePair("bill", getIntent().getStringExtra("unique_code") +
                                "," + strings_count.toString()));

                        nameValuePairs.add(new BasicNameValuePair("total", strings_total.toString()));
                        nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));
                        nameValuePairs.add(new BasicNameValuePair("status", "active"));
                        nameValuePairs.add(new BasicNameValuePair("given_amt", taken_amt));
                        nameValuePairs.add(new BasicNameValuePair("balance_amt", balance_amt));
                        int discount = 0;
                        if (edtDiscount.getText().toString().length() > 0) {
                            discount = Integer.parseInt(edtDiscount.getText().toString());
                        }
                        nameValuePairs.add(new BasicNameValuePair("discount", "" + discount));
                        nameValuePairs.add(new BasicNameValuePair("initialTotalSum", "" + initialTotalSum));
                        nameValuePairs.add(new BasicNameValuePair("overall_total", "" + sum));
                        nameValuePairs.add(new BasicNameValuePair("overall_count", txt_oveal_count.getText().toString()));
                        if (edt_payment.getText().toString().equals("POD")) {
                            nameValuePairs.add(new BasicNameValuePair("payment_mode", "POD"));

                        } else {
                            nameValuePairs.add(new BasicNameValuePair("payment_mode", "paying_now"));
                        }

                        nameValuePairs.add(new BasicNameValuePair("completion", "50"));
                        nameValuePairs.add(new BasicNameValuePair("mobile_date_pickuped", currentDateTime));

                        nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
                        System.out.println("namevalue" + nameValuePairs.toString());

                    }
                }

                Log.e("strings_items", strings_items.toString());
//                if (!(list_itemLinearLayout.getChildCount() == 0)) {
//                    nameValuePairs.add(new BasicNameValuePair("items", strings_items.toString()));
//                    nameValuePairs.add(new BasicNameValuePair("unique_code", getIntent().getStringExtra("unique_code")));
//
//                    nameValuePairs.add(new BasicNameValuePair("count", strings_count.toString()));
//                    nameValuePairs.add(new BasicNameValuePair("bill", getIntent().getStringExtra("unique_code") +
//                            "," + strings_count.toString()));
//
//                    nameValuePairs.add(new BasicNameValuePair("total", strings_total.toString()));
//                    nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));
//                    nameValuePairs.add(new BasicNameValuePair("status", "active"));
//
//
//                    nameValuePairs.add(new BasicNameValuePair("overall_total", txt_oveal_total.getText().toString()));
//                    nameValuePairs.add(new BasicNameValuePair("payment", edt_payment.getText().toString()));
//                    nameValuePairs.add(new BasicNameValuePair("completion", "50"));
//                    nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
//
//
////                    System.out.print("response" + nameValuePairs.toString());
////
////                    Toast.makeText(activity,"pair"+nameValuePairs.toString(),1000).show();
////
//
//
//                    if (!edt_payment.getText().toString().equals("")) {
//                        asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.PICKUP_CUSTOMER, activity);
//                        asyncPOST.execute();
//                        Log.w("output", nameValuePairs.toString());
//
//                        System.out.println("output" + nameValuePairs);
//
//                    } else {
//
//                        Toast.makeText(activity, "Choose The Payment.", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } else {

//                    Toast.makeText(activity, "Choose The Itesms", Toast.LENGTH_SHORT).show();
//
//                }
            }
        });

        if (common.Configg.isNetworkAvailable(activity)) {
            volley_get_price();
        } else {
            common.Configg.alert("Internet Disconnected....", "Please Check Your Internet Connection", 0, activity);
        }


        edt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.payment_list);
                ListView list_item2 = (ListView) dialog.findViewById(R.id.list_item2);
                ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, strings_payment);
                list_item2.setAdapter(arrayAdapter);
                list_item2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        if (strings_payment.get(i).equals("POD")) {
                            edt_payment.setText(strings_payment.get(i));
                            taken_amt = "";
                            balance_amt = "";
                            dialog.dismiss();
                        } else {
                            final Dialog dialog1 = new Dialog(activity);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.payment);
                            TextView tx_payment_type = (TextView) dialog1.findViewById(R.id.tx_payment_type);
                            final TextView total_amt = (TextView) dialog1.findViewById(R.id.total_amt);
                            final TextView total_balance = (TextView) dialog1.findViewById(R.id.total_balance);
                            final EditText edt_amt = (EditText) dialog1.findViewById(R.id.edt_amt);
                            Button btn_submit = (Button) dialog1.findViewById(R.id.btn_submit);
                            Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
                            tx_payment_type.setText(strings_payment.get(i));
                            total_amt.setText("Total Amt." + txt_oveal_total.getText().toString());
                            final double amt1 = Double.parseDouble(txt_oveal_total.getText().toString());

                            edt_amt.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    String s = charSequence + "";
                                    if (!s.equals("")) {
                                        double amt2 = Double.parseDouble(s);
                                        if (amt2 <= amt1) {
                                            double balance = amt1 - amt2;
                                            total_balance.setText("balance:" + String.valueOf(balance));
                                        } else {
                                            Toast.makeText(activity, "Exist Maximum Amt.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        s = "0";

                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int d3 = 0;
                                    if (!edt_amt.getText().toString().equals("")) {
                                        d3 = Integer.parseInt(edt_amt.getText().toString());
                                    }

                                    if (!edt_amt.getText().toString().equals("") &&
                                            d3 > 50) {
                                        double d1 = Double.parseDouble(txt_oveal_total.getText().toString());
                                        double d2 = Double.parseDouble(edt_amt.getText().toString());
                                        if (d2 > d1) {
                                            Toast.makeText(activity, "Enter Valid Amount.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            taken_amt = edt_amt.getText().toString();
                                            balance_amt = total_balance.getText().toString();
                                            System.out.println("taken" + total_amt + "balance" + balance_amt);
//                                            Toast.makeText(activity, "." + taken_amt + "ba" + balance_amt.replaceAll("[^\\d.]", ""), Toast.LENGTH_SHORT).show();
                                            dialog1.dismiss();
                                            dialog.dismiss();
                                            edt_payment.setText(strings_payment.get(i) + "-" + "Payed:" + taken_amt +
                                                    "," + "" + balance_amt);
                                        }
                                    } else {
                                        Toast.makeText(activity, "Enter The Valid Amount.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                            dialog1.setCancelable(false);

                        }

                    }
                });
                if (!txt_oveal_total.getText().toString().equals("") && !txt_oveal_total.getText().toString().equals("0.0")) {
                    for (int i = 0; i < list_itemLinearLayout.getChildCount(); i++) {
                        final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(i);

                        EditText edt_count = (EditText) linearLayout_parent.findViewById(R.id.edt_count);

                        if (edt_count.getText().toString().equals("")) {
                            edt_count.requestFocus();
                            boolean_count2 = true;
                            Toast.makeText(activity, "Add Item Or Remove it.", Toast.LENGTH_SHORT).show();
                            break;


                        }

                    }
                    if (!boolean_count2) {
                        dialog.show();
                    }
                    boolean_count2 = false;


                } else {
                    Toast.makeText(activity, "First Add The Items.", Toast.LENGTH_SHORT).show();
                    boolean_count2 = false;
                }

            }
        });
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void addSelectedProduct(String name, String amount, final String pcs) {

        bool_exist = false;
        for (int j = 0; j < list_itemLinearLayout.getChildCount(); j++) {
            final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(j);

            TextView view1 = (TextView) linearLayout_parent.getChildAt(0);
            final EditText edt_count = (EditText) linearLayout_parent.findViewById(R.id.edt_count);


            if (view1.getText().toString().equals(name)) {
                bool_exist = true;
                if (dialog.isShowing())
                    dialog.dismiss();
                edt_count.requestFocus();


                Toast.makeText(activity, "Already Item Added Just Increase The Count.", Toast.LENGTH_SHORT).show();
            }


        }
        if (!bool_exist) {
            inflater2 = (LayoutInflater) activity
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View convertView2 = inflater.inflate(R.layout.price_table, null);
            TextView txt_product2 = (TextView) convertView2.findViewById(R.id.txt_product);
            final TextView txt_price2 = (TextView) convertView2.findViewById(R.id.txt_price);
            final EditText edt_count = (EditText) convertView2.findViewById(R.id.edt_count);
            edt_count.requestFocus();

//                                final ImageView img_remove = (ImageView) convertView2.findViewById(R.id.img_remove);
//                                img_remove.setId(convertView.getId());
            txt_product2.setText(name);
            txt_price2.setText(amount);
            list_itemLinearLayout.addView(convertView2);

            for (int k = 0; k < list_itemLinearLayout.getChildCount(); k++) {
                final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(k);
                final EditText edt_count2 = (EditText) convertView2.findViewById(R.id.edt_count);
                final TextView txt_total = (TextView) convertView2.findViewById(R.id.txt_total);
                final TextView txt_price3 = (TextView) convertView2.findViewById(R.id.txt_price);

                edt_count2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        doubleArrayList.clear();
                        integerArrayList.clear();
                        initialTotalSum = 0;
                        sum = 0;
                        item_sum = 0;

                        String s = charSequence + "";
                        if (s.equals("")) {
                            edt_payment.getText().clear();
                            taken_amt = "";
                            balance_amt = "";
                        }
                        if (!s.equals("")) {

                            int c = Integer.parseInt(s);
                            double p = Double.parseDouble(txt_price3.getText().toString());

                            double t = c * p;
                            txt_total.setText(String.valueOf(t));


                        } else {
//                                               edt_count2.setText("0");
                            txt_total.setText("0");
////                                               txt_oveal_total.setText("0");
//                                              // sum=0.0;
//                                               item_sum=0;
                        }
                        doubleArrayList.clear();
                        integerArrayList.clear();

                        for (int k = 0; k < list_itemLinearLayout.getChildCount(); k++) {
                            final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(k);
                            final TextView txt_total = (TextView) linearLayout_parent.getChildAt(3);
                            final EditText edt_count5 = (EditText) linearLayout_parent.getChildAt(2);
                            TextView txt_product2 = (TextView) linearLayout_parent.getChildAt(0);
                            doubleArrayList.add(Double.parseDouble(txt_total.getText().toString()));
                            if (!edt_count5.getText().toString().equals("")) {
                                int cont = 1;
                                try {
                                    cont = Integer.parseInt(itemCountlist.get(txt_product2.getText().toString()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                integerArrayList.add(cont * Integer.parseInt(edt_count5.getText().toString()));
                            }
                        }
                        System.out.println("array_double" + doubleArrayList.toString());

                        for (int t = 0; t < doubleArrayList.size(); t++) {
                            sum += doubleArrayList.get(t);
                        }
                        initialTotalSum = sum;
                        for (int t = 0; t < integerArrayList.size(); t++) {
                            item_sum += integerArrayList.get(t);
                        }
                        txt_oveal_total1.setText("" + initialTotalSum);
                        String sdiscount = edtDiscount.getText().toString();
                        if (sdiscount.length() > 0) {
                            int discount = Integer.valueOf(sdiscount);
                            sum = initialTotalSum - discount;
                        }
                        double percentage = ((sum * 18) / 100);
                        txt_oveal_total2.setText(percentage + " GST 18%");
                        sum = sum + percentage;
                        txt_oveal_total.setText("" + sum);
                        txt_oveal_count.setText(item_sum + "");

                        getSupportActionBar().setTitle("Pricing-List " + "Amt:" + sum + '\n' +
                                "Qty:" + item_sum);


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                    }
                });

                final TextView txt_to = (TextView) linearLayout_parent.findViewById(R.id.txt_total);
                final EditText edt_1 = (EditText) linearLayout_parent.findViewById(R.id.edt_count);

                final EditText edt_comment = (EditText) linearLayout_parent.findViewById(R.id.edt_comment);
                edt_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.comment);
                        final TextView txt_straching = (TextView) dialog.findViewById(R.id.txt_straching);
                        final TextView txt_stain_removal = (TextView) dialog.findViewById(R.id.txt_stain_removal);
                        final TextView txt_polishing = (TextView) dialog.findViewById(R.id.txt_polishing);
                        txt_straching.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                edt_comment.setText(txt_straching.getText().toString());
                                dialog.dismiss();

                            }
                        });

                        txt_stain_removal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                edt_comment.setText(txt_stain_removal.getText().toString());
                                dialog.dismiss();
                            }
                        });
                        txt_polishing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                edt_comment.setText(txt_polishing.getText().toString());
                                dialog.dismiss();

                            }
                        });
                        dialog.show();


                    }
                });
                final ImageView img_remove2 = (ImageView) linearLayout_parent.findViewById(R.id.img_remove);
                img_remove2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("hello...." + list_itemLinearLayout.getId());
                        // list_itemLinearLayout.removeViewAt(0);
                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.confirmation);
                        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                        btn_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                list_itemLinearLayout.removeView(linearLayout_parent);
                                dialog.dismiss();
                                try {
                                    doubleArrayList.clear();
                                    integerArrayList.clear();
                                    edtDiscount.setText("");
                                    item_sum = 0;
                                    sum = 0;
                                    for (int k = 0; k < list_itemLinearLayout.getChildCount(); k++) {
                                        final LinearLayout linearLayout_parent = (LinearLayout) list_itemLinearLayout.getChildAt(k);
                                        final TextView txt_total = (TextView) linearLayout_parent.getChildAt(3);
                                        final EditText edt_count5 = (EditText) linearLayout_parent.getChildAt(2);
                                        TextView txt_product2 = (TextView) linearLayout_parent.getChildAt(0);
                                        doubleArrayList.add(Double.parseDouble(txt_total.getText().toString()));
                                        if (!edt_count5.getText().toString().equals("")) {
                                            int cont = 1;
                                            try {
                                                cont = Integer.parseInt(itemCountlist.get(txt_product2.getText().toString()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            integerArrayList.add(cont * Integer.parseInt(edt_count5.getText().toString()));
                                        }
                                    }
                                    System.out.println("array_double" + doubleArrayList.toString());

                                    for (int t = 0; t < doubleArrayList.size(); t++) {
                                        sum += doubleArrayList.get(t);
                                    }
                                    initialTotalSum = sum;
                                    for (int t = 0; t < integerArrayList.size(); t++) {
                                        item_sum += integerArrayList.get(t);
                                    }
                                    txt_oveal_total1.setText("" + initialTotalSum);
                                    String sdiscount = edtDiscount.getText().toString();
                                    if (sdiscount.length() > 0) {
                                        int discount = Integer.valueOf(sdiscount);
                                        sum = initialTotalSum - discount;
                                    }
                                    double percentage = ((sum * 18) / 100);
                                    txt_oveal_total2.setText(percentage + " GST 18%");
                                    sum = sum + percentage;
                                    txt_oveal_total.setText("" + sum);
                                    txt_oveal_count.setText(item_sum + "");

                                    getSupportActionBar().setTitle("Pricing-List " + "Amt:" + sum + '\n' +
                                            "Qty:" + item_sum);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                        btn_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

            }


            dialog.dismiss();
        }
        bool_exist = false;
    }

    private void addOthers() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final EditText etProductname = (EditText) alertLayout.findViewById(R.id.edtProductName);
        final EditText edtSize = (EditText) alertLayout.findViewById(R.id.edtSize);
        final EditText edtAmount = (EditText) alertLayout.findViewById(R.id.edtAmount);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Other Product");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etProductname.getText().toString();
                String size = edtSize.getText().toString();
                String amount = edtAmount.getText().toString();
                if (name.length() <= 0) {
                    Toast.makeText(activity, "Enter Product", Toast.LENGTH_SHORT).show();
                }

                if (amount.length() <= 0) {
                    Toast.makeText(activity, "Enter Amount", Toast.LENGTH_SHORT).show();
                }

                addSelectedProduct(name + size, amount, "1");
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            progressBar.show();
            progressBar.setCancelable(false);
            progressBar.setMessage("Loading...");

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
//
//            // updating progress bar value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Configg.MAIN_URL + Configg.POST_PICKUP_DATA);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                File sourceFile = new File(destination);

                // Adding file data to http body
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                currentDateTime = dateFormat.format(new Date()); // Find todays dat

                entity.addPart("image", new FileBody(destination));
//                entity.addPart("image", new FileBody(destination2));
//                entity.addPart("image3", new FileBody(destination3));


                System.out.println("image" + destination + "image2" + destination2 + "image3" + destination3);

                entity.addPart("items", new StringBody(strings_items.toString()));
                entity.addPart("rate", new StringBody(strings_rate.toString()));
                entity.addPart("count", new StringBody(strings_count.toString()));
                entity.addPart("total", new StringBody(strings_total.toString()));
                entity.addPart("instruction", new StringBody(strings_comment.toString()));

                int discount = 0;
                if (edtDiscount.getText().toString().length() > 0) {
                    discount = Integer.parseInt(edtDiscount.getText().toString());
                }
                entity.addPart("discount", new StringBody("" + discount));
                entity.addPart("initialTotalSum", new StringBody("" + initialTotalSum));

                entity.addPart("overall_total", new StringBody("" + sum));
                entity.addPart("overall_count", new StringBody(txt_oveal_count.getText().toString()));
                if (edt_payment.getText().toString().equals("POD")) {
                    entity.addPart("payment_mode", new StringBody("POD"));
                    entity.addPart("given_amt", new StringBody("0.0"));
                    entity.addPart("balance_amt", new StringBody("balance0.0"));
                } else {
                    entity.addPart("payment_mode", new StringBody("paying_now"));
                    entity.addPart("given_amt", new StringBody(taken_amt));
                    entity.addPart("balance_amt", new StringBody(balance_amt));
                }

                entity.addPart("unique_code", new StringBody(getIntent().getStringExtra("unique_code")));
                entity.addPart("bill", new StringBody(getIntent().getStringExtra("unique_code")));

                entity.addPart("shop_id", new StringBody(getIntent().getStringExtra("shop_id")));
                entity.addPart("delivery_id", new StringBody(Configg.getDATA(activity, "did")));
                entity.addPart("completion", new StringBody("50"));
                entity.addPart("mobile_date_pickuped", new StringBody(currentDateTime));


                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Log.e(TAG, "Response from server: " + result);

            System.out.println("Response from server: " + result);

            progressBar.dismiss();
            // showing the server response in an alert dialog


            Configg.alert("Response From Server...", "Posted Successfully.", 2, activity);

//            edt_date.getText().clear();
//            edt_time.getText().clear();
//            edt_title.getText().clear();
//            edt_content.getText().clear();
//            imf_set_image.setImageDrawable(null);
//            edt_title.requestFocus();

        }

    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        if (img_str.equals("1")) {
            destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            System.out.println("destination1" + destination);
        }
        if (img_str.equals("2")) {
            destination2 = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            System.out.println("destination2" + destination2);
        }
        if (img_str.equals("3")) {
            destination3 = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            System.out.println("destination3" + destination3);
        }

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        imf_set_image.setImageBitmap(thumbnail);

//        Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imf_set_image.setImageBitmap(Bitmap.createScaledBitmap(thumbnail, 600, 600, false));

        if (img_str.equals("1")) {
            img_1.setImageBitmap(thumbnail);

        } else if (img_str.equals("2")) {
            img_2.setImageBitmap(thumbnail);

        } else if (img_str.equals("3")) {
            img_3.setImageBitmap(thumbnail);

        }
//        progressBar = new ProgressDialog(getActivity());
//        new UploadFileToServer().execute();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

//                destination = new File(Environment.getExternalStorageDirectory(),
//                        System.currentTimeMillis() + ".jpg");

                if (img_str.equals("1")) {
                    destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    System.out.println("destination1" + destination);
                }
                if (img_str.equals("2")) {
                    destination2 = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    System.out.println("destination2" + destination2);
                }
                if (img_str.equals("3")) {
                    destination3 = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    System.out.println("destination3" + destination3);
                }


                System.out.println("destination_gallery" + destination);

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (img_str.equals("1")) {
            img_1.setImageBitmap(bm);

        } else if (img_str.equals("2")) {
            img_2.setImageBitmap(bm);

        } else if (img_str.equals("3")) {
            img_3.setImageBitmap(bm);

        }

//        img_1.setImageBitmap(bm);
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

    private void volley_get_price() {

        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Configg.MAIN_URL + Configg.GET_PRICE_ALL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    if (!(hashMapArrayList.size() == 0)) {
                        hashMapArrayList.clear();
                    }
                    itemCountlist.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    HashMap<String, String> stringHashMap = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        stringHashMap = new HashMap<String, String>();
                        stringHashMap.put("pid", jsonArray.getJSONObject(i).getString("pid"));
                        stringHashMap.put("product_type", jsonArray.getJSONObject(i).getString("product_type"));
                        stringHashMap.put("product_name", jsonArray.getJSONObject(i).getString("product_name"));
                        stringHashMap.put("product_price", jsonArray.getJSONObject(i).getString("product_price"));
                        stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                        stringHashMap.put("pcs", jsonArray.getJSONObject(i).getString("pcs"));
                        itemCountlist.put(jsonArray.getJSONObject(i).getString("product_name"), jsonArray.getJSONObject(i).getString("pcs"));
                        hashMapArrayList.add(stringHashMap);
                    }
                    stringHashMap = new HashMap<String, String>();
                    stringHashMap.put("pid", "1");
                    stringHashMap.put("product_type", "other");
                    stringHashMap.put("product_name", "Other");
                    stringHashMap.put("product_price", "");
                    stringHashMap.put("mobile_date", "");

                    hashMapArrayList.add(stringHashMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    @Override
    public void processFinish(String output) {
        try {
            if (!(hashMapArrayList.size() == 0)) {
                hashMapArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(output);
//            Configg.alert("Successfully Booked.", "TAG NO." + jsonObject.getJSONArray("patient_list").
//                    getJSONObject(0).getString("tagid"), 2, activity);

            Configg.alert("Successfully Booked.", "Bill.NO." + getIntent().getStringExtra("unique_code") + ", Qty." + strings_count.toString(), 2, activity);
//            HashMap<String, String> stringHashMap;
//            for (int i = 0; i < jsonArray.length(); i++) {
//                stringHashMap = new HashMap<String, String>();
//                stringHashMap.put("pid", jsonArray.getJSONObject(i).getString("pid"));
//                stringHashMap.put("product_type", jsonArray.getJSONObject(i).getString("product_type"));
//                stringHashMap.put("product_name", jsonArray.getJSONObject(i).getString("product_name"));
//                stringHashMap.put("product_price", jsonArray.getJSONObject(i).getString("product_price"));
//                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
//
//                hashMapArrayList.add(stringHashMap);
//            }
//            adapterPricelist = new AdapterPricelist(activity, hashMapArrayList, "");
//            list_item.setAdapter(adapterPricelist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
