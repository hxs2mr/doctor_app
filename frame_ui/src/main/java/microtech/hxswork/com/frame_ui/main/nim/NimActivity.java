package microtech.hxswork.com.frame_ui.main.nim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.LoginInfo;

import microtech.hxswork.com.frame_ui.R;


/**
 * Created by microtech on 2017/12/25.
 */

public class NimActivity extends AppCompatActivity {

    SharedPreferences share;
    String[] user;

    public  static   SharedPreferences NIM_Flage ;//标识当前用户处于那个界面  如果在聊天界面就不用弹出消息框
    public static SharedPreferences.Editor NIM_Flage_Edit ;//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        //初始化云信的弹框部分
        NIM_Flage= getSharedPreferences("nim_setting", MODE_PRIVATE);
        NIM_Flage_Edit = NIM_Flage.edit();

        share = getSharedPreferences("doctor_im", MODE_PRIVATE);//第一次获取医生详情
        Intent intent = getIntent();
        user = intent.getStringArrayExtra("USER");
        NIM_Flage_Edit.putString("nim_flage", "1");
        NIM_Flage_Edit.putString("nim_acc",user[0]);
        NIM_Flage_Edit.commit();
        NimUIKit.startP2PSession(this,user[0],user[1]);
        finish();
    }

    private void showSplashView() {
        StatusCode status = NIMClient.getStatus();
        System.out.println("********1在线状态********"+status);
        /*NimUIKit.doLogin(new LoginInfo(accid, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                NimUIKit.startP2PSession(WelcomeActivity.this, share.getString("accid","0"),share.getString("name","0"),share.getString("hospital","0"));
                finish();
            }
            @Override
            public void onFailed(int i) {
            }

            @Override
            public void onException(Throwable throwable) {
            }
        });
        customSplash = true;*/
    }

}
