package com.instag.vijay.fasttrending.video;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.Db.DataBaseHandler;
import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.Utils.CameraPreview;
import com.instag.vijay.fasttrending.Utils.PercentFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by razin on 04-10-2017..
 */
public class ReachVideoCall extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1234;
    public static ReachVideoCall reachVideoCall;
    public static boolean isVideoCallAlive = false;
    public HashMap<Integer, String> videoList = new HashMap<>();
    boolean isConnected;
    boolean endCallDone;
    boolean makeAgentCall = false;
    boolean makeNormalCall = false;
    private View rlremote1View, rlremote2View;
    private ImageView img_trova_video_call_video;
    private ImageView img_trova_video_call_mic;
    private ImageView img_trova_video_call_speaker;
    private ImageView img_trova_video_call_add;
    private ImageView img_trova_video_call_list;
    private ImageView ivMic1, ivMic2;
    private TextView txtVideo2Name;
    private RelativeLayout ll_trova_video_controls;
    private FrameLayout fl_reach_video_call_fragment_container;
    private LinearLayout ll_trova_video_call_contact_info_container;
    private FragmentTransaction ft;
    private String callType;
    private String otherUserID;
    private String agentKey;
    private String otherUserName = "";
    private DataBaseHandler dataBaseHandler;
    private boolean isRejected = false, isCallstarted = false, isdestroy = false;
    private TextView tv_trova_video_call_caller_name;
    public ImageView img_trova_video_call_reject, img_trova_video_call_accept;
    private boolean isSpeaker;
    private boolean isMicEnabled = true;
    private boolean isVideoEnabled = true;
    private long startTime;
    private TextView tv_trova_video_call_caller_id;
    private boolean restrictEscalate;
    private TextView tv_trova_video_call_finding_agents;
    private LinearLayout ll_trova_video_call_finding_agents;
    private TextView tv_trova_video_call_business_name;
    private String businessName = "";
    private CameraPreview mPreview;
    private int status = 0;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private long[] vibratepattern = {0, 200, 600};
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private MediaPlayer m; /*assume, somewhere in the global scope...*/
    private Runnable delayedTask;
    private MediaPlayer mbusy;
    private Handler mHandler = new Handler();
    private PercentFrameLayout incallremotevideolayout0;
    private ProgressBar mProgress;

    private int[] img_trova_video_call_speaker_pos = new int[2];
    private int[] img_trova_video_call_mic_pos = new int[2];
    private int[] img_trova_video_call_add_pos = new int[2];
    private int[] img_trova_video_call_video_pos = new int[2];
    private int[] img_trova_video_call_list_pos = new int[2];
    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long millisUntilFinished = System.currentTimeMillis() - startTime;
            tv_trova_video_call_caller_id.setVisibility(View.VISIBLE);
            tv_trova_video_call_caller_id.setText(String.format(Locale.getDefault(), "%s %02d:%02d:%02d", getString(R.string.call_duration),
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 1000);
        }
    };
    private Handler mCallingHandler = new Handler();
    private int callingTxtCount = 1;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isVideoCallAlive = true;
        reachVideoCall = this;
        activity = this;
        dataBaseHandler = DataBaseHandler.getInstance(activity);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_trova_video_conference_call_new);
