package common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by hp on 7/9/2016.
 */

public class AsyncGET extends AsyncTask<String, Void, Void> {
    private String url;

    private Context context;

    private Response response;
    private ProgressDialog Dialog;


    public AsyncGET(Context context, Response response, String url) {
        this.url = url;
        this.context = context;
        this.response = response;
        Dialog = new ProgressDialog(context);


    }

    private final HttpClient Client = new DefaultHttpClient();
    private String Content;
    private String Error = null;


    protected void onPreExecute() {

        Dialog.setMessage("Loading..");
        Dialog.show();
        Dialog.setCancelable(false);
    }

    // Call after onPreExecute method
    protected Void doInBackground(String... urls) {
        try {

            // Call long running operations here (perform background computation)
            // NOTE: Don't call UI Element here.

            // Server url call by GET method
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            Content = Client.execute(httpget, responseHandler);

        } catch (ClientProtocolException e) {
            Error = e.getMessage();
            cancel(true);
        } catch (IOException e) {
            Error = e.getMessage();
            cancel(true);
        }

        return null;
    }

    protected void onPostExecute(Void unused) {

        Dialog.dismiss();
        response.processFinish(Content);


    }
}