package com.peeyemcar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.peeyem.app.R;

/**
 * Created by vijay on 7/1/18.
 */

public class WebViewActivity extends AppCompatActivity {

    private Activity activity;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        activity = this;
        progressBar = (ProgressBar) findViewById(R.id.pbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        String loadUrl = getIntent().getStringExtra("loadUrl");
        webView = (WebView) findViewById(R.id.webView);
        progressBar.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(loadUrl)) {
            progressBar.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    webView.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    super.onPageCommitVisible(view, url);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    // ignore ssl error
                    if (handler != null) {
                        handler.proceed();
                    } else {
                        super.onReceivedSslError(view, null, error);
                    }
                }


                @Override
                public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {

                    Log.d("onRenderProcessGone ", detail.toString());
                    return super.onRenderProcessGone(view, detail);
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    Log.d("onRenderProcessGone ", "aaaa");
                    super.onPageFinished(view, url);

                    webView.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('dropMenu')[0].style.display='none'; })()");
                    // hide element by id
                    webView.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('topMenu')[0].style.display='none';})()");

                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Log.i("onReceivedError", "" + error.toString());
                }
            });

            String ua = "Mozilla/5.0 (Linux; U;`Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";

            webView.loadUrl(loadUrl);

            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().getJavaScriptEnabled();
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setUserAgentString(ua);
            webView.getSettings().setAppCacheEnabled(true);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                } else {
                    webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.addJavascriptInterface(new WebAppInterface(this), "JSInterface");
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.setVerticalScrollBarEnabled(true);
            webView.setHorizontalScrollBarEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webView.getSettings().setAllowFileAccessFromFileURLs(true);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            }
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            webView.setLongClickable(false);
            webView.setWebChromeClient(new MyWebChromeClient(this));
        }

    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        private MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                Log.i("WEbview :", "onProgressChanged : " + newProgress + "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        // hide element by class name
                        webView.loadUrl("javascript:(function() { " +
                                "document.getElementsByClassName('dropMenu')[0].style.display='none'; })()");
                        // hide element by id
                        webView.loadUrl("javascript:(function() { " +
                                "document.getElementsByClassName('topMenu')[0].style.display='none';})()");


                    }
                }, 1000);
            }

        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            return true;
        }

        // Need to accept permissions to use the camera
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            Log.d("onPermissionRequest", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
            }
        }

    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }


    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
