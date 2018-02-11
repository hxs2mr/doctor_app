package microtech.hxswork.com.frame_ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.phone.FormatUtil;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;

import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;

/**
 * Created by microtech on 2017/11/15.
 */

public class ForgetFragment extends MiddleFragment implements View.OnClickListener {
    TextInputEditText mName = null;
    AppCompatButton button_next = null;
    IconTextView forget_back = null;
    AppCompatImageView forget_delete_edittext=null;
    Bundle mphone=null;
    private ILoginListener mILoginListener =null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ILoginListener){
            mILoginListener = (ILoginListener) activity;
        }

    }
    private boolean checkForm(){//检查输入的文本是否正确
        final  String name = mName.getText().toString().trim();
        boolean isPass = true;
        if(name.isEmpty()){
            mName.setError("请输入手机号");
            isPass = false;
        }else if(isMobileNO(name)){
                mName.setError(null);
        }else {
            mName.setError("手机号格式错误");
            isPass = false;
        }
        return isPass;
    }
    @Override
    public Object setLayout() {
        return R.layout.register_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mphone = new Bundle();
         mName = bind(R.id.forget_username);
        button_next = bind(R.id.button_forget_next);
        forget_back = bind(R.id.forget_back);
        forget_delete_edittext = bind(R.id.forget_username_delete);
        mName.addTextChangedListener(new Edittext_change_username());
        button_next.setOnClickListener(this);
        forget_back.setOnClickListener(this);
        forget_delete_edittext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_forget_next) {
            if(checkForm())//检查用户注册输入的信息对不对
            {
           /*     RestClent.builder()//网络请求
                        .url("")//请求的地址
                        .params("","")
                        .params("","")
                        .params("","")//添加参数
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                LoginHandler.onSignUp(response,mILoginListener);//注册成功之后保存用户信息  达到数据的持久化
                            }
                        })
                        .build()
                        .post();*/

                huo_sms(mName.getText().toString().trim());

            }
        }else if(i == R.id.forget_back)
        {
            getSupportDelegate().pop();
        }else if(i == R.id.forget_username_delete){
            if (!mName.getText().toString().equals("")) {
                mName.setText("");
            }
        }

    }

    class Edittext_change_username implements TextWatcher {
        //设置edittext的监听时间
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            forget_delete_edittext.setVisibility(View.INVISIBLE);

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            forget_delete_edittext.setVisibility(View.VISIBLE);
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String username = mName.getText().toString();
            if (username.equals("")) {
                forget_delete_edittext.setVisibility(View.INVISIBLE);
            }else
            {
                forget_delete_edittext.setVisibility(View.VISIBLE);
            }
        }
    }

    private void huo_sms(String user_name) {
        RestClent.builder()
                .url("user_sms_create")
                .params("type","forget")
                .params("phone",user_name)
                .params("code","")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("手机验证返回的数据********"+response);
                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            ForgetNextFragment forgetFragment = new ForgetNextFragment();
                            mphone.putString("phone",mName.getText().toString().trim());
                            forgetFragment.setArguments(mphone);
                            getSupportDelegate().pop();
                            getSupportDelegate().start(forgetFragment);
                        }else {
                            SAToast.makeText(getContext(),"用户不存在！").show();
                        }

                    }
                })
                .build()
                .post();
    }

}