//            ContactListFragment.setCallBack(reachEscalationCallBack);
        setInitUI();
        InitVideoUI();
        setRegisterUI();
        handleCallViews();
    }

    private void setInitUI() {
        ll_trova_video_controls = findViewById(R.id.rl_trova_video_controls);
        fl_reach_video_call_fragment_container = findViewById(R.id.fl_reach_video_call_fragment_container);
        ll_trova_video_call_contact_info_container = findViewById(R.id.ll_trova_video_call_contact_info_container);
        img_trova_video_call_video = findViewById(R.id.img_trova_video_call_video);
        img_trova_video_call_mic = findViewById(R.id.img_trova_video_call_mic);
        img_trova_video_call_speaker = findViewById(R.id.img_trova_video_call_speaker);
        img_trova_video_call_add = findViewById(R.id.img_trova_video_call_add);
        img_trova_video_call_list = findViewById(R.id.img_trova_video_call_list);
        tv_trova_video_call_business_name = findViewById(R.id.tv_trova_video_call_business_name);
        tv_trova_video_call_caller_name = findViewById(R.id.tv_trova_video_call_caller_name);
        tv_trova_video_call_caller_id = findViewById(R.id.tv_trova_video_call_caller_id);
        img_trova_video_call_reject = findViewById(R.id.img_trova_video_call_reject);
        img_trova_video_call_accept = findViewById(R.id.img_trova_video_call_accept);
        img_trova_video_call_video.setImageResource(R.drawable.ic_videocam_black_24dp);
        img_trova_video_call_mic.setImageResource(R.drawable.ic_mic_on);

        img_trova_video_call_speaker.getLocationOnScreen(img_trova_video_call_speaker_pos);
        img_trova_video_call_mic.getLocationOnScreen(img_trova_video_call_mic_pos);
        img_trova_video_call_add.getLocationOnScreen(img_trova_video_call_add_pos);
        img_trova_video_call_video.getLocationOnScreen(img_trova_video_call_video_pos);
        img_trova_video_call_list.getLocationOnScreen(img_trova_video_call_list_pos);

        mProgress = findViewById(R.id.circularProgressbar);

    }

    private void InitVideoUI() {
        incallremotevideolayout0 = findViewById(R.id.incallremotevideolayout0);
        incallremotevideolayout0.setPosition(0, 0, 100, 100);
        ivMic1 = findViewById(R.id.ivMic1);
        ivMic2 = findViewById(R.id.ivMic2);
        ivMic1.setVisibility(View.GONE);
        txtVideo2Name = findViewById(R.id.txtVideo2Name);

        rlremote1View = findViewById(R.id.rlremote1View);
        rlremote2View = findViewById(R.id.rlremote2View);

        rlremote1View.setVisibility(View.GONE);
        rlremote2View.setVisibility(View.GONE);

        incallremotevideolayout0.setVisibility(View.VISIBLE);
        ll_trova_video_call_finding_agents = findViewById(R.id.ll_trova_video_call_finding_agents);
        tv_trova_video_call_finding_agents = findViewById(R.id.tv_trova_video_call_finding_agents);
        ll_trova_video_controls.setVisibility(View.GONE);
    }

    private void setRegisterUI() {
        img_trova_video_call_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                    isVideoEnabled = !isVideoEnabled;
                    if (!isVideoEnabled) {
                        img_trova_video_call_video.setImageResource(R.drawable.ic_videocam_off_black_24dp);
                    } else {
                        img_trova_video_call_video.setImageResource(R.drawable.ic_videocam_black_24dp);
                    }
                }
            }
        });

        isSpeaker = true;

        img_trova_video_call_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                    if (isSpeaker) {
                        img_trova_video_call_speaker.setImageResource(R.drawable.ic_volume_off_black_24dp);
                        isSpeaker = false;
                    } else {
                        img_trova_video_call_speaker.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        isSpeaker = true;
                    }
                }

            }
        });

        img_trova_video_call_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                    isMicEnabled = !isMicEnabled;
                    if (isMicEnabled) {
                        img_trova_video_call_mic.setImageResource(R.drawable.ic_mic_on);
                    } else {
                        img_trova_video_call_mic.setImageResource(R.drawable.ic_mic_off);
                    }
                    try {
                        JSONObject nuserObj = new JSONObject();
                        nuserObj.put("event", isMicEnabled ? "micon" : "micoff");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        img_trova_video_call_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reachVideoCall = null;
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (mPreview != null) {
                    mPreview.destroycamView();
                    mPreview = null;
                }

                if (otherUserID != null && !otherUserID.isEmpty()) {
                    if (isConnected) {
                        endCallDone = true;
                        reachVideoCall = null;
                        isdestroy = true;
                        onPause();
                        onfinish();
                    } else {
                        isRejected = true;
                        reachVideoCall = null;
                        isdestroy = true;
                        onPause();
                        onfinish();
                    }
                } else {
                    reachVideoCall = null;
                    isdestroy = true;
                    onPause();
                    onfinish();
                }
            }
        });

        img_trova_video_call_accept.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }

                incallremotevideolayout0.removeAllViews();
                incallremotevideolayout0.setVisibility(View.GONE);
                img_trova_video_call_accept.setVisibility(View.GONE);
                ll_trova_video_controls.setVisibility(View.VISIBLE);
                img_trova_video_call_speaker.setVisibility(View.VISIBLE);
                img_trova_video_call_add.setVisibility(View.VISIBLE);
                img_trova_video_call_list.setVisibility(View.VISIBLE);
                img_trova_video_call_mic.setVisibility(View.VISIBLE);
                img_trova_video_call_video.setVisibility(View.VISIBLE);
                tv_trova_video_call_caller_id.setVisibility(View.VISIBLE);
                tv_trova_video_call_caller_id.setText("connecting...");

