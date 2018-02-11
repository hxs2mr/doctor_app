package microtech.hxswork.com.frame_ui.main.Search;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.storage.LattePreference;

import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.search_xuanzhe;

/**
 * Created by microtech on 2017/12/7.历史搜寻不在本
 */

public class SearchDataConvert extends DataConVerter {
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
         String jsonStr ="";
                if(search_xuanzhe == 1)
                {
                    jsonStr= LattePreference.getCustomAppProfile("search_history1");//用于保存历史记录的患者姓名和id
                }else {
                    jsonStr= LattePreference.getCustomAppProfile("search_history0");//用于保存历史记录的患者姓名和id
                }
        if (!jsonStr.equals("")) {
            final JSONArray array = JSONArray.parseArray(jsonStr);
            final int size = array.size();
            for (int i = 0; i < size; i++) {
                final String historyItemText = array.getString(i);
                String arr[] = historyItemText.split("#");
                String accid = arr[5];
                System.out.println("********数据:"+historyItemText);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(SearchItemType.ITEM_SEARCH)
                        .setField(OrderQianFields.NAME, arr[0])
                        .setField(OrderQianFields.USER_ID,arr[1])
                        .setField(OrderQianFields.REGON_ID,arr[2])
                        .setField(OrderQianFields.GOV_ID,arr[3])
                        .setField(OrderQianFields.DOCTOR_ID,arr[4])
                        .setField(MultipleFields.ACCID,accid)
                        .build();
                ENTITYES.add(entity);
            }
        }
        return ENTITYES;
    }
}
