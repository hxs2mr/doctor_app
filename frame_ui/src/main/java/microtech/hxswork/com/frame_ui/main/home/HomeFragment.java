package microtech.hxswork.com.frame_ui.main.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.yokeyword.fragmentation.SupportFragmentDelegate;
import microtech.hxswork.com.frame_core.init.AccountManager;
import microtech.hxswork.com.frame_core.middle.bottom.BottomItemFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.UtilsStyle;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.LoginActivity;
import microtech.hxswork.com.frame_ui.login.LoginFragment;
import microtech.hxswork.com.frame_ui.main.businss.BusinssFragment;
import microtech.hxswork.com.frame_ui.main.dbdatabase.Event_sql;
import microtech.hxswork.com.frame_ui.main.event.EventAdapter;
import microtech.hxswork.com.frame_ui.main.event.EventDataConvert;
import microtech.hxswork.com.frame_ui.main.event.EventFilds;
import microtech.hxswork.com.frame_ui.main.event.EventFragment;
import microtech.hxswork.com.frame_ui.main.event.EventItemType;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.frame_ui.main.order.OrderFragment;
import microtech.hxswork.com.frame_ui.main.update.DownLoadUtils;
import microtech.hxswork.com.frame_ui.main.update.DownloadApk;
import microtech.hxswork.com.frame_ui.nim.DemoCache;
import microtech.hxswork.com.frame_ui.widget.LoginPopuWindow;
import microtech.hxswork.com.frame_ui.widget.SelectDataPopuple;

import static android.content.Context.MODE_PRIVATE;
import static microtech.hxswork.com.frame_core.init.Frame.getApplicationContext;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/11/23.首页
 */

public class HomeFragment extends BottomItemFragment implements View.OnClickListener {
    public static final String ORDER_TYPE = "ORDER_TYPE";
    private LinearLayoutCompat home_qian_linear = null;//签约
    private LinearLayoutCompat home_follow_linear = null;//随访
    private LinearLayoutCompat home_event_linear = null;//事件提醒
    private LinearLayoutCompat home_busniss_linear = null;//业绩
    public static AppCompatTextView qian_number = null;
    public static AppCompatTextView suifan_number = null;
    private AppCompatTextView bussniss_number = null;
    private Bundle mArgs=null;//标签事件
    public static  int mtype= 0;
    public static  int mSearchtype= 0;
    public  static List<MultipleItemEntity> event_list_data = null;
    public static EventAdapter event_adapter = null;
    public static  AppCompatTextView event_number = null;
    public static AppCompatImageView event_right = null;
    public static  int event_number_total=0;
    public static  int home_qian_number_total=0;
    public static  int home_follow_number_total=0;

    public static   Event_sql event_sql;//初始化数据库
    public static  SQLiteDatabase sq;
    public static  int ZuoMian_Number =0 ;
    public static  int search_xuanzhe =0 ;
    public static int nim_flage = 0 ;
    public static   PopupWindow update_pop;
    private LoginPopuWindow menuWindow = null;
    public static Observer<List<IMMessage>> incomingMessageObserver;

    public static SupportFragmentDelegate EVENT_FRAGMENT = null;
    public static String follow_user_id="";

     public  static   SharedPreferences NIM_Flage ;//标识当前用户处于那个界面  如果在聊天界面就不用弹出消息框
    public static SharedPreferences.Editor NIM_Flage_Edit ;//标识当前用户处于那个界面  如果在聊天界面就不用弹出消息框
    private SwipeRefreshLayout home_swipe=null;
    public static SharedPreferences XG_Setting ;//
    public static  SharedPreferences.Editor XG_edit;//
    @Override
    public Object setLayout() {
        return R.layout.chat_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStyle.setStatusBarMode((Activity) getContext(),false);
        mArgs  = new Bundle();
        event_sql = new Event_sql(getContext(),"system.db",null,1);
        sq = event_sql.getWritableDatabase();
        sq = event_sql.getReadableDatabase();

        XG_Setting= getContext().getSharedPreferences("setting", MODE_PRIVATE);
        XG_edit = XG_Setting.edit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        home_qian_linear = bind(R.id.home_qian_linear);
        home_follow_linear = bind(R.id.home_follow_linear);
        home_event_linear = bind(R.id.home_event_linear);
        home_busniss_linear = bind(R.id.home_business_linear);
        bussniss_number = bind(R.id.bussniss_number);
        event_number = bind(R.id.event_number);
        event_right  = bind(R.id.event_right);
        event_list_data  = new ArrayList<>();
        qian_number = bind(R.id.qian_number);
        suifan_number = bind(R.id.suifan_number);
        //home_swipe = bind(R.id.home_swipe);
        home_qian_linear.setOnClickListener(this);
        home_follow_linear.setOnClickListener(this);
        home_event_linear.setOnClickListener(this);
        home_busniss_linear.setOnClickListener(this);

        init_nim_nofiction();
       // initRefreshLayout();
    }

/*    private void initRefreshLayout(){//设置刷新的颜色和款式
        home_swipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        home_swipe.setHorizontalScrollBarEnabled(true);
        //home_swipe.setProgressViewOffset(true,120,300);
    }*/

