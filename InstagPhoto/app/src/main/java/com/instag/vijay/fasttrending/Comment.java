package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.adapter.CommentsAdapter;
import com.instag.vijay.fasttrending.model.Comments;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 26/11/17.
 */

public class Comment extends AppCompatActivity {

    private EditText edtComment;
    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Activity activity;
    private String post_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        activity = this;

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);


        edtComment = findViewById(R.id.edtComment);
        viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);

        if (getIntent() != null && getIntent().getExtras() != null) {
            post_id = getIntent().getStringExtra("post_id");
            getComments(post_id);
        }

        findViewById(R.id.btnCommentSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment(edtComment.getText().toString().trim());
            }
        });


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

    private ProgressDialog progressDoalog;

    private void initProgress(String title) {
        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage(title);
            progressDoalog.setTitle(activity.getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
            progressDoalog = null;
        }
    }

    private void closeProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.hide();
        progressDoalog = null;
    }

    private void postComment(String comment) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Posting your comment...");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.post_comments(usermail, preferenceUtil.getUserName(), post_id, comment);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        closeProgress();
                        edtComment.setText("");
                        getComments(post_id);
                        Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    String message = t.getMessage();
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else {
                        message = "Failed";
                    }
                    closeProgress();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getComments(String post_id) {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<ArrayList<Comments>> call = service.getcomments(post_id);
                call.enqueue(new Callback<ArrayList<Comments>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Comments>> call, Response<ArrayList<Comments>> response) {
                        commentsArrayList = response.body();
                        setList();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Comments>> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else {
                            message = "Failed";
                        }
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    CommentsAdapter commentsAdapter;
    ArrayList<Comments> commentsArrayList = new ArrayList<>();

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            commentsAdapter = new CommentsAdapter(activity, commentsArrayList);
            recyclerView.setAdapter(commentsAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            commentsAdapter.notifyDataSetChanged();
            if (commentsArrayList.size() == 0) {
                showView(1, "No Comments available");
            } else {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showView(int item, String text) {
        try {
            switch (item) {
                case 0:
                    progressBar.setVisibility(View.VISIBLE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        viewInfo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        viewInfo.setText(text);
                    }
                    break;
                case 2:
                    progressBar.setVisibility(View.GONE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    setList();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
