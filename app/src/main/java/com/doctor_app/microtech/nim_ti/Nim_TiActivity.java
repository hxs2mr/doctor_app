package com.doctor_app.microtech.nim_ti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.doctor_app.microtech.MainActivity;
import com.doctor_app.microtech.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import microtech.hxswork.com.frame_core.init.AccountManager;
import microtech.hxswork.com.frame_ui.nim.DemoCache;

import static com.doctor_app.microtech.MainActivity.login_ti_edit;
import static com.doctor_app.microtech.MainActivity.username;
import static com.tencent.android.tpush.XGPushManager.registerPush;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2018/1/5.
 */

public class Nim_TiActivity extends AppCompatActivity implements View.OnClickListener {
    private  AppCompatTextView tuichu =null;
    private  AppCompatTextView deng =null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_ti_layout);
        tuichu = findViewById(R.id.nim_tuichu);
        deng = findViewById(R.id.nim_deng);
        tuichu.setOnClickListener(this);
        deng.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.nim_tuichu:
                NIMClient.getService(AuthService.class).logout();//退出云信
                registerPush(Nim_TiActivity.this, username);//解绑信鸽账号
                       /* getSupportDelegate().pop();
                        getParentFragmen().getSupportDelegate().start(new LoginFragment());*/

                Intent intent = new Intent(Nim_TiActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //getParentFragmen().getSupportDelegate().start(new LoginFragment());
                AccountManager.setLoginState(false);
                login_ti_edit.putString("start_flage", "0");
                login_ti_edit.commit();
                break;
            case R.id.nim_deng:
                doLogin();
                finish();
                login_ti_edit.putString("start_flage", "0");
                login_ti_edit.commit();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doLogin();
        login_ti_edit.putString("start_flage", "0");
        login_ti_edit.commit();
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
}
