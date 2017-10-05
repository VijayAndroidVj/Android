package common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Karthik S on 9/6/2016.
 */
public class AsyncPOST extends AsyncTask<String, Void, Void> {
    private ProgressDialog progressDialog;
    private List<NameValuePair> nameValuePairs;
    private String responseServer;
    private Context context;
    private Response response;
    private String url;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    public AsyncPOST(List<NameValuePair> nameValuePairs, Context context, String url, Response response) {
        this.nameValuePairs = nameValuePairs;
        this.context = context;
        this.url = url;
        this.response = response;
    }


    @Override
    protected Void doInBackground(String... voids) {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(url);
//
//        try {
//
//
////            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
////
////            nameValuePairs.add(new BasicNameValuePair("city", city));
////            nameValuePairs.add(new BasicNameValuePair("day", day));
//
//
//            Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
//
//            // Use UrlEncodedFormEntity to send in proper format which we need
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            InputStream inputStream = response.getEntity().getContent();
//            InputStreamToStringExample str = new InputStreamToStringExample();
//            responseServer = str.getStringFromInputStream(inputStream);
//            Log.e("response", "response -----" + responseServer);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
         responseServer = jsonParser.makeHttpRequest(url,
                "POST", nameValuePairs);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();
        System.out.println("responseServer_as" + responseServer);
        if (responseServer != null) {

            response.processFinish(responseServer);
        }


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}

class InputStreamToStringExample {

    public static void main(String[] args) throws IOException {

        // intilize an InputStream
        InputStream is =
                new ByteArrayInputStream("file content..blah blah".getBytes());

        String result = getStringFromInputStream(is);

        System.out.println(result);
        System.out.println("Done");

    }

    // convert InputStream to String
    static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}

