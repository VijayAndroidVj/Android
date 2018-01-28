package com.pace.honda.pacehonda.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pace.honda.pacehonda.MainActivity;
import com.pace.honda.pacehonda.R;


/**
 * Created by vijay on 18/1/18.
 */

public class UltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;
    private MainActivity mainActivity;

    public UltraPagerAdapter(MainActivity mainActivity, boolean isMultiScr) {
        this.isMultiScr = isMultiScr;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String[] colors = new String[]{"#E91E63", "#9C27B0", "#F44336", "#3F51B5", "#2196F3", "#00BCD4", "#009688", "#03A9F4", "#4CAF50", "#FFC107"};

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_child, null);
        //new LinearLayout(container.getContext());
        Button ivDashBoard = linearLayout.findViewById(R.id.ivDashBoard);
        TextView txtTle = linearLayout.findViewById(R.id.txtTle);
        View tpview = linearLayout.findViewById(R.id.rlImg);
        tpview.setTag(position);
        ivDashBoard.setTag(position);
        txtTle.setTag(position);

        txtTle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                mainActivity.moveToActivity(position);

            }
        });
        tpview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                mainActivity.moveToActivity(position);

            }
        });
        ivDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                mainActivity.moveToActivity(position);

            }
        });
//        linearLayout.setBackgroundColor(Color.parseColor(colors[position]));
        switch (position) {
            case 0:
                ivDashBoard.setBackgroundResource(R.drawable.products);
                txtTle.setText("Products");
                break;
            case 1:
                ivDashBoard.setBackgroundResource(R.drawable.service);
                txtTle.setText("Service");
                break;
            case 2:
                ivDashBoard.setBackgroundResource(R.drawable.testdrive);
                txtTle.setText("Test Drive");
                break;
            case 3:
                ivDashBoard.setBackgroundResource(R.drawable.breakdown);
                txtTle.setText("Breakdown");

                break;
            case 4:
                ivDashBoard.setBackgroundResource(R.drawable.emi);
                txtTle.setText("Emi Calculator");
                break;
//            case 5:
//                ivDashBoard.setBackgroundResource(R.drawable.usedcar);
//                txtTle.setText("Car Used");
//                break;
            case 5:
                ivDashBoard.setBackgroundResource(R.drawable.offer);
                txtTle.setText("Offer");
                break;
            case 6:
                ivDashBoard.setBackgroundResource(R.drawable.aboutus);
                txtTle.setText("About");
                break;
            case 7:
                ivDashBoard.setBackgroundResource(R.drawable.locateus);
                txtTle.setText("Locate Us");
                break;
            case 8:
                ivDashBoard.setBackgroundResource(R.drawable.share);
                txtTle.setText("Share");
                break;
            case 9:
                ivDashBoard.setBackgroundResource(R.drawable.help);
                txtTle.setText("Help");
                break;
        }
        container.addView(linearLayout);
//        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        RelativeLayout view = (RelativeLayout) object;
        container.removeView(view);
    }
}
