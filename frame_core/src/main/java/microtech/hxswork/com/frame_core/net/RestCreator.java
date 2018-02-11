package microtech.hxswork.com.frame_core.net;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import microtech.hxswork.com.frame_core.R;
import microtech.hxswork.com.frame_core.init.ConfigKeys;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.rx.RxRestService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static microtech.hxswork.com.frame_core.net.RestClent.TOKEN;

/**
 * Created by microtech on 2017/11/13.
 */

public class RestCreator {//单利模式


    private  static final class ParamsHolder{
        public static final WeakHashMap<String,Object>  PARAMS = new WeakHashMap<>();
    }
public static WeakHashMap<String,Object> getParams(){
    return ParamsHolder.PARAMS;
}
    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }
    //构造全局Retrofit客户端
    private static final class RetrofitHolder{
        private static final String BASE_URL = (String) Frame.getConfigureation(ConfigKeys.API_HOST.name());

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static final class OKHttpHolder{
        private static final int TIME_OUT = 30;//设置超时时间OkHttpClient.Builder().sslSocketFactory(sslSocketFactory)
        private static final  OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS = Frame.getConfigureation(ConfigKeys.INTERCEPTOR);
        private static OkHttpClient.Builder addInterceptor(){
            if(INTERCEPTORS !=null && !INTERCEPTORS.isEmpty()){
                for(Interceptor interceptor:INTERCEPTORS){
                    BUILDER.addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .addHeader("token",TOKEN)
                                    //.addHeader("token",newToke)
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(request);
                        }
                    });
                }
            }
            return BUILDER;
        }
        private static  final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .sslSocketFactory(getSSLSocketFactory(Frame.getApplicationContext()))//配置证书
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }
    //service接口
    private  static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
    //service接口
    private  static final class RxRestServiceHolder{
        private static final RxRestService RXREST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RxRestService.class);
    }
    public static RxRestService getRxRestService(){
        return RxRestServiceHolder.RXREST_SERVICE;
    }

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        final String CLIENT_TRUST_PASSWORD = "123456";//信任证书密码，该证书默认密码是123456
        final String CLIENT_AGREEMENT = "TLS";//使用协议
        final String CLIENT_TRUST_KEYSTORE = "BKS";
        SSLContext sslContext = null;
        try {
            //取得SSL的SSLContext实例
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            //取得TrustManagerFactory的X509密钥管理器实例
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //取得BKS密库实例
            KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            InputStream is = context.getResources().openRawResource(R.raw.hxs);
            try {
                tks.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
            } finally {
                is.close();
            }
            //初始化密钥管理器
            trustManager.init(tks);
            //初始化SSLContext
            sslContext.init(null, trustManager.getTrustManagers(), null);
        } catch (Exception e) {
            Log.e("SslContextFactory", e.getMessage());
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }
}
