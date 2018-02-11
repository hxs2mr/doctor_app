package microtech.hxswork.com.frame_ui.main.Person.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.luncher.OnluncherFinishTag;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.ForgetFragment;
import microtech.hxswork.com.frame_ui.login.LoginFragment;
import microtech.hxswork.com.frame_ui.widget.SwitchButton;

import static android.content.Context.MODE_PRIVATE;
import static microtech.hxswork.com.frame_ui.launcher.LauncherFragment.iLuncherListener;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.data;
import static microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler.mAdapter;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.XG_Setting;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.XG_edit;
/**
 * Created by microtech on 2017/12/11.
 */

public class PersonSetting_Fragment extends MiddleFragment implements View.OnClickListener {
    private SwitchButton  switch_huan = null;
    private SwitchButton switch_wei = null;
    private LinearLayoutCompat setting_back_linear = null;
    private LinearLayoutCompat seting_password_linear = null;
    private AppCompatTextView setting_exit = null;
    private AppCompatTextView version_name=null;

    private int flage1 = 1 ;
    private int flahe1 = 2;
    @Override
    public Object setLayout() {
        return R.layout.person_setting;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        version_name=bind(R.id.setting_version_name);
        switch_huan = bind(R.id.switch_huan);
        switch_wei = bind(R.id.switch_wei);
        setting_back_linear = bind(R.id.setting_back_linear);
        seting_password_linear = bind(R.id.seting_password_linear);
        setting_exit = bind(R.id.setting_exit);
        setting_back_linear.setOnClickListener(this);//返回
        setting_exit.setOnClickListener(this);//退出登录
        seting_password_linear.setOnClickListener(this);//修改密码'
        version_name.setText(fristBean.getVersionname());
        initSwitch_button();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.setting_back_linear) {
            getSupportDelegate().pop();
        }else  if(i == R.id.setting_exit){
            show(setting_exit);
        }else if(i == R.id.seting_password_linear)//修改密码
        {
            getSupportDelegate().start(new ForgetFragment());
        }
    }
private  void initSwitch_button(){

    final String warning = XG_Setting.getString("warning", "0");
    final String speak = XG_Setting.getString("speak", "0");
    if(speak.equals("0"))
    {
        switch_huan.setChecked(false);
    }else {
        switch_huan.setChecked(true);
    }
    switch_huan.isChecked();
    switch_huan.toggle();     //switch state
    switch_huan.toggle(false);//switch without animation
    switch_huan.setShadowEffect(true);//disable shadow effect
    switch_huan.setEnabled(true);//是否可以改变状态
    switch_huan.setEnableEffect(true);//disable the switch animation
    switch_huan.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            //TODO do your job
          //  Toast.makeText(getContext(),"状态"+isChecked,Toast.LENGTH_SHORT).show();
            int isopen=0;
            if(isChecked)
            {
                isopen=1;
                XG_edit.putString("speak", "1");
            }else {
                isopen=0;
                XG_edit.putString("speak", "0");
            }
            XG_edit.commit();
            put_net(0,isopen);
        }
    });


    if(warning.equals("0"))
    {
        switch_wei.setChecked(false);//选中的状态
    }else {
        switch_wei.setChecked(true);//选中的状态
    }

    switch_wei.isChecked();
    switch_wei.toggle();     //switch state
    switch_wei.toggle(false);//switch without animation
    switch_wei.setShadowEffect(true);//disable shadow effect
    switch_wei.setEnabled(true);//disable button
    switch_wei.setEnableEffect(true);//disable the switch animation
    switch_wei.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            //TODO do your job
            //Toast.makeText(getContext(),"状态"+isChecked,Toast.LENGTH_SHORT).show();
            int isopen=0;
            if(isChecked)
            {
                isopen=1;
                XG_edit.putString("warning", "1");
            }else {
                isopen=0;
                XG_edit.putString("warning", "0");
            }
            XG_edit.commit();
            put_net(1,isopen);
        }
    });

}

    public void show(View v){

        //实例化建造者
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //设置警告对话框的标题
        builder.setTitle("退出登录");
        //设置警告显示的图片
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage("退出登录将不能接受到消息和提醒？");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NIMClient.getService(AuthService.class).logout();//退出云信
               //registerPush(getContext(), );//退出信鸽
                getSupportDelegate().pop();
                getSupportDelegate().startWithPop(new LoginFragment());
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
    private void put_net(int flage ,int value)
    {
        RestClent.builder()
                .url("message_set")
                .loader(getContext())
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("value",value)
                .params("type",flage)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("设置返回的结果**********"+response);
                    }
                })
                .build()
                .post();
    }

}
