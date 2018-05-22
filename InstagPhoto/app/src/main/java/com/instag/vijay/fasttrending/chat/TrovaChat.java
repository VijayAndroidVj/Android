package com.instag.vijay.fasttrending.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.Db.DataBaseHandler;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.Utils.CommonDateTimeZone;
import com.instag.vijay.fasttrending.Utils.Utils;
import com.instag.vijay.fasttrending.model.ChatMessageModel;
import com.instag.vijay.fasttrending.model.UserModel;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.instag.vijay.fasttrending.chat.CustomGrid.IMAGE_PREVIEW;
import static com.instag.vijay.fasttrending.fcm.MyFirebaseMessagingService.sendUpdateMessage;


public class TrovaChat extends AppCompatActivity implements View.OnClickListener {

    public Uri selectedImageUri;
    public Uri selectAudioUri;
    RecyclerView.LayoutManager mLayoutManager;
    public ChatMessageAdapter chatMessageAdapter;
    public static ArrayList<ChatMessageModel> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout chatSwipeRefreshLayout;
    private RecyclerView.SmoothScroller smoothScroller;
    private EditText messageEditText;
    private View edit;
    private Activity activity;
    private android.widget.PopupWindow showPopup;

    private TextView txtContactName, txtContactPhone, tv_actionbar_alphabet;
    private CircleImageView iv_contact_list_item_alphabet;
    public static TrovaChat trovaChat;
    public static String chatSenderId;
    public static String chatSenderName;
    private DataBaseHandler dataBaseHandler;
    private PreferenceUtil preferenceUtil;
    public static String YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE = "AIzaSyCDSZ3gOd1HRn9EPJ2128fR5MhKSdcLePY";

    private static String getDuration(Context context, Uri uri) {
        String duration;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        int hrs = (int) TimeUnit.MILLISECONDS.toHours(timeInMilliSec) % 24;
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(timeInMilliSec) % 60;
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(timeInMilliSec) % 60;
        if (hrs > 0)
            duration = String.format(Locale.getDefault(), "%02d:%02d:%02d", hrs, min, sec);
        else
            duration = String.format(Locale.getDefault(), "%02d:%02d", min, sec);

        return duration;
    }

