package microtech.hxswork.com.frame_core.middle.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import microtech.hxswork.com.frame_core.init.ConfigKeys;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.middle.web.route.RouteKeys;


/**
 * Created by microtech on 2017/11/21.
 */

public abstract class WebFragment extends MiddleFragment {
    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String url = null;
    private boolean mIsWebViewAbailable = false;
    private MiddleFragment middleFragment=null;
    public WebFragment() {
    }
    public abstract IWebViewInitializer setInitializer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        url = args.getString(RouteKeys.URL.name());
        initWebView();
    }
    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        if(mWebView!=null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }else {
            final  IWebViewInitializer initializer = setInitializer();
            if(initializer !=null){
                final WeakReference<WebView> webViewWeakReference= new WeakReference<WebView>(new WebView(getContext()),WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                mWebView = initializer.initWebView(mWebView);
                mWebView.setWebViewClient(initializer.initWebViewClient());
                mWebView.setWebChromeClient(initializer.initWebChromeClient());
                final  String name= Frame.getConfigureation(ConfigKeys.JAVASCRIPT_INTERFACE);
                mWebView.addJavascriptInterface(WebInterface.create(this),name);
                mIsWebViewAbailable = true;
            }else {
                throw  new NullPointerException("初始化构造器为空!");
            }
        }

    }
public void setTopFragment(MiddleFragment fragment)
{
    this.middleFragment = fragment;
}
public MiddleFragment getTopFragment(){
    if(middleFragment== null){
        middleFragment= this;
    }
    return middleFragment;
}
    public WebView getWebView(){
        if(mWebView == null){
            throw new NullPointerException("WebWiew为空！");
        }
        return mIsWebViewAbailable?mWebView:null;
    }
    public String getUrl(){
        if(url == null){
            throw new NullPointerException("WebWiew为空！");
        }
        return url;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mWebView != null){
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsWebViewAbailable = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
