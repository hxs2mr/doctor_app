package microtech.hxswork.com.frame_core.init;


import microtech.hxswork.com.frame_core.util.storage.LattePreference;

/**
 * Created by microtech on 2017/11/16.管理用户的信息
 */

public class AccountManager {
    private enum LoginTag{
        LOGIN_TAG
    }
    //保存用户登录状态 ，登陆后调用
    public static void setLoginState(boolean state){
        LattePreference.setAppFlag(LoginTag.LOGIN_TAG.name(),state);
    }
    public static boolean isLoninIn(){     //用来判断用户是否已经登录
        return LattePreference.getAppFlag(LoginTag.LOGIN_TAG.name());
    }
    public static void checkAccount(IUserChecker checker){
        if(isLoninIn()){
            checker.onLogin();
        }else{
            checker.onNotLogion();
        }
    }

}
