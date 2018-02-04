package com.peeyemcar.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peeyem.app.R;
import com.peeyemcar.MainActivity;

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

    public String[] colors = new String[]{"#E91E63", "#4CAF50", "#9C27B0", "#F44336", "#3F51B5", "#2196F3", "#673AB7", "#00BCD4", "#009688", "#03A9F4", "#4CAF50", "#733f55", "#FFC107", "#4CAF50"};

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_child, null);
        //new LinearLayout(container.getContext());
        Button ivDashBoard = linearLayout.findViewById(R.id.ivDashBoard);
        TextView txtTle = linearLayout.findViewById(R.id.txtTle);
        View tpview = linearLayout.findViewById(R.id.rlImg);
        //        linearLayout.setBackgroundColor(Color.parseColor(colors[position]));
        switch (position) {
            case 0:
                ivDashBoard.setBackgroundResource(R.drawable.products);
                txtTle.setText("Products");
                txtTle.setTag(position);
                tpview.setTag(position);
                ivDashBoard.setTag(position);
                break;
            case 1:
                ivDashBoard.setBackgroundResource(R.drawable.myvecihle);
                txtTle.setText(mainActivity.getString(R.string.myvechile));
                txtTle.setTag(13);
                tpview.setTag(13);
                ivDashBoard.setTag(13);
                break;
            case 2:
                ivDashBoard.setBackgroundResource(R.drawable.service);
                txtTle.setText("Service");
                txtTle.setTag(1);
                tpview.setTag(1);
                ivDashBoard.setTag(1);
                break;
            case 3:
                ivDashBoard.setBackgroundResource(R.drawable.testdrive);
                txtTle.setText("Test Drive");
                txtTle.setTag(2);
                tpview.setTag(2);
                ivDashBoard.setTag(2);
                break;
            case 4:
                ivDashBoard.setBackgroundResource(R.drawable.breakdown);
                txtTle.setText("Breakdown");
                txtTle.setTag(3);
                tpview.setTag(3);
                ivDashBoard.setTag(3);
                break;
            case 5:
                ivDashBoard.setBackgroundResource(R.drawable.emi);
                txtTle.setText("Emi Calculator");
                txtTle.setTag(4);
                tpview.setTag(4);
                ivDashBoard.setTag(4);
                break;
            case 6:
                ivDashBoard.setBackgroundResource(R.drawable.usedcar);
                txtTle.setText("Car Used");
                txtTle.setTag(5);
                tpview.setTag(5);
                ivDashBoard.setTag(5);
                break;
            case 7:
                ivDashBoard.setBackgroundResource(R.drawable.offer);
                txtTle.setText("Offer");
                txtTle.setTag(6);
                tpview.setTag(6);
                ivDashBoard.setTag(6);
                break;
            case 8:
                ivDashBoard.setBackgroundResource(R.drawable.aboutus);
                txtTle.setText("About");
                txtTle.setTag(7);
                tpview.setTag(7);
                ivDashBoard.setTag(7);
                break;
            case 9:
                ivDashBoard.setBackgroundResource(R.drawable.locateus);
                txtTle.setText("Locate Us");
                txtTle.setTag(8);
                tpview.setTag(8);
                ivDashBoard.setTag(8);
                break;
            case 10:
                ivDashBoard.setBackgroundResource(R.drawable.share);
                txtTle.setText("Share");
                txtTle.setTag(9);
                tpview.setTag(9);
                ivDashBoard.setTag(9);
                break;
            case 11:
                ivDashBoard.setBackgroundResource(R.drawable.help);
                txtTle.setText("Help");
                txtTle.setTag(10);
                tpview.setTag(10);
                ivDashBoard.setTag(10);
                break;
            case 12:
                ivDashBoard.setBackgroundResource(R.drawable.feedback);
                txtTle.setText("FeedBack");
                txtTle.setTag(11);
                tpview.setTag(11);
                ivDashBoard.setTag(11);
                break;
            case 13:
                ivDashBoard.setBackgroundResource(R.drawable.refer_and_earn);
                txtTle.setText(mainActivity.getString(R.string.referearn));
                txtTle.setTag(14);
                tpview.setTag(14);
                ivDashBoard.setTag(14);
                break;
        }
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
