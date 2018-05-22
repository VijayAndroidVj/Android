package com.instag.vijay.fasttrending.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.instag.vijay.fasttrending.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by razin on 2/11/17.
 */

public class Utils {

    public static void deleteLog() {
        try {
            File logFile = new File(Environment.getExternalStorageDirectory() + "/reachLog.txt");
            if (logFile.exists()) {
                logFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendLog(Context context, String text) {
        File logFile = new File("Reach/");
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        logFile = new File(Environment.getExternalStorageDirectory() + "/reachLog.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            text = text + "\n";
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static NodeList getNodeList(Context context, String tagName) {
        NodeList nList = null;
        try {
            InputStream is = context.getAssets().open("Configuration");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            nList = doc.getElementsByTagName(tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nList;
    }

    public static String getValue(String tag, Element element) {
        String value = "";
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        if (nodeList != null) {
            Node node = nodeList.item(0);
            if (node != null) {
                value = node.getNodeValue();
            }
        }
        return value;
    }

//        Realm.init(this);
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//
//        RealmUserObj reg = realm.createObject(RealmUserObj.class,2);
//        reg.setUsername("ahmed");
//        reg.setPassword("123456");
//
//        realm.commitTransaction();
//        String res = "";
//
//        RealmResults<RealmUserObj> result = realm.where(RealmUserObj.class).findAll();
//        for (int i =0;i<result.size();i++){
//            res = res+","+result.get(i).getUsername();
//        }

    public static boolean isNetworkAvailable(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = null;
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                }
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static ColorStateList getColorStateList(String color) {
        int[][] states = new int[][]{new int[]{android.R.attr.state_activated}, new int[]{-android.R.attr.state_activated}};
        int[] colors = new int[]{Color.parseColor(color), Color.parseColor(color)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
//        ColorStateList colorStateList = new ColorStateList(
//                new int[][] { new int[] { R.dimen._10dp } },
//                new int[] {Color.parseColor("#e3bb87")});
        return colorStateList;
    }

    public static String getFormatedDateTime(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void buttonTouch(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        view.setBackgroundResource(R.drawable.bg_button_pressed);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        view.setBackgroundResource(R.drawable.bg_button);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public static void dashBoardCardTouch(final View view, final TextView textview, final ImageView imageView) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        textview.setTextColor(Color.parseColor("#D3D3D3"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageView.setImageTintList(textview.getTextColors());
                        }
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        textview.setTextColor(Color.GRAY);
                        ColorStateList csl = getColorStateList("#009AE0");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageView.setImageTintList(csl);
                        }
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public static void dashBoardMoreTouch(final View view, final TextView textview, final TextView textMore) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        textview.setTextColor(Color.parseColor("#D3D3D3"));
                        textMore.setTextColor(Color.parseColor("#D3D3D3"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textMore.setCompoundDrawableTintList(textview.getTextColors());
                        }
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        textview.setTextColor(Color.GRAY);
                        textMore.setTextColor(Color.parseColor("#009AE0"));
                        ColorStateList csl = getColorStateList("#009AE0");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textMore.setCompoundDrawableTintList(csl);
                        }
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public static void dashBoardCircleTouch(final View view, final View bg_view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bg_view.setBackgroundResource(R.drawable.bg_circle_dashboard_icon_pressed);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        bg_view.setBackgroundResource(R.drawable.bg_circle_dashboard_icon);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public static void dashBoardCurveTouch(final View view, final View bg_view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bg_view.setBackgroundResource(R.drawable.bg_curve_dashboard_icon_pressed);
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        bg_view.setBackgroundResource(R.drawable.bg_curve_dashboard_icon);
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;

    public static String getFileSize(long length) {

        if (length == 0) {
            throw new IllegalArgumentException("Expected a file");
        }

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

}
