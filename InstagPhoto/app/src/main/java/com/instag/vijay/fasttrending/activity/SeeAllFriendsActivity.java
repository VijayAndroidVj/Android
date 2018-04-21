package com.instag.vijay.fasttrending.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.UserModel;
import com.instag.vijay.fasttrending.adapter.FriendsGridAdapter;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.instag.vijay.fasttrending.view.ExpandableHeightGridView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 18/4/18.
 */

public class SeeAllFriendsActivity extends AppCompatActivity {

    private String action = "";
    private Activity activity;
    private ExpandableHeightGridView gv_list;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeallfriends);
        activity = this;
        gv_list = findViewById(R.id.gv_list);
        if (getIntent() != null && getIntent().getExtras() != null) {
            action = getIntent().getStringExtra("action");
        }

        ActionBar actionBar = getSupportActionBar();
        if (action.equalsIgnoreCase("friends")) {
            actionBar.setTitle("Friends List");
        } else if (action.equalsIgnoreCase("following")) {
            actionBar.setTitle("Following Lists");
        } else if (action.equalsIgnoreCase("follower")) {
            actionBar.setTitle("Followers List");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        getLists();
    }

    private void getLists() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                final String usermail = new PreferenceUtil(activity).getUserMailId();
                Call<ArrayList<UserModel>> call = service.getallLists(usermail, action);
                call.enqueue(new Callback<ArrayList<UserModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                        ArrayList<UserModel> postModelMain = response.body();
                        if (postModelMain != null) {
//                            postsArrayList = postModelMain.getPostsArrayList();
                            FriendsGridAdapter friendsGridAdapter = new FriendsGridAdapter(activity, postModelMain);
                            gv_list.setAdapter(friendsGridAdapter);
                        }
                        // showGrid();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Post available";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
