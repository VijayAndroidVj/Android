package com.instag.vijay.fasttrending.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.instag.vijay.fasttrending.BuildConfig;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.Utils.ImageUtil;
import com.instag.vijay.fasttrending.Utils.Utils;
import com.instag.vijay.fasttrending.model.ChatMessageModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> implements View.OnClickListener {

    private int densityPix;
    //    private List<ChatMessageModel> chatList;
    private LayoutInflater layoutInflater;
    private Activity activity;

    MediaPlayer commonMediaPlayer;
    private ImageView commonivAudioPlay, commonivAudioPause;
    private ColorStateList deliverColors = Utils.getColorStateList("#FFFFFF");
    private ColorStateList seenColors = Utils.getColorStateList("#57C9FE");

    public ChatMessageAdapter(Activity activity) {
//        this.chatList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        float scale = activity.getResources().getDisplayMetrics().density;
        densityPix = (int) (20 * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.ivUploadDownload || i == R.id.ivVideoUploadDownload || i == R.id.ivAudioUploadDownload || i == R.id.ivDocUploadDownload) {
                ChatMessageModel chatMessageModel = (ChatMessageModel) v.getTag();
                chatMessageModel.setFileUploading(true);
                chatMessageModel.setFileUpload(false);
                if (chatMessageModel.getMessageSentOrReceived() == 1) {
                    String key = chatMessageModel.getMediaLink();
                    if (key.contains("amazonaws.com/"))
                        key = chatMessageModel.getMediaLink().split("amazonaws.com/")[1];

                    if (key.contains(".MOV")) {
                        key = key.split(".MOV")[0] + ".mp4";
                    }
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                    String str_Path = file.getPath().replace(file.getName(), "");
                    File filedir = new File(str_Path);
                    try {
                        filedir.mkdirs();
                    } catch (Exception ex1) {
                    }
                    notifyDataSetChanged();
                    //if (Globalclass.trovaSDK_init != null)
                    // Globalclass.trovaSDK_init.trovaAmazonS3_Download(file.getAbsolutePath(), chatMessageModel.getMediaLink().replaceAll("%40", "@"));

                } else {
                    if (TextUtils.isEmpty(chatMessageModel.getFilePath()) || !new File(chatMessageModel.getFilePath()).exists()) {
                        chatMessageModel.setFileUploading(false);
                        chatMessageModel.setFileUpload(true);
                        Toast.makeText(activity, "File not available", Toast.LENGTH_SHORT).show();
                    } else {
                        Calendar calender = Calendar.getInstance();
//                        if (Globalclass.trovaSDK_init != null)
//                            Globalclass.trovaSDK_init.trovaAmazonS3_Upload(chatMessageModel.getFilePath(), calender.get(Calendar.YEAR) + "/" + (calender.get(Calendar.MONTH) + 1) + "/" + calender.get(Calendar.DATE) + "/" + calender.get(Calendar.HOUR) + "/" + calender.get(Calendar.MINUTE), chatMessageModel.getMimeType());
                    }
                    notifyDataSetChanged();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.chat_message_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            ColorStateList textColors = holder.chatTime.getTextColors();

            ChatMessageModel model = TrovaChat.chatList.get(position);
            RelativeLayout currentView;
            if (model.getMessageSentOrReceived() == 0) {
                holder.llTextMessageStatus.setVisibility(View.VISIBLE);
                holder.llImageStatus.setVisibility(View.VISIBLE);
                holder.llAudioStatus.setVisibility(View.VISIBLE);
                holder.llVideoStatus.setVisibility(View.VISIBLE);
                holder.llDocStatus.setVisibility(View.VISIBLE);
            } else {
                holder.llTextMessageStatus.setVisibility(View.INVISIBLE);
                holder.llImageStatus.setVisibility(View.INVISIBLE);
                holder.llAudioStatus.setVisibility(View.INVISIBLE);
                holder.llVideoStatus.setVisibility(View.INVISIBLE);
                holder.llDocStatus.setVisibility(View.INVISIBLE);
            }

            if (model.getMimeType().equals("text")) {
                holder.txtMsgLyt.setVisibility(View.VISIBLE);
                currentView = holder.txtMsgLyt;
                holder.rlImageLayout.setVisibility(View.GONE);
                holder.rlAudioLayout.setVisibility(View.GONE);
                holder.rlVideoLayout.setVisibility(View.GONE);
                holder.rlDocLayout.setVisibility(View.GONE);
                switch (model.getSeenstatus()) {
                    case -1:
                        holder.img_chat_message_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_message_status_sent.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_message_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 0:
                        holder.img_chat_message_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_message_status_sent.setImageResource(R.drawable.ic_done_black);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_message_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 1:
                        holder.img_chat_message_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_message_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_message_status_sent.setImageTintList(textColors);
                        }
                        break;

                    case 2:
                        holder.img_chat_message_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_message_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_message_status_sent.setImageTintList(seenColors);
                        }
                        break;
                }
                holder.txtMessage.setText(model.getMessage());
                ImageUtil.setBackground(holder.txtMsgLyt, activity, (model.getMessageSentOrReceived() == 0) ? R.drawable.chat_rounded_cornner : R.drawable.chat_rounded_cornner_white);
                holder.chatTime.setText(model.getTime());

            } else if (model.getMimeType().contains("image")) {
                switch (model.getSeenstatus()) {
                    case -1:
                        holder.img_chat_image_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_image_status_sent.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_image_status_sent.setImageTintList(deliverColors);
                        }
                        break;
                    case 0:
                        holder.img_chat_image_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_image_status_sent.setImageResource(R.drawable.ic_done_black);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_image_status_sent.setImageTintList(deliverColors);
                        }
                        break;
                    case 1:
                        holder.img_chat_image_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_image_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_image_status_sent.setImageTintList(deliverColors);
                        }
                        break;

                    case 2:
                        holder.img_chat_image_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_image_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_image_status_sent.setImageTintList(seenColors);
                        }
                        break;
                }
                holder.rlImageLayout.setVisibility(View.VISIBLE);
                currentView = holder.rlImageLayout;
                holder.txtMsgLyt.setVisibility(View.GONE);
                holder.rlAudioLayout.setVisibility(View.GONE);
                holder.rlVideoLayout.setVisibility(View.GONE);
                holder.rlDocLayout.setVisibility(View.GONE);
                holder.rlImageLayout.setTag(model);
                holder.ivImage.setImageBitmap(null);
                ImageUtil.setBackground(holder.rlImageLayout, activity, (model.getMessageSentOrReceived() == 0) ? R.drawable.chat_rounded_cornner : R.drawable.chat_rounded_cornner_white);
                if (model.getMessageSentOrReceived() == 1) {
                    try {
                        String key = model.getMediaLink();
                        if (key.contains("amazonaws.com/"))
                            key = model.getMediaLink().split("amazonaws.com/")[1];
                        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                        if (file.exists()) {
                            Uri imageUri = Uri.fromFile(file);
                            Glide.with(activity)
                                    .load(imageUri)
                                    .into(holder.ivImage);
                            model.setFileUpload(true);
                            model.setFileUploading(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (TextUtils.isEmpty(model.getMediaLink())) {
                        if (model.getFilePath() != null) {
                            File file = new File(model.getFilePath());
                            if (file.exists()) {
                                Uri imageUri = Uri.fromFile(file);
                                Glide.with(activity)
                                        .load(imageUri)
                                        .into(holder.ivImage);
                            }
                        }
                    } else {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                        File file = new File(model.getFilePath());
                        if (file.exists()) {
                            Uri imageUri = Uri.fromFile(file);
                            Glide.with(activity)
                                    .load(imageUri)
                                    .into(holder.ivImage);
                        } else {
                            Glide.with(activity)
                                    .load(model.getMediaLink())
                                    .into(holder.ivImage);
                        }
                    }
                }
                holder.pbImage.setVisibility(View.GONE);
//                if (model.getMode().equalsIgnoreCase("1")) {
                holder.ivUploadDownload.setVisibility(View.GONE);
                if (!model.isFileUpload()) {
                    holder.ivUploadDownload.setVisibility(View.VISIBLE);
                    holder.ivUploadDownload.setOnClickListener(this);
                    if (model.getMessageSentOrReceived() == 0) {
                        holder.ivUploadDownload.setRotation((float) 0);
                    } else {
                        holder.ivUploadDownload.setRotation((float) 180);
                    }
                    holder.ivUploadDownload.setTag(model);
                } else {
                    holder.ivUploadDownload.setVisibility(View.GONE);
                }
                if (model.isFileUploading()) {
                    holder.ivUploadDownload.setVisibility(View.GONE);
                    holder.pbImage.setVisibility(View.VISIBLE);
                } else {
                    holder.pbImage.setVisibility(View.GONE);
                    if (model.isFileUpload())
                        holder.ivUploadDownload.setVisibility(View.GONE);
                    else {
                        holder.ivUploadDownload.setVisibility(View.VISIBLE);
                    }
                }
                holder.txtImageTime.setText(model.getTime());
            } else if (model.getMimeType().contains("video")) {
                switch (model.getSeenstatus()) {
                    case -1:
                        holder.img_chat_video_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_video_status_sent.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_video_status_sent.setImageTintList(deliverColors);
                        }
                        break;
                    case 0:
                        holder.img_chat_video_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_video_status_sent.setImageResource(R.drawable.ic_done_black);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_video_status_sent.setImageTintList(deliverColors);
                        }
                        break;
                    case 1:
                        holder.img_chat_video_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_video_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_video_status_sent.setImageTintList(deliverColors);
                        }
                        break;

                    case 2:
                        holder.img_chat_video_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_video_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_video_status_sent.setImageTintList(seenColors);
                        }
                        break;
                }
                holder.rlVideoLayout.setTag(model);
                holder.rlVideoLayout.setVisibility(View.VISIBLE);
                currentView = holder.rlVideoLayout;
                holder.txtMsgLyt.setVisibility(View.GONE);
                holder.rlImageLayout.setVisibility(View.GONE);
                holder.rlAudioLayout.setVisibility(View.GONE);
                holder.rlDocLayout.setVisibility(View.GONE);
                holder.pbVideo.setVisibility(View.GONE);
                ImageUtil.setBackground(holder.rlVideoLayout, activity, (model.getMessageSentOrReceived() == 0) ? R.drawable.chat_rounded_cornner : R.drawable.chat_rounded_cornner_white);
                holder.txtVideoTime.setText(model.getTime());

                if (model.getMessageSentOrReceived() == 1) {
                    String key = model.getMediaLink();
                    if (key.contains("amazonaws.com/"))
                        key = model.getMediaLink().split("amazonaws.com/")[1];
                    if (key.contains(".MOV")) {
                        key = key.split(".MOV")[0] + ".mp4";
                    }
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                    if (file.exists()) {
                        new loadVideoThumbFromFilePath(file.getAbsolutePath(), activity, holder.ivVideoThumb).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        Glide.with(activity).load(createThumbnailAtTime(file.getAbsolutePath(),1))
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(holder.ivVideoThumb);
//                        Bitmap bitmap = retriveVideoFrameFromVideo(file.getAbsolutePath());
//                        Bitmap bitmap = createThumbnailAtTime(file.getAbsolutePath(), 1);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
//                        holder.ivVideoThumb.setImageBitmap(bitmap);
                        //   new ThumbnailExtract(file.getAbsolutePath(), holder.ivVideoThumb).execute();

                       /* BitmapPool bitmapPool = Glide.get(activity).getBitmapPool();
                        int microSecond = 6000000;// 6th second as an example
                        VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond);
                        FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
                        Glide.with(activity)
                                .load(Uri.fromFile(new File(file.getAbsolutePath())))
                                .asBitmap()
                                .override(50, 50)// Example
                                .videoDecoder(fileDescriptorBitmapDecoder)
                                .into(holder.ivVideoThumb);
*/
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                } else {
                    if (TextUtils.isEmpty(model.getMediaLink())) {
                        if (model.getFilePath() != null) {
                            File file = new File(model.getFilePath());
                            if (file.exists()) {
                                new loadVideoThumbFromFilePath(model.getFilePath(), activity, holder.ivVideoThumb).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        }
                    } else {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                    new loadVideoThumbFromFilePath(model.getFilePath(), activity, holder.ivVideoThumb).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    Glide.with(activity).load(createThumbnailAtTime(model.getFilePath(),1))
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(holder.ivVideoThumb);
                  /*  try {
                        Bitmap bitmap = createThumbnailAtTime(model.getFilePath(), 1);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                        holder.ivVideoThumb.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                   /* BitmapPool bitmapPool = Glide.get(activity).getBitmapPool();
                    int microSecond = 6000000;// 6th second as an example
                    VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond);
                    FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
                    Glide.with(activity)
                            .load(Uri.fromFile(new File(model.getFilePath())))
                            .asBitmap()
                            .override(50, 50)// Example
                            .videoDecoder(fileDescriptorBitmapDecoder)
                            .into(holder.ivVideoThumb);
*/
                }
                holder.txtVideoDuration.setText(model.getDurationTime());
                if (model.isFileUpload()) {
                    holder.ivVideoUploadDownload.setVisibility(View.GONE);
                    holder.ivPlayVideo.setVisibility(View.VISIBLE);
                } else {
                    holder.ivVideoUploadDownload.setVisibility(View.VISIBLE);
                    holder.ivVideoUploadDownload.setOnClickListener(this);
                    if (model.getMessageSentOrReceived() == 0) {
                        holder.ivVideoUploadDownload.setRotation((float) 0);
                    } else {
                        holder.ivVideoUploadDownload.setRotation((float) 180);
                    }
                    holder.ivVideoUploadDownload.setTag(model);
                }

                if (model.isFileUploading()) {
                    holder.ivPlayVideo.setVisibility(View.GONE);
                    holder.ivVideoUploadDownload.setVisibility(View.GONE);
                    holder.pbVideo.setVisibility(View.VISIBLE);
                } else {
                    holder.pbVideo.setVisibility(View.GONE);
                    if (model.isFileUpload()) {
                        holder.ivPlayVideo.setVisibility(View.VISIBLE);
                        holder.ivVideoUploadDownload.setVisibility(View.GONE);
                    } else {
                        holder.ivPlayVideo.setVisibility(View.GONE);
                        holder.ivVideoUploadDownload.setVisibility(View.VISIBLE);
                    }
                }

            } else if (model.getMimeType().contains("audio")) {
                switch (model.getSeenstatus()) {
                    case -1:
                        holder.img_chat_audio_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_audio_status_sent.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_audio_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 0:
                        holder.img_chat_audio_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_audio_status_sent.setImageResource(R.drawable.ic_done_black);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_audio_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 1:
                        holder.img_chat_audio_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_audio_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_audio_status_sent.setImageTintList(textColors);
                        }
                        break;

                    case 2:
                        holder.img_chat_audio_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_audio_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_audio_status_sent.setImageTintList(seenColors);
                        }
                        break;
                }
                holder.rlAudioLayout.setVisibility(View.VISIBLE);
                holder.rlAudioLayout.setTag(model);
                currentView = holder.rlAudioLayout;
                holder.txtMsgLyt.setVisibility(View.GONE);
                holder.rlImageLayout.setVisibility(View.GONE);
                holder.rlVideoLayout.setVisibility(View.GONE);
                holder.rlDocLayout.setVisibility(View.GONE);
                holder.pbAudio.setVisibility(View.GONE);
                ImageUtil.setBackground(holder.rlAudioLayout, activity, (model.getMessageSentOrReceived() == 0) ? R.drawable.chat_rounded_cornner : R.drawable.chat_rounded_cornner_white);
                holder.txtAudioTime.setText(model.getTime());
                holder.rlAudioPlayLayout.setTag(holder);
                if (model.getMessageSentOrReceived() == 1) {
                    File file = new File(model.getMediaLink());
                    model.setFileName(file.getName());
                } else {
                    File file = new File(model.getFilePath());
                    model.setFileName(file.getName());
                }
                if (model.getMessageSentOrReceived() == 1) {
                    String key = model.getMediaLink();
                    if (key.contains("amazonaws.com/"))
                        key = model.getMediaLink().split("amazonaws.com/")[1];
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                    if (file.exists()) {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                } else {
                    if (TextUtils.isEmpty(model.getMediaLink())) {
                    } else {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                }

                if (model.isFileUpload()) {
                    holder.ivAudioUploadDownload.setVisibility(View.GONE);
                } else {
                    holder.ivAudioUploadDownload.setVisibility(View.VISIBLE);
                    holder.ivAudioUploadDownload.setOnClickListener(this);
                    if (model.getMessageSentOrReceived() == 0) {
                        holder.ivAudioUploadDownload.setRotation((float) 0);
                    } else {
                        holder.ivAudioUploadDownload.setRotation((float) 180);
                    }
                    holder.ivAudioUploadDownload.setTag(model);
                }
                if (model.isFileUploading()) {
                    holder.ivAudioUploadDownload.setVisibility(View.GONE);
                    holder.pbAudio.setVisibility(View.VISIBLE);
                } else {
                    holder.pbAudio.setVisibility(View.GONE);
                    if (model.isFileUpload())
                        holder.ivAudioUploadDownload.setVisibility(View.GONE);
                    else {
                        holder.ivAudioUploadDownload.setVisibility(View.VISIBLE);
                    }
                }
                holder.txtAudioDuration.setText(model.getDurationTime());

            } else {
                switch (model.getSeenstatus()) {
                    case -1:
                        holder.img_chat_doc_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_doc_status_sent.setImageResource(R.drawable.ic_access_alarms_black_24dp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_doc_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 0:
                        holder.img_chat_doc_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_doc_status_sent.setImageResource(R.drawable.ic_done_black);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_doc_status_sent.setImageTintList(textColors);
                        }
                        break;
                    case 1:
                        holder.img_chat_doc_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_doc_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_doc_status_sent.setImageTintList(textColors);
                        }
                        break;

                    case 2:
                        holder.img_chat_doc_status_sent.setVisibility(View.VISIBLE);
                        holder.img_chat_doc_status_sent.setImageResource(R.drawable.double_tick);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.img_chat_doc_status_sent.setImageTintList(seenColors);
                        }
                        break;
                }
                holder.rlDocLayout.setTag(model);
                holder.rlDocLayout.setVisibility(View.VISIBLE);
                currentView = holder.rlDocLayout;
                holder.txtMsgLyt.setVisibility(View.GONE);
                holder.rlImageLayout.setVisibility(View.GONE);
                holder.rlAudioLayout.setVisibility(View.GONE);
                holder.rlVideoLayout.setVisibility(View.GONE);
                holder.pbDoc.setVisibility(View.GONE);
                holder.ivDocTime.setText(model.getTime());
                if (model.getFileName().contains("."))
                    holder.tvDocType.setText(model.getFileName().substring(model.getFileName().lastIndexOf(".")).substring(1));
                if (model.getFileSize() > 0) {
                    String fileSize = Utils.getFileSize(model.getFileSize());
                    holder.txtDocSize.setText(fileSize);
                }
                if (model.getMessageSentOrReceived() == 1) {
                    File file = new File(model.getMediaLink());
                    model.setFileName(file.getName());
                } else {
//                File file = new File(model.getFilePath());
                    model.setFileName(model.getFileName());
                }
                holder.ivDocName.setText(model.getFileName());
                ImageUtil.setBackground(holder.rlDocLayout, activity, (model.getMessageSentOrReceived() == 0) ? R.drawable.chat_rounded_cornner : R.drawable.chat_rounded_cornner_white);
                if (model.getMessageSentOrReceived() == 1) {
                    String key = model.getMediaLink();
                    if (key.contains("amazonaws.com/"))
                        key = model.getMediaLink().split("amazonaws.com/")[1];
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                    if (file.exists()) {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                } else {
                    if (TextUtils.isEmpty(model.getMediaLink())) {
                    } else {
                        model.setFileUpload(true);
                        model.setFileUploading(false);
                    }
                }
                if (model.isFileUpload()) {
                    holder.ivDocUploadDownload.setVisibility(View.GONE);
                } else {
                    holder.ivDocUploadDownload.setVisibility(View.VISIBLE);
                    holder.ivDocUploadDownload.setOnClickListener(this);
                    if (model.getMessageSentOrReceived() == 0) {
                        holder.ivDocUploadDownload.setRotation((float) 0);
                    } else {
                        holder.ivDocUploadDownload.setRotation((float) 180);
                    }
                    holder.ivDocUploadDownload.setTag(model);
                }
                if (model.isFileUploading()) {
                    holder.ivDocUploadDownload.setVisibility(View.GONE);
                    holder.pbDoc.setVisibility(View.VISIBLE);
                } else {
                    holder.pbDoc.setVisibility(View.GONE);
                    if (model.isFileUpload())
                        holder.ivDocUploadDownload.setVisibility(View.GONE);
                    else {
                        holder.ivDocUploadDownload.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (currentView != null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ;
                if (model.getMessageSentOrReceived() == 0) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    if (model.getMimeType().equals("audio") || model.getMimeType().equals("doc")) {
                        layoutParams.setMargins(45, 20, 25, 0);
                    } else {
                        layoutParams.setMargins(0, 20, 25, 0);
                    }

                } else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    if (model.getMimeType().equals("audio") || model.getMimeType().equals("doc")) {
                        layoutParams.setMargins(25, 20, 45, 0);
                    } else {
                        layoutParams.setMargins(25, 20, 0, 0);
                    }
                }
                currentView.setLayoutParams(layoutParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ThumbnailExtract extends AsyncTask<String, long[], Bitmap> {

        private final String videoUrl;
        private final ImageView mThumbnail;

        public ThumbnailExtract(String videoLocalUrl, ImageView thumbnail) {
            this.videoUrl = videoLocalUrl;
            mThumbnail = thumbnail;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmap(videoUrl);
            //return ThumbnailUtils.createVideoThumbnail(videoUrl,
            //    MediaStore.Images.Thumbnails.MINI_KIND);
        }

        @Override
        protected void onPostExecute(Bitmap thumb) {
            if (thumb != null) {
                mThumbnail.setImageBitmap(thumb);
            } else {
                BitmapPool bitmapPool = Glide.get(activity).getBitmapPool();
                int microSecond = 6000000;// 6th second as an example
                VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond);
                FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
                Glide.with(activity)
                        .load(videoUrl)
                        .asBitmap()
                        .override(50, 50)// Example
                        .videoDecoder(fileDescriptorBitmapDecoder)
                        .into(mThumbnail);
            }
        }

        private Bitmap getBitmap(String fileUrl) {
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                FileInputStream inputStream = new FileInputStream(fileUrl);
                mediaMetadataRetriever.setDataSource(inputStream.getFD());
                bitmap = mediaMetadataRetriever.getFrameAtTime(1 * 1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            } catch (Exception e) {
                e.printStackTrace();
                // throw new Throwable(
//                        "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                                + e.getMessage());

            } finally {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            }
            if (bitmap == null) {
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileUrl), 40, 40);
            }
            if (bitmap == null) {
                bitmap = ThumbnailUtils.createVideoThumbnail(fileUrl, MediaStore.Images.Thumbnails.MICRO_KIND);
            }
            if (bitmap == null) {
                bitmap = ThumbnailUtils.createVideoThumbnail(fileUrl, MediaStore.Video.Thumbnails.MINI_KIND);
            }
            return bitmap;
            // return bitmap != null ? Bitmap.createScaledBitmap(bitmap, 40, 40, false) : bitmap;
        }
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
//        MediaMetadataRetriever mediaMetadataRetriever = null;
//        try {
//            mediaMetadataRetriever = new MediaMetadataRetriever();
//            if (Build.VERSION.SDK_INT >= 14)
//                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
//            else
//                mediaMetadataRetriever.setDataSource(videoPath);
//            //   mediaMetadataRetriever.setDataSource(videoPath);
//            bitmap = mediaMetadataRetriever.getFrameAtTime();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
//        } finally {
//            if (mediaMetadataRetriever != null) {
//                mediaMetadataRetriever.release();
//            }
//        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return TrovaChat.chatList == null ? 0 : TrovaChat.chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //        private ImageView sendCallout;
//        private ImageView recvCallout;
        private RelativeLayout txtMsgLyt;
        private TextView txtMessage;
        private TextView chatTime;
        private LinearLayout llTextMessageStatus;
        private RelativeLayout rlImageLayout;
        private ImageView ivImage;
        private ProgressBar pbImage;
        private ImageView ivUploadDownload;
        private TextView txtImageTime;
        private LinearLayout llImageStatus;
        private RelativeLayout rlAudioLayout;
        private RelativeLayout rlAudioInnerLayout;
        private RelativeLayout rlAudioPlayPauseLayout;
        private TextView txtAudioDuration;
        private ImageView ivAudioImage;
        private RelativeLayout rlAudioUploadDownloadLayout;
        private ProgressBar pbAudio;
        private ImageView ivAudioUploadDownload;
        private RelativeLayout rlAudioPlayLayout;
        private ImageView ivAudioPlay;
        private ImageView ivAudioPause;
        private SeekBar sbAudio;
        private TextView txtAudioTime;
        private LinearLayout llAudioStatus;
        private TextView txtAudioSize;
        private RelativeLayout rlVideoLayout;
        private ImageView ivVideoThumb;
        private ProgressBar pbVideo;
        private ImageView ivVideoUploadDownload;
        private ImageView ivPlayVideo;
        private TextView txtVideoDuration;
        private TextView txtVideoTime;
        private LinearLayout llVideoStatus;
        private RelativeLayout rlDocLayout;
        private RelativeLayout rlDocInnerLayout;
        private ImageView ivDocImage;
        private TextView ivDocName;
        private ProgressBar pbDoc;
        private ImageView ivDocUploadDownload;
        private TextView txtDocSize;
        private LinearLayout llDocStatus;
        private TextView ivDocTime;
        private TextView tvDocType;

        private ImageView img_chat_message_status_sent;
        private ImageView img_chat_image_status_sent;
        private ImageView img_chat_audio_status_sent;
        private ImageView img_chat_video_status_sent;
        private ImageView img_chat_doc_status_sent;

        private MyViewHolder(View view) {
            super(view);
//            sendCallout =  view.findViewById(R.id.send_callout);
//            recvCallout =  view.findViewById(R.id.recv_callout);
            txtMsgLyt = view.findViewById(R.id.rlTextLayout);
            txtMessage = view.findViewById(R.id.txtMessage);
            chatTime = view.findViewById(R.id.chat_time);
            llTextMessageStatus = view.findViewById(R.id.llTextMessageStatus);
            rlImageLayout = view.findViewById(R.id.rlImageLayout);
            ivImage = view.findViewById(R.id.ivImage);
            pbImage = view.findViewById(R.id.pbImage);
            ivUploadDownload = view.findViewById(R.id.ivUploadDownload);
            txtImageTime = view.findViewById(R.id.txtImageTime);
            llImageStatus = view.findViewById(R.id.llImageStatus);
            rlAudioLayout = view.findViewById(R.id.rlAudioLayout);
            rlAudioInnerLayout = view.findViewById(R.id.rlAudioInnerLayout);
            rlAudioPlayPauseLayout = view.findViewById(R.id.rlAudioPlayPauseLayout);
            txtAudioDuration = view.findViewById(R.id.txtAudioDuration);
            ivAudioImage = view.findViewById(R.id.ivAudioImage);
            rlAudioUploadDownloadLayout = view.findViewById(R.id.rlAudioUploadDownloadLayout);
            pbAudio = view.findViewById(R.id.pbAudio);
            ivAudioUploadDownload = view.findViewById(R.id.ivAudioUploadDownload);
            rlAudioPlayLayout = view.findViewById(R.id.rlAudioPlayLayout);
            ivAudioPlay = view.findViewById(R.id.ivAudioPlay);
            ivAudioPause = view.findViewById(R.id.ivAudioPause);
            sbAudio = view.findViewById(R.id.sbAudio);
            txtAudioTime = view.findViewById(R.id.txtAudioTime);
            llAudioStatus = view.findViewById(R.id.llAudioStatus);
            txtAudioSize = view.findViewById(R.id.txtAudioSize);
            rlVideoLayout = view.findViewById(R.id.rlVideoLayout);
            ivVideoThumb = view.findViewById(R.id.ivVideoThumb);
            pbVideo = view.findViewById(R.id.pbVideo);
            ivVideoUploadDownload = view.findViewById(R.id.ivVideoUploadDownload);
            ivPlayVideo = view.findViewById(R.id.ivPlayVideo);
            txtVideoDuration = view.findViewById(R.id.txtVideoDuration);
            txtVideoTime = view.findViewById(R.id.txtVideoTime);
            llVideoStatus = view.findViewById(R.id.llVideoStatus);
            rlDocLayout = view.findViewById(R.id.rlDocLayout);
            rlDocInnerLayout = view.findViewById(R.id.rlDocInnerLayout);
            ivDocImage = view.findViewById(R.id.ivDocImage);
            ivDocName = view.findViewById(R.id.ivDocName1);
            pbDoc = view.findViewById(R.id.pbDoc);
            ivDocUploadDownload = view.findViewById(R.id.ivDocUploadDownload);
            txtDocSize = view.findViewById(R.id.txtDocSize);
            llDocStatus = view.findViewById(R.id.llDocStatus);
            ivDocTime = view.findViewById(R.id.ivDocTime);
            tvDocType = view.findViewById(R.id.tvDocType);
            img_chat_message_status_sent = view.findViewById(R.id.img_chat_message_status_sent);
            img_chat_image_status_sent = view.findViewById(R.id.img_chat_image_status_sent);
            img_chat_audio_status_sent = view.findViewById(R.id.img_chat_audio_status_sent);
            img_chat_video_status_sent = view.findViewById(R.id.img_chat_video_status_sent);
            img_chat_doc_status_sent = view.findViewById(R.id.img_chat_doc_status_sent);

            rlImageLayout.setOnClickListener(this);
            rlVideoLayout.setOnClickListener(this);
            rlDocLayout.setOnClickListener(this);
            rlAudioPlayLayout.setOnClickListener(this);
        }


        /**
         * Function to convert milliseconds time to
         * Timer Format
         * Hours:Minutes:Seconds
         */
        public String milliSecondsToTimer(long milliseconds) {
            String finalTimerString = "";
            String secondsString = "";

            // Convert total duration into time
            int hours = (int) (milliseconds / (1000 * 60 * 60));
            int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            // Add hours if there
            if (hours > 0) {
                finalTimerString = hours + ":";
            }

            // Prepending 0 to seconds if it is one digit
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }

            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            // return timer string
            return finalTimerString;
        }


        @Override
        public void onClick(View view) {
            try {
                int i = view.getId();
                if (i == R.id.rlAudioPlayLayout) {
                    final MyViewHolder holder = (MyViewHolder) view.getTag();
                    if (holder.rlAudioLayout.getTag() != null) {
                        final ChatMessageModel model = (ChatMessageModel) holder.rlAudioLayout.getTag();
                        if (model.player == null) {

                            if (commonMediaPlayer != null && commonMediaPlayer.isPlaying()) {
                                commonMediaPlayer.pause();
                                commonivAudioPlay.setVisibility(View.VISIBLE);
                                commonivAudioPause.setVisibility(View.GONE);
                            }

                            Uri fileUri = Uri.fromFile(new File(model.getFilePath()));
                            model.player = MediaPlayer.create(activity, fileUri);
                            model.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {

                                    holder.ivAudioPlay.setVisibility(View.VISIBLE);
                                    holder.ivAudioPause.setVisibility(View.GONE);
                                    holder.sbAudio.setProgress(0);
                                    holder.txtAudioDuration.setText(model.getDurationTime());

                                }
                            });
                            holder.sbAudio.setMax(model.player.getDuration());
                            model.run = new Runnable() {
                                @Override
                                public void run() {
                                    if (model.player.isPlaying()) {
                                        holder.sbAudio.setProgress(model.player.getCurrentPosition());
                                        long currentDuration = model.player.getCurrentPosition();
                                        // Displaying time completed playing
                                        holder.txtAudioDuration.setText("" + milliSecondsToTimer(currentDuration));
                                        model.seekHandler.postDelayed(this, 100);
                                    } else {
                                        model.seekHandler.removeCallbacks(this);
//                                            long totalDuration = model.player.getDuration();
                                        // Displaying Total Duration time
//                                            holder.txtAudioDuration.setText("" + milliSecondsToTimer(totalDuration));
                                    }
                                }
                            };

                            holder.ivAudioPlay.setVisibility(View.GONE);
                            holder.ivAudioPause.setVisibility(View.VISIBLE);

                            holder.sbAudio.setProgress(model.player.getCurrentPosition());
                            model.seekHandler.postDelayed(model.run, 1000);
                            model.player.start();
                            commonMediaPlayer = model.player;
                            commonivAudioPlay = holder.ivAudioPlay;
                            commonivAudioPause = holder.ivAudioPause;
                        } else if (model.player.isPlaying()) {
                            holder.ivAudioPlay.setVisibility(View.VISIBLE);
                            holder.ivAudioPause.setVisibility(View.GONE);
                            model.player.pause();

                            commonMediaPlayer = null;
//                                commonivAudioPlay = null;
//                                commonivAudioPause = null;
                        } else {

                            if (commonMediaPlayer != null && commonMediaPlayer.isPlaying() && commonMediaPlayer != model.player) {
                                commonMediaPlayer.pause();
                                commonivAudioPlay.setVisibility(View.VISIBLE);
                                commonivAudioPause.setVisibility(View.GONE);
                            }
                            model.seekHandler.postDelayed(model.run, 1000);
                            holder.ivAudioPlay.setVisibility(View.GONE);
                            holder.ivAudioPause.setVisibility(View.VISIBLE);
                            model.player.start();

                            commonivAudioPlay = holder.ivAudioPlay;
                            commonivAudioPause = holder.ivAudioPause;

                            commonMediaPlayer = model.player;
                        }

                    }

                } else if (i == R.id.rlImageLayout || i == R.id.rlVideoLayout || i == R.id.rlDocLayout) {
                    if (view.getTag() != null) {

                        if (commonMediaPlayer != null) {
                            commonMediaPlayer.pause();
                        }

                        ChatMessageModel chatMessageModel = (ChatMessageModel) view.getTag();
                        if (chatMessageModel.getMessageSentOrReceived() != 1 && !TextUtils.isEmpty(chatMessageModel.getFilePath())) {
                            File file = (new File(chatMessageModel.getFilePath()));
//                                Toast.makeText(activity,getMimeType(file.getPath()),Toast.LENGTH_SHORT).show();
//                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
//                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
//                                String mimeType = myMime.getMimeTypeFromExtension(fileExt(chatMessageModel.getFilePath()));
////                                Uri photoURI = FileProvider.getUriForFile(activity,
////                                        BuildConfig.APPLICATION_ID + ".provider",
////                                        file);
//                                newIntent.setDataAndType(Uri.fromFile(new File(chatMessageModel.getFilePath())),mimeType);
////                                newIntent.setDataAndType(Uri.parse(file.toString()),mimeType);
//                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                try {
//                                    activity.startActivity(newIntent);
//                                } catch (ActivityNotFoundException e) {
//                                    Toast.makeText(activity, "No handler for this type of file.", Toast.LENGTH_LONG).show();
//                                }
//                                try {
//                                    //launch intent
//                                    Intent i = new Intent(Intent.ACTION_VIEW);
//                                    Uri uri = Uri.fromFile(new File(chatMessageModel.getFilePath()));
//                                    String url = uri.toString();
//
//                                    //grab mime
//                                    String newMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//                                            MimeTypeMap.getFileExtensionFromUrl(url));
//
//                                    i.setDataAndType(uri, newMimeType);
//                                    activity.startActivity(i);
//                                } catch (Exception e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
                            if (getMimeType(chatMessageModel.getFilePath()).equalsIgnoreCase("application/vnd.android.package-archive")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri photoURI = FileProvider.getUriForFile(activity,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file);
                                intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                activity.startActivity(intent);
                            } else {
                                TrovaChat.launchGalleryViewer(activity, file);
                            }
                        } else if (!TextUtils.isEmpty(chatMessageModel.getMediaLink())) {
                            String key = chatMessageModel.getMediaLink();
                            if (key.contains("amazonaws.com/"))
                                key = chatMessageModel.getMediaLink().split("amazonaws.com/")[1];
                            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
                            String mim = getMimeType(chatMessageModel.getFilePath());
                            if (mim != null && mim.equalsIgnoreCase("application/vnd.android.package-archive")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri photoURI = FileProvider.getUriForFile(activity,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file);
                                intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                activity.startActivity(intent);
                            } else {
                                TrovaChat.launchGalleryViewer(activity, file);
                            }
//                                TrovaChat.launchGalleryViewer(activity, file);


//                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
//                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
//                                String mimeType = myMime.getMimeTypeFromExtension(fileExt(chatMessageModel.getMediaLink()).substring(1));
//                                newIntent.setDataAndType(Uri.fromFile(getFile()),mimeType);
//                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                try {
//                                    activity.startActivity(newIntent);
//                                } catch (ActivityNotFoundException e) {
//                                    Toast.makeText(activity, "No handler for this type of file.", Toast.LENGTH_LONG).show();
//                                }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (TextUtils.isEmpty(extension)) {
            String extarray[] = url.split("\\.");
            extension = extarray[extarray.length - 1];
        }
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (type == null) {
            type = "doc";
        }
        return type;
    }

    private Bitmap createThumbnailAtTime(String filePath, int timeInSeconds) {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(filePath);
        //api time unit is microseconds
        return mMMR.getFrameAtTime(timeInSeconds * 1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }


    private class loadVideoThumbFromFilePath extends AsyncTask<Void, Bitmap, Bitmap> {
        String path;
        Activity activity;
        ImageView imageView;

        loadVideoThumbFromFilePath(String path, Activity activity, ImageView imageView) {
            this.path = path;
            this.activity = activity;
            this.imageView = imageView;
        }

        /**
         * Get file path
         */
        public String getPath(Uri uri) {
            String path = "";
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
            return path;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap thumbnail = null;
            if (path != null) {
                thumbnail = ThumbnailUtils.createVideoThumbnail(path,
                        MediaStore.Video.Thumbnails.MINI_KIND);
            }
            if (thumbnail == null) {
                thumbnail = createThumbnailAtTime(path, 2000);
            }
            return thumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
//                Glide.with(activity).load(stream.toByteArray())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into;
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(activity).load(path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }
    }


}