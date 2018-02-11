package microtech.hxswork.com.frame_ui.main.order;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_ui.R;

import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Nnumber_status;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Nnumber_time;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Number_no;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.mType;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_content;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_image;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_linear;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.order_content_flage;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.recyclerView;

/**
 * Created by microtech on 2017/11/30.签约管理的数据解析
 */

public class OrderQianDataConverter extends DataConVerter{
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
            final JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");

            final JSONArray array  = object.getJSONArray("list");
            final  int size = array.size();
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
            for(int i =0 ; i < size ;i++)
            {
                final JSONObject data = array.getJSONObject(i);
                String name = data.getString("name");
                String sex = data.getString("sex");
                String birthday = data.getString("age");
                String thumb="";
                if( data.getString("avatar")!= null) {
                   thumb = data.getString("avatar");
                }
                String statue = data.getString("effect");
                Nnumber_status = statue;
                String time = data.getString("timeu");
                Nnumber_time = time;
                String number = data.getString("number_no");
                Number_no = number;
                String bin = data.getString("marking");
                String userid = data.getString("user_id");

                String region_id = data.getString("user_id");
                String gov_id = data.getString("gov_id");
                String doctor_id = data.getString("doctor_id");
                String _id =  data.getString("_id");
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(OrderListItemType.ITEM_ORDER_QIAN)
                        .setField(OrderQianFields.NAME,name)
                        .setField(OrderQianFields.SEX,sex)
                        .setField(OrderQianFields.BIN,bin)
                        .setField(OrderQianFields.BIRTHDAY,birthday)
                        .setField(OrderQianFields.THUMB,thumb)
                        .setField(OrderQianFields.STATUE,statue)
                        .setField(OrderQianFields.TIME,time)
                        .setField(OrderQianFields.NUMBER,number)
                        .setField(OrderQianFields.USER_ID,userid)
                        .setField(OrderQianFields.GOV_ID,gov_id)
                        .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                        .setField(OrderQianFields.REGON_ID,region_id)
                        .setField(OrderQianFields._ID,_id)
                        .build();
                ENTITYES.add(entity);
            }
        return ENTITYES;
    }
}
