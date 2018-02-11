package microtech.hxswork.com.frame_ui.main.Search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/7.
 */

public class SearchNewDataConvert extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final  JSONArray array = object.getJSONArray("list");
        final  int size = array.size();
        for(int i =0 ; i < size ;i++)
        {
            JSONObject text  = (JSONObject) array.get(i);
            String name = text.getString("name");
            String _id = text.getString("_id");
            String user_id = text.getString("user_id");
            String region_id = text.getString("region_id");
            String gov_id = text.getString("gov_id");
            String doctor_id = text.getString("doctor_id");
            String phone = text.getString("phone");
            String accid = "";
            String tokent = "";
            final JSONObject im_conment = text.getJSONObject("im_account");
            if (im_conment != null) {
                accid = im_conment.getString("accid");
                tokent = im_conment.getString("token");
            }
            System.out.println("********new数据:"+name);
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(SearchItemType.ITEM_SEARCH)
                    .setField(OrderQianFields.NAME, name)
                    .setField(OrderQianFields.USER_ID, user_id)
                    .setField(OrderQianFields._ID, _id)
                    .setField(OrderQianFields.REGON_ID, region_id)
                    .setField(OrderQianFields.GOV_ID, gov_id)
                    .setField(OrderQianFields.DOCTOR_ID, doctor_id)
                    .setField(MultipleFields.ACCID, accid)
                    .setField(MultipleFields.TOKEN, tokent)
                    .setField(OrderQianFields.PHONE, phone)
                    .build();
            ENTITYES.add(entity);
        }
        return ENTITYES;
    }
}
