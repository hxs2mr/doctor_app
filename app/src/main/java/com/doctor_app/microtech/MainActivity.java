package com.doctor_app.microtech;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.doctor_app.microtech.nim_ti.Nim_TiActivity;
import com.doctor_app.microtech.xinge.XG_System;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import microtech.hxswork.com.frame_core.activity.ProxyActivity;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import microtech.hxswork.com.frame_core.ui.luncher.ILuncherListener;
import microtech.hxswork.com.frame_core.ui.luncher.OnluncherFinishTag;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.Mac;
import microtech.hxswork.com.frame_core.util.token.Token;
import microtech.hxswork.com.frame_ui.launcher.LauncherFragment;
import microtech.hxswork.com.frame_ui.login.ILoginListener;
import microtech.hxswork.com.frame_ui.login.LoginFragment;
import microtech.hxswork.com.frame_ui.login.LoginHandler;
import microtech.hxswork.com.frame_ui.main.ExBottomFragment;
import microtech.hxswork.com.frame_ui.main.event.EventFilds;
import microtech.hxswork.com.frame_ui.main.event.EventItemType;
import microtech.hxswork.com.frame_ui.main.nim.NimActivity;
import microtech.hxswork.com.frame_ui.nim.DemoCache;
import qiu.niorgai.StatusBarCompat;

import static com.tencent.android.tpush.XGPushManager.registerPush;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_USERID;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.event.EventFragment.event_recyclerView;
import static microtech.hxswork.com.frame_ui.main.event.EventFragment.no_event_linear;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.ZuoMian_Number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_adapter;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_right;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.nim_flage;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.sq;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.incomingMessageObserver;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.XG_Setting;

//单activity
public class MainActivity extends ProxyActivity implements ILoginListener,ILuncherListener {
    private static final int BAIDU_READ_PHONE_STATE = 100;
    public   int XG_FLAGE=1;
    public  int NIM_FLAGE=2;
    private  Context context;
    public static SharedPreferences share ;
    public static   String username ="" ;// = share.getString("PushToken", "0");
    int flage = 0;

    public static Mac mac ;
    SharedPreferences NIM_Flage ;//标识当前用户处于那个界面  如果在聊天界面就不用弹出消息框
    String warning="";//
    String speak=""; //
    public static int start_flage ;//=0 ;
    public  static  SharedPreferences login_ti;
    public static SharedPreferences.Editor login_ti_edit;
    public  static  SharedPreferences nim_message_flage;
    public static SharedPreferences.Editor nim_message_flage_edit;

