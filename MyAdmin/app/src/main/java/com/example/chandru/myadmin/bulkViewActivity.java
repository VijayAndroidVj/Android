package com.example.chandru.myadmin;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.myadmin.Base.BaseAcitivity;
import com.example.chandru.myadmin.ExcellReader.XlsSplitObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * Created by chandru on 10/8/2017.
 */



public class bulkViewActivity  extends BaseAcitivity {
    private Animation startAnimation;
    String strHyouji = "";
    String[][] arrays;
    Button button;
    boolean isClick;
    ArrayList<XlsSplitObject> list = new ArrayList<>();
    private TextView tvExcel;
    private EditText creates;
    private String societycodes,data;

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private TextView bulksingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        bulksingle = (TextView)findViewById(R.id.bulksingle);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        societycodes = pref.getString("societycode", null);


        bulksingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singel = new Intent(bulkViewActivity.this, ManagementLoginActivity.class);
                singel.putExtra("LoginTag", "1");
                startActivity(singel);
            }
        });

        startAnimation = AnimationUtils.loadAnimation(this, R.anim.blinking_animation);
        final Button btnExcel = (Button) findViewById(R.id.btnExcel);
        Button btnUpload = (Button) findViewById(R.id.btnUpload);
        tvExcel = (TextView) findViewById(R.id.tvExcel);
        creates =(EditText)findViewById(R.id.createBys) ;


        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 3434);


            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (arrays == null) {
                    strHyouji = "no such file";
                } else {
                    XlsSplitObject objectValue;
                    if (list != null) {
                        list.clear();
                    }
                    for (String[] array : arrays) {
                        for (String v : array) {
                            strHyouji = strHyouji + v + "~";
                        }
                        objectValue = new XlsSplitObject();
                        objectValue.setGetXslValue(strHyouji);
                        list.add(objectValue);
                        strHyouji = "";
                    }
                    if (list != null && list.size() > 0) {
                        if (list.get(0).getGetXslValue().contains("EMAIL_ID~FLAT_AREA~FLAT_NO~FLOOR_NO~MEMBER_CODE~MEMBER_NAME~MOBILE_NO~NO_OF_PARKING~SELF_OCCUPIED~STATUS~")) {
                            Toast.makeText(bulkViewActivity.this, "uploading..", Toast.LENGTH_LONG).show();
                            final JSONObject root = new JSONObject();
                            JSONArray arrs =new JSONArray();
                            for (int i=1 ;i<list.size();i++){

                                final JSONObject root1 = new JSONObject();
                               String [] val= list.get(i).getGetXslValue().split("~");
                                try {
                                    root1.put("EMAIL_ID", val[0]);
                                    root1.put("FLAT_AREA", val[1]);
                                    root1.put("FLAT_NO", val[2]);
                                    root1.put("FLOOR_NO", val[3]);
                                    root1.put("MEMBER_CODE", val[4]);
                                    root1.put("MEMBER_NAME", val[5]);
                                    root1.put("MOBILE_NO", val[6]);
                                    root1.put("NO_OF_PARKING", val[7]);
                                    root1.put("SELF_OCCUPIED", val[8]);
                                    root1.put("SOCIETY_CODE",societycodes );
                                    root1.put("STATUS", val[9]);
                                    root1.put("Userid", "");



                                    arrs.put(root1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            try {
                                root.put("createdby",creates.getText().toString());
                                root.put("dashboardlist",arrs);


                                sendDatatoServer(root);
                                System.out.println(root);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           /* LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView =  inflater.inflate(R.layout.excellpopup,null);
                            PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setOutsideTouchable(false);
                            popupWindow.setFocusable(true);
                            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(bulkViewActivity.this,R.color.transparent)));
                            popupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.CENTER, 0, 280);*/
                        } else {
                            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView =  inflater.inflate(R.layout.excellpopup,null);
                            PopupWindow  popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setOutsideTouchable(false);
                            popupWindow.setFocusable(true);
                            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(bulkViewActivity.this,R.color.transparent)));
                            popupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.CENTER, 0, 130);
                        }
                    }
                }

            }
        });

    }

    public String[][] read(String file) {
        File file1 = null;
        if (TextUtils.isEmpty(file)) {
            Toast.makeText(this, "Given File is empty", Toast.LENGTH_SHORT).show();
        } else {
            file1 = new File(file);
        }
        Workbook workbook = null;
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);
            workbook = Workbook.getWorkbook(file1, ws);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();
            String[][] result = new String[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                Cell[] row = sheet.getRow(i);
                result[i] = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    result[i][j] = row[j].getContents();
                }
            }
            return result;
        } catch (Exception e) {
            strHyouji = strHyouji + e.toString();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3434 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String valu = getPath(uri);
            Log.e("", valu);
            String settext = valu.substring(valu.lastIndexOf("/") + 1);
            tvExcel.setText(settext);
            arrays = read(valu);
            isClick = true;


        }
    }

    public String getPath(final Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;
                if (("primary".equalsIgnoreCase(type))) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    if (Environment.isExternalStorageRemovable()) {
                        storageDefinition = "EXTERNAL_STORAGE";

                    } else {
                        storageDefinition = "SECONDARY_STORAGE";
                    }
                    return System.getenv(storageDefinition) + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {// DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }

        return null;
    }

    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }






    private void sendDatatoServer(final JSONObject root) {
        final String json = String.valueOf(root);
        System.out.println("data---" + json);
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return getServerResponse(json);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    System.out.println("data---" + s);
                    //   JSONObject js=new JSONObject(s);
                    JSONObject jsono = new JSONObject(s);
                    String Razor_pay_key = jsono.getString("status");
                    String msg = jsono.getString("message");
                    // String societycode =jsono.getString("societycode");
                    // String societystatus =jsono.getString("societystatus");
                    System.out.println("Razor_pay_key---" + Razor_pay_key);
                    
                    if (Razor_pay_key.equals("true")) {
                        //getApplicationContext().getSharedPreferences("MyPref", 0).edit().clear().commit();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editors = pref.edit();
                        editors.putString("responce", jsono.toString());
                        editors.apply();


                        Intent Int_login = new Intent(bulkViewActivity.this, MainActivity.class);
                        startActivity(Int_login);
                    }else {
                        Toast.makeText(bulkViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }



   /* private String formatDataAsJSON() {
        final JSONObject root = new JSONObject();
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

            String user_id =  pref.getString("user_id", null);
            String client_id = pref.getString("client_id", null);

            root.put("user_id",user_id);
            root.put("client_id",client_id);
            root.put("message", "todays report");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            List<Contact> contacts =databaseHandler.getAllContacts();
            int i=1;
            JSONObject reporting_image = new JSONObject();
            for (Contact cn : contacts) {
                System.out.println("data---jdjdjd");
                String log =cn.getName();
                reporting_image.put("image"+i,cn.getName());
                i++;
            }




            root.put("reporting_image",reporting_image);
            root.put("date",currentDateTimeString);
            System.out.println("data---11" + root.toString());
            return root.toString();


        }catch (JSONException el){
            el.printStackTrace();
        }

        return null;
    }*/


    private String getServerResponse(String json) {

        HttpPost post = new HttpPost("http://52.66.129.48/TUS_SERVICE/Service1.svc/BulkEntry");

        try {
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            post.setHeader("Content-type","application/json");

            DefaultHttpClient client = new DefaultHttpClient();

            BasicResponseHandler handler = new BasicResponseHandler();

            String response = client.execute(post,handler);
            return response;

        }catch (UnsupportedEncodingException es){
            es.printStackTrace();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    };


}