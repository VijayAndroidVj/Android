package com.example.chandru.myadmin.ExcellReader;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class XslListActivity extends Activity {
    RecyclerView xlsList;
    ArrayList<XslObjectValue> xlsListValue = new ArrayList<>();
    ArrayList<XlsSplitObject> splitObjects = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_document_list);
        if (getIntent() != null) {
            splitObjects = (ArrayList<XlsSplitObject>) getIntent().getSerializableExtra("getListValue");
        }
       // xlsList = (RecyclerView) findViewById(R.id.xlsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xlsList.setLayoutManager(layoutManager);
        setAdapterValue();

    }

    private void setAdapterValue() {
        if (splitObjects != null) {
            for (int i = 0; i < splitObjects.size(); i++) {
                XlsSplitObject xlsSplitObject = splitObjects.get(i);
                String xlsValue = xlsSplitObject.getXslValue;
                String[] arrayValue = xlsValue.split(",");
                XslObjectValue xslObjectValue = new XslObjectValue();
                if(arrayValue.length>0){
                    xslObjectValue.setSno(arrayValue[0].trim());
                }
                if(arrayValue.length>1){
                    xslObjectValue.setaValue(arrayValue[1].trim());
                }
                if(arrayValue.length>2) {
                    xslObjectValue.setbValue(arrayValue[2].trim());
                }
                if(arrayValue.length>3) {
                    xslObjectValue.setcValue(arrayValue[3].trim());
                }
                xlsListValue.add(xslObjectValue);
            }
            if(xlsListValue!=null){
               /* XslsAdapter adapter = new XslsAdapter(this, xlsListValue);
                xlsList.setAdapter(adapter);*/
            }

        }
    }

}
