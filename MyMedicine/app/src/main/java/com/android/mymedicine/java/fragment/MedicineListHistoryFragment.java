package com.android.mymedicine.java.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.adapter.MedicineListAdapter;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by razin on 24/11/17.
 */

public class MedicineListHistoryFragment extends Fragment {

    MedicineListAdapter medicineListAdapter;
    RecyclerView rv_home_medicine_list;
    ArrayList<MedicineModel> medicineList;
    DbService dbService;
    SearchView searchview_home_medicines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        dbService = new DbService(getActivity());
        rv_home_medicine_list = view.findViewById(R.id.rv_home_medicine_list);
        searchview_home_medicines = view.findViewById(R.id.searchview_home_medicines);
        searchview_home_medicines.clearFocus();
        view.findViewById(R.id.fab_add_medicine).setVisibility(View.GONE);
        setAdapter();

        return view;
    }

    private void setAdapter() {
        medicineList = dbService.getHistoryMedicineList();
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        rv_home_medicine_list.setLayoutManager(LayoutManager);
        rv_home_medicine_list.setItemAnimator(new DefaultItemAnimator());
        rv_home_medicine_list.addItemDecoration(new VerticalSpaceItemDecoration(1));
        medicineListAdapter = new MedicineListAdapter(getActivity(),medicineList);
        rv_home_medicine_list.setAdapter(medicineListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

}
