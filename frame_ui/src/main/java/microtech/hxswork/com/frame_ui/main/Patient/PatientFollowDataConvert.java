package microtech.hxswork.com.frame_ui.main.Patient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/14.
 */

public class PatientFollowDataConvert extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {

        final JSONObject obj = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray array = obj.getJSONArray("list");
        int size = array.size();
        for(int i =0; i<size ; i++)
        {
            JSONObject object = array.getJSONObject(i);
            String title =(size-1)-i+1+"";
            String time = object.getString("times");
            String bin = object.getString("desc");
            String content = object.getString("content");
            String xiaoguo = object.getString("effect");
            String zhidao = object.getString("suggest");
            String suifan = object.getString("visitsType");
            String suiji = object.getString("doctor_name");

            String xueya = object.getString("xueya");
            String xueya_flage = object.getString("xueya_flage");

            String xuetan = object.getString("xuetan");
            String xuetan_flage = object.getString("xuetan_flage");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(PatientItemType.PATIENT_FOLLOW)
                    .setField(MultipleFields.TITLE,title)
                    .setField(MultipleFields.TIME,time)
                    .setField(MultipleFields.BIN,bin)
                    .setField(MultipleFields.CONTENT,content)
                    .setField(MultipleFields.EFFECT,xiaoguo)
                    .setField(MultipleFields.GUIDANCE,zhidao)
                    .setField(MultipleFields.FOLLOW,suifan)
                    .setField(MultipleFields.FOLLOW_PEOPLE,suiji)
                    .setField(MultipleFields.PRESSURE,xueya)
                    .setField(MultipleFields.PRESSURE_FLAGE,xueya_flage)
                    .setField(MultipleFields.SUGAR,xuetan)
                    .setField(MultipleFields.SUGAR_FLAGE,xuetan_flage)
                    .build();

            ENTITYES.add(entity);
        }
        return ENTITYES;

    }
}
