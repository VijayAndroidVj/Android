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

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class FactoryAssignedData extends AppCompatActivity implements Response {

    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private FactoryAssignedData activity;
    private AdapterFactoryAssignedData adapterFactoryAssignedData;
    private ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_factory);
        activity = this;
        list_item = (ListView) findViewById(R.id.list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Assign-To-Factory");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.getDATA(activity, "type").equals("shop")) {
            nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));

            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_FACTORY_ASSIGNED, activity);
            asyncPOST.execute();
        }
//        else if(Configg.getDATA(activity,"type").equals("factory")){
//            nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(activity, "fid")));
//
//            asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.GET_PICKUP_CUSTOMER_FACTORY, activity);
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


                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("items", jsonArray.getJSONObject(i).getString("items"));
                stringHashMap.put("overall_total", jsonArray.getJSONObject(i).getString("overall_total"));
                hashMapArrayList.add(stringHashMap);
            }
            adapterFactoryAssignedData = new AdapterFactoryAssignedData(activity, hashMapArrayList);
            list_item.setAdapter(adapterFactoryAssignedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static HashMap<String, String> shashMapArrayList;

    public void createandDisplayPdf() {
        String shopcode = shashMapArrayList.get("shop_code_and_name");
        String shopcontact = shashMapArrayList.get("shop_contact");
        String unique_code = shashMapArrayList.get("unique_code");
        String items = shashMapArrayList.get("overall_count");
        String overall_total = shashMapArrayList.get("overall_total");
        String payment_mode = shashMapArrayList.get("payment_mode");
        String given_amt = shashMapArrayList.get("given_amt");
        String balance_amt = shashMapArrayList.get("balance_amt");

        Document doc = new Document();
        String path = "";
        try {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aster";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, shopcode + ".pdf");
            path = file.getAbsolutePath();
            FileOutputStream fOut = new FileOutputStream(file);

            try {
                PdfWriter.getInstance(doc, fOut);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            //open the document
            doc.open();

            doc.addTitle("Cinderella");

            Paragraph preface = new Paragraph();
            // We add one empty line
            addEmptyLine(preface, 1);

         /*   Paragraph p1 = new Paragraph("Shop                  : " + shopcode);
            Font paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Shop Contact                 : " + shopcontact);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Request Id                   : " + unique_code);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("No.of Cloths                     : " + items);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Total Amount                 : " + overall_total);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Payment Mode                     : " + payment_mode);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Payed Amt                     : " + given_amt);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            p1 = new Paragraph("Balance Amt                 : " + balance_amt);
            paraFont = new Font();
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            //add paragraph to document
            doc.add(p1);*/

            Anchor anchor = new Anchor();
            // Second parameter is the number of the chapter
            Chapter catPart = new Chapter(new Paragraph(anchor), 1);
            Paragraph subPara = new Paragraph();
            Section subCatPart = catPart.addSection(subPara);
            createTable(subCatPart);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
            shareImage(new File(path));
        }

    }


    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
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

        subCatPart.add(table);

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private void shareImage(File file) {
        Uri contentUri = Uri.fromFile(file);
//        Uri contentUri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", file);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setPackage("com.google.android.gm");
        sharingIntent.setType("text/plain");
//        sharingIntent.setType("application/image");
//        sharingIntent.setData(Uri.parse(Configg.EMAIL));
        if (Configg.getDATA(FactoryAssignedData.this, "type").equals("shop")) {
            sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"cinderellalaundryshop@gmail.com"});
        } else {
            sharingIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"tocinderellalaundryfactory@gmail.com"});
        }
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "compose mail");

        //has to be an ArrayList
//        ArrayList<Uri> uris = new ArrayList<Uri>();
//        //convert from paths to Android friendly Parcelable Uri's
//        Uri u = Uri.parse(dirPath);
//        uris.add(u);
//        sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, "Send mail..."));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && data != null && resultCode == Activity.RESULT_OK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //createandDisplayPdf();
                }
            }, 2000);
        }
    }
}
