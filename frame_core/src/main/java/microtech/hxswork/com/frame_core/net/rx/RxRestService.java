package microtech.hxswork.com.frame_core.net.rx;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by microtech on 2017/11/14.
 */

public interface RxRestService {
    //一系列办法的网络清求方法

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params); //QueryMap会以键值对的方式自动拼接到浏览器里面

    @FormUrlEncoded//post需要的请求
    @POST
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);//post请求

    @POST
    Observable<String> potRaw(@Url String Url, @Body ResponseBody body);

    @FormUrlEncoded
    @PUT
    Observable<String> put(@Url String url, @FieldMap Map<String, Object> params);//上传文件请求

    @PUT
    Observable<String> putRaw(@Url String url, @Body ResponseBody body);
    @DELETE
    Observable<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    @Streaming//加上次注解  下载好多存储好多
    @GET
    Observable<ResponseBody> dowload(@Url String url, @QueryMap Map<String, Object> params);//Retorft默认下载文件是一次性下载在内存里  （会出现内存溢出）

    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part file);//文件上传

}
