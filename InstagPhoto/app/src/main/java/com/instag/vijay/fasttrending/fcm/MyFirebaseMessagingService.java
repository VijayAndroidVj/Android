package com.instag.vijay.fasttrending.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.Db.DataBaseHandler;
import com.instag.vijay.fasttrending.Keys;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.chat.TrovaChat;
import com.instag.vijay.fasttrending.model.ChatMessageModel;
import com.instag.vijay.fasttrending.model.UserModel;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.instag.vijay.fasttrending.MainActivity.mainActivity;
import static com.instag.vijay.fasttrending.chat.TrovaChat.YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE;
import static com.instag.vijay.fasttrending.chat.TrovaChat.jsonValuePut;

/**
 * Created by vijay on 16/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private int i = 0;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        } else {
            Map<String, String> map = remoteMessage.getData();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                if (entry.getKey().equalsIgnoreCase("nameValuePairs")) {
                    Message message = mHandler.obtainMessage(7, entry.getValue());
                    message.sendToTarget();
                }
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
        Log.d(TAG, "Message sent: " + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
        Log.e(TAG, "Error sending upstream message: " + e);
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        intent.putExtra("notification", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "123456";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.feelout_noti)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        PreferenceUtil preferenceUtil = new PreferenceUtil(this);
        int noti_count = preferenceUtil.getInt(Keys.NOTI_COUNT, 0);
        preferenceUtil.putInt(Keys.NOTI_COUNT, noti_count + 1);
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.updateNotiCount();
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            // This is where you do your work in the UI thread.
            // Your worker tells you in the message what to do.
            if (message.what == 7)
                try {
                    DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(MyFirebaseMessagingService.this);
                    JSONObject jsonObject = new JSONObject(message.obj.toString());
                    if (jsonObject.has("updateDelivered")) {
                        jsonObject = new JSONObject(jsonObject.getString("updateDelivered"));
                        jsonObject = new JSONObject(jsonObject.getString("nameValuePairs"));
                        Long messageID = jsonObject.getLong("messageId");
                        String senderId = jsonObject.getString("senderId");
                        dataBaseHandler.updateChatMessageStatus(messageID + "", 1);
                        if (TrovaChat.trovaChat != null && TrovaChat.chatSenderId.equalsIgnoreCase(senderId)) {
                            TrovaChat.trovaChat.updateMesageStatus(messageID, 1);
                        }
                        dataBaseHandler.updateChatLogSeenStatus(senderId, 1, true, messageID + "");
                        if (mainActivity != null) {
                            mainActivity.refreshChat();
                        }
                    } else if (jsonObject.has("updateSeenMessage")) {
                        jsonObject = new JSONObject(jsonObject.getString("updateSeenMessage"));
                        jsonObject = new JSONObject(jsonObject.getString("nameValuePairs"));
                        Long messageID = jsonObject.getLong("messageId");
                        String senderId = jsonObject.getString("senderId");
                        dataBaseHandler.updateChatMessageStatus(messageID + "", 2);
                        if (TrovaChat.trovaChat != null && TrovaChat.chatSenderId.equalsIgnoreCase(senderId)) {
                            TrovaChat.trovaChat.updateMesageStatus(messageID, 2);
                        }
                        dataBaseHandler.updateChatLogSeenStatus(senderId, 2, true, messageID + "");
                        if (mainActivity != null) {
                            mainActivity.refreshChat();
                        }
                    } else if (jsonObject.has("messageJson")) {
                        jsonObject = new JSONObject(jsonObject.getString("messageJson"));
                        long timemilliseconds = System.currentTimeMillis();
                        Calendar cal = Calendar.getInstance();
                        TimeZone tz = cal.getTimeZone();
                        String timezone = tz.getDisplayName();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String currDate = df.format(cal.getTime());
                        df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String time = df.format(cal.getTime());
                        String message1 = jsonObject.getString("message");
                        String senderId = jsonObject.getString("senderId");
                        ChatMessageModel chatMessageModel = new ChatMessageModel();
                        chatMessageModel.setMessageId(jsonObject.getLong("messageId"));
                        chatMessageModel.setUserId(senderId);
                        chatMessageModel.setMessage(message1);
                        chatMessageModel.setMimeType("text");
                        chatMessageModel.setMessageSentOrReceived(1);
                        chatMessageModel.setName(jsonObject.getString("senderName"));
                        chatMessageModel.setDate(currDate);
                        chatMessageModel.setTime(time);
                        chatMessageModel.setTimeZone(timezone);
                        chatMessageModel.setSeenstatus(0);
                        chatMessageModel.setId("0");
                        chatMessageModel.setFileUploading(true);
                        chatMessageModel.setTimemilliseconds(timemilliseconds);

                        //  dataBaseHandler.saveFilteredContacts(userModel);
                        dataBaseHandler.saveChatMessage(chatMessageModel);

                        String name = dataBaseHandler.getUserName(chatMessageModel.getUserId());
                        UserModel userModel = new UserModel();
                        userModel.setUserId(chatMessageModel.getUserId());
                        cal = Calendar.getInstance();
                        df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        currDate = df.format(cal.getTime());
                        df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        time = df.format(cal.getTime());
                        userModel.setDate(currDate);
                        userModel.setTime(time);
                        userModel.setMimeType("text");
                        userModel.setTo_token(dataBaseHandler.getUserToken(chatMessageModel.getUserId()));
                        userModel.setImage(dataBaseHandler.getUserImage(chatMessageModel.getUserId()));
                        userModel.setStatus(chatMessageModel.getMessageSentOrReceived());
                        userModel.setMessage(chatMessageModel.getMessage());
                        userModel.setUnseenMessageCount(dataBaseHandler.unseenCount(senderId));
                        userModel.setName(name);
                        String senderToken = jsonObject.has("senderToken") ? jsonObject.getString("senderToken") : "";
                        if (!TextUtils.isEmpty(senderToken)) {
                            dataBaseHandler.updateToken(senderId, senderToken);
                            dataBaseHandler.updateContactToken(senderId, senderToken);
                            userModel.setTo_token(senderToken);
                        }
                        if (TrovaChat.trovaChat != null) {
                            if (TrovaChat.chatSenderId.equalsIgnoreCase(senderId)) {
                                userModel.setUnseenMessageCount(0);
                                //handle trova event
                                sendUpdateMessage(MyFirebaseMessagingService.this, chatMessageModel.getMessageId(), dataBaseHandler.getUserToken(senderId), 2);
                                TrovaChat.chatList.add(chatMessageModel);
                                TrovaChat.trovaChat.TrovaEvents(TrovaChat.chatList);
                            } else {
                                sendUpdateMessage(MyFirebaseMessagingService.this, chatMessageModel.getMessageId(), dataBaseHandler.getUserToken(senderId), 1);
                                Notification.Builder mNotificationBuilder = new Notification.Builder(getApplicationContext());
                                Intent ChatActivity = new Intent(getApplicationContext(), TrovaChat.class);
                                try {
                                    ChatActivity.putExtra("otherUserName", chatMessageModel.getName());
                                    ChatActivity.putExtra("otherUserID", senderId);
                                    ChatActivity.putExtra("incoming", 1);
                                    ChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(), 0, ChatActivity,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

                                    Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);

                                    String title = chatMessageModel.getName();
                                    mNotificationBuilder.setContentTitle(title);
                                    mNotificationBuilder.setContentText(message1).setTicker(message1);
                                    mNotificationBuilder.setAutoCancel(true);
                                    mNotificationBuilder.setSound(notificationSoundURI);
                                    mNotificationBuilder.setContentIntent(resultIntent).setPriority(Notification.PRIORITY_MAX);
                                    String CHANNEL_ID = getPackageName() + "_Channel";// The id of the channel.

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        mNotificationBuilder.setChannelId(CHANNEL_ID);
                                    }
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);
                                        mNotificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);
                                    }
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        String channelName = getPackageName();
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel mChannel = new NotificationChannel(
                                                CHANNEL_ID, channelName, importance);
                                        notificationManager.createNotificationChannel(mChannel);
                                    }
                                    mNotificationBuilder.setStyle(new Notification.BigTextStyle()
                                            .bigText(message1));
                                    Notification nf = mNotificationBuilder.build();
                                    i = (i + 1);
                                    notificationManager.notify(i, nf);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sendUpdateMessage(MyFirebaseMessagingService.this, chatMessageModel.getMessageId(), dataBaseHandler.getUserToken(senderId), 1);
                            Notification.Builder mNotificationBuilder = new Notification.Builder(getApplicationContext());
                            Intent ChatActivity = new Intent(getApplicationContext(), TrovaChat.class);
                            try {
                                ChatActivity.putExtra("otherUserName", chatMessageModel.getName());
                                ChatActivity.putExtra("otherUserID", senderId);
                                ChatActivity.putExtra("incoming", 1);

                                ChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(), 0, ChatActivity,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);
                                String title = "";
                                if (!TextUtils.isEmpty(chatMessageModel.getName())) {
                                    title = chatMessageModel.getName();
                                }
                                mNotificationBuilder.setContentTitle(title);
                                mNotificationBuilder.setContentText(message1).setTicker(message1);
                                mNotificationBuilder.setAutoCancel(true);
                                mNotificationBuilder.setSound(notificationSoundURI);
                                mNotificationBuilder.setContentIntent(resultIntent).setPriority(Notification.PRIORITY_MAX);
                                String CHANNEL_ID = getPackageName() + "_Channel";// The id of the channel.

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    mNotificationBuilder.setChannelId(CHANNEL_ID);
                                }
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);
                                    mNotificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    mNotificationBuilder.setSmallIcon(R.drawable.feelout_noti);
                                }
                                NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    String channelName = getPackageName();
                                    int importance = NotificationManager.IMPORTANCE_HIGH;
                                    NotificationChannel mChannel = new NotificationChannel(
                                            CHANNEL_ID, channelName, importance);
                                    notificationManager.createNotificationChannel(mChannel);
                                }
                                mNotificationBuilder.setStyle(new Notification.BigTextStyle()
                                        .bigText(message1));
                                Notification nf = mNotificationBuilder.build();
                                i = (i + 1);
                                notificationManager.notify(i, nf);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        dataBaseHandler.saveContactLogs(userModel);

                        if (mainActivity != null) {
                            mainActivity.refreshChat();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    };


    public static void sendUpdateMessage(final Context activity, final long messageId, final String instanceIdToken, final int status) {

        if (CommonUtil.isNetworkAvailable(activity)) {
            //DashBoardActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClientFcm().create(ApiInterface.class);
            HashMap<String, Object> jsonParams = new HashMap<>();
            JSONObject dataValue = new JSONObject();
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            try {
                dataValue.put("messageId", messageId);
                dataValue.put("status", status);
                jsonValuePut(dataValue, "userId", preferenceUtil.getUserMailId());
                jsonValuePut(dataValue, "senderId", preferenceUtil.getUserMailId());
                JSONObject userObj = new JSONObject();
                if (status == 1)
                    jsonValuePut(userObj, "updateDelivered", dataValue);
                else {
                    jsonValuePut(userObj, "updateSeenMessage", dataValue);
                }
                jsonParams.put("data", userObj);
            } catch (Exception e) {
                e.printStackTrace();
            }


            jsonParams.put("to", instanceIdToken);
            Call<ResponseBody> call = apiService.sendFcm(jsonParams, "key=" + YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    final ResponseBody requestBody = response.body();
                    //DashBoardActivity.dismissProgress();
                    if (requestBody != null) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //  Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }

    }

}