//                tv_trova_video_call_caller_name.setText(otherUserID);
                ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("In-Call");
                isConnected = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                            slideDown(ll_trova_video_controls, 0, ll_trova_video_controls.getHeight(), 400);
                            slideDown(ll_trova_video_call_contact_info_container, 0, -ll_trova_video_call_contact_info_container.getHeight(), 400);
                        }
                    }
                }, 5000);
            }
        });


        img_trova_video_call_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(agentKey))
                    if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                        if (!restrictEscalate) {
                            fl_reach_video_call_fragment_container.setVisibility(View.VISIBLE);
                            ft = getFragmentManager().beginTransaction();
                        }
                    }
            }
        });
    }

    // slide the view from below itself to the current position
    public void slideUp(View view, int fromYDelta, int toYDelta) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                fromYDelta,  // fromYDelta
                toYDelta);                // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view, int fromYDelta, int toYDelta, int duration) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                fromYDelta,                 // fromYDelta
                toYDelta); // toYDelta
        animate.setDuration(duration);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    private void handleCallViews() {
        status = getIntent().getIntExtra("status", 0);

        restrictEscalate = getIntent().getBooleanExtra("restrictEscalate", false);

        callType = getIntent().getStringExtra("callType");
        if (getIntent().hasExtra("otherUserID")) {
            otherUserID = getIntent().getStringExtra("otherUserID");
        }
        if (getIntent().hasExtra("agentKey")) {
            agentKey = getIntent().getStringExtra("agentKey");
        }
//        if (agentKey != null && !agentKey.equalsIgnoreCase("")) {
//            ReachVoiceCall.agentKey1 = agentKey;
//            if (!TextUtils.isEmpty(otherUserID))
//                ReachVoiceCall.conferenceId = md5(preferenceUtil.getBusinessKey() + otherUserID);
//        }


        if (getIntent().hasExtra("otherUserName")) {
            if (TextUtils.isEmpty(otherUserID))
                otherUserName = dataBaseHandler.getUserName(otherUserID);
            if (TextUtils.isEmpty(otherUserName))
                otherUserName = getIntent().getStringExtra("otherUserName");
        }
        if (getIntent().hasExtra("businessName")) {
            businessName = getIntent().getStringExtra("businessName");
        }
        {
            tv_trova_video_call_business_name.setVisibility(View.GONE);
            ll_trova_video_call_finding_agents.setVisibility(View.GONE);
            if (callType.equals("answer")) {
                ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("Incoming");
                if (businessName != null && !businessName.isEmpty()) {
                    tv_trova_video_call_business_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setText(otherUserName);
                    tv_trova_video_call_business_name.setText(businessName);
                    tv_trova_video_call_caller_id.setVisibility(View.GONE);
                } else {
                    tv_trova_video_call_business_name.setVisibility(View.GONE);
                    tv_trova_video_call_caller_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_id.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setText(otherUserName);
                    tv_trova_video_call_caller_id.setText(otherUserID);
                }

                img_trova_video_call_accept.setVisibility(View.VISIBLE);
                img_trova_video_call_reject.setVisibility(View.VISIBLE);
                ll_trova_video_controls.setVisibility(View.GONE);
                img_trova_video_call_speaker.setVisibility(View.GONE);
                img_trova_video_call_add.setVisibility(View.GONE);
                img_trova_video_call_list.setVisibility(View.GONE);
                img_trova_video_call_mic.setVisibility(View.GONE);
                img_trova_video_call_video.setVisibility(View.GONE);
                ArrayList<String> permissionlist = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                if (permissionlist.size() <= 0)
                    requestCameraPermissionAndContinue();
            } else if (callType.equalsIgnoreCase("in_call")) {
                if (businessName != null && !businessName.isEmpty()) {
                    tv_trova_video_call_business_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_business_name.setText(businessName);

                    if (otherUserName.isEmpty() || otherUserName.equalsIgnoreCase("")) {
                        tv_trova_video_call_caller_name.setText(otherUserID);
                        tv_trova_video_call_caller_id.setText("");
                    } else {
                        tv_trova_video_call_caller_name.setText(otherUserName);
                        tv_trova_video_call_caller_id.setText("");
                    }

                } else {
                    tv_trova_video_call_business_name.setVisibility(View.GONE);
                    tv_trova_video_call_caller_id.setText(otherUserID);
                }
                ll_trova_video_controls.setVisibility(View.VISIBLE);
                img_trova_video_call_speaker.setVisibility(View.VISIBLE);
                img_trova_video_call_mic.setVisibility(View.VISIBLE);
                img_trova_video_call_accept.setVisibility(View.GONE);
                img_trova_video_call_reject.setVisibility(View.VISIBLE);
                isConnected = true;
                ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("In-Call");
            } else {


                if (businessName != null && !businessName.isEmpty()) {
                    tv_trova_video_call_business_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setText(otherUserName);
                    tv_trova_video_call_business_name.setText(businessName);
                    tv_trova_video_call_caller_id.setVisibility(View.GONE);
                } else {
                    tv_trova_video_call_business_name.setVisibility(View.GONE);
                    tv_trova_video_call_caller_name.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_id.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_name.setText(otherUserName);
                    tv_trova_video_call_caller_id.setText(otherUserID);
                }
                img_trova_video_call_accept.setVisibility(View.GONE);
                img_trova_video_call_reject.setVisibility(View.VISIBLE);
                ll_trova_video_controls.setVisibility(View.GONE);
                img_trova_video_call_speaker.setVisibility(View.GONE);
                img_trova_video_call_add.setVisibility(View.GONE);
                img_trova_video_call_list.setVisibility(View.GONE);
                img_trova_video_call_mic.setVisibility(View.GONE);
                img_trova_video_call_video.setVisibility(View.GONE);

//                if (makeAgentCall || makeNormalCall) {
                requestCameraPermissionAndContinue();
//                }
                playCallerTone();
            }
        }
        if (getIntent().hasExtra("action")) {
            if (getIntent().getStringExtra("action").equalsIgnoreCase("accept_call")) {
                img_trova_video_call_accept.callOnClick();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fl_reach_video_call_fragment_container.getVisibility() == View.VISIBLE) {
            fl_reach_video_call_fragment_container.setVisibility(View.GONE);
        }
    }

    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (byte anA : a) {
                sb.append(Character.forDigit((anA & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(anA & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setRinger() {
        if (mediaPlayer == null)
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                switch (audio.getRingerMode()) {
                    case AudioManager.RINGER_MODE_NORMAL:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), notification);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        vibrator.vibrate(vibratepattern, 0);
                        break;
                    case AudioManager.RINGER_MODE_SILENT:
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        vibrator.vibrate(vibratepattern, 0);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void requestCameraPermissionAndContinue() {
        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() > 0) {
            PermissionCheck.requestPermission(activity, pendingPermissions, PERMISSION_REQUEST_CODE);
        } else {
//            getCameraPreview();
            setCamera();
        }
    }

//    private void getCameraPreview() {
////        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();
//        if (cameraCount > 0) {
//            setCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
//        } else {
//            setCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
//        }
//    }

    boolean isAlreadayPlaying = false;

    private void setCamera() {
        if (!isAlreadayPlaying) {
            mPreview = new CameraPreview(this);
            incallremotevideolayout0.setVisibility(View.VISIBLE);
            incallremotevideolayout0.removeAllViews();
            incallremotevideolayout0.setScaleX(mPreview.getScaleX());
            incallremotevideolayout0.setScaleY(mPreview.getScaleY());
            incallremotevideolayout0.addView(mPreview);
            //mPreview.startCamera();
            isAlreadayPlaying = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (callType.equals("answer")) {
                        setRinger();
                    } else if (status != 1) {
                        playCallerTone();
                    }
                }
            }, 1200);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        initDestroy();
    }

    private void initDestroy() {
        if (isFinishing() || isdestroy) {
            isVideoCallAlive = false;
            isdestroy = false;
            try {
                if (mPreview != null) {
                    mPreview.destroycamView();
                    mPreview = null;
                }
                reachVideoCall = null;
                mHandler.removeCallbacks(mUpdateTimeTask);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }
                stopCallerTone();
                stopBusyTone();
                System.gc();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopBusyTone() {
        try {
            if (mbusy != null && mbusy.isPlaying()) {
                mbusy.stop();
                mbusy.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playCallBusyTone() {
        try {
            if (mbusy == null) {
                mbusy = new MediaPlayer();
                AssetFileDescriptor descriptor = getAssets().openFd("phone_busy.mp3");
                mbusy.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mbusy.prepare();
                mbusy.setVolume(1f, 1f);
                mbusy.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopCallerTone() {
        try {
            mainThreadHandler.removeCallbacks(delayedTask);
            if (m != null && m.isPlaying()) {
                m.stop();
                m.release();
                m = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playCallerTone() {
        try {
            if (m == null) {
                m = new MediaPlayer();
                AssetFileDescriptor descriptor = getAssets().openFd("ringing.mp3");
                m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                m.prepare();
                m.setVolume(1f, 1f);
                m.setLooping(true);
                m.start();

                delayedTask = new Runnable() {
                    @Override
                    public void run() {
                        onfinish();
                    }

                };
                mainThreadHandler.postDelayed(delayedTask, 30000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void flipCamera() {
//        if (camId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//            setCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
//        } else {
//            setCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initDestroy();
    }

    private void onfinish() {
        if (isTaskRoot()) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    String escalatedUserId = "";

    public void TrovaEvents(final JSONObject jsonObject) {
        String event;
        try {
            event = jsonObject.getString("trovaEvent");
            switch (event) {
                case "micoff":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String callerId = null;
                            try {
                                callerId = jsonObject.get("callerId").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ivMic1.setImageResource(R.drawable.ic_mic_off);
                        }
                    });

                    break;
                case "micon":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String callerId = null;
                            try {
                                callerId = jsonObject.get("callerId").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ivMic1.setImageResource(R.drawable.ic_mic_on);
                        }
                    });

                    break;
                case "OnTrovaReceiveCalleeBusy":
                    String callerId = jsonObject.get("callerId").toString();
                    if (callerId.equalsIgnoreCase(otherUserID)) {
                        stopCallerTone();
                        Toast.makeText(activity, callerId + " is Busy right now", Toast.LENGTH_LONG).show();
                        playCallBusyTone();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopCallerTone();
                                stopBusyTone();
                                isdestroy = true;
                                onPause();
                                onfinish();
                            }
                        }, 2000);
                    }
                    break;
                case "OnTrovaReceiveLocalStream":
/*
                    try {
                        MediaStream mediaStream = (MediaStream) jsonObject.get("stream");
                        videoList.put(0, "You");
                        if (mediaStream.videoTracks.size() == 1) {
                            videoTrackLocal = mediaStream.videoTracks.get(0);
                            videoTrackLocal.setEnabled(true);

                            if (callType.equalsIgnoreCase("in_call")) {
                                incallremotevideolayout0.setVisibility(View.GONE);
                                flincallremotevideoview.setVisibility(View.VISIBLE);
                                flincallremotevideoviewtwo.setVisibility(View.GONE);

                                incalllocalvideoview.setVisibility(View.VISIBLE);
                                videoRendererLocal = new VideoRenderer(incalllocalvideoview);
                                videoTrackLocal.addRenderer(videoRendererLocal);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
*/
                    break;

                case "OnTrovaReceiveRemoteStream":
                    try {
                        if (!isCallstarted) {
                            isCallstarted = true;
                            // TrovaService.createInCallNotification(otherUserName, otherUserID, "video");
                            stopCallerTone();
                            img_trova_video_call_accept.setVisibility(View.GONE);
                            ll_trova_video_controls.setVisibility(View.VISIBLE);
                            img_trova_video_call_speaker.setVisibility(View.VISIBLE);
                            img_trova_video_call_add.setVisibility(View.VISIBLE);
                            img_trova_video_call_list.setVisibility(View.VISIBLE);
                            img_trova_video_call_mic.setVisibility(View.VISIBLE);
                            img_trova_video_call_video.setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("In-Call");
                            isConnected = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                                        slideDown(ll_trova_video_controls, 0, ll_trova_video_controls.getHeight(), 400);
                                        slideDown(ll_trova_video_call_contact_info_container, 0, -ll_trova_video_call_contact_info_container.getHeight(), 400);
                                    }
                                }
                            }, 5000);
                        }


                      /*  MediaStream mediaStream = (MediaStream) jsonObject.get("stream");
                        String callerPhone = jsonObject.getString("callerId").trim();
                        if (!videoList.containsValue(callerPhone)) {
                            videoList.put(videoList.size(), callerPhone);
                        }

                        if (videoList.size() == 2) {
                            ivMic1.setVisibility(View.VISIBLE);
                            ivMic1.setImageResource(R.drawable.ic_mic_on);
                            incallremotevideolayout0.removeAllViews();
                            incallremotevideolayout0.setVisibility(View.GONE);
                            flincallremotevideoview.setVisibility(View.VISIBLE);
                            flincallremotevideoviewtwo.setVisibility(View.GONE);
                            incallremotevideoview.setZOrderMediaOverlay(false);
                            incallremotevideoview.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                            incallremotevideoview.setMirror(false);
                            incalllocalvideoview.setVisibility(View.VISIBLE);
                            videoTrackRemote = mediaStream.videoTracks.get(0);
                            videoTrackRemote.setEnabled(true);
                            videoRendererRemote = new VideoRenderer(incallremotevideoview);
                            videoTrackRemote.addRenderer(videoRendererRemote);
                            flincallremotevideoview.setVisibility(View.VISIBLE);

                            videoRendererLocal = new VideoRenderer(incalllocalvideoview);
                            videoTrackLocal.addRenderer(videoRendererLocal);

                        } else if (videoList.size() == 3) {
                            rlremote1View.setVisibility(View.VISIBLE);
                            rlremote2View.setVisibility(View.VISIBLE);
                            txtVideo2Name.setText(JoinConferenceDialog.agentName);
                            ivMic2.setImageResource(R.drawable.ic_mic_on);
                            isEscalationCancelled = false;
                            countDownTimer.cancel();
                            restrictEscalate = true;
                            img_trova_video_call_add.setBackgroundResource(R.drawable.rounded_corner_button_disabled);
                            incallremotevideolayout0.removeAllViews();
                            incallremotevideolayout0.setVisibility(View.GONE);
                            flincallremotevideoview.setVisibility(View.VISIBLE);
                            flincallremotevideoviewtwo.setVisibility(View.VISIBLE);
                            incallremotevideoviewtwo.bringToFront();
                            videoTrackRemotetwo = mediaStream.videoTracks.get(0);
                            videoTrackRemotetwo.setEnabled(true);
                            videoRendererRemotetwo = new VideoRenderer(incallremotevideoviewtwo);
                            videoTrackRemotetwo.addRenderer(videoRendererRemotetwo);
                            incallremotevideoviewtwo.bringToFront();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "OnTrovaCallState":
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("connected")) {
                        if (!isCallstarted) {
                            isCallstarted = true;
//                            TrovaService.createInCallNotification(otherUserName, otherUserID, "video");
                            stopCallerTone();
//                            incallremotevideolayout0.removeAllViews();
//                            incallremotevideolayout0.setVisibility(View.GONE);

                            img_trova_video_call_accept.setVisibility(View.GONE);
                            ll_trova_video_controls.setVisibility(View.VISIBLE);
                            img_trova_video_call_speaker.setVisibility(View.VISIBLE);
                            img_trova_video_call_add.setVisibility(View.VISIBLE);
                            img_trova_video_call_list.setVisibility(View.VISIBLE);
                            img_trova_video_call_mic.setVisibility(View.VISIBLE);
                            img_trova_video_call_video.setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("In-Call");
                            isConnected = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                                        slideDown(ll_trova_video_controls, 0, ll_trova_video_controls.getHeight(), 400);
                                        slideDown(ll_trova_video_call_contact_info_container, 0, -ll_trova_video_call_contact_info_container.getHeight(), 400);
                                    }
                                }
                            }, 5000);

                        }
                        if (startTime == 0) {
                            startTime = System.currentTimeMillis();
                            mHandler.postDelayed(mUpdateTimeTask, 100);
                        }
                    } else if (status.equalsIgnoreCase("failed")) {
                        callerId = jsonObject.has("callerId") ? jsonObject.getString("callerId") : "";
                        if (TextUtils.isEmpty(agentKey) || callerId.equalsIgnoreCase(otherUserID)) {
                            endCallDone = true;
                            Toast.makeText(activity, "App unexpectedly stopped", Toast.LENGTH_SHORT).show();
                            isdestroy = true;
                            onPause();
                            reachVideoCall = null;
                            onfinish();
                        }
                    }
                    break;
                case "OnTrovaReceiveEndCall":

                    try {
                        String callMode = jsonObject.getString("callMode");
                        if (callMode.equalsIgnoreCase("video")) {
                            callerId = jsonObject.getString("callerId");
                            if (TextUtils.isEmpty(agentKey) || callerId.equalsIgnoreCase(otherUserID)) {
                                endCallDone = true;
//                                if (!TextUtils.isEmpty(agentKey))
//                                    Globalclass.trovaSDK_init.trovaCall_End("video", otherUserID);
                                reachVideoCall = null;

                                stopCallerTone();
                                stopBusyTone();
                                isdestroy = true;
                                onPause();
                                onfinish();
                            } else {
                                restrictEscalate = false;
                                isEscalationCancelled = true;
//                                img_trova_video_call_add.setBackgroundResource(R.drawable.rounded_corner_button_color);
                                mProgress.setVisibility(View.GONE);
                                slideDown(rlremote2View, 0, -(ll_trova_video_call_contact_info_container.getHeight() + rlremote2View.getHeight()), 400);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "OnTrovaReceiveRejectCall":
                    reachVideoCall = null;
                    otherUserID = "";
                    stopCallerTone();
                    stopBusyTone();
                    isdestroy = true;
                    onPause();
                    onfinish();
                    break;

                case "OnPermissionsRequired":
                    Toast.makeText(activity, "Permission Required", Toast.LENGTH_SHORT).show();
                    PermissionCheck.requestPermission(activity, PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions()), PERMISSION_REQUEST_CODE);
                    break;
                case "OnTrovaReceiveMissedCall":

                    reachVideoCall = null;
                    isRejected = true;
                    try {
                        String callerid = jsonObject.get("callerId").toString();
                        String callMode = jsonObject.get("callMode").toString();
                        if (TextUtils.isEmpty(otherUserID) || callerid.equalsIgnoreCase(otherUserID) && callMode.equalsIgnoreCase("video")) {
                            stopCallerTone();
                            stopBusyTone();
                            isdestroy = true;
                            onPause();
                            onfinish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String callerid = jsonObject.get("callerId").toString();
                        Toast.makeText(activity, "Missed call from " + callerid, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "OnTrovaAcceptedCall":
                    isCallstarted = true;
//                    TrovaService.createInCallNotification(otherUserName, otherUserID, "video");
                    stopCallerTone();
                    incallremotevideolayout0.removeAllViews();
                    incallremotevideolayout0.setVisibility(View.GONE);
                    tv_trova_video_call_caller_id.setVisibility(View.VISIBLE);
                    tv_trova_video_call_caller_id.setText("connecting...");

                    img_trova_video_call_accept.setVisibility(View.GONE);
                    ll_trova_video_controls.setVisibility(View.VISIBLE);
                    img_trova_video_call_speaker.setVisibility(View.VISIBLE);
                    img_trova_video_call_add.setVisibility(View.VISIBLE);
                    img_trova_video_call_list.setVisibility(View.VISIBLE);
                    img_trova_video_call_mic.setVisibility(View.VISIBLE);
                    img_trova_video_call_video.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_trova_video_calling)).setText("In-Call");
                    isConnected = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ll_trova_video_controls.getVisibility() == View.VISIBLE) {
                                slideDown(ll_trova_video_controls, 0, ll_trova_video_controls.getHeight(), 400);
                                slideDown(ll_trova_video_call_contact_info_container, 0, -ll_trova_video_call_contact_info_container.getHeight(), 400);
                            }
                        }
                    }, 5000);
                    String callMode = jsonObject.getString("callMode");
                    if (callMode.equalsIgnoreCase("video")) {
                        callerId = jsonObject.getString("callerId");
                        if (!callerId.equalsIgnoreCase(otherUserID)) {
                            isEscalationCancelled = false;
                            countDownTimer.cancel();
                        }
                    }

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



/*

    public static ReachEscalationCallBack reachEscalationCallBack = new ReachEscalationCallBack() {
        @Override
        public void setReachEscalationCallBack(JSONObject jsonObject) {
            try {
                String contact_name = jsonObject.getString("contact_name");
                String contact_number = jsonObject.getString("contact_number");
                String contact_user_id = jsonObject.getString("contact_user_id");
                JSONObject sendNotificationObject = new JSONObject();
                sendNotificationObject.put("event", "onJoinConferenceRequest");
                sendNotificationObject.put("agentKey", ReachVideoCall.reachVideoCall.agentKey);
                sendNotificationObject.put("displayName", ReachVideoCall.reachVideoCall.businessName);
                sendNotificationObject.put("callMode", "video");
                sendNotificationObject.put("callbackTime", System.currentTimeMillis());
                sendNotificationObject.put("widgetUserName", ReachVideoCall.reachVideoCall.otherUserName);
                sendNotificationObject.put("agentUserName", ReachVideoCall.reachVideoCall.preferenceUtil.getUserName());
                sendNotificationObject.put("agentUserId", ReachVideoCall.reachVideoCall.preferenceUtil.getUserId());
                sendNotificationObject.put("widgetUserId", ReachVideoCall.reachVideoCall.otherUserID);
                Globalclass.trovaSDK_init.trovaAddToConference(contact_user_id, sendNotificationObject.toString());
                if (!TextUtils.isEmpty(mainAgent)) {
                    mainAgent = ReachVideoCall.reachVideoCall.preferenceUtil.getUserId();
                    GadgetAgent = ReachVideoCall.reachVideoCall.otherUserID;
                }
                agentName = contact_name;
                ReachVideoCall.reachVideoCall.restrictEscalate = true;
                ReachVideoCall.reachVideoCall.escalatedUserId = contact_user_id;
                Toast.makeText(ReachVideoCall.reachVideoCall, contact_name + " escalated successfully", Toast.LENGTH_SHORT).show();
                ReachVideoCall.reachVideoCall.countDownTimer.start();
                ReachVideoCall.reachVideoCall.img_trova_video_call_add.setBackgroundResource(R.drawable.rounded_corner_button_disabled);
                ReachVideoCall.reachVideoCall.setProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
*/


    private boolean isEscalationCancelled = true;

    private CountDownTimer countDownTimer = new

            CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.i("CountDownTimer:", "seconds remaining: " + millisUntilFinished / 1000);
                    if (millisUntilFinished / 1000 == 0) {
                        isEscalationCancelled = true;
                    }
                    int progress = (int) (((Double.valueOf(millisUntilFinished) / 30000) * 100));
                    Log.i("CountDown:", "percentage: " + progress);
                    mProgress.setProgress(mProgress.getMax() - progress);
                }

                public void onFinish() {
                    if (isEscalationCancelled) {
                        restrictEscalate = false;
//                        img_trova_video_call_add.setBackgroundResource(R.drawable.rounded_corner_button_color);
                        mProgress.setVisibility(View.GONE);
                    }
                }
            };


//    public static void setCallBack(TrovaUIAPICallBack trovaUIAPICallBack) {
//        trovaUICallBack = trovaUIAPICallBack;
//    }
//
//    private void postEvent(String event) {
//        if (trovaUICallBack != null) {
//            JSONObject jobj = new JSONObject();
//            try {
//                jobj.put("trovaEvent", event);
//                jobj.put("callerId", otherUserID);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            trovaUICallBack.setTrovaUICallBack(jobj);
//        }
//    }
}

