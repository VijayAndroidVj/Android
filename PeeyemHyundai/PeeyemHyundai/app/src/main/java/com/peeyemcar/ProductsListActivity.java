package com.peeyemcar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import com.peeyemcar.adapter.RecyclerViewAdapter;
import com.peeyemcar.data.ItemObject;
import com.peeyem.app.R;

public class ProductsListActivity extends ActionBarActivity {

    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
        setTitle(null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(ProductsListActivity.this);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(ProductsListActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }

    private List<ItemObject> getAllItemList(){

        List<ItemObject> allItems = new ArrayList<ItemObject>();
        allItems.add(new ItemObject("EON", R.drawable.eon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.PDF
        allItems.add(new ItemObject("GRAND i10", R.drawable.grandi10));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.PDF
        allItems.add(new ItemObject("XCENT", R.drawable.xcent));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.pdf
        allItems.add(new ItemObject("ELITE i20", R.drawable.i20));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.PDF
        allItems.add(new ItemObject("i20 ACTIVE", R.drawable.activei20));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.pdf
        allItems.add(new ItemObject("VERNA", R.drawable.verna));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.pdf
        allItems.add(new ItemObject("ELANTRA", R.drawable.elentra));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.pdf
        allItems.add(new ItemObject("CRETA", R.drawable.creta));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("TUCSON", R.drawable.tucson));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        return allItems;
    }
}
