package microtech.hxswork.com.frame_ui.main.order;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.Json;
import microtech.hxswork.com.frame_ui.R;

import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.mType;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_content;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_image;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_linear;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.order_content_flage;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.recyclerView;

/**
 * Created by microtech on 2017/11/30.随访任务
 * "sui_flage":"上面随访",
 "name":"李二",
 "time":"2017/03/22 12时",
 "phone":"18385655626",
 "shen_time":"也随访"
 */

public class OrderFollowUpDataConverter extends DataConVerter{
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");
        JSONArray array = object.getJSONArray("list");
        int size = array.size();
        if(size<=0)
        {
            recyclerView.setVisibility(View.GONE);
            no_order_linear.setVisibility(View.VISIBLE);
            if(mType.equals("qian"))
            {
                no_order_image.setBackgroundResource(R.mipmap.no_qianyue);
                no_order_content.setText("您还没有签约患者");
            }else {
                no_order_image.setBackgroundResource(R.mipmap.no_suifan);
                no_order_content.setText("您还没有创建任务");
            }
        }
        for(int i = 0 ; i < size ; i++){
            final JSONObject data= array.getJSONObject(i);
            String _id = data.getString("_id");
            String gov_id = data.getString("gov_id");
            String region_id = data.getString("region_id");
            String user_id = data.getString("user_id");
            String doctor_id = data.getString("doctor_id");

            String sui_flage = data.getString("visitsType");

            String name = data.getString("name");
            String time = data.getString("timed");
            String shen_time = data.getString("day");
            String phone = data.getString("phone");

            String bin = data.getString("marking");
            String type = data.getString("type");
            //又是一堆的解析

            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP)
                    .setField(OrderQianFields.NAME,name)
                    .setField(OrderQianFields.SUI_FLAGE,sui_flage)
                    .setField(OrderQianFields.TIME,time)
                    .setField(OrderQianFields.PHONE,phone)
                    .setField(OrderQianFields.SHEN_TIME,shen_time)
                    .setField(OrderQianFields.GOV_ID,gov_id)
                    .setField(OrderQianFields.REGON_ID,region_id)
                    .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                    .setField(OrderQianFields._ID,_id)
                    .setField(OrderQianFields.STATUE,type)
                    .build();
            ENTITYES.add(entity);
        }
        return ENTITYES;
    }
}
