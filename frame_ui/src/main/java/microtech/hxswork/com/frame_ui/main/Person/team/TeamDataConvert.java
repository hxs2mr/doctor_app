package microtech.hxswork.com.frame_ui.main.Person.team;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/11.
 */

public class TeamDataConvert extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject obj = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray array = obj.getJSONArray("list");

        final  int size = array.size();
        for(int i =0 ; i < size ; i ++)
        {
            final JSONObject data = array.getJSONObject(i);
            String name = data.getString("name");
            String user_id = data.getString("_id");
            String flage = data.getInteger("isleader")+"";

            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(TeamItem.MY_TEAM)
                    .setField(MultipleFields.NAME,name)
                    .setField(MultipleFields.STATUE,flage)
                    .setField(MultipleFields.USER_ID,user_id)
                    .build();
            ENTITYES.add(entity);
        }
        return ENTITYES;
    }
}
