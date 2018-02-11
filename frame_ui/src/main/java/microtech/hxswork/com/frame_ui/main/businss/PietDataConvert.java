package microtech.hxswork.com.frame_ui.main.businss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_ui.main.Person.team.TeamItem;

/**
 * Created by microtech on 2017/12/12.
 */

public class PietDataConvert extends DataConVerter{
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray effect = object.getJSONArray("effect");
        final  JSONArray marking = object.getJSONArray("marking");
        final  int size0 = effect.size();
        final  int size1 = marking.size();
        final  int team_total = object.getInteger("team_total");
        final  int total = object.getInteger("total");
        final  int visits_count = object.getInteger("visits_count");

        final List<String> effect_list = new ArrayList<>();
        for(int i =0 ; i < size0; i++)
        {
            JSONObject e_object = effect.getJSONObject(i);
            effect_list.add(e_object.getInteger("_id")+"#"+e_object.getInteger("count"));

        }
        for (int  i =0 ;  i < size1 ; i++) {
            final JSONObject data = marking.getJSONObject(i);
            JSONArray id_arr =data.getJSONArray("_id");
            String[] mak= new String[id_arr.size()];
            int size_mak = id_arr.size();
            for(int k =0 ; k <size_mak ;k++){
                mak[k] = id_arr.getString(k);
            }
            int value = data.getInteger("count");

            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(PieItem.MY_PIE)
                    .setField(PieFileds.ARRAY, mak)
                    .setField(PieFileds.VALUE, value)
                    .setField(PieFileds.TEAM_TOTAL, team_total)
                    .setField(PieFileds.TOTAL, total)
                    .setField(PieFileds.VISITS_COUNT, visits_count)
                    .setField(OrderQianFields.IMAGE_ARRAY, effect_list)
                    .build();
            ENTITYES.add(entity);
        }
        return ENTITYES;
    }
}
