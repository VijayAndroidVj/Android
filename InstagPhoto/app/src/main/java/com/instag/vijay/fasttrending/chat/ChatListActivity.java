package com.instag.vijay.fasttrending.chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.Db.DataBaseHandler;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.UserModel;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;


/**
 * Created by vijay on 1/5/18.
 */


public class ChatListActivity extends AppCompatActivity implements View.OnClickListener {
    private ContactListAdapter contactListAdapter;
    private DataBaseHandler dataBaseHandler;
    private ArrayList<UserModel> contact_list;
    private Activity activity;
    private PreferenceUtil preferenceUtil;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        activity = this;
        dataBaseHandler = new DataBaseHandler(this);
        preferenceUtil = new PreferenceUtil(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Friends");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        setView();
        setRegisterUI();
        getLists();

    }

    private void getLists() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                final String usermail = new PreferenceUtil(activity).getUserMailId();
                Call<ArrayList<com.instag.vijay.fasttrending.UserModel>> call = service.getallLists(usermail, "friends");
                call.enqueue(new Callback<ArrayList<com.instag.vijay.fasttrending.UserModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<com.instag.vijay.fasttrending.UserModel>> call, Response<ArrayList<com.instag.vijay.fasttrending.UserModel>> response) {
                        ArrayList<com.instag.vijay.fasttrending.UserModel> postModelMain = response.body();
                        if (postModelMain != null) {
//                            postsArrayList = postModelMain.getPostsArrayList();
                            for (com.instag.vijay.fasttrending.UserModel userModel : postModelMain) {
                                UserModel userModel1 = new UserModel();
                                userModel1.setUserId(userModel.getEmail());
                                userModel1.setName(userModel.getName());
                                userModel1.setImage(userModel.getServerimage());
                                userModel1.setTo_token(userModel.getTo_token());
                                //contact_list.add(userModel1);
                                dataBaseHandler.saveFilteredContacts(userModel1);
                            }

                        }
                        fetchContacts();
                        setAdapter();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<com.instag.vijay.fasttrending.UserModel>> call, Throwable t) {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchContacts() {
        try {
//            dataBaseHandler.updateChatStatus(1);
            contact_list = dataBaseHandler.getFilteredContactList();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    private void setAdapter() {
        if (contact_list == null || contact_list.size() == 0) {
            tv_contact_info.setVisibility(VISIBLE);
            rv_contact_list.setVisibility(View.GONE);
        } else {
            tv_contact_info.setVisibility(View.GONE);
            rv_contact_list.setVisibility(VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
            rv_contact_list.setLayoutManager(mLayoutManager);
            rv_contact_list.setItemAnimator(new DefaultItemAnimator());
            contactListAdapter = new ContactListAdapter(activity, contact_list);
            rv_contact_list.setAdapter(contactListAdapter);
        }


    }

    private void setRegisterUI() {
        searchView_contact_list.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                try {
                    if (contactListAdapter != null) {
                        contactListAdapter.filter(newText);
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

                return false;
            }
        }));
        searchView_contact_list.clearFocus();
        searchView_contact_list.setFocusable(false);
    }

    RecyclerView rv_contact_list;
    View tv_contact_info;
    SearchView searchView_contact_list;

    private void setView() {
        rv_contact_list = findViewById(R.id.rv_contact_list);
        tv_contact_info = findViewById(R.id.tv_contact_info);
        searchView_contact_list = findViewById(R.id.searchView_contact_list);

        tv_contact_info.setVisibility(View.GONE);
        rv_contact_list.setVisibility(VISIBLE);

        searchView_contact_list.setQueryHint("Type to search");
        searchView_contact_list.setIconified(false);
        searchView_contact_list.clearFocus();
    }

    public void onClick(@Nullable View view) {

        int i = view.getId();


    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        fetchContacts();
        setAdapter();
    }

}
