package cinderellaadmin.vajaralabs.com.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class BookingZonePickuped extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterBooking adapterBooking;
    private ListView list_booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_zone);
        findViewById(R.id.flShareView).setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pickup-Zone");

        list_booking = (ListView) findViewById(R.id.list_booking);
        final EditText  edt_search = (EditText) findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                if (adapterBooking != null)
                    adapterBooking.filter(text);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        findViewById(R.id.ivShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doc = new Document();
                String path = "";
                try {
                    Date date = new Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);

                    path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cinderella";

                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();

                    File file = new File(dir, "cinderella_" + timeStamp + ".pdf");
                    if (file.exists()) {
                        file.delete();
                    }

                    Rectangle rect = new Rectangle(36, 108);
                    rect.enableBorderSide(1);
                    rect.setBorder(Rectangle.BOX); // left, right, top, bottom border
                    rect.setBorderWidth(1); // a width of 5 user units
                    rect.setBorderColor(BaseColor.LIGHT_GRAY); // a red border
                    rect.setUseVariableBorders(false); // the full width will be visible

                    path = file.getAbsolutePath();
                    FileOutputStream fOut = new FileOutputStream(file);
                    try {
                        PdfWriter.getInstance(doc, fOut);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }


                    //open the document
                    doc.open();
                    doc.add(rect);
                    doc.addTitle("Cinderella");
                    doc.addSubject("Using iText");
                    doc.addKeywords("Android, PDF, iText");
                    doc.addAuthor("Vijay");
                    doc.addCreator("Vijay");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < hashMapArrayList.size(); i++) {
                    HashMap<String, String> shashMapArrayList = hashMapArrayList.get(i);
                    createandDisplayPdf(shashMapArrayList);
                }
                try {
                    doc.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shareImage(new File(path));
            }
        });
    }

    private Document doc;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);

    private static Font subcatFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font scatFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    private static Font norFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL);

    public void createandDisplayPdf(HashMap<String, String> shashMapArrayList) {
        String factoryName = shashMapArrayList.get("factory_name");
        try {
            Paragraph paragraph = new Paragraph("", catFont);
            addEmptyLine(paragraph, 2);
            doc.add(paragraph);
            String title = "Branch to Factory DC - Cinderella Laundry & Dry Cleaners";

            if (shashMapArrayList.get("completion").equals("50")) {
                title = "Factory to Shop Pickuped BY " + shashMapArrayList.get("delivery_contact_name");
            } else if (shashMapArrayList.get("completion").equals("75")) {
                title = "Branch To Factory " + shashMapArrayList.get("factory_name");
            }

            paragraph = new Paragraph(title, catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            PdfPTable table = new PdfPTable(1);

            PdfPCell pdfPCell = new PdfPCell(paragraph);
            table.addCell(pdfPCell);

            PdfPTable table2 = new PdfPTable(3);

            paragraph = new Paragraph("From:", norFont);
            PdfPCell pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.LEFT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph("To:", norFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.NO_BORDER);
            table2.addCell(pdfPCell21);


            paragraph = new Paragraph("DC NO :", norFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.RIGHT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph(shashMapArrayList.get("address"), subcatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.LEFT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph(factoryName, subcatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.NO_BORDER);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph(shashMapArrayList.get("unique_code"), catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.RIGHT);
            table2.addCell(pdfPCell21);

            String subaddress = shashMapArrayList.get("city");
            String locality = shashMapArrayList.get("locality");
            boolean add = false;
            if (TextUtils.isEmpty(subaddress)) {
                subaddress = "" + (TextUtils.isEmpty(locality) ? "" : locality);
            } else {
                add = true;
            }


            paragraph = new Paragraph(subaddress, scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.LEFT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph("", scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.NO_BORDER);
            table2.addCell(pdfPCell21);

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("dd-MMM-yyyy h:mm a", Locale.getDefault()).format(date);

            paragraph = new Paragraph(timeStamp
                    , scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.RIGHT);
            table2.addCell(pdfPCell21);


            /////////Empty

            if (TextUtils.isEmpty(locality)) {
                subaddress = "";
            } else if (add) {
                subaddress = locality;
            }

            paragraph = new Paragraph(subaddress, scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.LEFT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph("", scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.NO_BORDER);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph("", scatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.RIGHT);
            table2.addCell(pdfPCell21);

            doc.add(table);
            doc.add(table2);

            PdfPTable table3 = new PdfPTable(4);
            Paragraph paragraph3 = new Paragraph("Order", catFont);
            PdfPCell pdfPCell3 = new PdfPCell(paragraph3);
            pdfPCell3.setPaddingBottom(5);
            table3.addCell(pdfPCell3);

            paragraph3 = new Paragraph("Items", catFont);
            pdfPCell3 = new PdfPCell(paragraph3);
            pdfPCell3.setPaddingBottom(5);
            table3.addCell(pdfPCell3);

            paragraph3 = new Paragraph("Booked", catFont);
            pdfPCell3 = new PdfPCell(paragraph3);
            pdfPCell3.setPaddingBottom(5);
            table3.addCell(pdfPCell3);

            paragraph3 = new Paragraph("Items", catFont);
            pdfPCell3 = new PdfPCell(paragraph3);
            pdfPCell3.setPaddingBottom(5);
            table3.addCell(pdfPCell3);


            try {
                String items = shashMapArrayList.get("items");
                String itemwobrace = items.substring(1, items.length() - 1);
                String[] itemarray = itemwobrace.split(",");
                for (int i = 0; i < itemarray.length; i++) {
                    String pcid = "";
                    String overall_count = "";
                    String pickup_date = "";
                    if (i == 0) {
                        pcid = shashMapArrayList.get("pcid");
                        overall_count = shashMapArrayList.get("overall_count");
                        pickup_date = shashMapArrayList.get("pickup_date");
                    }

                    paragraph3 = new Paragraph(pcid, scatFont);
                    pdfPCell3 = new PdfPCell(paragraph3);
                    pdfPCell3.setBorder(Rectangle.LEFT);
//                    if (i == (itemarray.length - 1)) {
//                        pdfPCell3.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
//                    }
                    table3.addCell(pdfPCell3);

                    paragraph3 = new Paragraph(overall_count, scatFont);
                    pdfPCell3 = new PdfPCell(paragraph3);
                    pdfPCell3.setBorder(Rectangle.NO_BORDER);
//                    if (i == (itemarray.length - 1)) {
//                        pdfPCell3.setBorder(Rectangle.BOTTOM);
//                    }
                    table3.addCell(pdfPCell3);


                    paragraph3 = new Paragraph(pickup_date, scatFont);
                    pdfPCell3 = new PdfPCell(paragraph3);
                    pdfPCell3.setBorder(Rectangle.NO_BORDER);
//                    if (i == (itemarray.length - 1)) {
//                        pdfPCell3.setBorder(Rectangle.BOTTOM);
//                    }
                    table3.addCell(pdfPCell3);

                    paragraph3 = new Paragraph(itemarray[i], scatFont);
                    pdfPCell3 = new PdfPCell(paragraph3);
                    pdfPCell3.setBorder(Rectangle.RIGHT);
//                    if (i == (itemarray.length - 1)) {
//                        pdfPCell3.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
//                    }
                    table3.addCell(pdfPCell3);
                }

                paragraph3 = new Paragraph("", scatFont);
                pdfPCell3 = new PdfPCell(paragraph3);
                pdfPCell3.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
                table3.addCell(pdfPCell3);

                paragraph3 = new Paragraph("", scatFont);
                pdfPCell3 = new PdfPCell(paragraph3);
                pdfPCell3.setBorder(Rectangle.BOTTOM);
                table3.addCell(pdfPCell3);


                paragraph3 = new Paragraph("", scatFont);
                pdfPCell3 = new PdfPCell(paragraph3);
                pdfPCell3.setBorder(Rectangle.BOTTOM);
                table3.addCell(pdfPCell3);

                paragraph3 = new Paragraph("", scatFont);
                pdfPCell3 = new PdfPCell(paragraph3);
                pdfPCell3.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
                table3.addCell(pdfPCell3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            doc.add(table3);
            //          createTable(doc);

//            doc.add(catPart);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } finally {


            //PDF file is now ready to be sent to the bluetooth printer using PrintShare
           /* Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("com.dynamixsoftware.printershare");
            i.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
            startActivity(i);*/

        }

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private void shareImage(File file) {
        try {
            Uri contentUri = Uri.fromFile(file);
//        Uri contentUri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", file);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setPackage("com.google.android.gm");
            sharingIntent.setType("text/plain");
//        sharingIntent.setType("application/image");
//        sharingIntent.setData(Uri.parse(Configg.EMAIL));
            if (Configg.getDATA(BookingZonePickuped.this, "type").equals("shop")) {
                sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"cinderellalaundryshop@gmail.com"});
            } else {
                sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"tocinderellalaundryfactory@gmail.com"});
            }
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Cinderella");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");

            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Send mail..."));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.isNetworkAvailable(this)) {
            if (Configg.getDATA(BookingZonePickuped.this, "type").equals("shop")) {
                nameValuePairs.add(new BasicNameValuePair("city_id", Configg.getDATA(this, "city_id")));
                nameValuePairs.add(new BasicNameValuePair("locality_id", Configg.getDATA(this, "locality_id")));
                nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(this, "sid")));

                nameValuePairs.add(new BasicNameValuePair("completion1", "50"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "75"));
                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING2, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            } else if (Configg.getDATA(BookingZonePickuped.this, "type").equals("delivery")) {
                nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(this, "did")));
                nameValuePairs.add(new BasicNameValuePair("completion1", "50"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "75"));

//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_DELIVER_50_70, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
//            else if (Configg.getDATA(BookingZonePickuped.this, "type").equals("factory")) {
//                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
////                nameValuePairs.add(new BasicNameValuePair("completion", "25"));
//
//                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_FACTORY, this);
//                asyncPOST.execute();
//                System.out.println("bookingzone" + nameValuePairs.toString());
//            }
            else if (Configg.getDATA(BookingZonePickuped.this, "type").equals("admin")) {
                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));
                nameValuePairs.add(new BasicNameValuePair("completion1", "50"));
                nameValuePairs.add(new BasicNameValuePair("completion2", "75"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_BOOKING_ADMIN2, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
        } else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
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
        System.out.println("booking" + output);
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            HashMap<String, String> stringHashMap;
            if (jsonArray.length() == 0) {
                Configg.alert("Theres No Any Recent Bookings.", "", 2, BookingZonePickuped.this);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("overall_count", jsonArray.getJSONObject(i).getString("overall_count"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));
                stringHashMap.put("customer_name", jsonArray.getJSONObject(i).getString("customer_name"));
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

                hashMapArrayList.add(stringHashMap);


            }
            adapterBooking = new AdapterBooking(BookingZonePickuped.this, hashMapArrayList);
            list_booking.setAdapter(adapterBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
