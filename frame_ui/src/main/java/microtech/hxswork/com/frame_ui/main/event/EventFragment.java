package microtech.hxswork.com.frame_ui.main.event;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.netease.nim.uikit.common.framework.infra.Task;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.LoginHandler;
import microtech.hxswork.com.frame_ui.main.Patient.PatientNextDataConvert;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.EVENT_FRAGMENT;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.ZuoMian_Number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_adapter;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_list_data;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_right;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_sql;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.sq;

/**
 * Created by microtech on 2017/12/11.事件提醒中心
 */
/*
*
*     public static int Apply = 0;//申请
    public static int MESSAGE = 1;//消息
    public static int FOLLOW = 2;//随访
    public static int TASK = 3;//任务
    public static int CRISIS =4;//危机
    */
public class EventFragment extends MiddleFragment implements View.OnClickListener {
    public static RecyclerView event_recyclerView=  null;
    private LinearLayoutCompat event_back_linear = null;
    private AppCompatTextView event_clear = null;
    public static LinearLayoutCompat no_event_linear = null;
    private List<String> list_user_id  ;
    private List<MultipleItemEntity> list_multipe;
    private           List<String> list_message_id;
    @Override
    public Object setLayout() {
        return R.layout.event_fragment;
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        event_recyclerView = bind(R.id.event_recycle);
        event_back_linear = bind(R.id.event_back_linear);
        EVENT_FRAGMENT = getSupportDelegate();
        event_clear = bind(R.id.event_clear);
        no_event_linear = bind(R.id.no_event_linear);
        event_back_linear.setOnClickListener(this);
        event_clear.setOnClickListener(this);
        list_user_id = new ArrayList<>();
        list_multipe = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        event_recyclerView.setLayoutManager(manager);
        event_number_total =0 ;

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {//添加分隔线
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(1)
                        .margin(0, 0)
                        .color(Color.parseColor("#E9F1F5"))
                        .build();
            }
        });
        event_recyclerView.addItemDecoration(itemDecoration);
        //event_recyclerView.addOnItemTouchListener(EventItemClickListener.create(getSupportDelegate(),getContext()));

        System.out.println("历史数据的条数**************："+event_adapter.getData());
        if(event_adapter.getData().size()<=0)
        {
            event_recyclerView.setVisibility(View.GONE);
            no_event_linear.setVisibility(View.VISIBLE);
        }else {
            event_recyclerView.setVisibility(View.VISIBLE);
            event_recyclerView.setAdapter(event_adapter);
            no_event_linear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        ///------------------------------------------------------------测试
       // ShortcutBadger.removeCount(getContext()); //for 1.1.4+
        try {
            ShortcutBadger.removeCountOrThrow(getContext());//移除角标的数目
            ZuoMian_Number = 0;
           //ZuoMian_Number = 0 ;
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.event_back_linear) {
            getSupportDelegate().pop();
        }else if(i == R.id.event_clear){//清空消息

            show(event_clear);
        }

    }


    public void show(View v){
        //实例化建造者
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //设置警告对话框的标题
        builder.setTitle("清空");
        //设置警告显示的图片
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage("您是否需要清空也读消息？");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(event_adapter.getItemCount()<=0)
                {
                    SAToast.makeText(getContext(),"暂无历史消息").show();
                }else {
                    //deleteData();
                    //event_adapter.refresh_delete_data();

                    list_message_id = new ArrayList<>();

                    List<MultipleItemEntity> data = event_adapter.getData();
                    int size = event_adapter.getItemCount();

                    String accid="";
                    System.out.println("event_adapter的大小**************："+size);
                    List<Integer>  list_int = new ArrayList<Integer>();
                    for(int i =0 ; i < size ; i ++)
                    {
                            if(event_adapter.getItem(i).getField(EventFilds.READ).equals("1"))//表示已读
                            {
                                list_int.add(i);
                                list_message_id.add(event_adapter.getItem(i).getField(EventFilds.MESSAGE_ID)+"");
                                if(!event_adapter.getItem(i).getField(EventFilds.ACC_ID).equals("")){
                                    accid = event_adapter.getItem(i).getField(EventFilds.ACC_ID);
                                    delete(accid);//删除数据库---》改为从服务器获取
                            }
                            }
                    }

                    int size1 = list_int.size();
                    for(int j =0 ; j < size1 ; j++)
                    {
                        if(event_adapter.getItem(list_int.get(j)).getField(EventFilds.USER_ID)!=null)
                        {
                            list_user_id.add(event_adapter.getItem(list_int.get(j)).getField(EventFilds.USER_ID).toString());
                        }
                    }

                    int flage_data = 0;
                    for (int k =0; k< size1;k++)
                    {
                        if(list_int.get(k)>0)
                        {
                            event_adapter.remove((list_int.get(k)-flage_data));
                        }else {
                            event_adapter.remove(list_int.get(k));
                        }
                        flage_data ++;
                    }
                    if(size1!=0)
                    {
                        put_delete_qin();
                    }else {
                        SAToast.makeText(getContext(),"暂无已读消息!").show();
                    }

                    if(event_adapter.getItemCount()<=0)
                    {
                        event_recyclerView.setVisibility(View.GONE);
                        no_event_linear.setVisibility(View.VISIBLE);
                    }else {
                        event_recyclerView.setVisibility(View.VISIBLE);
                        no_event_linear.setVisibility(View.GONE);
                    }

                    event_number.setText("0");
                    event_right.setVisibility(View.INVISIBLE);
                }
            }

        });
        //设置“反面”按钮，及点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //设置“中立”按钮，及点击事件
        builder.setNeutralButton("等等看吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //显示对话框
        builder.show();
    }
    private void deleteData(){//清空数据库数据
        sq.execSQL("delete from event");
        sq.close();
    }


    private void put_delete_qin() {
        RestClent.builder()
                .url("messageSubmit")
                .loader(getContext())
                .params("message_id", JSON.toJSON(list_message_id))
                .params("gov_id",fristBean.getGov_id())
                .params("status",2)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        SAToast.makeText(getContext(),"清空已读消息完成!").show();
                        System.out.println("清空返回的内容*******"+response);
                    }
                })
                .build()
                .post();
    }
    private void delete(String accid) {//删除数据
//删除条件
        String whereClause = "accid_id=?";
                String[] whereArgs = {String.valueOf(accid)};
                sq.delete("event",whereClause,whereArgs);
//执行删除
    }
}
