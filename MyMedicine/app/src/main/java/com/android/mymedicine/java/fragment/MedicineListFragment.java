package com.android.mymedicine.java.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.AddMedicine;
import com.android.mymedicine.java.adapter.MedicineListAdapter;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by razin on 24/11/17.
 */

public class MedicineListFragment extends Fragment {

    MedicineListAdapter medicineListAdapter;
    RecyclerView rv_home_medicine_list;
    ArrayList<MedicineModel> medicineList;
    DbService dbService;
    SearchView searchview_home_medicines;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        dbService = new DbService(getActivity());
        rv_home_medicine_list = view.findViewById(R.id.rv_home_medicine_list);
        searchview_home_medicines = view.findViewById(R.id.searchview_home_medicines);
        searchview_home_medicines.clearFocus();
        view.findViewById(R.id.fab_add_medicine).setVisibility(View.VISIBLE);
        view.findViewById(R.id.fab_add_medicine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AddMedicine.class));
            }
        });
        setAdapter();

        return view;
    }

    private void setAdapter() {
        medicineList = new ArrayList<>();
        medicineList = dbService.getNextMedicineList();
        ArrayList<MedicineModel> appendList = dbService.getFutureMedicineList();
        for (int i = 0; i < appendList.size(); i++) {
            medicineList.add(appendList.get(i));
        }
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        rv_home_medicine_list.setLayoutManager(LayoutManager);
        rv_home_medicine_list.setItemAnimator(new DefaultItemAnimator());
        rv_home_medicine_list.addItemDecoration(new VerticalSpaceItemDecoration(1));
        medicineListAdapter = new MedicineListAdapter(getActivity(), medicineList);
        rv_home_medicine_list.setAdapter(medicineListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }
}
