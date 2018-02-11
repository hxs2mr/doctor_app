package microtech.hxswork.com.frame_ui.main.Search;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.List;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/7.
 */

public class SearhAdapter extends MultipleRecyclerAdapter {

    protected SearhAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SearchItemType.ITEM_SEARCH,R.layout.item_search);
    }

    public void refresh(List<MultipleItemEntity> data, View view) {
        getData().clear();
        setNewData(data);
        notifyDataSetChanged();
    }
    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case SearchItemType.ITEM_SEARCH:
                final AppCompatTextView  Searchotem_tv= helper.getView(R.id.tv_search_item);
                final  String history = item.getField(OrderQianFields.NAME);
                Searchotem_tv.setText(history);
                break;
                default:
                    break;
        }
    }
}
