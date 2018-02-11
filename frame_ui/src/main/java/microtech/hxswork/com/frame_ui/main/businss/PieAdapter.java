package microtech.hxswork.com.frame_ui.main.businss;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.Person.team.TeamItem;

/**
 * Created by microtech on 2017/12/12.
 */

public class PieAdapter extends MultipleRecyclerAdapter {
    protected PieAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(PieItem.MY_PIE, R.layout.pit_item_end);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case PieItem.MY_PIE:
                final AppCompatTextView item_image = helper.getView(R.id.pie_item_image);
                final AppCompatTextView item_title= helper.getView(R.id.pie_item_title);
                final AppCompatTextView  item_value= helper.getView(R.id.pie_item_value);

                String color = item.getField(PieFileds.COLOR);
                String a[] = color.split(",");
                int r = Integer.parseInt(a[0]);

                int g = Integer.parseInt(a[1]);

                int b = Integer.parseInt(a[2]);
                int end_color = Color.rgb(r, g, b);
                item_image.setBackgroundColor(end_color);
                item_title.setText(item.getField(PieFileds.TITLE).toString());
                int value = item.getField(PieFileds.VALUE);
                int total = item.getField(PieFileds.TOTAL);

                java.text.DecimalFormat   df   =   new   java.text.DecimalFormat("#0.00%")   ;
                System.out.println("##########"+df.format(value/(float)total))   ;
                item_value.setText(df.format(value/(float)total));

            break;
            default:
                break;
        }
    }
}
