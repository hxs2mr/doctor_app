package microtech.hxswork.com.frame_ui.main.sign;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

import static microtech.hxswork.com.frame_ui.main.sign.SignFragment.sign_no_data_lienar;
import static microtech.hxswork.com.frame_ui.main.sign.SignFragment.sign_rv;

/**
 * Created by microtech on 2018/1/8.
 */

public class SignDataConverter extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {

        final JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray index_0 = object.getJSONArray("index_0");//血压
        final JSONArray index_1 = object.getJSONArray("index_1");//血氧
        final JSONArray index_2 = object.getJSONArray("index_2");//体温
        final JSONArray index_3 = object.getJSONArray("index_3");//脉搏
        final JSONArray index_4 = object.getJSONArray("index_4");//血糖
        int size_0 = index_0.size();
        int size_1 = index_1.size();
        int size_2 = index_2.size();
        int size_3 = index_3.size();

        List<MultipleItemEntity> list_0 = new ArrayList<>();
        List<MultipleItemEntity> list_1 = new ArrayList<>();
        List<MultipleItemEntity> list_2 = new ArrayList<>();
        List<MultipleItemEntity> list_3 = new ArrayList<>();
        int item_type = 0;
        String type = "";
        String day = "";
        String status = "";
        String value = "";

        if (size_0 == 0 && size_1 == 0 && size_2 == 0 && size_3 == 0) {
            sign_rv.setVisibility(View.GONE);
            sign_no_data_lienar.setVisibility(View.VISIBLE);
        }
        for (int i0 = 0; i0 < size_0; i0++) {
            JSONObject object0 = index_0.getJSONObject(i0);
            type = object0.getInteger("type") + "";
            day = (String) object0.get("day");
            status = object0.getInteger("status") + "";
            value = (String) object0.get("value");
            final MultipleItemEntity entity0 = MultipleItemEntity.builder()
                    .setField(SignFilds.TYPE, type)
                    .setField(SignFilds.DAY, day)
                    .setField(SignFilds.STATUS, status)
                    .setField(SignFilds.VALUE, value)
                    .build();
            list_0.add(entity0);
        }
        if (size_0 > 0) {
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(SignitemType.SIGN_ITEM_0)
                    .setField(SignitemType.LIST, list_0)
                    .build();
            ENTITYES.add(entity);
        }

        for (int i1 = 0; i1 < size_1; i1++) {
            JSONObject object1 = index_1.getJSONObject(i1);
            type = object1.getInteger("type") + "";
            day = (String) object1.get("day");
            status = object1.getInteger("status") + "";
            value = (String) object1.get("value");
            final MultipleItemEntity entity1 = MultipleItemEntity.builder()
                    .setField(SignFilds.TYPE, type)
                    .setField(SignFilds.DAY, day)
                    .setField(SignFilds.STATUS, status)
                    .setField(SignFilds.VALUE, value)
                    .build();
            list_1.add(entity1);
        }
        if (size_1 > 0) {
            final MultipleItemEntity entity1 = MultipleItemEntity.builder()
                    .setItemType(SignitemType.SIGN_ITEM_1)
                    .setField(SignitemType.LIST, list_1)
                    .build();
            ENTITYES.add(entity1);
        }


        for (int i2 = 0; i2 < size_2; i2++) {
            JSONObject object2 = index_2.getJSONObject(i2);
            type = object2.getInteger("type") + "";
            day = (String) object2.get("day");
            status = object2.getInteger("status") + "";
            value = (String) object2.get("value");
            final MultipleItemEntity entity2 = MultipleItemEntity.builder()
                    .setField(SignFilds.TYPE, type)
                    .setField(SignFilds.DAY, day)
                    .setField(SignFilds.STATUS, status)
                    .setField(SignFilds.VALUE, value)
                    .build();
            list_2.add(entity2);
        }
        if (size_2 > 0) {
            final MultipleItemEntity entity2 = MultipleItemEntity.builder()
                    .setItemType(SignitemType.SIGN_ITEM_2)
                    .setField(SignitemType.LIST, list_2)
                    .build();
            ENTITYES.add(entity2);
        }

        for (int i3 = 0; i3 < size_3; i3++) {
            JSONObject object3 = index_3.getJSONObject(i3);
            type = object3.getInteger("type") + "";
            day = (String) object3.get("day");
            status = object3.getInteger("status") + "";
            System.out.println("****status**" + status);
            value = (String) object3.get("value");
            final MultipleItemEntity entity3 = MultipleItemEntity.builder()
                    .setField(SignFilds.TYPE, type)
                    .setField(SignFilds.DAY, day)
                    .setField(SignFilds.STATUS, status)
                    .setField(SignFilds.VALUE, value)
                    .build();
            list_3.add(entity3);
        }
        if (size_3 > 0) {
            final MultipleItemEntity entity3 = MultipleItemEntity.builder()
                    .setItemType(SignitemType.SIGN_ITEM_3)
                    .setField(SignitemType.LIST, list_3)
                    .build();
            ENTITYES.add(entity3);
        }

        return ENTITYES;
    }
}
