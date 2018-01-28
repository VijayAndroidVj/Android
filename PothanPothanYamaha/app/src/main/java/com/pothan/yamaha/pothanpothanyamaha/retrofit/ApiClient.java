package com.pothan.yamaha.pothanpothanyamaha.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iyara_rajan on 06-07-2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;
    public static String serverAddress = "http://www.itmspl.com/pioneer/";


    private static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            }
    };

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);

        try {
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                client.hostnameVerifier(new RelaxedHostNameVerifier());
                client.sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        if (retrofit == null) {
        retrofit = new Retrofit.Builder().client(client.build())
                .baseUrl(serverAddress)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

//        }
        return retrofit;
    }

    private static class RelaxedHostNameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}
