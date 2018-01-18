package cinderellaadmin.vajaralabs.com.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cinderellaadmin.vajaralabs.com.admin.model.Cell;
import cinderellaadmin.vajaralabs.com.admin.model.ColumnHeader;
import cinderellaadmin.vajaralabs.com.admin.model.RowHeader;
import common.AsyncPOST;
import common.Configg;
import common.Response;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Report extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterReport adapterBooking;
    private ListView list_booking;
    private EditText edt_search;
    TextView txtTotalItem;
    private Report activity;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayList<String> arrayList2 = new ArrayList<String>();
    private TableViewAdapter adapter;
    private EditText edt_sort_by, edtStarDate, edtEndDate;
    private String percentage = "";
    private int mYear, mMonth, mDay;

    private List<RowHeader> mRowHeaderList = new ArrayList<>();
    private List<ColumnHeader> mColumnHeaderList = new ArrayList<>();
    private List<List<Cell>> mCellList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report");
        txtTotalItem = findViewById(R.id.txtTotalItem);
        edt_search = (EditText) findViewById(R.id.edt_search);
        edt_sort_by = (EditText) findViewById(R.id.edt_sort_by);
        edtStarDate = findViewById(R.id.edtStartDate);
        edtStarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Report.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1;
                                edtStarDate.setText(year + "-" + (monthOfYear < 10 ? ("0" + monthOfYear) : monthOfYear) + "-" + (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
        edtEndDate = findViewById(R.id.edtEndDate);
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Report.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1;
                                edtEndDate.setText(year + "-" + (monthOfYear < 10 ? ("0" + monthOfYear) : monthOfYear) + "-" + (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });

        findViewById(R.id.btnReportByDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Configg.isNetworkAvailable(Report.this)) {

                    if (edtStarDate.getText().toString().length() == 0) {
                        edtStarDate.setError("Set Start Date");
                        return;
                    }

                    if (edtEndDate.getText().toString().length() == 0) {
                        edtEndDate.setError("Set End Date");
                        return;
                    }

                    nameValuePairs.add(new BasicNameValuePair("start_date", edtStarDate.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("end_date", edtEndDate.getText().toString()));

                    asyncPOST = new AsyncPOST(nameValuePairs, Report.this, Configg.MAIN_URL + Configg.GET_REPORT, Report.this);
                    asyncPOST.execute();
                    System.out.println("bookingzone" + nameValuePairs.toString());
                } else {
                    Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, Report.this);
                }
            }
        });

        findViewById(R.id.btnReportdownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "myReport-" + System.currentTimeMillis() + ".xls";
                File directory = new File(sd.getAbsolutePath());
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                try {
                    //file path
                    File file = new File(directory, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //Excel sheet name. 0 represents first sheet
                    WritableSheet sheet = workbook.createSheet("ReportList", 0);
                    // column and row
                    sheet.addCell(new Label(0, 0, "Request No"));
                    sheet.addCell(new Label(1, 0, "City"));
                    sheet.addCell(new Label(2, 0, "Locality"));
                    sheet.addCell(new Label(3, 0, "Address"));
                    sheet.addCell(new Label(4, 0, "Request Date"));
                    sheet.addCell(new Label(5, 0, "Request Time"));
                    sheet.addCell(new Label(6, 0, "Customer Name"));
                    sheet.addCell(new Label(7, 0, "Customer Contact"));
                    sheet.addCell(new Label(8, 0, "Total Amount"));
                    double gvnrevenue = 0;
                    double totalActual = 0;
                    for (int j = 0; j < hashMapArrayList.size(); j++) {
                        HashMap<String, String> stringHashMap = hashMapArrayList.get(j);
                        String pickup_date = stringHashMap.get("pickup_date");
                        String unique_code = stringHashMap.get("unique_code");
                        String pickup_time = stringHashMap.get("pickup_time");
                        String city = stringHashMap.get("city");
                        String locality = stringHashMap.get("locality");
                        String address = stringHashMap.get("address");
                        String mobile = stringHashMap.get("pickup_date");
                        String overall_total = stringHashMap.get("overall_total");
                        String given_amt = stringHashMap.get("given_amt");
                        String customer_name = stringHashMap.get("customer_name");
                        if (!TextUtils.isEmpty(given_amt)) {
                            gvnrevenue = gvnrevenue + Double.valueOf(given_amt);
                        }
                        if (!TextUtils.isEmpty(overall_total)) {
                            totalActual = totalActual + Double.valueOf(overall_total);
                        }

                        sheet.addCell(new Label(0, j + 1, unique_code));
                        sheet.addCell(new Label(1, j + 1, city));
                        sheet.addCell(new Label(2, j + 1, locality));
                        sheet.addCell(new Label(3, j + 1, address));
                        sheet.addCell(new Label(4, j + 1, pickup_date));
                        sheet.addCell(new Label(5, j + 1, pickup_time));
                        sheet.addCell(new Label(6, j + 1, customer_name));
                        sheet.addCell(new Label(7, j + 1, mobile));
                        sheet.addCell(new Label(8, j + 1, overall_total));
                    }
                    DecimalFormat df2 = new DecimalFormat(".##");
                    sheet.addCell(new Label(1, hashMapArrayList.size() + 1, "Total Revenue Collected"));
                    sheet.addCell(new Label(2, hashMapArrayList.size() + 1, "" + df2.format(gvnrevenue)));

                    sheet.addCell(new Label(1, hashMapArrayList.size() + 3, "Total Revenue Pending"));
                    sheet.addCell(new Label(2, hashMapArrayList.size() + 3, "" + df2.format(totalActual - gvnrevenue)));


                    //closing cursor
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplication(),
                            "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(Report.this);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(directory.getAbsolutePath()));
                    PendingIntent pendingIntent = PendingIntent.getActivity(Report.this, 0, intent, 0);

                    mBuilder.setContentIntent(pendingIntent);

                    mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                    mBuilder.setContentTitle(getString(R.string.app_name));
                    mBuilder.setContentText("Report Download in " + directory.getAbsolutePath());

                    NotificationManager mNotificationManager =

                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(001, mBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        arrayList.add("Show All");
        arrayList.add("Total Bookings");
//        arrayList.add("Total Assigned-25%");
        arrayList.add("Total Pickup");
        arrayList.add("Total Jobs at factory");
        arrayList.add("Total completed jobs by factory");
        arrayList.add("Ready To Deliver");
        arrayList.add("Total delivered to customers");
        arrayList2.add("All");
        arrayList2.add("");
//        arrayList2.add("25");
        arrayList2.add("50");
        arrayList2.add("75");
        arrayList2.add("90");
        arrayList2.add("95");
        arrayList2.add("100");


        edt_sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sort_by);
                ListView list_sort = (ListView) dialog.findViewById(R.id.list_sort);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrayList);
                list_sort.setAdapter(adapter1);
                list_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edt_sort_by.setText(arrayList.get(position));
                        percentage = arrayList2.get(position);
                        String text = percentage;
//                        adapterBooking.filter2(text);
//                        getSupportActionBar().setTitle("Report" + ", " + arrayList.get(position) +
//                                " : " + adapterBooking.getCount());

                        if (position == 0) {
                            loadAllData();
                        } else {
                            mRowHeaderList = new ArrayList<>();
                            mColumnHeaderList = new ArrayList<>();
                            mCellList = new ArrayList<>();
                            ArrayList<HashMap<String, String>> hashMapArrayListclone = new ArrayList<HashMap<String, String>>();
                            for (int j = 0; j < hashMapArrayList.size(); j++) {
                                HashMap<String, String> stringHashMap = hashMapArrayList.get(j);
                                String completion = stringHashMap.get("completion");
                                if (text.isEmpty()) {
                                    if (completion.equals(""))
                                        hashMapArrayListclone.add(stringHashMap);
                                } else if (completion.toLowerCase().contains(text)) {
                                    hashMapArrayListclone.add(stringHashMap);
                                }

                            }
                            txtTotalItem.setText("Total : " + hashMapArrayListclone.size());
                            for (int k = 0; k < (hashMapArrayListclone.size() + 2); k++) {
                                mCellList.add(new ArrayList<Cell>());
                            }
                            adapterBooking = new AdapterReport(activity, hashMapArrayListclone);
                            list_booking.setAdapter(adapterBooking);
                            List<List<Cell>> cellList = getCellListForSorting(hashMapArrayListclone);
                            mColumnHeaderList = getColumnHeaderList();
                            for (int l = 0; l < cellList.size(); l++) {
                                mCellList.get(l).addAll(cellList.get(l));
                            }
                            mRowHeaderList = getRowHeaderList(hashMapArrayListclone);
                            adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
                        }
                        dialog.dismiss();

                    }
                });
                dialog.show();
