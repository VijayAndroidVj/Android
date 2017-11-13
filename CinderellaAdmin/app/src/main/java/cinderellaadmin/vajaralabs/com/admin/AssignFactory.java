package cinderellaadmin.vajaralabs.com.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class AssignFactory extends AppCompatActivity implements Response {

    public static HashMap<String, String> shashMapArrayList;
    public static HashMap<String, String> saddresshashMapArrayList;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AssignFactory activity;
    private AdapterPickupedCustomer adapterPickupedCustomer;
    private ListView list_item;

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_factory);
        activity = this;
        list_item = (ListView) findViewById(R.id.list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Configg.getDATA(activity, "type").equals("delivery"))
            getSupportActionBar().setTitle("Delivery-To-Home");
        else
            getSupportActionBar().setTitle("Assign-To-Factory");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.getDATA(activity, "type").equals("shop")) {
            nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));
            nameValuePairs.add(new BasicNameValuePair("pcid", getIntent().getStringExtra("pcid")));
            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER, activity);
            asyncPOST.execute();
        } else if (Configg.getDATA(activity, "type").equals("factory")) {
            nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(activity, "fid")));
            nameValuePairs.add(new BasicNameValuePair("pcid", getIntent().getStringExtra("pcid")));


            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER_FACTORY, activity);
            asyncPOST.execute();
        } else if (Configg.getDATA(activity, "type").equals("delivery")) {
            nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
            nameValuePairs.add(new BasicNameValuePair("pcid", getIntent().getStringExtra("pcid")));
            nameValuePairs.add(new BasicNameValuePair("completion", getIntent().getStringExtra("95")));


            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER_DELIVERY, activity);
            asyncPOST.execute();
        }
//        else if(Configg.getDATA(activity,"type").equals("factory")){
//            nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(activity, "fid")));
//
//            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER_FACTORY, activity);
//            asyncPOST.execute();
//
//        }
//        else if(Configg.getDATA(activity,"type").equals("delivery")){
//            nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
//
//            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER_DELIVERY, activity);
//            asyncPOST.execute();
//
//        }
        Log.w("namevalue", nameValuePairs.toString());
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

        Log.e("output", output);
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }

        try {
            HashMap<String, String> stringHashMap;
            JSONArray jsonArray = new JSONArray(output);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("tag_no", jsonArray.getJSONObject(i).getString("tag_no"));
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("count", jsonArray.getJSONObject(i).getString("count"));

                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("shop_contact_name", jsonArray.getJSONObject(i).getString("shop_contact_name"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));
                stringHashMap.put("delivery_contact", jsonArray.getJSONObject(i).getString("delivery_contact"));


                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));

                stringHashMap.put("shop_contact", jsonArray.getJSONObject(i).getString("shop_contact"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                stringHashMap.put("overall_count", jsonArray.getJSONObject(i).getString("overall_count"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                stringHashMap.put("payment_mode", jsonArray.getJSONObject(i).getString("payment_mode"));
                stringHashMap.put("given_amt", jsonArray.getJSONObject(i).getString("given_amt"));
                stringHashMap.put("balance_amt", jsonArray.getJSONObject(i).getString("balance_amt"));
                stringHashMap.put("image_one", jsonArray.getJSONObject(i).getString("image_one"));
                stringHashMap.put("bill", jsonArray.getJSONObject(i).getString("bill"));
                stringHashMap.put("given_amt", jsonArray.getJSONObject(i).getString("given_amt"));

                String total = jsonArray.getJSONObject(i).getString("overall_total");
                String given = jsonArray.getJSONObject(i).getString("given_amt");
                try {
                    double ttl = Double.valueOf(total);
                    double gvn = Double.valueOf(given);
                    double blnce = ttl - gvn;
                    stringHashMap.put("balance_amt", "" + blnce);
                } catch (Exception e) {
                    e.printStackTrace();
                    stringHashMap.put("balance_amt", jsonArray.getJSONObject(i).getString("balance_amt"));
                }

                stringHashMap.put("delivery_id", jsonArray.getJSONObject(i).getString("delivery_id"));


                hashMapArrayList.add(stringHashMap);
            }
            adapterPickupedCustomer = new AdapterPickupedCustomer(activity, hashMapArrayList);
            list_item.setAdapter(adapterPickupedCustomer);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);

    private static Font subcatFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font scatFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    private static Font norFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL);

    public void createandDisplayPdf(String factoryName) {
        String shopcode = shashMapArrayList.get("shop_code_and_name");

        Document doc = new Document();
        String path = "";
        try {
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);

            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cinderella";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, shopcode + "_" + timeStamp + ".pdf");
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
            PdfWriter pdfWriter = null;
            try {
                pdfWriter = PdfWriter.getInstance(doc, fOut);
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

            Paragraph paragraph = new Paragraph("Branch to Factory DC - Cinderella Laundry & Dry Cleaners", catFont);
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

            paragraph = new Paragraph(saddresshashMapArrayList.get("address"), subcatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.LEFT);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph(factoryName, subcatFont);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.NO_BORDER);
            table2.addCell(pdfPCell21);

            paragraph = new Paragraph(saddresshashMapArrayList.get("unique_code"), catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            pdfPCell21 = new PdfPCell(paragraph);
            pdfPCell21.setBorder(Rectangle.RIGHT);
            table2.addCell(pdfPCell21);

            String subaddress = saddresshashMapArrayList.get("city");
            String locality = saddresshashMapArrayList.get("locality");
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

            date = new Date();
            timeStamp = new SimpleDateFormat("dd-MMM-yyyy h:mm a", Locale.getDefault()).format(date);

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
                        pcid = saddresshashMapArrayList.get("pcid");
                        overall_count = saddresshashMapArrayList.get("overall_count");
                        pickup_date = saddresshashMapArrayList.get("pickup_date");
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
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
            shareImage(new File(path));

            //PDF file is now ready to be sent to the bluetooth printer using PrintShare
           /* Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("com.dynamixsoftware.printershare");
            i.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
            startActivity(i);*/

        }

    }


    private static void createTable(Document doc)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setBackgroundColor(new BaseColor(255, 255, 45));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        String shopcode = shashMapArrayList.get("shop_code_and_name");
        String shopcontact = shashMapArrayList.get("shop_contact");
        String unique_code = shashMapArrayList.get("unique_code");
        String items = shashMapArrayList.get("overall_count");
        String overall_total = shashMapArrayList.get("overall_total");
        String payment_mode = shashMapArrayList.get("payment_mode");
        String given_amt = shashMapArrayList.get("given_amt");
        String balance_amt = shashMapArrayList.get("balance_amt");

        table.addCell("Shop");
        table.addCell(shopcode);
        table.addCell("Shop Contact");
        table.addCell(shopcontact);
        table.addCell("Request Id");
        table.addCell(unique_code);

        table.addCell("No.of Cloths");
        table.addCell(items);
        table.addCell("Total Amount");
        table.addCell(overall_total);
        table.addCell("Payment Mode");
        table.addCell(payment_mode);
        table.addCell("Payed Amt");
        table.addCell(given_amt);
        table.addCell("Balance Amt");
        table.addCell(balance_amt);

        try {
            doc.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
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
            sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"bhardwaj2930@gmail.com"});
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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && data != null && resultCode == Activity.RESULT_OK) {
            final String factName = data.getStringExtra("factory_name");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createandDisplayPdf(factName);
                }
            }, 2000);
        }
    }
}
