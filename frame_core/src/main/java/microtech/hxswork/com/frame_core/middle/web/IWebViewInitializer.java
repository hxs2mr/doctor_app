package microtech.hxswork.com.frame_core.middle.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by microtech on 2017/11/21.
 */

public interface IWebViewInitializer {
    WebView initWebView(WebView webView);
    WebViewClient initWebViewClient();
    WebChromeClient initWebChromeClient();
}
