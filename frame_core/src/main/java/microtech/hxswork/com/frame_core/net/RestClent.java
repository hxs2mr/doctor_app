package microtech.hxswork.com.frame_core.net;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;

import microtech.hxswork.com.frame_core.bean.UserBean;
import microtech.hxswork.com.frame_core.net.callback.IError;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.IRequest;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.net.callback.RequestCallbacks;
import microtech.hxswork.com.frame_core.net.download.DownLoadHandler;
import microtech.hxswork.com.frame_core.ui.LatteLoader;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by microtech on 2017/11/13.
 */

public class RestClent {
    public static String TIGE_USERID="";

    public static String TIGE_REGION_ID="";
    public static String TIGE_GOV_ID="";
    public static String TIGE_DOCTOR_ID="";

    public static String TOKEN="";
    private final String URL;
    private  static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private  final IError ERROR;
    private final ResponseBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context context;
    private final File FILE;
    private final String DOWNLOAD_DIR;
    private  final String EXTENSION;
    private final String NAME;
    Map<String, String> HEADERS;
    public RestClent(String url, Map<String, Object> params, String downloadDir, String extension, String name, IRequest request, ISuccess success, IFailure failure, IError error,
                     ResponseBody body,
                     File file,
                     Context context,
                     LoaderStyle loaderStyle, Map<String, String> headers) {
        this.URL = url;
        PARAMS.putAll( params);
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.FILE = file;
        this.context = context;
        this.LOADER_STYLE = loaderStyle;
        this.HEADERS = headers;
    }

    public static RestClientBuilder  builder(){
        return  new RestClientBuilder();
    }
    private void request(HttpMethod method)
    {
        final  RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if(REQUEST!=null)
        {
            REQUEST.onRequestStart();
        }
        if(LOADER_STYLE !=null){
            LatteLoader.showLoading(context,LOADER_STYLE);
        }
        switch (method){
            case GET:

                call = service.get(URL,PARAMS);
                break;
            case POST:

                call = service.post(URL,PARAMS);
                break;
            case POST_RAW:
                    call = service.potRaw(URL,BODY);
                break;
            case PUT:
                call = service.put(URL,PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL,BODY);
                break;
            case DELETE:
                call = service.delete(URL,PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final  MultipartBody.Part body = MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                call = RestCreator.getRestService().upload(URL,body);
                break;
            default:
                break;
        }
        if(call!=null)
        {
            call.enqueue(getRequestCallback());//execute()是在主线程中执行  enqueue()是在兴起的线程中执行
        }
    }
    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }
    public final  void get(){
        request(HttpMethod.GET);
    }
    public final void pot_head(){
        request(HttpMethod.GET);
    }
    public final  void post(){
        if(BODY == null){
                request(HttpMethod.POST);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
                request(HttpMethod.POST_RAW);
        }
    }
    public final  void put(){
        if(BODY == null){
            request(HttpMethod.POST);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
    }
    public final  void delete(){
        request(HttpMethod.DELETE);
    }

    public final void download(){
        new DownLoadHandler(URL,REQUEST,SUCCESS,FAILURE,ERROR,DOWNLOAD_DIR,EXTENSION,NAME).handleDownload();
    }
}