    public static byte[] readFileUriContentToBytes(Context ctx, Uri uri) throws IOException {
        byte bytes[] = null;

        try {
            InputStream input = ctx.getContentResolver().openInputStream(uri);
            if (input != null)
                bytes = IOUtils.toByteArray(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
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
            Cursor cursor;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        String data = cursor.getString(column_index);
                        cursor.close();
                        return data;
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static void launchGalleryViewer(Activity context, File url) {
        Uri screenshotUri = Uri.fromFile(url);
        Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(screenshotUri.toString()));
        if (type == null) {
            type = getMimeType(context, url.getAbsolutePath());
        }
        sharingIntent.setDataAndType(screenshotUri, type == null ? "*/*" : type);
        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    boolean inComing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_view);
        activity = this;
        trovaChat = this;
        dataBaseHandler = DataBaseHandler.getInstance(activity);
        preferenceUtil = new PreferenceUtil(activity);
        if (getIntent().hasExtra("incoming")) {
            inComing = getIntent().getIntExtra("incoming", 0) == 1;
        }
//        registerReceiver(myBroadcast, new IntentFilter("MessageReceived"));
        messageEditText = findViewById(R.id.messageEditText);
        edit = findViewById(R.id.edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            View view = LayoutInflater.from(this).inflate(R.layout.actionbar_chat, null);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setCustomView(view);
            tv_actionbar_alphabet = view.findViewById(R.id.tv_actionbar_alphabet);
            iv_contact_list_item_alphabet = view.findViewById(R.id.iv_contact_list_item_alphabet);
            txtContactPhone = view.findViewById(R.id.tv_actionbar_caller_id);
            txtContactName = view.findViewById(R.id.tv_actionbar_caller_name);
            View atach = view.findViewById(R.id.iv_attach);
            atach.setVisibility(View.VISIBLE);
            atach.setOnClickListener(this);
        }

        findViewById(R.id.iv_chat_camera).setOnClickListener(this);
        findViewById(R.id.iv_chat_gallery).setOnClickListener(this);
        findViewById(R.id.iv_chat_file).setOnClickListener(this);
        findViewById(R.id.iv_chat_mic).setOnClickListener(this);
        findViewById(R.id.iv_chat_video).setOnClickListener(this);
        findViewById(R.id.sendMessageButton).setOnClickListener(this);
        chatSwipeRefreshLayout = findViewById(R.id.chatSwipeRefreshLayout);
        chatSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatSwipeRefreshLayout.setRefreshing(false);
                // Refresh items
            }
        });
        recyclerView = findViewById(R.id.msgListView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        processIntent(intent);
        chatMessageAdapter = new ChatMessageAdapter(this);
        recyclerView.setAdapter(chatMessageAdapter);

        smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        if (chatList != null && chatList.size() > 1) {
            smoothScroller.setTargetPosition(chatList.size() - 1);
            mLayoutManager.startSmoothScroll(smoothScroller);
        }
//        recyclerView.smoothScrollToPosition(chatList.size());

        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void sendMessageSeenStatus(Long messageId, String serverPath) {
        if (Utils.isNetworkAvailable(activity)) {
            JSONObject sendNotificationObject = new JSONObject();
            try {
                sendNotificationObject.put("event", "onMessageSeen");
                sendNotificationObject.put("messageId", messageId);
                sendNotificationObject.put("serverPath", serverPath);
                sendNotificationObject.put("userId", preferenceUtil.getUserContactNumber());

                sendUpdateMessage(activity, messageId, dataBaseHandler.getUserToken(chatSenderId), 2);
                dataBaseHandler.updateChatMessageStatus("" + messageId, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.w("Chat", "Check your internet");
        }
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            chatSenderName = intent.getStringExtra("otherUserName");
            chatSenderId = intent.getStringExtra("otherUserID");
            if (TextUtils.isEmpty(chatSenderName)) {
                txtContactName.setVisibility(View.GONE);
                txtContactPhone.setText(chatSenderId);
                tv_actionbar_alphabet.setText(chatSenderId.substring(0, 1).toUpperCase());
            } else {
                txtContactName.setText(chatSenderName);
                txtContactPhone.setText(chatSenderId);
                tv_actionbar_alphabet.setText(chatSenderName.substring(0, 1).toUpperCase());
            }
            String image = dataBaseHandler.getUserImage(chatSenderId);
            if (image != null && !image.isEmpty()) {
                iv_contact_list_item_alphabet.setVisibility(View.VISIBLE);
                tv_actionbar_alphabet.setVisibility(View.GONE);
                Glide.with(activity).load(image).transform(new LogListAdapter.CircleTransform(activity)).centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_contact_list_item_alphabet);
            } else {
                iv_contact_list_item_alphabet.setVisibility(View.GONE);
                tv_actionbar_alphabet.setVisibility(View.VISIBLE);
            }
            dataBaseHandler = DataBaseHandler.getInstance(this);
            if (chatSenderId != null) {
                chatList = dataBaseHandler.getChatMessages(chatSenderId);
                getToken();
            }
        }

    }

    private void getToken() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            //DashBoardActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            JSONObject dataValue = new JSONObject();
            try {
                dataValue.put("email", chatSenderId);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Call<ResponseBody> call = apiService.getToken(chatSenderId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    final ResponseBody requestBody = response.body();
                    //DashBoardActivity.dismissProgress();
                    if (requestBody != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(requestBody.string());
                            String token = jsonObject.getString("token");
                            dataBaseHandler.updateToken(chatSenderId, token);
                            dataBaseHandler.updateContactToken(chatSenderId, token);
                            String tokenGet = dataBaseHandler.getUserToken(chatSenderId);
                            Log.w("tokenGet", tokenGet);
                            for (int i = 0; i < chatList.size(); i++) {
                                if (chatList.get(i).getMessageSentOrReceived() == 1 && chatList.get(i).getSeenstatus() != 2) {
                                    sendMessageSeenStatus(chatList.get(i).getMessageId(), chatList.get(i).getMediaLink());
                                }
                            }
                            dataBaseHandler.updateChatLogSeenStatus(chatSenderId, 2, false, "");
                            dataBaseHandler.updateChatStatus(chatSenderId, 2, false);

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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_attach) {
            //showAttachmentPopup(v);
        } else if (i == R.id.iv_chat_camera) {
            launchCameraIntent();
        } else if (i == R.id.iv_chat_gallery) {
            launchGalleryIntent();
        } else if (i == R.id.iv_chat_file) {
            launchDocumentPicker();
        } else if (i == R.id.iv_chat_mic) {
            launchAudioIntent();
        } else if (i == R.id.iv_chat_video) {
            launchVideoIntent();
        } else if (i == R.id.sendMessageButton) {
            String message = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                if (Utils.isNetworkAvailable(activity)) {
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setMessage(message);
                    chatMessageModel.setMimeType("text");
                    sendMessage(chatMessageModel, message);
                    messageEditText.setText("");
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            try {
                if (chatMessageAdapter != null && chatMessageAdapter.commonMediaPlayer != null && chatMessageAdapter.commonMediaPlayer.isPlaying()) {
                    chatMessageAdapter.commonMediaPlayer.stop();
                }
                trovaChat = null;
                chatSenderId = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(ChatMessageModel chatMessageModel, String message) {
        long timemilliseconds = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        String timezone = tz.getDisplayName();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currDate = df.format(cal.getTime());
        df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String time = df.format(cal.getTime());

        chatMessageModel.setName(chatSenderName);
        chatMessageModel.setUserId(chatSenderId);
        chatMessageModel.setMessage(message);

        if (chatMessageModel.getMessageId() == 0) {
            chatMessageModel.setMessageId(System.currentTimeMillis());
            chatMessageModel.setDate(currDate);
            chatMessageModel.setTime(time);
            chatMessageModel.setTimeZone(timezone);
        }


        chatMessageModel.setName(chatSenderName);
        chatMessageModel.setSeenstatus(-1);
        chatMessageModel.setMimeType("text");
        chatMessageModel.setTimemilliseconds(timemilliseconds);
        chatMessageModel.setMessageSentOrReceived(0);
        chatList.add(chatMessageModel);
        chatMessageAdapter.notifyDataSetChanged();

        recyclerView.smoothScrollToPosition(chatList.size());

        dataBaseHandler.saveChatMessage(chatMessageModel);

        UserModel userModel = new UserModel();
        userModel.setUserId(chatSenderId);
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currDate1 = df1.format(cal.getTime());
        df1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String time1 = df1.format(cal.getTime());
        userModel.setDate(currDate1);
        userModel.setTime(time1);
        userModel.setStatus(0);
        userModel.setMessage(message);
        userModel.setMessageId("" + chatMessageModel.getMessageId());
        userModel.setMessageSeenStatus(-1);
        userModel.setMimeType("text");
        userModel.setTo_token(dataBaseHandler.getUserToken(chatSenderId));
        userModel.setImage(dataBaseHandler.getUserImage(chatMessageModel.getUserId()));
        userModel.setName(chatSenderName);
        if (TextUtils.isEmpty(userModel.getName())) {
            String userName = dataBaseHandler.getUserName(userModel.getUserId());
            userModel.setName(userName);
        }
        userModel.setAgentKey("");
        userModel.setDisplayName("");
        dataBaseHandler.saveContactLogs(userModel);
        try {
            if (chatMessageModel.getMimeType().equalsIgnoreCase("text")) {
                Calendar calendar = Calendar.getInstance();
                JSONObject jsonObject = new JSONObject();
                jsonValuePut(jsonObject, "mode", "text");
                jsonValuePut(jsonObject, "messageId", chatMessageModel.getMessageId());
                jsonValuePut(jsonObject, "message", message);
                jsonValuePut(jsonObject, "TimeZoneOffset", calendar.getTimeZone().getRawOffset());
                jsonValuePut(jsonObject, "userId", preferenceUtil.getUserMailId());
                jsonValuePut(jsonObject, "senderId", preferenceUtil.getUserMailId());
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                jsonValuePut(jsonObject, "senderToken", refreshedToken);
                jsonValuePut(jsonObject, "receiverId", chatSenderId);
                jsonValuePut(jsonObject, "senderName", preferenceUtil.getUserName());

                String data = jsonObject.toString();

                JSONObject userObj = new JSONObject();
                jsonValuePut(userObj, "messageJson", data);
                String tokenGet = dataBaseHandler.getUserToken(chatSenderId);
                Log.w("tokenGet", tokenGet);
//                sendPushToSingleInstance(activity, userObj, dataBaseHandler.getUserToken(chatSenderId));
                sendPushToSingleInstanceRetro(activity, chatMessageModel.getMessageId(), userObj, tokenGet, chatList.size() - 1);
//
//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.send(new RemoteMessage.Builder(dataBaseHandler.getUserToken(chatSenderId) + "@gcm.googleapis.com")
//                        .setMessageId(Long.toString(chatMessageModel.getMessageId()))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action", jsonObject.toString())
//                        .build());


//                if (Globalclass.trovaSDK_init != null)
//                    Globalclass.trovaSDK_init.trovaXmit_Chat(chatMessageModel.getMessage(), "", chatMessageModel.getUserId(), preferenceUtil.getUserName(), chatMessageModel.getMessage(), chatMessageModel.getMessageId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendPushToSingleInstanceRetro(final Context activity, final long messageId, final JSONObject dataValue /*your data from the activity*/, final String instanceIdToken, /*firebase instance token you will find in documentation that how to get this*/final int position) {

        if (CommonUtil.isNetworkAvailable(activity)) {
            //DashBoardActivity.showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClientFcm().create(ApiInterface.class);
            HashMap<String, Object> jsonParams = new HashMap<>();
            jsonParams.put("data", dataValue);
            jsonParams.put("to", instanceIdToken);
            Call<ResponseBody> call = apiService.sendFcm(jsonParams, "key=" + YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    final ResponseBody requestBody = response.body();
                    //DashBoardActivity.dismissProgress();
                    if (requestBody != null) {
                        try {
                            dataBaseHandler.updateChatMessageStatus("" + messageId, 0);
                            dataBaseHandler.updateChatLogSeenStatus(chatSenderId, 0, true, "" + messageId);
                            chatList.get(position).setSeenstatus(0);
                            chatMessageAdapter.notifyDataSetChanged();
                            MainActivity.mainActivity.refreshChat();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }

    }

    public static void sendPushToSingleInstance(final Context activity, final JSONObject dataValue /*your data from the activity*/, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {

        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, String> rawParameters = new Hashtable<String, String>();
                rawParameters.put("data", dataValue.toString());
                rawParameters.put("to", instanceIdToken);
                return new JSONObject(rawParameters).toString().getBytes();
            }

            ;

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=" + YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE);
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    public static void jsonValuePut(JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, String[] value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, ArrayList<String> value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, long value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, JSONObject value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, int value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, boolean value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Dialog dialog;

    private void showAttachmentPopup(View view) {
        if (dialog == null || !dialog.isShowing()) {
            String[] menuText = getResources().getStringArray(R.array.attachment1);
            int[] images = {R.drawable.ic_file, R.drawable.ic_camera,
                    R.drawable.ic_insert_photo, R.drawable.ic_mic,
                    R.drawable.ic_video_library};
            GridView menuGrid;
            //showPopup = PopupWindow.getPopup(activity);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.custom_grid_layout, null);
            menuGrid = popupView.findViewById(R.id.grid_layout);
            CustomGrid adapter = new CustomGrid(activity, menuText, images);
            menuGrid.setAdapter(adapter);
       /* showPopup.setContentView(popupView);
//        float widthPixels = activity.getResources().getDisplayMetrics().widthPixels;

        showPopup.setWidth(Toolbar.LayoutParams.WRAP_CONTENT);
        showPopup.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);
        showPopup.showAsDropDown(view);*/


            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            builder.setView(popupView);
            dialog = builder.create();
            dialog.show();
        }
    }

    public void launchDocumentPicker() {
        if (showPopup != null) {
            showPopup.dismiss();
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
//        startActivityForResult(intent, REQUEST_CODE);
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("*/*");
//        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Document"), CustomGrid.PICK_FILE_REQUEST);
    }

//    public void launchCameraIntent() {
//        if (showPopup != null) {
//            showPopup.dismiss();
//        }
//
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion <= Build.VERSION_CODES.KITKAT) {
//            String directory = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name) +
//                    File.separator + "Images" + File.separator;
//            if (new File(directory).exists() || new File(directory).mkdirs()) {
//                Log.i("Directory Created", directory);
//            }
//            String file = directory + "Image-" + System.currentTimeMillis() + ".jpg";
//            File newfile = new File(file);
//            try {
//                newfile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            selectedImageUri = Uri.fromFile(newfile);
//
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
//            startActivityForResult(cameraIntent, CustomGrid.CAMERA_REQUEST);
//        } else {
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            // Ensure that there's a camera activity to handle the intent
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                }
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    selectedImageUri = FileProvider.getUriForFile(this,
//                            "com.trova.android.reach.provider",
//                            photoFile);
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
//                    startActivityForResult(takePictureIntent, CustomGrid.CAMERA_REQUEST);
//                }
//            }
//        }
//
//    }

    public void launchCameraIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, CustomGrid.CAMERA_REQUEST);
    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void launchGalleryIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(i, "Select Image"), CustomGrid.PICK_IMAGE_REQUEST);
    }

    public void launchAudioIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }
        List<Intent> targetedAudioIntents = new ArrayList<>();

        Intent audioRecorder = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        Intent meidaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        targetedAudioIntents.add(audioRecorder);
        targetedAudioIntents.add(meidaIntent);
        Intent chooserIntent = Intent.createChooser(targetedAudioIntents.remove(0), "");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedAudioIntents.toArray(new Parcelable[targetedAudioIntents.size()]));
        activity.startActivityForResult(chooserIntent, CustomGrid.PICK_AUDIO_REQUEST);
    }

    public void launchVideoIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), CustomGrid.PICK_VIDEO_REQUEST);


       /* List<Intent> targetedVideoIntents = new ArrayList<Intent>();
        Intent videoRecorder = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        targetedVideoIntents.add(mediaIntent);
        targetedVideoIntents.add(videoRecorder);
        Intent videoIntent = Intent.createChooser(targetedVideoIntents.remove(0), "");
        videoIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedVideoIntents.toArray(new Parcelable[targetedVideoIntents.size()]));
        activity.startActivityForResult(videoIntent, PICK_VIDEO_REQUEST);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CustomGrid.PICK_FILE_REQUEST:
                    uri = data.getData();

                    if (Utils.isNetworkAvailable(activity)) {
                        try {
                            onDocumentPickerSuccess(activity, uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    break;
                case CustomGrid.CAMERA_REQUEST:
                    if (Utils.isNetworkAvailable(activity)) {
//                        selectedImageUri = getImageUri(activity, (Bitmap) data.getExtras().get("data"));
//                    thumbnail = (Bitmap) data.getExtras().get("data");
//                    compressedImage = compressBitmap(thumbnail, 100);
//                    selectedImageUri = Uri.fromFile(new File(saveImage(thumbnail)));
                        String filePath = "";
                        if (selectedImageUri != null)
                            try {
                                filePath = getRealPathFromURI(activity, selectedImageUri);
                                if (filePath == null) {
                                    filePath = getFilePathFromURI(activity, selectedImageUri);
                                }
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                        if (filePath == null) {
                            Snackbar snackbar = Snackbar.make(edit, "File path not found..", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            return;
                        }
                        Intent intent = new Intent(activity, ImagePreviewActivity.class);
                        intent.putExtra("filePath", filePath);
                        startActivityForResult(intent, IMAGE_PREVIEW);
                    } else {
                        Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    break;
                case CustomGrid.PICK_IMAGE_REQUEST:
                    if (Utils.isNetworkAvailable(activity)) {
                        selectedImageUri = data.getData();
                        String filePath = "";
                        if (selectedImageUri != null)
                            try {
                                filePath = getRealPathFromURI(activity, selectedImageUri);
                                if (filePath == null) {
                                    filePath = getFilePathFromURI(activity, selectedImageUri);
                                }
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                        if (filePath == null) {
                            Snackbar snackbar = Snackbar.make(edit, "File path not found..", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            return;
                        }
                        Intent intent = new Intent(activity, ImagePreviewActivity.class);
                        intent.putExtra("filePath", filePath);
                        startActivityForResult(intent, IMAGE_PREVIEW);
                    } else {
                        Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    break;
                case IMAGE_PREVIEW:
                    if (data.hasExtra("filePath")) {
                        String path = data.getStringExtra("filePath");
                        onSelectFromGalleryResult(path);
                    }
                    break;
                case CustomGrid.PICK_AUDIO_REQUEST:
                    if (Utils.isNetworkAvailable(activity)) {
                        selectAudioUri = data.getData();
                        try {
                            onAudioPickerSuccess(activity, selectAudioUri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    break;
                case CustomGrid.PICK_VIDEO_REQUEST:
                    if (Utils.isNetworkAvailable(activity)) {
                        uri = data.getData();
                        try {
                            onVideoCaptureSuccess(activity, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(edit, "Check your internet connection..", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    break;
            }
        }
    }


    public void onAudioPickerSuccess(Context context, Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);
        String mimeType = getMimeType(activity, sourceFilePath);

        try {
            byte data[];
            File thumbFile;
            String filePath;
            String dur = null;

            if (sourceFilePath == null) {
                return;
            }
            long totalSize = 0;
            final String fileExt = (sourceFilePath.substring(sourceFilePath.lastIndexOf("."))).substring(1);
            final CommonDateTimeZone commonDateTimeZone = new CommonDateTimeZone();

            data = readFileUriContentToBytes(context, uri);


            filePath = sourceFilePath;
            final File file = new File(filePath);
            totalSize = file.length();
            String message;
            if (mimeType != null && mimeType.contains("video/")) {
                message = "video";
                // thumbFile = saveBitmap(thumbnail, "jpg", String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_thumb_folder));
                // thumbFilePath = Uri.fromFile(thumbFile).toString();
                selectedImageUri = uri;
                dur = getDuration(context, uri);
            } else {
                // Audio / Documents
                if (mimeType != null && mimeType.contains("audio/")) {
                    message = "audio";
                    dur = getDuration(context, uri);
                } else {
                    mimeType = "doc";
                    message = "document";
                    try {
                        thumbFile = saveFile(data, fileExt, String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_document_folder));
                        sourceFilePath = filePath = Uri.fromFile(thumbFile).toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            if (totalSize > 0) {
                ChatMessageModel chatMessageModel = new ChatMessageModel();
                chatMessageModel.setMessage(message);
                chatMessageModel.setMimeType(mimeType);
                chatMessageModel.setFileExt(fileExt);
                chatMessageModel.setFileSize(totalSize);
                chatMessageModel.setFileName(file.getName());
                chatMessageModel.setFilePath(filePath);
                chatMessageModel.setSeenstatus(-1);
                chatMessageModel.setFileUploading(true);
                chatMessageModel.setFileUpload(false);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = df.format(cal.getTime());
                chatMessageModel.setTime(time);
                chatMessageModel.setTimemilliseconds(System.currentTimeMillis());
                chatMessageModel.setUserId(chatSenderId);
                chatMessageModel.setName(chatSenderName);
                chatMessageModel.setDurationTime(dur);

                uploadFilesAlert(chatMessageModel, "Audio", file.getName());
            } else {
                Toast.makeText(activity, "Upload file size is too low", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // UPDATED!
    public String getVidPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String data = cursor.getString(column_index);
            cursor.close();
            return data;
        } else
            return null;
    }

    public void onVideoCaptureSuccess(Context context, Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);
        if (sourceFilePath == null)
            sourceFilePath = getVidPath(uri);
        String mimeType = getMimeType(activity, sourceFilePath);
        try {
            byte data[];
            File thumbFile;
            String filePath;
            String dur = null;

            long totalSize = 0;
            final String fileExt = (sourceFilePath.substring(sourceFilePath.lastIndexOf("."))).substring(1);
            final CommonDateTimeZone commonDateTimeZone = new CommonDateTimeZone();

            data = readFileUriContentToBytes(context, uri);

            filePath = sourceFilePath;
            final File file = new File(filePath);
            totalSize = file.length();
            String message;
            if (mimeType != null && mimeType.contains("video/")) {
                message = "video";
                // thumbFile = saveBitmap(thumbnail, "jpg", String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_thumb_folder));
                // thumbFilePath = Uri.fromFile(thumbFile).toString();
                selectedImageUri = uri;
                dur = getDuration(context, uri);
            } else {
                // Audio / Documents
                if (mimeType != null && mimeType.contains("audio/")) {
                    message = "audio";
                    dur = getDuration(context, uri);
                } else {
                    mimeType = "doc";
                    message = "document";
                    try {
                        thumbFile = saveFile(data, fileExt, String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_document_folder));
                        filePath = Uri.fromFile(thumbFile).toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


            if (totalSize > 0) {
                ChatMessageModel chatMessageModel = new ChatMessageModel();
                chatMessageModel.setMessage(message);
                chatMessageModel.setMimeType(mimeType);
                chatMessageModel.setFileExt(fileExt);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = df.format(cal.getTime());
                chatMessageModel.setTime(time);
                chatMessageModel.setUserId(chatSenderId);
                chatMessageModel.setName(chatSenderName);
                chatMessageModel.setSeenstatus(-1);
                chatMessageModel.setFileUploading(true);
                chatMessageModel.setFileUpload(false);
                chatMessageModel.setFileSize(totalSize);
                chatMessageModel.setFileName(file.getName());
                chatMessageModel.setFilePath(filePath);
                chatMessageModel.setDurationTime(dur);
                chatMessageModel.setTimemilliseconds(System.currentTimeMillis());
                uploadFilesAlert(chatMessageModel, "Video", file.getName());
            } else {
                Toast.makeText(activity, "Upload file size is too low", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDocumentPickerSuccess(Context context, Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);

//        String mimeType = getMimeType(activity, sourceFilePath);
        String mimeType = "doc/";
//        if ((mimeType.contains("video/")) || (mimeType.contains("audio/")) || (mimeType.contains("image/"))) {
//            Toast.makeText(context, "Please choose a different file.", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (sourceFilePath == null) {
            return;
        }
        File file = new File(sourceFilePath);
        if (file.exists() && file.length() > 0) {
            try {
                byte data[];
                File thumbFile;
                String dur = null;

                long totalSize = 0;
                final String fileExt = (sourceFilePath.substring(sourceFilePath.lastIndexOf("."))).substring(1);

                data = readFileUriContentToBytes(context, uri);
                String filenameOnly = new File(sourceFilePath).getName();
                filenameOnly = (filenameOnly.split("\\."))[0] + String.valueOf(System.currentTimeMillis());
                String message;
                if (mimeType.contains("video/")) {
                    message = "video";
                    // thumbFile = saveBitmap(thumbnail, "jpg", String.valueOf(commonDateTimeZone.messageId), context.getString(R.string.trova_base_thumb_folder));
                    // thumbFilePath = Uri.fromFile(thumbFile).toString();
                    selectedImageUri = uri;
                    dur = getDuration(context, uri);
                } else {
                    // Audio / Documents
                    if (mimeType.contains("audio/")) {
                        message = "audio";
                        dur = getDuration(context, uri);
                    } else {
                        mimeType = "doc/";
                        message = "document";
                        try {

                            thumbFile = saveFile(data, fileExt, filenameOnly, context.getString(R.string.trova_base_document_folder));
//                        filePath = Uri.fromFile(thumbFile).toString();
                            sourceFilePath = thumbFile.getPath();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                totalSize = new File(sourceFilePath).length();
                ChatMessageModel chatMessageModel = new ChatMessageModel();
                chatMessageModel.setMessage(message);
                chatMessageModel.setMimeType(mimeType);
                chatMessageModel.setFileExt(fileExt);
                chatMessageModel.setFileSize(totalSize);
                chatMessageModel.setSeenstatus(-1);
                chatMessageModel.setFileUploading(true);
                chatMessageModel.setFileUpload(false);
                chatMessageModel.setFileName(filenameOnly);
                chatMessageModel.setFilePath(sourceFilePath);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = df.format(cal.getTime());
                chatMessageModel.setTime(time);
                chatMessageModel.setUserId(chatSenderId);
                chatMessageModel.setName(chatSenderName);
                chatMessageModel.setDurationTime(dur);
                chatMessageModel.setTimemilliseconds(System.currentTimeMillis());
                uploadFilesAlert(chatMessageModel, "Document", file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "Upload file size is too low", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadFilesAlert(final ChatMessageModel chatMessageModel, String title, String fileName) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

            // Setting Dialog Title
            alertDialog.setTitle("Send " + title);

            // Setting Dialog Message
            alertDialog.setMessage(fileName);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Calendar calender = Calendar.getInstance();

                    dataBaseHandler.saveChatMessage(chatMessageModel);
                    chatList.add(chatMessageModel);
                    chatMessageAdapter.notifyDataSetChanged();
                    if (chatList.size() > 1)
                        recyclerView.smoothScrollToPosition(chatList.size());

//                    if (Globalclass.trovaSDK_init != null)
//                        Globalclass.trovaSDK_init.trovaAmazonS3_Upload(chatMessageModel.getFilePath(), calender.get(Calendar.YEAR) + "/" + (calender.get(Calendar.MONTH) + 1) + "/" + calender.get(Calendar.DATE) + "/" + calender.get(Calendar.HOUR) + "/" + calender.get(Calendar.MINUTE), chatMessageModel.getMimeType());

                    UserModel userModel = new UserModel();
                    userModel.setUserId(chatSenderId);
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String currDate1 = df1.format(calender.getTime());
                    df1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String time1 = df1.format(calender.getTime());
                    userModel.setDate(currDate1);
                    userModel.setTime(time1);
                    userModel.setStatus(0);
                    userModel.setMessageId("" + chatMessageModel.getMessageId());
                    userModel.setMessage(chatMessageModel.getMimeType());
                    userModel.setMessageSeenStatus(-1);
                    userModel.setFileName(chatMessageModel.getFileName());
                    userModel.setName(chatSenderName);
                    userModel.setTo_token(dataBaseHandler.getUserToken(chatMessageModel.getUserId()));
                    userModel.setImage(dataBaseHandler.getUserImage(chatMessageModel.getUserId()));
                    userModel.setMimeType(chatMessageModel.getMimeType());
                    if (TextUtils.isEmpty(userModel.getName())) {
                        String userName = dataBaseHandler.getUserName(userModel.getUserId());
                        userModel.setName(userName);
                    }
                    userModel.setAgentKey("");
                    userModel.setDisplayName("");
                    dataBaseHandler.saveContactLogs(userModel);
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getMimeType(Activity activity, String url) {
        try {
            String extension = url.substring(url.lastIndexOf("."));
            String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
            if (mimeType == null) {
                Uri uri = Uri.fromFile(new File(url));
                if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    ContentResolver cr = activity.getContentResolver();
                    mimeType = cr.getType(uri);
                } else {
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                            .toString());
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            fileExtension.toLowerCase());
                }
                return mimeType;
            }
            return mimeType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(String filePath) {
        try {
            long totalSize = 0;
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setFilePath(filePath);
      /*  Intent intent = new Intent(context.getApplicationContext(), ViewImage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/

            final String fileExt = (filePath.substring(filePath.lastIndexOf("."))).substring(1);
//            byte[] data = readFileUriContentToBytes(context, selectedImageUri);

            final File file = new File(filePath);
            totalSize = file.length();
            if (totalSize > 0) {

                long timeMilliSeconds = System.currentTimeMillis();
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();
                String timezone = tz.getDisplayName();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currDate = df.format(cal.getTime());
                df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = df.format(cal.getTime());
                chatMessageModel.setMessage("image");
                chatMessageModel.setMimeType("image/");
                chatMessageModel.setFileExt(fileExt);
                chatMessageModel.setFileSize(totalSize);
                chatMessageModel.setFileName(file.getName());
                chatMessageModel.setFilePath(filePath);
                chatMessageModel.setDurationTime("0");
//                chatMessageModel.setMediaLink(mediaLink);
                chatMessageModel.setSeenstatus(-1);
                chatMessageModel.setFileUploading(true);
                chatMessageModel.setFileUpload(false);
                chatMessageModel.setUserId(chatSenderId);
                chatMessageModel.setName(chatSenderName);

                chatMessageModel.setDate(currDate);
                chatMessageModel.setTime(time);
                chatMessageModel.setTimeZone(timezone);
                chatMessageModel.setMessageId(System.currentTimeMillis());
                chatMessageModel.setId("0");
                chatMessageModel.setTimemilliseconds(timeMilliSeconds);
                chatMessageModel.setMessageSentOrReceived(0);

                Calendar calender = Calendar.getInstance();

                dataBaseHandler.saveChatMessage(chatMessageModel);
                chatList.add(chatMessageModel);
                chatMessageAdapter.notifyDataSetChanged();
                if (chatList.size() > 1)
                    recyclerView.smoothScrollToPosition(chatList.size());

                UserModel userModel = new UserModel();
                userModel.setUserId(chatSenderId);
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currDate1 = df1.format(calender.getTime());
                df1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time1 = df1.format(calender.getTime());
                userModel.setDate(currDate1);
                userModel.setTime(time1);
                userModel.setStatus(0);
                userModel.setMessageId("" + chatMessageModel.getMessageId());
                userModel.setMessage(chatMessageModel.getMimeType());
                userModel.setMessageSeenStatus(-1);
                userModel.setMessage("image");
                userModel.setMimeType("image");
                userModel.setTo_token(dataBaseHandler.getUserToken(chatMessageModel.getUserId()));
                userModel.setFileName(chatMessageModel.getFileName());
                userModel.setImage(dataBaseHandler.getUserImage(chatMessageModel.getUserId()));
                userModel.setName(chatSenderName);
                if (TextUtils.isEmpty(userModel.getName())) {
                    String userName = dataBaseHandler.getUserName(userModel.getUserId());
                    userModel.setName(userName);
                }
                userModel.setAgentKey("");
                userModel.setDisplayName("");
                dataBaseHandler.saveContactLogs(userModel);

                //uploadFilesAlert(chatMessageModel, "Image", file.getName());

            } else {
                Toast.makeText(activity, "Upload file size is too low", Toast.LENGTH_LONG).show();
            }
//            Upload upload = new Upload();
//            upload.setAmezonFileUploadListener(amezonFileListener);
//            upload.setFileToUpload(activity,chatMessageModel.getFilePath(),"Trova2.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public File saveFile(byte[] data, String fileExt, String messageId, String folderType) throws IOException {
        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + getString(R.string.trova_base_folder) + folderType;
        if (!new File(mBaseFolderPath).exists() && new File(mBaseFolderPath).mkdirs()) {
            Log.i("Directory created", mBaseFolderPath);
        }
        String fileName = messageId + "." + fileExt;
        String path = mBaseFolderPath + fileName;

        File f = new File(path);
        FileOutputStream fo = null;

        try {
            f.createNewFile();
            fo = new FileOutputStream(f);
            fo.write(data);
            fo.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return f;
    }

    public void TrovaEvents(final ArrayList<ChatMessageModel> chatMessageModelArrayList) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                chatList = chatMessageModelArrayList;
                chatMessageAdapter.notifyDataSetChanged();
                if (chatSwipeRefreshLayout.isRefreshing()) {
                    chatSwipeRefreshLayout.setRefreshing(false);
                } else {
//                    recyclerView.smoothScrollToPosition(chatList.size());
                    if (chatList != null && chatList.size() > 1) {
                        smoothScroller.setTargetPosition(chatList.size() - 1);
                        mLayoutManager.startSmoothScroll(smoothScroller);
                    }
                }
            }
        });
    }

    public void updateMesageStatus(Long messageID, int status) {
        try {
            for (int i = 0; i < chatList.size(); i++) {
                ChatMessageModel chatMessageModel = chatList.get(i);
                if (messageID == chatMessageModel.getMessageId()) {
                    chatMessageModel.setSeenstatus(status);
                    break;
                }
            }
            chatMessageAdapter.notifyDataSetChanged();
            dataBaseHandler.updateChatMessageStatus("" + messageID, status);
            dataBaseHandler.updateChatLogSeenStatus(chatSenderId, status, true, "" + messageID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void TrovaEvent(JSONObject jsonObject) {
        String event;
        try {
            event = jsonObject.getString("trovaEvent");
            switch (event) {

                case OnTrovaAmazonS3_Upload:
                    String state = jsonObject.getString("state");

                    if (state.equalsIgnoreCase("success")) {
                        JSONObject dataMain = new JSONObject(jsonObject.getString("data"));
                        ChatMessageModel chatMessageModel = new ChatMessageModel();
                        String localFilePath = dataMain.getString("localFilePath");
                        long fileSize = dataMain.getLong("filesize");
                        String filename = dataMain.getString("filename");
                        String fileDuration = dataMain.getString("fileduration");
                        String mimeType = dataMain.getString("mimeType");
                        String fileExt = dataMain.getString("fileExt");
                        String serverPath = dataMain.getString("serverPath");
                        long messageId = dataMain.getLong("messageId");
                        String currDate = dataMain.getString("currDate");
                        String time = dataMain.getString("time");
                        String timezone = dataMain.getString("timezone");

                        chatMessageModel.setFilePath(localFilePath);
                        chatMessageModel.setMediaLink(serverPath);
                        chatMessageModel.setFileSize(fileSize);
                        chatMessageModel.setFileName(filename);
                        chatMessageModel.setDurationTime(fileDuration);
                        String identifier = dataMain.getString("identifier");
                        if (TextUtils.isEmpty(identifier)) {
                            chatMessageModel.setMimeType(mimeType);
                        } else {
                            chatMessageModel.setMimeType(identifier);
                        }

                        chatMessageModel.setFileExt(fileExt);
                        chatMessageModel.setMessageId(messageId);
                        chatMessageModel.setFileUpload(true);
                        chatMessageModel.setDate(currDate);
                        chatMessageModel.setTime(time);
                        chatMessageModel.setTimeZone(timezone);
                        chatMessageModel.setMediaLink(serverPath);
                        chatMessageModel.setUserId(chatSenderId);

                        for (int j = 0; j < TrovaChat.chatList.size(); j++) {
                            if (chatList.get(j).getFilePath() != null && chatList.get(j).getFilePath().equalsIgnoreCase(localFilePath)) {
                                chatList.get(j).setMediaLink(serverPath);
                            }
                        }

                        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(activity);
                        dataBaseHandler.updateFileMessage(localFilePath, serverPath);
                        if (Globalclass.trovaSDK_init != null)
                            Globalclass.trovaSDK_init.trovaXmit_File(chatMessageModel.getMediaLink(), chatMessageModel.getFileName(), chatMessageModel.getMimeType(), chatMessageModel.getFileSize(), chatMessageModel.getDurationTime(), "", chatMessageModel.getUserId(), preferenceUtil.getUserName(), "File Received", chatMessageModel.getMessageId());
                        chatMessageAdapter.notifyDataSetChanged();
//                        recyclerView.smoothScrollToPosition(chatList.size());
                        smoothScroller.setTargetPosition(chatList.size() - 1);
                    }
                    break;

                case OnTrovaAmazonS3_Download:
                    state = jsonObject.getString("state");
                    if (state.equalsIgnoreCase("success")) {
                        JSONObject jobj = new JSONObject(jsonObject.getString("data"));
                        String server_path = jobj.getString("serverPath");
                        for (int i = 0; i < chatList.size(); i++) {
                            if (chatList.get(i).getMediaLink().equalsIgnoreCase(server_path)) {
                                sendMessageSeenStatus(chatList.get(i).getMessageId(), server_path);
                            }
                        }
                        chatMessageAdapter.notifyDataSetChanged();
//                        recyclerView.smoothScrollToPosition(chatList.size());
                        smoothScroller.setTargetPosition(chatList.size() - 1);
                    }
                    break;

                case OnTrovaReceiveNotification:
                    JSONObject notification = new JSONObject(jsonObject.getString("notification"));
                    String serverPath = "";
                    if (notification.has("serverPath")) {
                        serverPath = notification.getString("serverPath");
                    }
                    switch (notification.getString("event")) {
                        case "onMessageDelivered":
                            for (int j = 0; j < TrovaChat.chatList.size(); j++) {
                                if (chatList.get(j).getMessageId() == notification.getLong("messageId")) {
                                    chatList.get(j).setSeenstatus(1);
                                    chatMessageAdapter.notifyDataSetChanged();
                                } else if (!serverPath.equalsIgnoreCase("") && chatList.get(j).getMediaLink() != null && chatList.get(j).getMediaLink().equalsIgnoreCase(serverPath)) {
                                    chatList.get(j).setSeenstatus(1);
                                    chatMessageAdapter.notifyDataSetChanged();
                                }
                            }
                            break;
                        case "onMessageSeen":
                            for (int j = 0; j < TrovaChat.chatList.size(); j++) {
                                if (chatList.get(j).getMessageId() == notification.getLong("messageId")) {
                                    chatList.get(j).setSeenstatus(2);
                                    chatMessageAdapter.notifyDataSetChanged();
                                } else if (!serverPath.equalsIgnoreCase("") && chatList.get(j).getMediaLink() != null && chatList.get(j).getMediaLink().equalsIgnoreCase(serverPath)) {
                                    chatList.get(j).setSeenstatus(2);
                                    chatMessageAdapter.notifyDataSetChanged();
                                }
                            }
                            break;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}