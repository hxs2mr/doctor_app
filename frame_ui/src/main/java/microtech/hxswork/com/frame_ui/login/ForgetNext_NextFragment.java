package microtech.hxswork.com.frame_ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.MD5;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/4.
 */

public class ForgetNext_NextFragment extends MiddleFragment implements View.OnClickListener {
    IconTextView forget_ok_back = null;
    TextInputEditText  forget_ok_password = null;
    AppCompatButton  button_forget_ok = null;
    AppCompatImageView forget_ok_look =null;
    int look_flage=0;
    String userphone = null;
    @Override
    public Object setLayout() {
        return R.layout.forgetnext_next_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle phone = getArguments();
        userphone = phone.getString("phone");
        getSupportDelegate().hideSoftInput();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        forget_ok_back = bind(R.id.forget_ok_back);
        forget_ok_password = bind(R.id.forget_ok_password);
        button_forget_ok = bind(R.id.button_forget_ok);
        forget_ok_look = bind(R.id.forget_ok_look);

        forget_ok_back.setOnClickListener(this);
        button_forget_ok.setOnClickListener(this);
        forget_ok_look.setOnClickListener(this);
        forget_ok_password.addTextChangedListener(new Edittext_change_userpassword());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_forget_ok) {//修改密码的网络请求
            if(!forget_ok_password.getText().toString().equals(""))
            {
                if(forget_ok_password.getText().toString().length()>=6)
                {

                    init_update_word();

                }else {
                    SAToast.makeText(getContext(),"密码长度不能小于6位！").show();
                }

            }else {
                SAToast.makeText(getContext(),"请输入密码！").show();
            }

        } else if (i == R.id.forget_ok_back) {
            getSupportDelegate().pop();
        }else if(i == R.id.forget_ok_look)
        {
            if(look_flage == 0) {
                forget_ok_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                look_flage = 1;
            }else {
                forget_ok_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                look_flage =0;
            }
        }
    }

    private void init_update_word() {
        RestClent.builder()
                .url("user_update_password")
                .params("phone",userphone)
                .params("password", MD5.MD51(forget_ok_password.getText().toString().trim()))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("更新密码返回的数据**********"+response);
                         int code = JSON.parseObject(response).getInteger("code");
                    if(code == 200)
                    {
                        SAToast.makeText(getContext(),"密码更新成功!").show();
                        getSupportDelegate().pop();
                        getSupportDelegate().startWithPop(new LoginFragment());
                    }else {
                        SAToast.makeText(getContext(),"用户名不存在!").show();
                        getSupportDelegate().pop();
                        getSupportDelegate().startWithPop(new LoginFragment());
                    }

                    }
                })
                .build()
                .post();
    }

    class  Edittext_change_userpassword implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            forget_ok_look.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            forget_ok_look.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = forget_ok_password.getText().toString();
            if (password.equals("")) {
                forget_ok_look.setVisibility(View.INVISIBLE);
                forget_ok_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else
            {
                forget_ok_look.setVisibility(View.VISIBLE);
            }
        }
    }

}
