package microtech.hxswork.com.frame_core.middle.web.event;

import android.content.Context;
import android.webkit.WebView;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.middle.web.WebFragment;

/**
 * Created by microtech on 2017/11/21.
 */

public abstract  class Event implements IEvent {
    private Context mContent =null;
    private String mAction = null;
    private WebFragment fragment = null;
    private String mUrl = null;

    private WebView webView = null;

    public WebView getWebView() {
        return fragment.getWebView();
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public Context getContent() {
        return mContent;
    }

    public void setContent(Context mContent) {
        this.mContent = mContent;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }

    public MiddleFragment getFragment() {
        return fragment;
    }

    public void setFragment(WebFragment fragment) {
        this.fragment = fragment;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
