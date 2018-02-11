package microtech.hxswork.com.frame_core.middle.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import microtech.hxswork.com.frame_core.middle.IPageLoadListener;
import microtech.hxswork.com.frame_core.middle.web.ChromeClient.webChromeClientImpl;
import microtech.hxswork.com.frame_core.middle.web.client.WebViewClientImpl;
import microtech.hxswork.com.frame_core.middle.web.route.RouteKeys;
import microtech.hxswork.com.frame_core.middle.web.route.Router;

/**
 * Created by microtech on 2017/11/21.
 */

public class WebFragmentImpl extends WebFragment implements IWebViewInitializer{
    private IPageLoadListener mIPageLoadListener = null;
    public static WebFragmentImpl create(String url){//静态工厂方法  初始化自己
        final  Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(),url);
        final  WebFragmentImpl fragment = new WebFragmentImpl();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Object setLayout() {
        return getWebView();
    }
    public  void setPageLoadListener(IPageLoadListener listener)
    {
        this.mIPageLoadListener = listener;
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        if(getUrl()!= null){//用原生的方式模拟webview跳转
            Router.getInstance().loadPage(this,getUrl());
        }
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    public WebView initWebView(WebView webView) {

        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        client.setPageLoadListener(mIPageLoadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient() {

        return new webChromeClientImpl();
    }
}
