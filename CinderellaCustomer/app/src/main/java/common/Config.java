package common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

/**
 * Created by Karthik S on 9/3/2016.
 */
public class Config {


    //notification
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public static final String MAIN_URL = "http://vajralabs.com/Cinderella/";
    public static final String SIGNUP_ROOT = "signup_POST.php";
    public static final String SIGNUP_CUSTOMER = "signup_customer.php";
    public static final String GET_CUSTOMER_PRICE = "get_customer_price.php";

    public static final String SIGNUP_CONFIRMATION_ROOT = "signup_confirmation.php";
    public static final String DOCTOR_LIST = "doctor_list.php";
    public static final String GET_APPOINTMENT = "get_appointment.php";
    public static final String POST_STATUS = "post_status.php";

    public static final String GET_OFFERS = "get_offer.php";


    public static final String LOGIN_CUSTOMER = "login_customer.php";
    public static final String USER_LOGIN_DOCTOR = "user_login_doctor.php";


    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String EMAIL = "kpfbanoreply@gmail.com";
    public static final String PASSWORD = "kpfba2016";
    public static final String PRICE_LIST = "get_price_list.php";
    public static final String GET_LOCALITY = "get_locality.php";
    public static final String CUSTOMER_BOOKING = "customer_bookings.php";
    public static final String GET_BOOKING = "get_booking.php";
    public static final String GET_STATUS = "get_status.php";
    public static final String GET_PROFILE = "get_profile.php";
    public static final String POST_EDIT_CUSTOMER_PROFILE = "edit_customer_profile.php";
    public static final String POST_FORGOT_PASSWORD = "post_forgot_password.php";
    public static final String BOOKING_CANCEL = "booking_cancel.php";
    public static final String CUSTOMER_FEED = "customer_feedback.php";
    public static final String POST_TOKEN = "customer_token_update.php";
    public static final String GET_PRODUCT = "get_product.php";
    public static String url_path = "http://kpfba.info/";

    public static void storeDATA(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("Store", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeDATA(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("Store", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getDATA(Context context, String key) {
        SharedPreferences prfs = context.getSharedPreferences("Store", 0);
        return prfs.getString(key, "");

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void alert(String title, String message, final int i, final Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        if (i == 1) {
                            ((Activity) context).finish();
                            ((Activity) context).overridePendingTransition(0, 0);
                            context.startActivity(((Activity) context).getIntent());
                            ((Activity) context).overridePendingTransition(0, 0);
                        }
                        if (i == 2) {
                            ((Activity) context).finish();

                        }

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.setCancelable(false);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
