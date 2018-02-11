package microtech.hxswork.com.frame_core.net.rx;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import microtech.hxswork.com.frame_core.util.storage.LattePreference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by microtech on 2017/11/21.
 */

public class AddCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        Observable
                .just(LattePreference.getCustomAppProfile("cookie"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        //给原生API请求附带上的webview拦截下来的cookie
                        builder.addHeader("Cookie",s);
                    }
                });
        return chain.proceed(builder.build());
    }
}
