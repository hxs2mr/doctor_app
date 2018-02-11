package microtech.hxswork.com.frame_core.util.token;

import com.alibaba.fastjson.JSON;

import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;

import static microtech.hxswork.com.frame_core.net.RestClent.TOKEN;

/**
 * Created by microtech on 2017/12/18.
 */

public class Token {

    private WeakHashMap<String,Object> list = new WeakHashMap<>();
   public String obtain_Token="";
    public Token(WeakHashMap<String, Object> list) {
        this.list = list;
        ObtainToken();
    }

    public String ObtainToken()//授权token
    {
        RestClent.builder()
                .url("https://doc.newmicrotech.cn/mk/app/access_token")
                .params(list)

                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("授权得到的数据********"+response);
                        String  obj = JSON.parseObject(response).getString("obj");//获取token
                        obtain_Token = obj.toString();
                        TOKEN = obtain_Token;
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onIFailure() {
                        System.out.println("授权得失败**************");
                    }
                })
                .build()
                .post();

        return obtain_Token;
    }
}
