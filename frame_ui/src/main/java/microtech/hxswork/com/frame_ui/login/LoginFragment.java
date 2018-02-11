package microtech.hxswork.com.frame_ui.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import microtech.hxswork.com.frame_core.util.MD5;
import microtech.hxswork.com.frame_core.util.Mac;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_core.wechat.LatteWeChat;
import microtech.hxswork.com.frame_core.wechat.callbacks.IWeChatLoginCallBack;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.nim.DemoCache;

import static android.content.Context.MODE_PRIVATE;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;
import static microtech.hxswork.com.frame_core.util.MD5.MD51;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/11/15.
 */

public class LoginFragment extends MiddleFragment implements View.OnClickListener {
    TextInputEditText edit_login_name = null;
    TextInputEditText edit_login_password = null;
    AppCompatButton button_login = null;//登录
    AppCompatTextView text_forget = null;//忘记密码
    LinearLayoutCompat login_start_linear = null;
    LinearLayoutCompat login_end_linear = null;
    AppCompatImageView login_username_image = null;
    AppCompatImageView login_password_image = null;
    IconTextView wechar = null;
    public static SharedPreferences share ;
    int look_flage=0;
    private void onClickWxChat(){//微信登录
        LatteWeChat
                .getInstance()
                .onLoginSuccess(new IWeChatLoginCallBack() {
            @Override
            public void onLoginSuccess(String userinfo) {
                Toast.makeText(getContext(),"微信登录成功"+userinfo,Toast.LENGTH_SHORT).show();
            }
        }).LogIn();
    }

    String login_url="http://192.168.1.134:8080/php_android_api/api/login_json.php";

    private ILoginListener mILoginListener =null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ILoginListener){
            mILoginListener = (ILoginListener) activity;
        }

    }
    @Override
    public Object setLayout() {
        return R.layout.login_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
         edit_login_name = bind(R.id.login_username);
         edit_login_password = bind(R.id.login_userpassword);
         button_login =  bind(R.id.button_login);//登录
         text_forget =  bind(R.id.text_forget);//忘记密码
         login_start_linear =   bind(R.id.login_start_linear);
         wechar = bind(R.id.login_weichat);
        login_username_image  = bind(R.id.login_username_delete);
        login_password_image = bind(R.id.login_password_look);

       // new Start_EndListener(login_start_linear,wechar);//用户名和密码一起上去
        button_login.setOnClickListener(this);
        text_forget.setOnClickListener(this);
        login_password_image.setOnClickListener(this);
        login_username_image.setOnClickListener(this);
        wechar.setOnClickListener(this);
        share = getContext().getSharedPreferences("user", MODE_PRIVATE);
        edit_login_name.addTextChangedListener(new Edittext_change_username());//监听edittext的输入
        edit_login_password.addTextChangedListener(new Edittext_change_userpassword());
        edit_login_name.setText(share.getString("user_name", ""));
        addLayoutListener(login_start_linear,button_login);
    }


    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 200) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        int i  = view.getId();
        if(i == R.id.button_login){//登录操作
            if(!edit_login_password.getText().toString().equals("")&&!edit_login_name.getText().toString().equals("")) {
                if(isMobileNO(edit_login_name.getText().toString().trim())) {
                    login_star();
                }else {
                    SAToast.makeText(getContext(),"手机号码格式不对").show();
                }
            }else {
                Toast.makeText(getContext(),"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
            }
        }else if(i == R.id.text_forget)
        {
            getSupportDelegate().start(new ForgetFragment());
        }else if(i == R.id.login_weichat){
            onClickWxChat();
        }else if( i== R.id.login_username_delete)
        {
            if (!edit_login_name.getText().toString().equals("")) {
                edit_login_name.setText("");
            }
        }else if(i == R.id.login_password_look)
        {
            if(look_flage == 0) {
                edit_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                look_flage = 1;
            }else {
                edit_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                look_flage =0;
            }
        }
    }
    private void login_star(){
        RestClent.builder()//网络请求
                .url("login")//请求的地址
                .params("user_name",edit_login_name.getText().toString())
                .params("password", MD51(edit_login_password.getText().toString()))//添加参数
                .loader(getContext(), LoaderStyle.BallClipRotateIndicator)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("********登录返回**********"+response);
                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            LoginHandler.onSignIn(getContext(),response,mILoginListener);//登录成功之后保存用户信息  达到数据的持久化
                            Save_PushToken(edit_login_name.getText().toString(),MD51(edit_login_password.getText().toString()));
                            doLogin();
                        }else {
                            SAToast.makeText(getContext(),"用户名或密码错误").show();
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
       class Edittext_change_username implements TextWatcher {
        //设置edittext的监听时间
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            login_username_image.setVisibility(View.INVISIBLE);

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            login_username_image.setVisibility(View.VISIBLE);
            if(!edit_login_password.getText().toString().equals(""))
            {
                button_login.setAlpha(1.0f);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String username = edit_login_name.getText().toString();
            if (username.equals("")) {
                login_username_image.setVisibility(View.INVISIBLE);
                button_login.setAlpha(0.2f);
            } else
            {
                login_username_image.setVisibility(View.VISIBLE);
            }

        }
    }


    class  Edittext_change_userpassword implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            login_password_image.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            login_password_image.setVisibility(View.VISIBLE);
            if(!edit_login_name.getText().toString().equals(""))
            {
                button_login.setAlpha(1.0f);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = edit_login_password.getText().toString();
            if (password.equals("")) {
                login_password_image.setVisibility(View.INVISIBLE);
                button_login.setAlpha(0.2f);
                edit_login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else
            {
                login_password_image.setVisibility(View.VISIBLE);
            }
        }
    }
    public void Save_PushToken(String username,String password) {
        //指定操作的文件名称
        //指定操作的文件名称
        SharedPreferences share = getContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit(); //编辑文件
        edit.putString("user_name", username);
        edit.putString("password", password);
        edit.commit();
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
