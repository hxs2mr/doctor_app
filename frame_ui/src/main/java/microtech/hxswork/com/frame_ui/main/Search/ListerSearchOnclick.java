package microtech.hxswork.com.frame_ui.main.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.storage.LattePreference;

import static microtech.hxswork.com.frame_ui.main.Search.Search_Activity.search_flage;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.search_xuanzhe;
import static microtech.hxswork.com.frame_ui.main.home_deatils.Follow_Add_Frgamnet.follow_phone;

/**
 * Created by microtech on 2017/12/7.
 */

public class ListerSearchOnclick extends SimpleClickListener {
    private final Activity mActivity;
    private Bundle mArgs=  null;
    private ListerSearchOnclick(Activity activity){
        this.mActivity = activity;
    }
    public static SimpleClickListener create(Activity activity){
        return new ListerSearchOnclick(activity);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MultipleItemEntity entity= (MultipleItemEntity) adapter.getItem(position);
        //Toast.makeText(Frame.getApplicationContext(), "点击"+position, Toast.LENGTH_SHORT).show();
        Intent data = new Intent();
        String[] a= new String[6];
        if(search_flage== 1){//标识点击
            follow_phone = entity.getField(OrderQianFields.PHONE);
            if(search_xuanzhe == 1)
            {
                saveItem1(entity.getField(OrderQianFields.NAME).toString() + "#" + entity.getField(OrderQianFields._ID).toString() + "#" + entity.getField(OrderQianFields.REGON_ID).toString() + "#" +
                        entity.getField(OrderQianFields.GOV_ID).toString() + "#" + entity.getField(OrderQianFields.DOCTOR_ID).toString()+"#"+entity.getField(MultipleFields.ACCID).toString()
                );
                //保存点击的历史记录
                a[0] = entity.getField(OrderQianFields.NAME);
                a[1] = entity.getField(OrderQianFields._ID);
                a[2] = entity.getField(OrderQianFields.REGON_ID);
                a[3] = entity.getField(OrderQianFields.GOV_ID);
                a[4] = entity.getField(OrderQianFields.DOCTOR_ID);
                a[5] = entity.getField(MultipleFields.ACCID);
            }else {
                saveItem0(entity.getField(OrderQianFields.NAME).toString() + "#" + entity.getField(OrderQianFields.USER_ID).toString() + "#" + entity.getField(OrderQianFields.REGON_ID).toString() + "#" +
                        entity.getField(OrderQianFields.GOV_ID).toString() + "#" + entity.getField(OrderQianFields.DOCTOR_ID).toString()+"#"+entity.getField(OrderQianFields.PHONE).toString()
                );
                a[0] = entity.getField(OrderQianFields.NAME);
                a[1] = entity.getField(OrderQianFields.USER_ID);
                a[2] = entity.getField(OrderQianFields.REGON_ID);
                a[3] = entity.getField(OrderQianFields.GOV_ID);
                a[4] = entity.getField(OrderQianFields.DOCTOR_ID);
                a[5] = "";
            }
    }else {
            if(search_xuanzhe == 1)
            {
                a[0] = entity.getField(OrderQianFields.NAME);
                a[1] = entity.getField(OrderQianFields._ID);
                a[2] = entity.getField(OrderQianFields.REGON_ID);
                a[3] = entity.getField(OrderQianFields.GOV_ID);
                a[4] = entity.getField(OrderQianFields.DOCTOR_ID);
                a[5] = entity.getField(MultipleFields.ACCID);
            }else {
                follow_phone = entity.getField(MultipleFields.ACCID);
                a[0] = entity.getField(OrderQianFields.NAME);
                a[1] = entity.getField(OrderQianFields.USER_ID);
                a[2] = entity.getField(OrderQianFields.REGON_ID);
                a[3] = entity.getField(OrderQianFields.GOV_ID);
                a[4] = entity.getField(OrderQianFields.DOCTOR_ID);
                a[5] = entity.getField(MultipleFields.ACCID);
            }
        }
        data.putExtra("Form_search",a);
        mActivity.setResult(999, data);
        mActivity.finish();
    }


    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @SuppressWarnings("unchecked")
    private void saveItem1(String item){     //保存已经搜寻的患者  是点击过后的患者
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {
            List<String> history;
            final String historyStr =
                    LattePreference.getCustomAppProfile("search_history1");
            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                history = JSON.parseObject(historyStr, ArrayList.class);
            }
            history.add(item);
            final String json = JSON.toJSONString(history);
            LattePreference.addCustomAppProfile("search_history1", json);
        }
    }
    @SuppressWarnings("unchecked")
    private void saveItem0(String item){     //保存已经搜寻的患者  是点击过后的患者
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {
            List<String> history;
            final String historyStr =
                    LattePreference.getCustomAppProfile("search_history0");
            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                history = JSON.parseObject(historyStr, ArrayList.class);
            }
            history.add(item);
            final String json = JSON.toJSONString(history);
            LattePreference.addCustomAppProfile("search_history0", json);
        }
    }
}