    private void init_nim_nofiction() {

        //初始化云信的弹框部分
        NIM_Flage= getContext().getSharedPreferences("nim_setting", MODE_PRIVATE);
        NIM_Flage_Edit = NIM_Flage.edit();

        NIM_Flage_Edit.putString("nim_flage", "0");
        NIM_Flage_Edit.putString("nim_acc","0");
        NIM_Flage_Edit.commit();
    }

    private void list_message()
    {
        RestClent.builder()
                .url("messageList")
                .loader(getContext(), LoaderStyle.BallClipRotateIndicator)
                .params("region_id", fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("事件消息列表返回的数据************"+response);
                        final int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200){
                            event_list_data = new EventDataConvert().setJsonData(response).CONVERT();//测试的DataConvert
                            init_event_list();

                            EVENT_FRAGMENT = getSupportDelegate();
                            event_adapter = new EventAdapter(event_list_data,getSupportDelegate());//事件提醒的Adapter
                        }
                    }
                })
                .build()
                .post();
    }
    private void init_event_list(){
        Cursor cursor  = sq.query("event",null,null,null,null,null,null);//本地数据库查询
        cursor.moveToLast();
        System.out.println("*****数据库表中有多少天数据********"+cursor.getCount());
        int read_flage =0 ;
        if(cursor.getCount()>0) {
            do {
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(EventItemType.EVENT_ITEM)
                        .setField(EventFilds.TITLE, cursor.getString(1))
                        .setField(EventFilds.CONTENT, cursor.getString(2))
                        .setField(EventFilds.FLAGE, cursor.getString(3))
                        .setField(EventFilds.USER_ID, cursor.getString(4))
                        .setField(EventFilds.ACC_ID, cursor.getString(5))
                        .setField(EventFilds.TIME, cursor.getString(6))
                        .setField(EventFilds.READ, cursor.getString(7))
                        .setField(EventFilds.QITA, cursor.getString(8))
                        .build();
                if(cursor.getString(7).equals("0"))
                {
                    read_flage++;
                }
                System.out.println("*********标志位：" + cursor.getString(3));
                event_list_data.add(entity);
            } while (cursor.moveToPrevious());

            if(event_number_total>0)
            {
                event_number.setText("" +event_number_total+read_flage);
                event_right.setVisibility(View.VISIBLE);
            }else {
                event_number.setText(read_flage+"");
                if(read_flage == 0)
                {
                    event_right.setVisibility(View.INVISIBLE);
                }else {
                    event_right.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        put_net();
        //init_event_list();//查询数据库操作 所有的消息重服务器获取
        event_adapter = new EventAdapter(event_list_data,getSupportDelegate());//事件提醒的Adapter//放在这防止服务器挂掉
        list_message();//查看患者申请有没有没有收到的数据
        System.out.println("*****服务器最新的版本号****"+fristBean.getVersionname());
        System.out.println("*****当前用用户的版本号****"+app_vierson());
        if(!fristBean.getVersionname().equals(""))
        {
            if(!fristBean.getVersionname().equals(app_vierson()+".0"))
            {
                Dialog_Update_App(getActivity());
            }
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.home_qian_linear) {//签约管理
            mArgs.putString(ORDER_TYPE,"qian");//表示的是这是订单的部分 显示
            StartOrderType();
        }else if(i == R.id.home_follow_linear)//随访任务
        {
            mArgs.putString(ORDER_TYPE,"follow");//表示的是这是订单的部分 显示
            StartOrderType();
        }else if(i == R.id.home_business_linear)//业绩统计
        {
            BusinssFragment fragment = new BusinssFragment();
            getParentFragmen().getSupportDelegate().start(fragment);//父布局跳转 去除底部的导航

        }else if( i== R.id.home_event_linear)//提醒事件
        {
            event_number_total =0;
            event_right.setVisibility(View.INVISIBLE);
            event_number.setText(event_number_total+"");
            EventFragment fragment = new EventFragment();
            getParentFragmen().getSupportDelegate().start(fragment);//父布局跳转 去除底部的导航
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
            put_net();
    }

    private void StartOrderType()
    {
        mtype = 0;
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(mArgs);
        getParentFragmen().getSupportDelegate().startForResult(fragment,103);//父布局跳转 去除底部的导航
    }

    private void put_net()
    {
        RestClent.builder()
                .url("table")
                .loader(getContext())
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .success(new ISuccess() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("********主界面返回**********"+response);
                        final JSONObject object = com.alibaba.fastjson.JSON.parseObject(response).getJSONObject("obj");//解析返回的json数据
                        if(object!=null)
                        {
                            int sig_count= (int) object.get("sig_count");
                            int visits_count= (int) object.get("visits_count");
                            int count = (int) object.get("count");
                            int message_count = (int) object.get("message_count");
                            home_qian_number_total = sig_count;
                            home_follow_number_total = visits_count;
                            event_number_total = message_count;
                            qian_number.setText(sig_count+"");
                            suifan_number.setText(visits_count+"");
                            bussniss_number.setText(count+"");
                            event_number.setText(message_count+"");
                        }

                    }
                })
                .build()
                .post();
    }

    private  int app_vierson()
    {
        PackageManager manager;

        PackageInfo info = null;

        manager = getActivity().getPackageManager();

        try {

            info = manager.getPackageInfo( getActivity().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
        return info.versionCode;

      /*  info.versionName;

        info.packageName;
        info.signatures;*/
    }


    public static void Dialog_Update_App(final Activity mActivity)
    {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        final View imgEntryView = inflater.inflate(R.layout.update_pop, null);  // 加载更新的布局文件
        TextView update_version = imgEntryView.findViewById(R.id.update_version);//最新的版本
        TextView update_size = imgEntryView.findViewById(R.id.update_size);//版本大小
        TextView update_info = imgEntryView.findViewById(R.id.update_info);//版本内容
        TextView update_cancal = imgEntryView.findViewById(R.id.update_cancal);//取消
        TextView update_ok = imgEntryView.findViewById(R.id.update_ok);//确认更新

        update_version.setText(fristBean.getVersionname());
        update_size.setText(fristBean.getSize());
        update_info.setText(fristBean.getUpgradeinfo());
        update_pop= new PopupWindow(imgEntryView);
        update_pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        update_pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha=0.5f;
        mActivity.getWindow().setAttributes(lp);
        update_pop.setBackgroundDrawable(mActivity.getResources().getDrawable(R.color.transparent));
        update_pop.setOutsideTouchable(true);
        update_pop.setFocusable(true);
        update_pop.showAtLocation(imgEntryView, Gravity.CENTER, 0, 0);

        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        update_cancal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha=1.0f;
                mActivity.getWindow().setAttributes(lp);
                update_pop.dismiss();
            }
        });
        update_ok.setOnClickListener(new View.OnClickListener() {//更新操作
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha=1.0f;
                mActivity.getWindow().setAttributes(lp);
                //3.如果手机已经启动下载程序，执行downloadApk。否则跳转到设置界面
                if (DownLoadUtils.getInstance(mActivity).canDownload()) {
                    DownloadApk.downloadApk(mActivity, fristBean.getUpdateurl(), "蜂鸟医生", "蜂鸟医生");
                } else {
                    DownLoadUtils.getInstance(mActivity).skipToDownloadManager();
                }
                update_pop.dismiss();
            }
        });

    }



    private  void show_login_pop()
    {
        menuWindow = new LoginPopuWindow(getContext(), itemsOnClick,getActivity());
        menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        menuWindow.showAtLocation(getView(),
                Gravity.CENTER , 0, 0);
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // menuWindow.dismiss();
            int i = v.getId();

        }
    };
}
