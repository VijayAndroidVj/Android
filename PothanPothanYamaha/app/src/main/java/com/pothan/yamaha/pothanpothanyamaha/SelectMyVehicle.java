package com.pothan.yamaha.pothanpothanyamaha;

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
import com.pothan.yamaha.pothanpothanyamaha.data.ItemObject;
import com.pothan.yamaha.pothanpothanyamaha.models.CarModel;
import com.pothan.yamaha.pothanpothanyamaha.models.Group;

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

        allItems.add(new ItemObject("YZF R1", R.drawable.yzfr1));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2012_EON_ebrochure_1509590304235.PDF
        allItems.add(new ItemObject("MT-09", R.drawable.mt_09));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2017_Grand_i10_ebrochure_1509590347421.PDF
        allItems.add(new ItemObject("FAZER25", R.drawable.fazer25));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Xcent_ebrochure_1497354964823.pdf
        allItems.add(new ItemObject("FZ25", R.drawable.fz25));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2014_Elite_i20_ebrochure_1509590267760.PDF
        allItems.add(new ItemObject("YZF R15 VER 2.0", R.drawable.yzfr15));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_i20_Active_ebrochure_1509590386222.pdf
        allItems.add(new ItemObject("YZF R15s", R.drawable.yzfr15s));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Verna_ebrochure_1509962453566.pdf
        allItems.add(new ItemObject("FAZER-FI", R.drawable.fazerf1));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Elantra_ebrochure_1509962411161.pdf
        allItems.add(new ItemObject("FZS-FI", R.drawable.fzsf1));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2015_Creta_ebrochure_1507607141789.pdf
        allItems.add(new ItemObject("SZ-RR VER 2.0", R.drawable.szrr));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf

        allItems.add(new ItemObject("SALUTO DISK BRAKE", R.drawable.solutodisk));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        allItems.add(new ItemObject("SALUTO", R.drawable.soluto));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        allItems.add(new ItemObject("SALUTO RX", R.drawable.solutorx));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf

        allItems.add(new ItemObject("FASCINO", R.drawable.fazino));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        allItems.add(new ItemObject("ALPHA", R.drawable.alpha));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        allItems.add(new ItemObject("RAY-Z", R.drawable.rax));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf
        allItems.add(new ItemObject("RAY ZR", R.drawable.rayzr));//http://www.hyundai.com/wcm/upload_wwn/IN/ebrochure/2016_Tucson_ebrochure_1508226350887.pdf


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
