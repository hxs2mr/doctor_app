package microtech.hxswork.com.frame_core.net.rx;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.net.RestCreator;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Created by microtech on 2017/11/13.
 */

public class RxRestClientBuilder {//建造体
    private String mUrl = null;
    private static final Map<String, Object> PARAMS = RestCreator.getParams();

    private ResponseBody mBody= null;
    private Context mContext= null;
    private LoaderStyle mLoaderStyle= null;
    private File mFile= null;

    RxRestClientBuilder() {

    }
    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> Params) {
        PARAMS.putAll(Params);
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value) {
        this.PARAMS.put(key, value);
        return this;
    }


    public final RxRestClientBuilder file(File  file) {
        this.mFile = file;
        return this;
    }
    public final RxRestClientBuilder file(String  filepath) {
        this.mFile = new File(filepath);
        return this;
    }
    public final RxRestClientBuilder raw(String raw) {
        this.mBody = ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RxRestClientBuilder loader(Context context)//默认的loader
    {
        this.mContext = context;
        this.mLoaderStyle =LoaderStyle.BallClipRotateMultipleIndicator;
        return this;
    }
    public final RxRestClientBuilder loader(Context context, LoaderStyle style)
    {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }
    public final RxRestClent build() {
        return new RxRestClent(mUrl, PARAMS,mBody,mFile,mContext,mLoaderStyle);
    }

}
