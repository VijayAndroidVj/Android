package com.peeyem.honda.peeyemhonda.holder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peeyem.honda.peeyemhonda.R;

public class DownloadRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName;
    public ImageView countryPhoto;

    public DownloadRecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
    }

    @Override
    public void onClick(View view) {
        String pdfURL = "";
        if (getPosition() == 0) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/brochuer-hornet.pdf";
        } else if (getPosition() == 1) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CBUnicorn-160-Brochure.pdf";
        } else if (getPosition() == 2) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/unicorn-150.pdf";
        } else if (getPosition() == 3) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/brochure-shineSP.pdf";
        } else if (getPosition() == 4) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CB-Shine-Brochure.pdf";
        } else if (getPosition() == 5) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Livo-Brochure.pdf";
        } else if (getPosition() == 6) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Dream-Yuga-Brochure.pdf";
        } else if (getPosition() == 7) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/DreamNeoBrochure-Hinglish.pdf";
        } else if (getPosition() == 8) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/cd110dream_brochure.pdf";
        } else if (getPosition() == 9) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/activa-ebrochure.pdf";
        } else if (getPosition() == 10) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/hondaGraziaBrochure.pdf";
        } else if (getPosition() == 11) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/DreamNeoBrochure-Hinglish.pdf";
        } else if (getPosition() == 12) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/dio_brochure.pdf";
        } else if (getPosition() == 13) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/aviator_brochure.pdf";
        } else if (getPosition() == 14) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/activai.pdf";
        } else if (getPosition() == 15) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/Goldwing.pdf";
        } else if (getPosition() == 16) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CBR1000RR.pdf";
        } else if (getPosition() == 17) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/CB1000R.pdf";
        } else if (getPosition() == 18) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/cbr650fBrochure.pdf";
        } else if (getPosition() == 19) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/africatwin-Brochure.pdf";
        } else if (getPosition() == 20) {
            pdfURL = "https://www.honda2wheelersindia.com/assets/pdf/navi-brochure.pdf";
        }
        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL)));
        /*Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent in=new Intent(view.getContext(), ViewPDFActivity.class);
        in.putExtra("ID",String.valueOf(getPosition()));
        view.getContext().startActivity(in);*/
    }
}