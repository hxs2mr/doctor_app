//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package microtech.hxswork.com.frame_ui.main.szxing.clent;

import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams;


public class WebSocketApiClient extends com.alibaba.cloudapi.sdk.client.WebSocketApiClient {
    public final static String HOST = "dm-51.data.aliyun.com";
    static WebSocketApiClient instance = new WebSocketApiClient();
    public static WebSocketApiClient getInstance(){return instance;}


    public void init(WebSocketClientBuilderParams websocketClientBuilderParams){
        websocketClientBuilderParams.setScheme(Scheme.WEBSOCKET);
        websocketClientBuilderParams.setHost(HOST);
        super.init(websocketClientBuilderParams);
    }



    public void apitest(byte[] body , ApiCallback callback) {
        String path = "/rest/160601/ocr/ocr_idcard.json";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        
        sendAsyncRequest(request , callback);
    }
}