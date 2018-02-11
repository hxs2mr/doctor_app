package microtech.hxswork.com.frame_ui.main.Patient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.ItemType;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/11.
 */
public class PatientNextDataConvert extends DataConVerter {


    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
        final JSONObject object1 = JSON.parseObject(getJsonData()).getJSONObject("obj");
        final JSONArray array = object1.getJSONArray("list");
        final  int size = array.size();
        for (int i = 0 ; i < size ; i ++) {
            JSONObject object = array.getJSONObject(i);
            String time = object.getString("times");
            String flage = object.getString("type");
            String text="";
            String title ="";
            ArrayList<String> photos = new ArrayList<>();
            int type = 0;
            if (flage.equals("2")) {   //为语音的时候
                type = ItemType.VOICE;
                text = object.getString("title");
                title =  object.getString("title");
            } else if (flage.equals("1")) {//图片的时候

                type = ItemType.IMAGE;
                title =  object.getString("title");
                final JSONArray array_image = object.getJSONArray("image");
                int size1 = array_image.size();
                for (int j =0  ; j< size1 ; j++ )
                {
                    photos.add(array_image.getString(j));
                }

            } else if (flage.equals("0"))//为文本的时候
            {
                text = object.getString("text");
                type = ItemType.TEXT;
            }

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,type)
                    .setField(MultipleFields.IMAGE_ARRAY,photos)
                    .setField(MultipleFields.TIME,time)
                    .setField(MultipleFields.TITLE,title)
                    .setField(MultipleFields.TEXT,text)
                    .build();
            ENTITYES.add(entity);

        }
        return ENTITYES;
    }
}
