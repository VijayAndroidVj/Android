package com.peeyem.honda.peeyemhonda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.peeyem.honda.peeyemhonda.data.ItemObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 31/1/18.
 */

public class SelectMyVehicle extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    SliderLayout mDemoSlider;
    List<ItemObject> allItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmyvehicle);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        allItems = new ArrayList<ItemObject>();
        {

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

        }

        for (ItemObject itemObject : allItems) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(itemObject.getPhoto())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", itemObject.getName());

            mDemoSlider.addSlider(textSliderView);
        }

//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();
        findViewById(R.id.bselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMyVehicle.this, PhoneNumberAuthentication.class);
                intent.putExtra("vehicletype", allItems.get(mDemoSlider.getCurrentPosition()).getName());
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.link_Try_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(SelectMyVehicle.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
