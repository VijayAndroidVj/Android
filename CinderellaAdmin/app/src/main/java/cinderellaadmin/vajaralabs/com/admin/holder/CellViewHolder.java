package cinderellaadmin.vajaralabs.com.admin.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import cinderellaadmin.vajaralabs.com.admin.R;

/**
 * Created by vijay on 17/1/18.
 */

public class CellViewHolder extends AbstractViewHolder {

    public final TextView cell_textview;
    public final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
        cell_container = (LinearLayout) itemView.findViewById(R.id.cell_container);
    }
}
