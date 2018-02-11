package microtech.hxswork.com.frame_core.wechat.templates;

import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;

import microtech.hxswork.com.frame_core.wechat.BaseWXPayEntryActivity;


/**
 * Created by microtech on 2017/11/17.
 */

public class WXPayEntryTemplate extends BaseWXPayEntryActivity {
    @Override
    protected void onPaySuccess() {
        Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onPayFail() {
        Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onPayCancle() {
        Toast.makeText(this,"取消支付",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