    public int nim_message_number=0;
    public  static  int nim_ti_number=0;
    private WeakHashMap<String,Object> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mac = new Mac(this);
        list  = new WeakHashMap<>();
        //云信模块
        NIM_Flage =  getSharedPreferences("nim_setting", MODE_PRIVATE);
        long time = System.currentTimeMillis();
        start_flage =0 ;
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.hide();
        }
        Frame.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this,true);
        //信鸽模块需要的数据
         context = this;
        share = getSharedPreferences("user", MODE_PRIVATE);

        login_ti = getSharedPreferences("login_ti", MODE_PRIVATE);
        login_ti_edit = login_ti.edit(); //编辑文件

        nim_message_flage = getSharedPreferences("nim", MODE_PRIVATE);
        nim_message_flage_edit = nim_message_flage.edit(); //编辑文件

        new XG_System().initCustomPushNotificationBuilder(context);

        Log.d("TAG","time+++++++++++++:"+(System.currentTimeMillis()-time));
        ///监听互踢机制
        //nof("a");

           init_XG_message();//监听信鸽推送过来的消息


        //if(nim_message_flage.getString("start_flage", "0").equals("0")) {
            init_NIM_message();//监听云信的消息
      //  }
           init_ti();//监听云信互踢

        login_ti_edit.putString("start_flage", "0");
        login_ti_edit.commit();

        initlist();
    }
    @Override
    public MiddleFragment setRootDelegate() {
        return new LauncherFragment();
    }
    @Override
    public void onLoginSuccess() {
        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
        username = share.getString("user_name", "0");
        init_XG(username);//初始化信鸽
        getSupportDelegate().pop();
        getSupportDelegate().startWithPop(new ExBottomFragment());//startWithPop把盏中的上一个fragmen清楚掉
    }
  /* @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }*/
    @Override
    public void OnResgisterSuuecc() {
        Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLuncherFinish(OnluncherFinishTag tag) {
        switch (tag){
            case SINGED://已经登录
                System.out.println("用户已经登录**************");
                username = share.getString("user_name", "0");
                init_XG(username);
                login_star();
                break;
            case NOT_SINGED://没有登录
                System.out.println("用户没有登录******************");
                getSupportDelegate().pop();
                getSupportDelegate().startWithPop(new LoginFragment());//startWithPop把盏中的上一个fragmen清楚掉
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void init_ti(){
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        if (status.wontAutoLogin()) {
                            nim_ti_number++;
                                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                                if(login_ti.getString("start_flage", "0").equals("0")) {
                                    Intent intent = new Intent(MainActivity.this, Nim_TiActivity.class);
                                    startActivity(intent);
                                    login_ti_edit.putString("start_flage", "1");
                                    login_ti_edit.commit();
                            }
                        }
                    }
                }, true);
    }




    private void initlist()
    {
        list.put("version",mac.os_id);
        list.put("device_id",mac.uniqueId);
        list.put("model",mac.phone_id);
        list.put("os","android");
        list.put("mac",mac.getMac()+"");
        list.put("lng",mac.d_j+"");
        list.put("lat",mac.d_w+"");
        System.out.println("version******"+mac.os_id);
        System.out.println("device_id******"+mac.uniqueId);
        System.out.println("model******"+mac.phone_id);
        System.out.println("mac******"+mac.getMac());
        System.out.println("lng*****"+mac.d_j);
        System.out.println("lat******"+mac.d_w);

        new Token(list);
    }

    private void login_star(){
        //指定操作的文件名称
        SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
        String user_name = share.getString("user_name", "0");
        String password = share.getString("password", "0");

        RestClent.builder()//网络请求
                .url("login")//请求的地址
                .params("user_name",user_name)
                .params("password", password)//添加参数
                .loader(this, LoaderStyle.BallClipRotateIndicator)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("********也登录返回的数据**********"+response);
                        int code  = JSON.parseObject(response).getInteger("code");
                        if(code == 200) {
                            LoginHandler.onSignIn1(getApplicationContext(), response);//登录成功之后保存用户信息  达到数据的持久化
                            getSupportDelegate().pop();
                            getSupportDelegate().startWithPop(new ExBottomFragment());//startWithPop把盏中的上一个fragmen清楚掉
                            doLogin();//登录云信
                        }else {
                            getSupportDelegate().pop();
                            getSupportDelegate().startWithPop(new LoginFragment());//startWithPop把盏中的上一个fragmen清楚掉
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onIFailure() {
                        System.out.println("*********请求失败*********");
                    }
                })
                .build()
                .post();
    }
    private  void init_XG(final String name){

        System.out.println("当前的用户名**************："+name);
        XGPushManager.registerPush(context,name,new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int i) {
                System.out.println("******信鸽注册成功*****"+data);
                String token = (String) data;
                init_push_token(token, name);

            }
            @Override
            public void onFail(Object o, int i, String s) {
                System.out.println("******信鸽注册失败*****错误码"+ i + ",错误信息：" + s);

            }
        });
    }

    private void init_push_token(String token,String name) {//上传token
        RestClent.builder()
                .url("push_token")
                .params("device_id",mac.uniqueId)
                .params("push_token",token)
                .params("account",name)
                .params("channl","android")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("上传pushToken返回的结果*******************"+response);
                    }
                })
                .build()
                .post();
    }
    private   void  init_NIM_message()
    {
        if(nim_message_flage.getString("start_flage", "0").equals("1")) {
            if(incomingMessageObserver != null)
            {
                NIMClient.getService(MsgServiceObserve.class)
                        .observeReceiveMessage(incomingMessageObserver, false);
                init_NIM_observe();
            }else {
                init_NIM_observe();
            }
        }else {
            init_NIM_observe();
        }
    }

    private void init_NIM_observe()
    {
        incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        System.out.println("********收到新消息内容*******" + messages.get(messages.size() - 1).getSessionId());//聊天的ID
                        // NIMBean nimBean = new NIMBean(messages.get(messages.size()-1).getContent(),new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        //list_NIM.add(nimBean);
                        if (nim_flage == 0) {
                            //添加角标
                            ZuoMian_Number++;
                            ShortcutBadger.applyCount(context, ZuoMian_Number); //for 1.1.4+
                            try {
                                ShortcutBadger.applyCountOrThrow(context, ZuoMian_Number); //for 1.1.3
                            } catch (ShortcutBadgeException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = messages;
                            handler.handleMessage(msg);
                        }
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }
    private void init_XG_message() {
        XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
            @Override
            public void handleNotify(XGNotifaction xGNotifaction) {
                System.out.println("处理信鸽通*********************知"+xGNotifaction);
                // 获取标签、内容、自定义内容
                String XG_title = xGNotifaction.getTitle();
                String XG_content = xGNotifaction.getContent();
                String XG_customContent = xGNotifaction.getCustomContent();

                //添加角标
                ZuoMian_Number++;
                ShortcutBadger.applyCount(context, ZuoMian_Number); //for 1.1.4+
                try {
                    ShortcutBadger.applyCountOrThrow(context,ZuoMian_Number); //for 1.1.3
                } catch (ShortcutBadgeException e) {
                    e.printStackTrace();
                }
                //XGBean XG = new XGBean(XG_title,XG_content,XG_customContent);
                //list_XG.add(XG);
                Message msg = new Message();
                msg.what=XG_FLAGE;//代表跟新图标的状态
                msg.obj = xGNotifaction;
                handler.sendMessage(msg);
                System.out.println("信鸽通知消息*************:"+XG_title+XG_content+XG_customContent);
                // 其它的处理
                // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                xGNotifaction.doNotify();
               // initCustomPushNotificationBuilder(getBaseContext());
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1://实时监听信鸽的消息
                    XGNotifaction xgNotifaction = (XGNotifaction) msg.obj;
                    String XG_title = xgNotifaction.getTitle();
                    String XG_content = xgNotifaction.getContent();
                    String XG_customContent = xgNotifaction.getCustomContent();
                    String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    String type= JSON.parseObject(XG_customContent).getString("type");

                    String accid="";
                    String qita="";
                    String  user_id="";
                    String name="";
                    name= JSON.parseObject(XG_customContent).getString("name");
                    String message_id ="";// (String) data.get("_id");
                    if(type.equals("2"))
                    {
                        XG_content =  name;
                        user_id = JSON.parseObject(XG_customContent).getString("user_id");
                        message_id= JSON.parseObject(XG_customContent).getString("massage_id");
                    }else if(type.equals("3"))
                    {
                        XG_content =  name+"刚发生了一个危机指标，请及时关注";
                        qita = JSON.parseObject(XG_customContent).getString("msg")+"#"+JSON.parseObject(XG_customContent).getString("marking")+"#"+JSON.parseObject(XG_customContent).getString("address")+"#"+JSON.parseObject(XG_customContent).getString("phone");
                        user_id =JSON.parseObject(XG_customContent).getString("msg");
                        message_id= JSON.parseObject(XG_customContent).getString("massage_id");
                    }else if(type.equals("1"))
                    {
                        XG_content =  name;
                        user_id = JSON.parseObject(XG_customContent).getString("visits_id");
                        message_id= JSON.parseObject(XG_customContent).getString("massage_id");
                    }else if(type.equals("0"))
                    {
                        user_id =XG_content+time;
                    }else if(type.equals("9")){
                        user_id =XG_title+time;
                    }
                    event_number_total++;
                    event_number.setText(""+event_number_total);
                    event_right.setVisibility(View.VISIBLE);

                    if(event_recyclerView!=null)
                    {
                        event_recyclerView.setVisibility(View.VISIBLE);
                        no_event_linear.setVisibility(View.GONE);
                    }
                    final MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(EventItemType.EVENT_ITEM)
                            .setField(EventFilds.TITLE,XG_title)
                            .setField(EventFilds.CONTENT, XG_content)
                            .setField(EventFilds.FLAGE, type)
                            .setField(EventFilds.READ, 0+"")
                            .setField(EventFilds.TIME,time)
                            .setField(EventFilds.ACC_ID,accid)
                            .setField(EventFilds.USER_ID,user_id)
                            .setField(EventFilds.QITA,qita)
                            .setField(EventFilds.MESSAGE_ID,message_id)
                            .build();

                    if(type.equals("3"))
                    {
                        if(XG_Setting.getString("warning", "0").equals("1"))
                        {
                            //switch_wei.setChecked(true);//选中的状态
                            event_adapter.addData(0,entity);//添加新消息到里面
                            //insert_sql(XG_title,XG_content,type,user_id,accid,time,"0",qita);
                        }
                    }else {
                        event_adapter.addData(0,entity);//添加新消息到里面
                        //insert_sql(XG_title,XG_content,type,user_id,accid,time,"0",qita);
                    }
                    break;
                case 2://实时监听云信的消息
                    List<IMMessage> messages = (List<IMMessage>) msg.obj;
                        String thumb = "";
                    messages.get(messages.size()-1).getSessionId();
                    String time1 = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    List<String>  data = new ArrayList<>();
                    int count_deng=-1;
                    for (int i =0 ; i < messages.size();i++)
                    {
                        data.add(messages.get(i).getFromAccount());
                    }
                    List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(data);

                   // System.out.println("**********************"+users.get(0).getAvatar());
                    if(users.size()>0)
                    {
                        if(!users.get(0).getAvatar().equals("")|| !(users.get(0).getAvatar() ==null))
                        {
                            thumb=users.get(0).getAvatar().equals("")+"";
                        }
                    }

                    TIGE_USERID = messages.get(messages.size()-1).getSessionId();
                    String qita_number="1";
                    final MultipleItemEntity entity1 = MultipleItemEntity.builder()
                            .setItemType(EventItemType.EVENT_ITEM)
                            .setField(EventFilds.TITLE, messages.get(messages.size()-1).getFromNick())
                            .setField(EventFilds.CONTENT, messages.get(messages.size()-1).getContent())
                            .setField(EventFilds.FLAGE, 5+"")
                            .setField(EventFilds.READ, 0+"")
                            .setField(EventFilds.TIME,time1)
                            .setField(EventFilds.QITA,qita_number)
                            .setField(EventFilds.ACC_ID,messages.get(messages.size()-1).getSessionId())
                            .setField(EventFilds.IMAGE_URL,thumb)
                            .build();
                    System.out.println("事件提醒的数据数量******"+event_adapter.getItemCount());
                    int nim_item_flage= 0 ;
                    if(event_adapter.getItemCount() >0)
                    {
                        for (int j =0 ; j< event_adapter.getItemCount() ; j++)
                        {
                                if (event_adapter.getItem(j).getField(EventFilds.ACC_ID).equals(messages.get(messages.size() - 1).getSessionId())) {
                                    count_deng= j;
                                }
                        }
                        if(count_deng == -1)
                        {
                            entity1.setField(EventFilds.QITA,qita_number);
                            event_adapter.addData(0, entity1);//添加新消息到里面
                            nim_item_flage = 0;
                        }else {
                            String number_str= event_adapter.getItem(count_deng).getField(EventFilds.QITA);
                            qita_number =  Integer.parseInt(number_str)+1+"";
                            entity1.setField(EventFilds.QITA,qita_number);
                            event_adapter.setData(count_deng, entity1);//添加新消息到里面
                            nim_item_flage = count_deng;
                        }
                    }else {
                        entity1.setField(EventFilds.QITA,qita_number);
                        event_adapter.addData(0,entity1);//添加新消息到里面
                        nim_item_flage = 0;
                        event_adapter.notifyItemChanged(0);
                    }

                    if(event_recyclerView!=null)
                    {
                        if(event_recyclerView.getVisibility()== View.GONE)
                        {
                            event_recyclerView.setVisibility(View.VISIBLE);
                            no_event_linear.setVisibility(View.GONE);
                        }
                    }
                    nim_message_number =0;
                    //先查询有没有这个userid
                    delete(messages.get(messages.size()-1).getSessionId());
                    insert_sql( messages.get(messages.size()-1).getFromNick(),messages.get(messages.size()-1).getContent(),"5",time1,messages.get(messages.size()-1).getSessionId(),time1,"0",qita_number);
                    String NIM= NIM_Flage.getString("nim_flage", "0");
                    String nim_acc =NIM_Flage.getString("nim_acc", "0");
                    if(NIM.equals("0"))
                    {
                        //speak= XG_Setting.getString("speak", "0");
                        if(XG_Setting.getString("speak", "0").equals("1"))
                        {
                            notification(messages.get(messages.size()-1).getSessionId(),messages.get(messages.size()-1).getFromNick(),messages.get(messages.size()-1).getContent());
                        }
                    }else {//正在聊天室
                        if(!messages.get(messages.size()-1).getSessionId().equals(nim_acc))//来的消息和当前聊天的是不是一个人
                        {
                            if(XG_Setting.getString("speak", "0").equals("1"))
                            {
                                notification(messages.get(messages.size()-1).getSessionId(),messages.get(messages.size()-1).getFromNick(),messages.get(messages.size()-1).getContent());
                            }
                        }
                    }
                    event_number_total++;
                    event_number.setText(""+event_number_total);
                    event_right.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void insert_sql(String title , String content , String type,String user_id,String acc_id,String time,String du,String qita)//插入新数据
    {
        /*
               +"title text,"//标题
            +"content text,"//文本
            +"type text,"//类型
            +"user_id text,"//user_id
            +"accid_id text,"//聊天需要
            +"time text,"//时间
            +"du text,"//已读和未读的状态
            +"qita text)";//预留字段
    */
        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("content",content);
        cv.put("type",type);
        cv.put("user_id",user_id);
        cv.put("accid_id",acc_id);
        cv.put("time",time);
        cv.put("du",du);
        cv.put("qita",qita);
        sq.insert("event",null,cv);
    }

    private void delete(String accid) {//删除数据
//删除条件
                String whereClause ="accid_id=?";
                String[] whereArgs = {String.valueOf(accid)};
                sq.delete("event",whereClause,whereArgs); //改为重服务器获取
                }


    private void  notification(String accid,String name,String content)
    {
        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,NimActivity.class);
        String[] user = new String[2];
        user[0]=accid;
        user[1]=name;
        intent.putExtra("USER",user);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(name+" 患者的消息").setContentText(content).setSmallIcon(R.drawable.start_head_url).setContentIntent(pendingIntent).setTicker(content);
        Notification notification = builder.build();
        //指定标志和声音
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND;
        //3、指定通知的标题、内容和intent
        try {

            Field field = notification.getClass().getDeclaredField(content);

            Object extraNotification = field.get(notification);

            Method method = extraNotification.getClass().getDeclaredMethod("3", int.class);

            method.invoke(extraNotification, 3);

        } catch (Exception e) {

            e.printStackTrace();

        }

        mNotificationManager.notify(0,notification);
    }

    public void doLogin()
    {
        LoginInfo info = new LoginInfo(userBean.getAccid(),userBean.getToken());

        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {

                System.out.println("*******云信登录成功*******");

                DemoCache.setAccount(userBean.getAccid());
                NimUIKit.setAccount(userBean.getAccid());
                //init_nim_ti();
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }
    private boolean isAppOnForeground(Context context)//判断App是否在后台运行
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        if(appProcessInfos==null)
        {
            return false;
        }
        final String packageName = context.getPackageName();
        for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessInfos)
        {
            if(appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND&& appProcessInfo.processName.equals(packageName)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("这次是进入后台运行*****************");
        nim_message_flage_edit.putString("start_flage", "1");
        nim_message_flage_edit.commit();
    }
}
