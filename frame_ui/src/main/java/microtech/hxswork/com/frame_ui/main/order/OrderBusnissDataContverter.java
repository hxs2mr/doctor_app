package microtech.hxswork.com.frame_ui.main.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/11/30.业绩
 */

public class OrderBusnissDataContverter extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject  array = JSON.parseObject(getJsonData()).getJSONObject("data");

        //一堆的数据解析
        JSONObject Recovery_data= array.getJSONObject("");//康复情况数据

        //一堆的解析

        JSONObject Disease_data = array.getJSONObject("");//疾病类型数据

        //一堆的解析


        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_BUNINESS)
                .setField("","")//一堆的数据加载
                .build();
        ENTITYES.add(entity);

        return ENTITYES;
    }
}
