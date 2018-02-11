package com.doctor_app.microtech;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.IOException;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.interceptors.DebugInterceptor;
import microtech.hxswork.com.frame_core.net.rx.AddCookieInterceptor;
import microtech.hxswork.com.frame_core.util.SystemUtil;
import microtech.hxswork.com.frame_ui.database.DataBaseManager;
import microtech.hxswork.com.frame_ui.icon.FontEcModule;
import microtech.hxswork.com.frame_ui.main.nim.NimActivity;
import microtech.hxswork.com.frame_ui.main.update.SystemParams;
import microtech.hxswork.com.frame_ui.nim.DemoCache;
import microtech.hxswork.com.frame_ui.nim.NimSDKOptionConfig;
import microtech.hxswork.com.frame_ui.nim.Preferences;
import microtech.hxswork.com.frame_ui.nim.UserPreferences;

/**
 * Created by microtech on 2017/11/10.
 */

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {//初始化multdex分包
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        long time = System.currentTimeMillis();
        //MultiDex.install(this);
        final Context context = this;
        SystemParams.init(this);
        Frame.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())//https://doc.newmicrotech.cn/otsmobile/app/     //http://192.168.1.135:9988/mk/app/
                .withApiHost("https://doc.newmicrotech.cn/mk/app/")
                //.withInterceptor(new DebugInterceptor("index.html",R.raw.test))
                .withWxchaAppId("wxace207babfef510d")//微信的APPID
                .withWxchartSecRet("ec5f7134a2c99e34e9a0f90c896da95d")//微信的scret
                .withJavaScriptinterface("web")
                //添加Cookie同步拦截器
                .withInterceptor(new AddCookieInterceptor())
                .weithWebHost("https://www.baidu.com/")
                .configure();//初始化

        Log.d("TAA","time===========:"+(System.currentTimeMillis()-time));

                //初始化友盟
                PlatformConfig.setWeixin("wxace207babfef510d", "ec5f7134a2c99e34e9a0f90c896da95d");
                PlatformConfig.setQQZone("1105461976", "2CnuTbECo9181TND");
                UMShareAPI.get(context);
                //Config.DEBUG = true;//友盟测试DEBU
        Log.d("TAB","time===========:"+(System.currentTimeMillis()-time));

//BUG测试
               // Bugtags.start("9c4a33bf8496fe14cf985744030cf73c", this, Bugtags.BTGInvocationEventBubble);
//信鸽debug
                // 开启logcat输出，方便debug，发布时请关闭
//XGPushConfig.enableDebug(this, true);
//初始化云信
                DemoCache.setContext(context);
                DataBaseManager.getInstance().init(context);//初始化数据库 用来存储用户的信息



        long time1 = System.currentTimeMillis();


        NIMClient.init(this, LoginInfo(),null);
        if (NIMUtil.isMainProcess(this)) {
            // 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
            initUIKit();
        }

    /*    InitAsyncTask task = new InitAsyncTask(this);
        task.execute();*/
/*
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));
        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            // 初始化红包模块，在初始化UIKit模块之前执行
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        }
*/
       System.out.println("初始化云信所用时间time===========:"+(System.currentTimeMillis()-time1));
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private LoginInfo LoginInfo(){
        return  null;
    }
    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());
        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
       // NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
       // SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
      //  ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
       // ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        //NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

    }

    private SDKOptions options() {

        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = NimActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.app_logo;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;
        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

// 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
// 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 480 / 2;
// 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionTypeEnum, String s) {
                return null;
            }
        };
        return options;
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    class InitAsyncTask extends AsyncTask<Integer, Integer, String> {

        Context mContent;
        public  InitAsyncTask(Context context)
        {
                this.mContent = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            NIMClient.init(mContent, LoginInfo(),null);
            if (NIMUtil.isMainProcess(mContent)) {
                // 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
                initUIKit();
            }
            return "初始话完成";
        }
    }
}
