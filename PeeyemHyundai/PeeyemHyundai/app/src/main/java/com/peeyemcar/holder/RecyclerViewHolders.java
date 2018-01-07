package com.peeyemcar.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peeyem.app.R;
import com.peeyemcar.WebViewActivity;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName;
    public ImageView countryPhoto;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.car_photo);
    }

    @Override
    public void onClick(View view) {
        String pdfURL = "";
        String loadUrl = "";
        if (getPosition() == 0) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=10";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.pdf";
        } else if (getPosition() == 1) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=12";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.pdf";
        } else if (getPosition() == 2) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=14";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.pdf";
        } else if (getPosition() == 3) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=15";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.pdf";
        } else if (getPosition() == 4) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=17";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.pdf";
        } else if (getPosition() == 5) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=16";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.pdf";
        } else if (getPosition() == 6) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=19";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.pdf";
        } else if (getPosition() == 7) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=18";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf";
        } else if (getPosition() == 8) {
            loadUrl = "http://m.hyundai.co.in/mobile/details.aspx?carmodelid=22";
            pdfURL = "http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf";
        }
//        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL)));
        /*Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent in=new Intent(view.getContext(), ViewPDFActivity.class);
        in.putExtra("ID",String.valueOf(getPosition()));
        view.getContext().startActivity(in);*/

        if (!TextUtils.isEmpty(loadUrl)) {
            Intent in = new Intent(view.getContext(), WebViewActivity.class);
            in.putExtra("loadUrl", loadUrl);
            view.getContext().startActivity(in);
        }

    }
}