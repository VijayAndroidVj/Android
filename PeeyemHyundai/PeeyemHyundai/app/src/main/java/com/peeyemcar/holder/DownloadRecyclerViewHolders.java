package com.peeyemcar.holder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peeyem.app.R;


public class DownloadRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView countryName;
    public ImageView countryPhoto;

    public DownloadRecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
    }

    @Override
    public void onClick(View view) {
        String pdfURL ="";
        if(getPosition()==0){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.PDF";
        }else if(getPosition()==1){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.PDF";
        }else if(getPosition()==2){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.PDF";
        }else if(getPosition()==3){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.PDF";
        }else if(getPosition()==4){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.PDF";
        }else if(getPosition()==5){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.PDF";
        }else if(getPosition()==6){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.PDF";
        }else if(getPosition()==7){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.PDF";
        }else if(getPosition()==8){
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.PDF";
        }
        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL)));
        /*Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent in=new Intent(view.getContext(), ViewPDFActivity.class);
        in.putExtra("ID",String.valueOf(getPosition()));
        view.getContext().startActivity(in);*/
    }
}