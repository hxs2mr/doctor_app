package microtech.hxswork.com.frame_core.wechat.templates;


import microtech.hxswork.com.frame_core.wechat.BaseWXEntryActivity;
import microtech.hxswork.com.frame_core.wechat.LatteWeChat;

/**
 * Created by microtech on 2017/11/17.
 */

public class WXEntrytemplate  extends BaseWXEntryActivity {//微信登录

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0,0);//不需要动画效果
    }

    @Override
    protected void onSignInSuccess(String userInfo) {
        LatteWeChat.getInstance().getLoginCallback().onLoginSuccess(userInfo);//微信登录成功的回调
    }
}
