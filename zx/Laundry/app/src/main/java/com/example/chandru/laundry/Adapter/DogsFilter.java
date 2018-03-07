package com.example.chandru.laundry.Adapter;

import android.widget.Filter;

import com.example.chandru.laundry.Pojo.CustomerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 23/2/18.
 */

public class DogsFilter extends Filter {

    AutoCompleteDogsAdapter adapter;
    List<CustomerModel> originalList;
    List<CustomerModel> filteredList;

    public DogsFilter(AutoCompleteDogsAdapter adapter, List<CustomerModel> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final CustomerModel dog : originalList) {
                if (dog.getCustomer_contact1().toLowerCase().contains(filterPattern)) {
                    filteredList.add(dog);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredDogs.clear();
        adapter.filteredDogs.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}