package cinderellaadmin.vajaralabs.com.admin.holder;

import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import cinderellaadmin.vajaralabs.com.admin.R;

/**
 * Created by vijay on 17/1/18.
 */

public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textview;

    public RowHeaderViewHolder(View p_jItemView) {
        super(p_jItemView);
        row_header_textview = (TextView) p_jItemView.findViewById(R.id.row_header_textview);
    }
}
