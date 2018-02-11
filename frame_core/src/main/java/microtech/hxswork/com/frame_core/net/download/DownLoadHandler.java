package microtech.hxswork.com.frame_core.net.download;

import android.os.AsyncTask;

import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.net.RestCreator;
import microtech.hxswork.com.frame_core.net.callback.IError;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.IRequest;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by microtech on 2017/11/14.
 */

public class DownLoadHandler {
    private final String URL;
    private  static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private  final IError ERROR;
    private final String DOWNLOAD_DIR;
    private  final String EXTENSION;
    private final String NAME;

    public DownLoadHandler(String url, IRequest request,
                           ISuccess success,
                           IFailure failure,
                           IError error,
                           String download_dir,
                           String extension,
                           String name) {
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
    }

    public final void handleDownload(){
        if(REQUEST != null){
            REQUEST.onRequestStart();//开始下载
        }
        RestCreator.getRestService().dowload(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            final  ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST,SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,responseBody,NAME);
                            //这里一定要注意判断，否侧文件下载不全
                            if(task.isCancelled()){
                                if(REQUEST!=null){
                                    REQUEST.onRequestEnd();
                                }
                            }
                        }else {
                            if(ERROR!=null){
                                ERROR.onError(response.code(),response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(FAILURE!=null)
                    {
                        FAILURE.onIFailure();
                    }
                    }
                });
    }
}
