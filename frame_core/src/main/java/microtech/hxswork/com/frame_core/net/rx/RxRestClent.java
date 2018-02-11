package microtech.hxswork.com.frame_core.net.rx;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import microtech.hxswork.com.frame_core.net.HttpMethod;
import microtech.hxswork.com.frame_core.net.RestCreator;
import microtech.hxswork.com.frame_core.ui.LatteLoader;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by microtech on 2017/11/13.
 */

public class RxRestClent {
    private final String URL;
    private  static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final ResponseBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context context;
    private final File FILE;
    public RxRestClent(String url,
                       Map<String, Object> params,
                       ResponseBody body,
                       File file,
                       Context context,
                       LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.putAll( params);
        this.BODY = body;
        this.FILE = file;
        this.context = context;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RxRestClientBuilder  builder(){
        return  new RxRestClientBuilder();
    }
    private Observable<String> request(HttpMethod method)
    {
        final  RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;
        if(LOADER_STYLE !=null){
            LatteLoader.showLoading(context,LOADER_STYLE);
        }
        switch (method){
            case GET:
                observable = service.get(URL,PARAMS);
                break;
            case POST:
                observable = service.post(URL,PARAMS);
                break;
            case POST_RAW:
                observable = service.potRaw(URL,BODY);
                break;
            case PUT:
                observable = service.put(URL,PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL,BODY);
                break;
            case DELETE:
                observable = service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final  MultipartBody.Part body = MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                observable = RestCreator.getRxRestService().upload(URL,body);
                break;
            default:
                break;
        }
       return observable;
    }
    public final  Observable<String> get(){
      return   request(HttpMethod.GET);
    }
    public final  Observable<String> post(){
        if(BODY == null){
            return request(HttpMethod.POST);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.POST_RAW);
        }
    }
    public final  Observable<String> put(){
        if(BODY == null){
            return request(HttpMethod.POST);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            return  request(HttpMethod.POST_RAW);
        }
    }
    public final  Observable<String> delete(){
        return  request(HttpMethod.DELETE);
    }

    public final Observable<ResponseBody> download(){
        final  Observable<ResponseBody> responseBodyObservable = RestCreator.getRxRestService().dowload(URL,PARAMS);
        return responseBodyObservable;
    }
}

