package microtech.hxswork.com.frame_core.init;

/**
 * Created by microtech on 2017/11/16.登录注册成功的回掉  是否有用户状态的回掉
 */

public interface IUserChecker {
    void onLogin();//有用户信息
    void onNotLogion();//没有用户信息
}
