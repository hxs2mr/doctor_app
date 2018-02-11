package microtech.hxswork.com.frame_core.wechat;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by microtech on 2017/11/22.
 */

public abstract class BaseWXPayEntryActivity extends BaseWXActivity {

    private static final  int WX_PAY_SUCCESS = 0;
    private static final  int WX_PAY_FAIL= -1;
    private static final  int WX_PAY_CANCLE= -2;

    protected  abstract void onPaySuccess();//微信支付回调

    protected  abstract void onPayFail();

    protected  abstract void onPayCancle();


    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            switch (baseResp.errCode)
            {
                case WX_PAY_SUCCESS:
                    onPaySuccess();
                    break;
                case WX_PAY_FAIL:
                    onPayFail();
                    break;
                case WX_PAY_CANCLE:
                    onPayCancle();
                    break;
               default:
                   break;
            }
        }
    }
}
