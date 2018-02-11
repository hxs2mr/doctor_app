package microtech.hxswork.com.frame_ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.width.CountDownTimerUtils;
import microtech.hxswork.com.frame_ui.login.width.VerificationCodeView;

/**
 * Created by microtech on 2017/12/4.
 */

public class ForgetNextFragment extends MiddleFragment implements View.OnClickListener {
    AppCompatTextView forget_next_phone = null;
    AppCompatTextView forget_next_miao = null;
    VerificationCodeView  verificationCodeView = null;//验证码框体
    String userphone = null;
    IconTextView forget_next_back = null;
    @Override
    public Object setLayout() {
        return R.layout.forgetnext_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle phone = getArguments();
        userphone = phone.getString("phone");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        forget_next_back  = bind(R.id.forget_next_back);
        verificationCodeView =  bind(R.id.verificationcodeview);
        forget_next_miao = bind(R.id.forget_next_miao);
        forget_next_phone = bind(R.id.forget_next_phone);
        forget_next_phone.setText(userphone);
        forget_next_back.setOnClickListener(this);
        forget_next_miao.setOnClickListener(this);
        verificationCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                //验证码输入完成开启网络请求验证验证码是否正确
               // Toast.makeText(getContext(),""+content,Toast.LENGTH_SHORT).show();
                getSupportDelegate().hideSoftInput();
                init_sms_check(content);//验证短信

            }
        });
    }

    private void init_sms_check(String content) {
        RestClent.builder()
                .url("user_sms_check")
                .params("type","forget")
                .params("phone",userphone)
                .params("code",content)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("验证手机短信返回的数据********"+response);
                        //如果验证码正确测跳到下一个fragment重设密码
                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            ForgetNext_NextFragment forgetFragment = new ForgetNext_NextFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("phone",userphone);
                            forgetFragment.setArguments(bundle);
                            getSupportDelegate().start(forgetFragment);
                        }else {
                            SAToast.makeText(getContext(),"验证码不正确").show();
                        }
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        CountDownTimerUtils countDownTimerUtils  = new CountDownTimerUtils(forget_next_miao,60000,1000);
        countDownTimerUtils.start();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.forget_next_back) {
            getSupportDelegate().pop();
        }else if(i == R.id.forget_next_miao)
        {
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(forget_next_miao, 60000, 1000);//发送手机验证码 timer验证码
            mCountDownTimerUtils.start();
            huo_sms();//获取信息

        }
    }

    private void huo_sms() {
        RestClent.builder()
                .url("user_sms_create")
                .params("type","forget")
                .params("phone",userphone)
                .params("code","")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("手机验证返回的数据********"+response);

                    }
                })
                .build()
                .post();

    }

}
