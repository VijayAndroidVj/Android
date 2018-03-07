package com.example.chandru.laundry.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.chandru.laundry.AddCustomer;
import com.example.chandru.laundry.Pojo.CustomerModel;
import com.example.chandru.laundry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 23/2/18.
 */

public class AutoCompleteDogsAdapter extends ArrayAdapter<CustomerModel> {

    private final List<CustomerModel> dogs;
    public List<CustomerModel> filteredDogs = new ArrayList<>();
    private AddCustomer addCustomer;

    public AutoCompleteDogsAdapter(Activity context, List<CustomerModel> dogs) {
        super(context, 0, dogs);
        this.dogs = dogs;
        addCustomer = (AddCustomer) context;
    }

    @Override
    public int getCount() {
        return filteredDogs.size();
    }

    @Override
    public Filter getFilter() {
        return new DogsFilter(this, dogs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        CustomerModel dog = filteredDogs.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_dog, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.username);
        TextView ivIcon = (TextView) convertView.findViewById(R.id.usermsobile);
        tvName.setText(dog.getCustomer_name());
        ivIcon.setText(dog.getCustomer_contact1());
        convertView.setTag(dog);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerModel customerModel = (CustomerModel) view.getTag();
                addCustomer.setValues(customerModel);
            }
        });
        return convertView;
    }
}