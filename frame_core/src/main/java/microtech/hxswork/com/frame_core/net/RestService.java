package microtech.hxswork.com.frame_core.net;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by microtech on 2017/11/13.
 */

public interface RestService {
    //一系列办法的网络清求方法

    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params); //QueryMap会以键值对的方式自动拼接到浏览器里面

    @FormUrlEncoded//post需要的请求
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);//post请求

    @POST
    Call<String> potRaw(@Url String Url, @Body ResponseBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);//上传文件请求

    @PUT
    Call<String> putRaw(@Url String url, @Body ResponseBody body);
    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    @Streaming//加上次注解  下载好多存储好多
    @GET
    Call<ResponseBody> dowload(@Url String url, @QueryMap Map<String, Object> params);//Retorft默认下载文件是一次性下载在内存里  （会出现内存溢出）

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);//文件上传


    @GET("/search")
    Call<ResponseBody> query(@HeaderMap Map<String, String> headers);
}
