//
//  Created by  fred on 2016/10/26.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package microtech.hxswork.com.frame_ui.main.szxing.clent;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Demo {

    static{
        HttpClientBuilderParams param = new HttpClientBuilderParams();
        param.setAppKey("24712202");
        param.setAppSecret("c1aa0ebf6922f0efa4b88e501908c093");
        /**
         * 以HTTPS方式提交请求
         * 本DEMO采取忽略证书的模式,目的是方便大家的调试
         * 为了安全起见,建议采取证书校验方式
         */
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        param.setSslSocketFactory(sslContext.getSocketFactory());
        param.setX509TrustManager(xtm);
        param.setHostnameVerifier(DO_NOT_VERIFY);


        HttpsApiClient.getInstance().init(param);

    }

    public static void BeiJingapitestAsyncTest(String base64){

        HttpsApiClient.getInstance().apitest( ("{\n \"inputs\": [\n {\n \"image\": {\n \"dataType\": 50,\n \"dataValue\": \"+" +
                base64+"\" \n },\n \"configure\": {\n \"dataType\": 50,\n \"dataValue\": \"{\\\"side\\\":\\\"face\\\"}\"\n }\n }\n ]\n}").getBytes(SdkConstant.CLOUDAPI_ENCODING) , new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    System.out.println(getResultString(response));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }



    public static String getResultString(ApiResponse response) throws IOException{
        StringBuilder result = new StringBuilder();
        result.append("【服务器返回结果为】").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if(response.getCode() != 200){
            result.append("错误原因：").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));
        return result.toString();
    }
}
