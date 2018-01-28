package com.pothan.yamaha.pothanpothanyamaha.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pothan.yamaha.pothanpothanyamaha.R;
import com.pothan.yamaha.pothanpothanyamaha.WebViewActivity;


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
            loadUrl = "https://www.honda2wheelersindia.com/CBHornet160R/";
        } else if (getPosition() == 1) {
            loadUrl = "https://www.honda2wheelersindia.com/CBUnicorn160/";
        } else if (getPosition() == 2) {
            loadUrl = "https://www.honda2wheelersindia.com/unicorn/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/unicorn-150.pdf";
        } else if (getPosition() == 3) {
            loadUrl = "https://www.honda2wheelersindia.com/shinesp/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/brochure-shineSP.pdf";
        } else if (getPosition() == 4) {
            loadUrl = "https://www.honda2wheelersindia.com/cbShine/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CB-Shine-Brochure.pdf";
        } else if (getPosition() == 5) {
            loadUrl = "https://www.honda2wheelersindia.com/Livo/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Livo-Brochure.pdf";
        } else if (getPosition() == 6) {
            loadUrl = "https://www.honda2wheelersindia.com/dreamyuga/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Dream-Yuga-Brochure.pdf";
        } else if (getPosition() == 7) {
            loadUrl = "https://www.honda2wheelersindia.com/dreamneo/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/DreamNeoBrochure-Hinglish.pdf";
        } else if (getPosition() == 8) {
            loadUrl = "https://www.honda2wheelersindia.com/cd110dream/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/cd110dream_brochure.pdf";
        } else if (getPosition() == 9) {
            loadUrl = "https://www.honda2wheelersindia.com/activa4g/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/activa-ebrochure.pdf";
        } else if (getPosition() == 10) {
            loadUrl = "https://www.honda2wheelersindia.com/grazia/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/hondaGraziaBrochure.pdf";
        } else if (getPosition() == 11) {
            loadUrl = "https://www.honda2wheelersindia.com/dreamneo/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/DreamNeoBrochure-Hinglish.pdf";
        } else if (getPosition() == 12) {
            loadUrl = "https://www.honda2wheelersindia.com/dio/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/dio_brochure.pdf";
        } else if (getPosition() == 13) {
            loadUrl = "https://www.honda2wheelersindia.com/aviator/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/aviator_brochure.pdf";
        } else if (getPosition() == 14) {
            loadUrl = "https://www.honda2wheelersindia.com/activai/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/activai.pdf";
        } else if (getPosition() == 15) {
            loadUrl = "https://www.honda2wheelersindia.com/goldwing/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Goldwing.pdf";
        } else if (getPosition() == 16) {
            loadUrl = "https://www.honda2wheelersindia.com/cbr1000rr/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CBR1000RR.pdf";
        } else if (getPosition() == 17) {
            loadUrl = "https://www.honda2wheelersindia.com/cb1000r/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CB1000R.pdf";
        } else if (getPosition() == 18) {
            loadUrl = "https://www.honda2wheelersindia.com/cbr650f/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/cbr650fBrochure.pdf";
        } else if (getPosition() == 19) {
            loadUrl = "https://www.honda2wheelersindia.com/africatwin/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/africatwin-Brochure.pdf";
        } else if (getPosition() == 20) {
            loadUrl = "https://www.honda2wheelersindia.com/navi/";
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/navi-brochure.pdf";
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