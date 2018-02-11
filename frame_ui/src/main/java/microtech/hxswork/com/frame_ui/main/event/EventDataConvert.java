package microtech.hxswork.com.frame_ui.main.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

import static microtech.hxswork.com.frame_core.util.TimeUtils.stampToDate;

/**
 * Created by microtech on 2018/1/15.
 */

public class EventDataConvert extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject object = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray array = object.getJSONArray("list");
        int size = array.size();
        for(int i =0 ; i < size ; i ++){
            final JSONObject data = array.getJSONObject(i);
            String title = (String) data.get("title");
            String name="";
            if(data.getJSONObject("body")!=null)
            {
                name = data.getJSONObject("body").getString("name");

            }
            String qita="";
            String user_id="";
            String gov_id = (String) data.get("gov_id");
            int type = (int) data.get("type");
            String times="";
            String message_id = (String) data.get("_id");
            if(data.getJSONObject("times")!=null)
            {
                times = data.getJSONObject("times").getString("$numberLong");
            }
            if(type == 2){
                user_id = (String) data.get("user_id");
            }else if(type == 3)
            {
                user_id =data.getJSONObject("body").getString("msg");
                qita = data.getJSONObject("body").getString("msg")+"#"
                        +data.getJSONObject("body").getString("marking")+"#"
                        +data.getJSONObject("body").getString("address")+"#"
                        +data.getJSONObject("body").getString("phone");
            }else if(type == 1){
                user_id = data.getJSONObject("body").getString("visits_id");
            }else {
                user_id =  new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            }
            times =stampToDate(times,"yyyy-MM-dd");
            String read=data.get("status")+"";
        final MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(EventItemType.EVENT_ITEM)
                .setField(EventFilds.TITLE,title)
                .setField(EventFilds.CONTENT, name)
                .setField(EventFilds.FLAGE, type+"")
                .setField(EventFilds.READ, read)
                .setField(EventFilds.TIME,times)
                .setField(EventFilds.ACC_ID,"")
                .setField(EventFilds.USER_ID,user_id)
                .setField(EventFilds.QITA,qita)
                .setField(EventFilds.MESSAGE_ID,message_id)
                .build();
            ENTITYES.add(entity);
        }
        return ENTITYES;
    }
}
