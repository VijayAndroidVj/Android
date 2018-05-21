package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instag.vijay.fasttrending.Db.DataBaseHandler;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.chat.ChatListActivity;
import com.instag.vijay.fasttrending.chat.LogListAdapter;
import com.instag.vijay.fasttrending.model.UserModel;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by vijay on 21/11/17.
 */

public class ChatFragment extends Fragment {

    private LogListAdapter contactListAdapter;
    private DataBaseHandler dataBaseHandler;
    private ChatListActivity contactList;
    private static boolean isAgentLog;
    private static boolean isChat;
    private ArrayList<UserModel> contact_list;
    private static ChatFragment chatFragment;
    private Activity activity;

    public static ChatFragment getInstance() {
        if (chatFragment == null) {
            chatFragment = new ChatFragment();
        }
        return chatFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contact_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        dataBaseHandler = new DataBaseHandler(activity);

        setView(view);
        setRegisterUI();
        fetchContacts();
        setAdapter();
    }

    public void fetchContacts() {
        try {
            DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(activity);
            dataBaseHandler.updateChatStatus(1);
            contact_list = dataBaseHandler.getAllContactDisplayCallLogs();

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
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
            rv_contact_list.setLayoutManager(mLayoutManager);
            rv_contact_list.setItemAnimator(new DefaultItemAnimator());
            contactListAdapter = new LogListAdapter(activity, contact_list);
            rv_contact_list.setAdapter(contactListAdapter);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        fetchContacts();
        setAdapter();
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

    private void setView(View view) {
        rv_contact_list = view.findViewById(R.id.rv_contact_list);
        tv_contact_info = view.findViewById(R.id.tv_contact_info);
        searchView_contact_list = view.findViewById(R.id.searchView_contact_list);

        tv_contact_info.setVisibility(View.GONE);
        rv_contact_list.setVisibility(VISIBLE);
        searchView_contact_list.setQueryHint("Type to search");
        searchView_contact_list.setIconified(false);
        searchView_contact_list.clearFocus();
    }

    public void onClick(@Nullable View view) {

        int i = view.getId();


    }

}