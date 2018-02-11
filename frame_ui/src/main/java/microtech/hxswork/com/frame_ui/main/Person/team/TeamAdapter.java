package microtech.hxswork.com.frame_ui.main.Person.team;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import java.util.List;

import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/11.
 */

public class TeamAdapter extends MultipleRecyclerAdapter {


    protected TeamAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(TeamItem.MY_TEAM, R.layout.item_team);
    }

    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case TeamItem.MY_TEAM:
                final AppCompatImageView item_team_image = helper.getView(R.id.item_team_image);
                final AppCompatTextView  item_team_name= helper.getView(R.id.item_team_name);
                final AppCompatTextView  item_team_leibie= helper.getView(R.id.item_team_leibie);

                final  String name= item.getField(MultipleFields.NAME);
                item_team_name.setText(name);
                final  String statue= item.getField(MultipleFields.STATUE);
                if(statue.equals("1"))
                {
                    item_team_leibie.setText("负责人");
                    item_team_image.setBackgroundResource(R.mipmap.principal);
                }else {
                    item_team_leibie.setText("随访医生");
                    item_team_image.setBackgroundResource(R.mipmap.member);
                }
                break;
            default:
                break;
        }
    }
}