//

            }
        });
        list_booking = (ListView) findViewById(R.id.list_booking);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());

                if (text.length() == 0) {
                    loadAllData();
                } else {
                    mRowHeaderList = new ArrayList<>();
                    mColumnHeaderList = new ArrayList<>();
                    mCellList = new ArrayList<>();
                    ArrayList<HashMap<String, String>> hashMapArrayListclone = new ArrayList<HashMap<String, String>>();
                    for (int j = 0; j < hashMapArrayList.size(); j++) {
                        HashMap<String, String> stringHashMap = hashMapArrayList.get(j);
                        String unique_code = stringHashMap.get("unique_code");
                        String city = stringHashMap.get("city");
                        String locality = stringHashMap.get("locality");
                        String address = stringHashMap.get("address");
                        String mobile = stringHashMap.get("mobile");
                        String customer_name = stringHashMap.get("customer_name");
                        if (unique_code.toLowerCase().contains(text) || city.toLowerCase().contains(text) || locality.toLowerCase().contains(text) || address.toLowerCase().contains(text) || mobile.toLowerCase().contains(text) || customer_name.toLowerCase().contains(text)) {
                            hashMapArrayListclone.add(stringHashMap);
                        }

                    }
                    txtTotalItem.setText("Total : " + hashMapArrayListclone.size());
                    for (int k = 0; k < (hashMapArrayListclone.size() + 2); k++) {
                        mCellList.add(new ArrayList<Cell>());
                    }
                    adapterBooking = new AdapterReport(activity, hashMapArrayListclone);
                    list_booking.setAdapter(adapterBooking);
                    List<List<Cell>> cellList = getCellListForSorting(hashMapArrayListclone);
                    mColumnHeaderList = getColumnHeaderList();
                    for (int l = 0; l < cellList.size(); l++) {
                        mCellList.get(l).addAll(cellList.get(l));
                    }
                    mRowHeaderList = getRowHeaderList(hashMapArrayListclone);
                    adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
                }
