package com.doctor_app.microtech.xinge;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import com.doctor_app.microtech.R;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGPushManager;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by microtech on 2017/12/26.
 */

public class XG_System {
    public void initCustomPushNotificationBuilder(Context context) {
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();

        //设置通知栏布局文件
      /*  RemoteViews remoteViews =new RemoteViews("microtech.hxswork.com.android_work.ui.activity",R.layout.test_xingge);
        build.setbigContentView(remoteViews);*/
        build.setSound(
                RingtoneManager.getActualDefaultRingtoneUri(
                        context, RingtoneManager.TYPE_ALARM))
                .setDefaults(Notification.DEFAULT_LIGHTS); // 振动
                //.setFlags(Notification.FLAG_NO_CLEAR); // 是否可清除
        // 设置自定义通知layout,通知背景等可以在layout里设置
       // build.setLayoutId(R.layout.test_xingge);
        // 设置自定义通知内容id
        build.setTitle("患者提醒");
        build.setLayoutTextId(R.id.content);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.mipmap.app_logo);
        // 设置状态栏的通知小图标
        //build.setbigContentView((R.mipmap.label)
        build.setIcon(R.mipmap.app_logo);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        //build.setNotificationLargeIcon(R.drawable.ic_action_search);
        // 客户端保存build_id
        XGPushManager.setPushNotificationBuilder(context, 1, build);
        XGPushManager.setDefaultNotificationBuilder(context,build);
    }
}
