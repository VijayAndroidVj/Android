package cinderellaadmin.vajaralabs.com.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class PriceList extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private EditText edt_type;
    private EditText edt_product, edt_price, edt_pcs;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Price-List");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));
        edt_type = (EditText) findViewById(R.id.edt_type);
        edt_product = (EditText) findViewById(R.id.edt_product_name);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_pcs = (EditText) findViewById(R.id.edt_pcs);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        final View lladdmanually = findViewById(R.id.lladdmanually);
        lladdmanually.setVisibility(View.GONE);
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lladdmanually.setVisibility(View.GONE);

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("file/*");
                Intent intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, 1234);

            }
        });

        findViewById(R.id.btn_add_manually).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lladdmanually.setVisibility(View.VISIBLE);
            }
        });
        edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PriceList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.product_list);
                Button btn_women = (Button) dialog.findViewById(R.id.btn_women);
                Button btn_men = (Button) dialog.findViewById(R.id.btn_men);
                Button btn_kids = (Button) dialog.findViewById(R.id.btn_kids);
                Button btn_home = (Button) dialog.findViewById(R.id.btn_home);

                btn_women.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Women");
                        dialog.dismiss();
                    }
                });
                btn_men.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Men");
                        dialog.dismiss();
                    }
                });
                btn_kids.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Kids");
                        dialog.dismiss();
                    }
                });

                btn_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edt_type.setText("Home");
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                if (!edt_type.getText().toString().equals("")) {
                    if (!edt_product.getText().toString().equals("")) {
                        if (!edt_price.getText().toString().equals("")) {
                            nameValuePairs.add(new BasicNameValuePair("product_type", edt_type.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("product_name", edt_product.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("product_price", edt_price.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
                            nameValuePairs.add(new BasicNameValuePair("product_pcs", edt_pcs.getText().toString()));


                            asyncPOST = new AsyncPOST(nameValuePairs, PriceList.this, Configg.MAIN_URL + Configg.PRICE_LIST, PriceList.this);
                            asyncPOST.execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter The Product Price.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter The Product Name.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Select The Product Type First.", Toast.LENGTH_SHORT).show();
                }

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
                Configg.alert("Response From Server.", jsonObject.getString("message"), 1, PriceList.this);

            } else if (jsonObject.getString("success").equals("3")) {
                Configg.alert("Response From Server.", jsonObject.getString("message"), 0, PriceList.this);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1234 && data != null) {
            Uri uri = data.getData();
            String pathFromURI = getRealPathFromURI(uri); // should the path be here in this string
            if (pathFromURI == null) {
                try {
                    pathFromURI = getRealPathFromURI(PriceList.this, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            destination = new File(pathFromURI);
            new UploadFileToServer().execute();
            System.out.print("Path  = " + pathFromURI);
        }
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;

        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(column_index);
                    cursor.close();
                    return data;
                }
                cursor.close();
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private ProgressDialog progressBar;
    private File destination;

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            progressBar = new ProgressDialog(PriceList.this);
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
            HttpPost httppost = new HttpPost(Configg.MAIN_URL + Configg.UPLOAD_PRICE_LIST);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("file", new FileBody(destination));

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

            System.out.println("Response from server: " + result);
            progressBar.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("message")) {
                        Configg.alert("Response From Server...", jsonObject.getString("message"), 2, PriceList.this);
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Configg.alert("Response From Server...", "failed", 2, PriceList.this);
            // showing the server response in an alert dialog


//            edt_date.getText().clear();
//            edt_time.getText().clear();
//            edt_title.getText().clear();
//            edt_content.getText().clear();
//            imf_set_image.setImageDrawable(null);
//            edt_title.requestFocus();

        }

    }
}
