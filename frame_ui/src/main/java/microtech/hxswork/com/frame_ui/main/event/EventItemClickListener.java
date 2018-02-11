package microtech.hxswork.com.frame_ui.main.event;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.C;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragmentDelegate;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderItemClickListener;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.sq;

/**
 * Created by microtech on 2017/12/12.
 */

public class EventItemClickListener  extends SimpleClickListener {
    private final SupportFragmentDelegate FRAGMENT;
    private Bundle mArgs=  null;
    private Context context;
    private EventItemClickListener(SupportFragmentDelegate fragment,Context mcontext){
        this.FRAGMENT = fragment;
        this.context = mcontext;
    }
    public static SimpleClickListener create(SupportFragmentDelegate fragment,Context mcontext){
        return new EventItemClickListener(fragment,mcontext);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final String[] a = new String[4];
        AppCompatTextView title = view.findViewById(R.id.event_item_title);
        final LinearLayoutCompat infomation_width_linear = view.findViewById(R.id.infomation_width_linear);
        title.setTextColor(Color.parseColor("#C1CFD6"));

        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getItem(position);
        String flage = entity.getField(EventFilds.FLAGE);
        a[0] =flage+"";
        a[1]=entity.getField(EventFilds.CONTENT);
        a[2]=entity.getField(EventFilds.USER_ID);
        a[3]=entity.getField(EventFilds.QITA);

        mArgs = new Bundle();
        mArgs.putStringArray("data",a);

        final EventOrderFragment fragment = new EventOrderFragment();
        fragment.setArguments(mArgs);
        if(!flage.equals("2"))
        {
            entity.setField(EventFilds.READ,1+"");
        }else {
            entity.setField(EventFilds.READ,3+"");
        }
        if(Integer.parseInt(flage)!=5)
        {
            infomation_width_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    put_net(entity.getField(EventFilds.MESSAGE_ID)+"");
                }
            });
        }

        if(Integer.parseInt(flage) != 2&& Integer.parseInt(flage)!=5&& Integer.parseInt(flage)!=9&& Integer.parseInt(flage)!=0) {

            infomation_width_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FRAGMENT.start(fragment);
                    //update_sql(a[2]);
                }
            });
        }

    }

    private void put_net(String  message_id) {

        List<String> list = new ArrayList<>();
        list.add(message_id);
        RestClent.builder()
                .url("messageSubmit")
                .params("message_id",JSON.toJSON(list))
                .params("gov_id",fristBean.getGov_id())
                .params("status",3)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("点击返回的内容*******"+response);
                    }
                })
                .build()
                .post();
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



    private void update_sql(String user_id)
    {
/*
//修改SQL语句
        String sql = "update event set du = 1 where user_id ="+user_id;
//执行SQL
        sq.execSQL(sql);
*/
    ContentValues values = new ContentValues();
//在values中添加内容
        values.put("du","1");
//修改条件
        String whereClause = "user_id=?";
//修改添加参数
        String[] whereArgs={String.valueOf(user_id)};
//修改
        sq.update("event",values,whereClause,whereArgs);
    }

    private void init_net(final String user_id, final AppCompatTextView event_item_account,String messgae_id) {

       /* System.out.println("region_id*****"+fristBean.getRegion_id());
        System.out.println("gov_id*****"+fristBean.getGov_id());
        System.out.println("doctor_id*****"+userBean.getId());
        System.out.println("user_id*****"+user_id);*/
        if(user_id!=null)
        {
        RestClent.builder()
                .url("confirm_audit")
                .loader(context)
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("user_id",user_id)
                .params("status",2)
                .params("message_id",messgae_id)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("患者申请返回的结果********"+response);
                        event_item_account.setText("已同意");
                        event_item_account.setTextColor(Color.parseColor("#000000"));
                        event_item_account.setBackgroundResource(R.drawable.event_item_shape1);
                        update_sql(user_id);
                    }
                })
                .build()
                .post();
        }else {
            SAToast.makeText(context,"该用户存在信息不全问题").show();
        }

    }
}
