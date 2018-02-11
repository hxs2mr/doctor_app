package microtech.hxswork.com.frame_core.middle.web;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;

import microtech.hxswork.com.frame_core.middle.web.event.Event;
import microtech.hxswork.com.frame_core.middle.web.event.EventManager;


/**
 * Created by microtech on 2017/11/21.webwview和员生进行交互
 */

public class WebInterface {
    private final  WebFragment FRAGMENT;
    private WebInterface(WebFragment fragment){
        this.FRAGMENT = fragment;
    }
    static WebInterface create(WebFragment fragment){
        return new WebInterface(fragment);
    }
    //Android4.4之后必须加次注解
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params){
        final String action = JSON.parseObject(params).getString("action");
        final Event event = EventManager.getIntance().createEvent(action);
        if(event!= null){//添加事件
            event.setAction(action);
            event.setFragment(FRAGMENT);
            event.setContent(FRAGMENT.getContext());
            event.setUrl(FRAGMENT.getUrl());
            return event.execute(params);
        }
        return null;
    }
}
