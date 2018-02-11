package microtech.hxswork.com.frame_core.net.interceptors;

import android.support.annotation.RawRes;

import java.io.IOException;

import microtech.hxswork.com.frame_core.util.file.FileUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by microtech on 2017/11/14.
 */

public class DebugInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String debug_url, int debug_raw_id) {
        this.DEBUG_URL = debug_url;
        this.DEBUG_RAW_ID = debug_raw_id;
    }

    private Response getResponse(Interceptor.Chain chain, String json){
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type","application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"),json))
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }
    private Response debugResponse(Interceptor.Chain chain, @RawRes int rawId){
        final  String json = FileUtil.getRawFile(rawId);
        return getResponse(chain,json);
    }
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        final  String url = chain.request().url().toString();
        if(url.contains(DEBUG_URL)){
            return debugResponse(chain,DEBUG_RAW_ID);
        }
        return chain.proceed(chain.request());
    }
}
