package microtech.hxswork.com.frame_core.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by microtech on 2017/11/17.
 */

public abstract class BaseWXActivity extends AppCompatActivity implements IWXAPIEventHandler{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这个必须在这里面写
        LatteWeChat.getInstance().getWXAPI().handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LatteWeChat.getInstance().getWXAPI().handleIntent(getIntent(),this);
    }
}
