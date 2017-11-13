package common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

import util.CallBack;

/**
 * Created by Karthik S on 9/3/2016.
 */
public class Configg {

    //notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public static final String MAIN_URL = "http://vajralabs.com/Cinderella/";
    public static final String CREATE_SHOP = "create_shop.php";
    public static final String SIGNUP_PATIENT_ROOT = "signup_pharmacy.php";

    public static final String SIGNUP_CONFIRMATION_ROOT = "signup_confirmation.php";
    public static final String DOCTOR_LIST = "doctor_list.php";
    public static final String GET_APPOINTMENT = "get_appointment.php";
    public static final String POST_STATUS = "post_status.php";


    public static final String USER_LOGIN = "user_login_pharmacy.php";
    public static final String USER_LOGIN_DOCTOR = "user_login_doctor.php";


    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String EMAIL = "kpfbanoreply@gmail.com";
    public static final String PASSWORD = "kpfba2016";
    public static final String PRICE_LIST = "price_list.php";
    public static final String GET_LOCALITY = "get_locality.php";
    public static final String GET_CITY_LIST = "get_city.php";
    public static final String GET_LOCALITY_LIST = "get_locality.php";
    public static final String GET_PRICE_LIST = "get_price_list.php";

    public static final String ADMIN_LOGIN = "admin_login.php";
    public static final String ADD_LOCALITY = "add_locality.php";

    public static final String GET_BOOKING = "get_booking_shop.php";

    public static final String GET_BOOKING_DELIVERY = "get_booking_delivery.php";
    public static final String POST_BOOKING_DELIVERY = "post_booking_delivery.php";


    public static final String GET_SHOP_LIST = "shop_list.php";
    public static final String GET_DELIVERY_PERSON = "get_delivery_person.php";
    public static final String GET_DELIVERY_PERSON_ADMIN = "get_delivery_person_admin.php";

    public static final String GET_FACTORY_LIST = "get_factory.php";

    public static final String GET_PRICE_ALL = "get_price_all.php";
    public static final String GET_PICKUP_CUSTOMER = "get_pickup_customer.php";
    public static final String ASSIGN_FACTORY = "assign_factory.php";
    public static final String GET_PICKUP_CUSTOMER_FACTORY = "get_pickup_customer_factory.php";
    public static final String FACTORY_JOB_DONE = "factory_job_done.php";
    public static final String CHANGE_BILL = "changebill.php";
    public static final String GET_FROM_FACTORY = "get_from_factory.php";
    public static final String POST_DELIVERY_ID = "post_delivery_id.php";
    public static final String GET_FROM_FACTORY_FACTORY = "get_from_factory_factory.php";
    public static final String GET_FROM_FACTORY_DELIVERY = "get_from_factory_delivery.php";
    public static final String GET_BOOKING_DELIVERY_SHOP = "get_booking_delivery_shop.php";
    public static final String GET_FACTORY_ASSIGNED = "get_factory_assigned.php";
    public static final String GET_PICKUP_CUSTOMER_DELIVERY = "get_pickup_customer_delivery.php";
    public static final String GET_BOOKING_FACTORY = "get_booking_factory.php";
    public static final String GET_BOOKING_ADMIN = "get_booking_admin.php";
    public static final String FINAL_JOB_DONE = "final_job_done.php";
    public static final String COMPLETED_LIST = "completed_list.php";
    public static final String COMPLETED_LIST_SHOP = "completed_list_shop.php";
    public static final String COMPLETED_LIST_FACTORY = "completed_list_factory.php";
    public static final String COMPLETED_LIST_ADMIN = "completed_list_all.php";
    public static final String GET_CUSTOMER_LIST = "customer_list.php";
    public static final String INSERT_LOCATION = "add_location.php";
    public static final String POST_PICKUP_DATA = "post_pickup_data.php";
    public static final String BILL = "post_bill.php";
    public static final String NOTIFICATION_POST = "post_notification.php";
    public static final String GET_BOOKING2 = "get_pickuped_booking.php";
    public static final String GET_BOOKING_DELIVERY2 = "get_pickuped_delivery.php";
    public static final String GET_BOOKING_ADMIN2 = "get_pickuped_admin.php";
    public static final String GET_LOCATION = "get_location.php";
    public static final String BOOKING_CANCEL = "booking_cancel.php";
    public static final String GET_REPORT = "get_report.php";
    public static final String GET_FEEDBACK = "get_feedback.php";
    public static final String GET_DELIVER_50_70 = "get_deliver_50_75.php";
    public static final String FIRBASE_NOTIFICATION = "firebase/index_2.php";
    public static final String SIGNUP_CUSTOMER = "signup_customer2.php";
    public static final String CUSTOMER_BOOKING = "customer_bookings2.php";
    public static String url_path = "http://kpfba.info/";
    public static String CREATE_FACTORY = "create_factory.php";
    public static String CREATE_DELIVERY = "delivery_person.php";
    public static String ADD_CITY = "add_city.php";
    public static String PICKUP_CUSTOMER = "get_tag.php";
    public static String GET_GRAPH = "get_graph.php";
    public static String FCM_TOKEN_UPDATE = "fcm_token_update.php";

    public static void storeDATA(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeDATA(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getDATA(Context context, String key) {
        SharedPreferences prfs = context.getSharedPreferences(key, 0);
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


    public static void alert(String title, String message, final int i, final Context context, final CallBack callBack) {
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
                            callBack.onCallBack();

                        }

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.setCancelable(false);
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
