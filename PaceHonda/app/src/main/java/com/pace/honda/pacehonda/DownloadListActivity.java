package com.pace.honda.pacehonda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.pace.honda.pacehonda.adapter.DownloadRecyclerViewAdapter;
import com.pace.honda.pacehonda.data.ItemObject;

import java.util.ArrayList;
import java.util.List;


public class DownloadListActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_list_activity);
        setTitle(null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(DownloadListActivity.this);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        DownloadRecyclerViewAdapter rcAdapter = new DownloadRecyclerViewAdapter(DownloadListActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }

    private List<ItemObject> getAllItemList() {

        List<ItemObject> allItems = new ArrayList<ItemObject>();
        allItems.add(new ItemObject("CB HORNET 160R", R.drawable.cbhornet160r_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.PDF
        allItems.add(new ItemObject("UNICORN 160", R.drawable.unicorn160_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.PDF
        allItems.add(new ItemObject("UNICORN", R.drawable.unicorn_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.pdf
        allItems.add(new ItemObject("SHINESP", R.drawable.shinesp_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.PDF
        allItems.add(new ItemObject("CB SHINE", R.drawable.cb_shine_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.pdf
        allItems.add(new ItemObject("LIVA", R.drawable.liva_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.pdf
        allItems.add(new ItemObject("DREAM YUGA", R.drawable.dream_yuga_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.pdf
        allItems.add(new ItemObject("DREAM NEO", R.drawable.dream_neo_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("CD110", R.drawable.cd110_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf

        allItems.add(new ItemObject("Activa3G", R.drawable.activa3g_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.PDF
        allItems.add(new ItemObject("Grazia", R.drawable.grazia_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.PDF
        allItems.add(new ItemObject("DIO", R.drawable.dio_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.pdf
        allItems.add(new ItemObject("AVIATOR", R.drawable.aviator_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.PDF
        allItems.add(new ItemObject("ACTIVA-I", R.drawable.activa_i_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.pdf
        allItems.add(new ItemObject("ACTIVA125", R.drawable.activa_125_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.pdf
        allItems.add(new ItemObject("CLIQ", R.drawable.cliq));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.pdf


        allItems.add(new ItemObject("GOLDWING", R.drawable.goldwing_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("CBR-1000RR", R.drawable.cbr_1000rr_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("CBR-1000R", R.drawable.cb_1000r_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("CBR-650F", R.drawable.cbr_650f_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("AFRICAN TWIN", R.drawable.africa_twin));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("NAVI", R.drawable.navi_icon));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf

        return allItems;
    }
}
