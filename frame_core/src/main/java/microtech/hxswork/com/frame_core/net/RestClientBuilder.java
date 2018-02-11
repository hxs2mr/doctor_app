package microtech.hxswork.com.frame_core.net;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.net.callback.IError;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.IRequest;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Created by microtech on 2017/11/13.
 */

public class RestClientBuilder {//建造体
    private String mUrl = null;
    private static final Map<String, Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest= null;
    private ISuccess mISuccess= null;
    private IFailure mIFailure= null;
    private IError mIError= null;

    private ResponseBody mBody= null;

    private Context mContext= null;
    private LoaderStyle mLoaderStyle= null;
    private File mFile= null;

    private String mDownLoaderDir = null;
    private String mExtension = null;
    private String mName = null;
    private  Map<String, String> mHeaders = null;
    RestClientBuilder() {

    }
    public final  RestClientBuilder headrs( Map<String, String> headers)
    {
        this.mHeaders = headers;
        return this;
    }
    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> Params) {
        PARAMS.putAll(Params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        this.PARAMS.put(key, value);
        return this;
    }


    public final RestClientBuilder file(File  file) {
        this.mFile = file;
        return this;
    }
    public final RestClientBuilder file(String  filepath) {
        this.mFile = new File(filepath);
        return this;
    }
    public final RestClientBuilder dir(String dir){//下载后文件存放在那个目录
        this.mDownLoaderDir = dir;
        return this;
    }
    public final RestClientBuilder extension(String extension){//文件后缀名
        this.mExtension = extension;
        return  this;
    }
    public final RestClientBuilder name (String name){//完整的文件名
        this.mName = name;
        return  this;
    }
    public final RestClientBuilder raw(String raw) {
        this.mBody = ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder request(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder loader(Context context)//默认的loader
    {
        this.mContext = context;
        this.mLoaderStyle =LoaderStyle.BallClipRotateMultipleIndicator;
        return this;
    }
    public final RestClientBuilder loader(Context context,LoaderStyle style)
    {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }
    public final RestClent build() {
        return new RestClent(mUrl, PARAMS,mDownLoaderDir,mExtension,mName, mIRequest, mISuccess, mIFailure, mIError, mBody,mFile,mContext,mLoaderStyle,mHeaders);
    }

}
