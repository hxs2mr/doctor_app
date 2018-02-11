package microtech.hxswork.com.frame_core.middle.web.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.webkit.URLUtil;
import android.webkit.WebView;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.middle.web.WebFragment;
import microtech.hxswork.com.frame_core.middle.web.WebFragmentImpl;


/**
 * Created by microtech on 2017/11/21.
 */

public class Router {//单列模式  惰性加载
    private Router(){

    }
    private static class Holder{
        private static final Router INSTANCE = new Router();
    }
    public static Router getInstance(){
        return Holder.INSTANCE;
    }
    public final boolean handleWebUrl(WebFragment fragment, String url)
    {
        //电话连接的判断
        if(url.contains("tel:")){
            callphone(fragment.getContext(),url);
            return true;
        }
        final MiddleFragment topfragment = fragment.getTopFragment();
        final WebFragmentImpl webFragment = WebFragmentImpl.create(url);
        topfragment.getSupportDelegate().start(webFragment);
        return true;
    }
    private void loadWebPage(WebView webView,String url){
        if(webView!= null){
            webView.loadUrl(url);
        }else {
            throw new NullPointerException("webview为空");
        }
    }
    private void loadLocalPage(WebView webView,String url){
        loadWebPage(webView,"file:///android_asset/"+url);
    }
    private  void loadPage(WebView webView,String url)
    {
        if(URLUtil.isNetworkUrl(url)||URLUtil.isAssetUrl(url)){
            loadWebPage(webView,url);
        }else {
            loadLocalPage(webView,url);
        }
    }
    public final void loadPage(WebFragment fragment,String url)
    {
        loadPage(fragment.getWebView(),url);
    }
    private void callphone(Context context,String url){
        final Intent intent = new Intent(Intent.ACTION_CALL);
        final Uri data = Uri.parse(url);
        intent.setData(data);
        ContextCompat.startActivity(context,intent,null);

    }
}