//                adapterBooking.filter(text);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (Configg.isNetworkAvailable(this)) {

            nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

            asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_REPORT, this);
            asyncPOST.execute();
            System.out.println("bookingzone" + nameValuePairs.toString());
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
        }

        TableView tableView = findViewById(R.id.content_container);

// Create our custom TableView Adapter
        adapter = new TableViewAdapter(this);
// Set this adapter to the our TableView
        tableView.setAdapter(adapter);

// Let's set datas of the TableView on the Adapter

    }

    @Override
    protected void onResume() {
        super.onResume();

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
        System.out.println("booking" + output);
        if (hashMapArrayList.size() > 0) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
//            getSupportActionBar().setTitle("Report" + ", Total : " + jsonArray.length());

            HashMap<String, String> stringHashMap;
            if (jsonArray.length() == 0) {
                Configg.alert("Theres No Any Recent Bookings.", "", 3, activity);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));
                stringHashMap.put("customer_id", jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));
                stringHashMap.put("locality_id", jsonArray.getJSONObject(i).getString("locality_id"));
                stringHashMap.put("delivery_contact", jsonArray.getJSONObject(i).getString("delivery_contact"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("pickup_time", jsonArray.getJSONObject(i).getString("pickup_time"));
                stringHashMap.put("pickup_date", jsonArray.getJSONObject(i).getString("pickup_date"));
                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));
                stringHashMap.put("factory_name", jsonArray.getJSONObject(i).getString("factory_name"));
                stringHashMap.put("factory_person", jsonArray.getJSONObject(i).getString("factory_person"));
                stringHashMap.put("factory_contact", jsonArray.getJSONObject(i).getString("factory_contact"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("given_amt", jsonArray.getJSONObject(i).getString("given_amt"));
                stringHashMap.put("customer_name", jsonArray.getJSONObject(i).getString("customer_name"));
//                if (jsonArray.getJSONObject(i).getString("cancel").equals("")) {

                hashMapArrayList.add(stringHashMap);
//                }


            }
            loadAllData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadAllData() {
        mRowHeaderList = new ArrayList<>();
        mColumnHeaderList = new ArrayList<>();
        mCellList = new ArrayList<>();

        for (int i = 0; i < (hashMapArrayList.size() + 2); i++) {
            mCellList.add(new ArrayList<Cell>());
        }
        adapterBooking = new AdapterReport(activity, hashMapArrayList);
        list_booking.setAdapter(adapterBooking);
        List<List<Cell>> cellList = getCellListForSorting(hashMapArrayList);
        mColumnHeaderList = getColumnHeaderList();
        for (int i = 0; i < cellList.size(); i++) {
            mCellList.get(i).addAll(cellList.get(i));
        }
        txtTotalItem.setText("Total : " + hashMapArrayList.size());
        mRowHeaderList = getRowHeaderList(hashMapArrayList);
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);

    }


    private List<List<Cell>> getCellListForSorting(ArrayList<HashMap<String, String>> hashMapArrayList) {
        List<List<Cell>> list = new ArrayList<>();
        double gvnrevenue = 0;
        double totalActual = 0;
        for (int i = 0; i < hashMapArrayList.size(); i++) {

            HashMap<String, String> stringHashMap = hashMapArrayList.get(i);
            String pickup_date = stringHashMap.get("pickup_date");
            String unique_code = stringHashMap.get("unique_code");
            String pickup_time = stringHashMap.get("pickup_time");
            String city = stringHashMap.get("city");
            String locality = stringHashMap.get("locality");
            String address = stringHashMap.get("address");
            String mobile = stringHashMap.get("pickup_date");
            String overall_total = stringHashMap.get("overall_total");
            String given_amt = stringHashMap.get("given_amt");
            String customer_name = stringHashMap.get("customer_name");
            if (!TextUtils.isEmpty(given_amt)) {
                gvnrevenue = gvnrevenue + Double.valueOf(given_amt);
            }
            if (!TextUtils.isEmpty(overall_total)) {
                totalActual = totalActual + Double.valueOf(overall_total);
            }

            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                Object strText = "cell " + j + " " + i;

                if (j == 0) {
                    strText = unique_code;
                } else if (j == 1) {
                    strText = city;
                } else if (j == 2) {
                    strText = locality;
                } else if (j == 3) {
                    strText = address;
                } else if (j == 4) {
                    strText = pickup_date;
                } else if (j == 5) {
                    strText = pickup_time;
                } else if (j == 6) {
                    strText = customer_name;
                } else if (j == 7) {
                    strText = mobile;
                } else if (j == 8) {
                    strText = overall_total;
                }

                String strID = j + "-" + i;

                Cell cell = new Cell(strID, strText);
                cellList.add(cell);
            }

            list.add(cellList);
        }
        DecimalFormat df2 = new DecimalFormat(".##");
        List<Cell> cellList = new ArrayList<>();
        String strID = 0 + "-" + hashMapArrayList.size();
        Cell cell = new Cell(strID, "Total Revenue Collected");
        cellList.add(cell);

        strID = 1 + "-" + hashMapArrayList.size();
        cell = new Cell(strID, "" + df2.format(gvnrevenue));
        cellList.add(cell);
        list.add(cellList);

        cellList = new ArrayList<>();
        strID = 0 + "-" + hashMapArrayList.size() + 1;
        cell = new Cell(strID, "Total Revenue Pending");
        cellList.add(cell);

        strID = 1 + "-" + hashMapArrayList.size() + 1;
        cell = new Cell(strID, "" + df2.format(totalActual - gvnrevenue));
        cellList.add(cell);
        list.add(cellList);

        return list;
    }

    private List<RowHeader> getRowHeaderList(ArrayList<HashMap<String, String>> hashMapArrayList) {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < (hashMapArrayList.size() + 2); i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "" + (i + 1));
            list.add(header);
        }

        return list;
    }

    private List<ColumnHeader> getColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String strTitle = "";
            switch (i) {
                case 0:
                    strTitle = "Request No";
                    break;
                case 1:
                    strTitle = "City";
                    break;
                case 2:
                    strTitle = "Locality";
                    break;
                case 3:
                    strTitle = "Address";
                    break;
                case 4:
                    strTitle = "Request Date";
                    break;
                case 5:
                    strTitle = "Request Time";
                    break;
                case 6:
                    strTitle = "Customer Name";
                    break;
                case 7:
                    strTitle = "Customer Contact";
                    break;
                case 8:
                    strTitle = "Total Amount";
                    break;

            }
            ColumnHeader header = new ColumnHeader(String.valueOf(i), strTitle);
            list.add(header);
        }

        return list;
    }
}
