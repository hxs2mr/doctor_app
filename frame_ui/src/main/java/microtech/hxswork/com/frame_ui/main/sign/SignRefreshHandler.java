package microtech.hxswork.com.frame_ui.main.sign;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderAdapter;
import microtech.hxswork.com.frame_ui.main.order.OrderPageBean;
import microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2018/1/8.
 */

public class SignRefreshHandler {
    private final RecyclerView RECYCLEVIEW;
    public static SignAdapater mAdapter=null;
    private final DataConVerter CONVERTER;
    private String TYPE = null;
    private String mUrl =null;
    private String next_start ="";
    private int new_data_flage =0 ;
    private Context mContext;
    private String mUser_Id="";

    public SignRefreshHandler( RecyclerView recycleview, DataConVerter conVerter, String type) {
        this.RECYCLEVIEW = recycleview;
        this.CONVERTER = conVerter;
        this.TYPE = type;
    }

    public static SignRefreshHandler create( RecyclerView recycleview, DataConVerter conVerter, String type){
        return new SignRefreshHandler(recycleview,conVerter,type);
    }

    public void firstPage(final Context context, String user_id)
    {
        mUser_Id = user_id;
        this.mContext = context;
        RestClent.builder()
                .url("dataInfo")
                .loader(mContext)
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("user_id",user_id)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("体征数据详情返回的数据***********"+response);
                        final int  code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            mAdapter= SignAdapater.create(CONVERTER.setJsonData(response));
                            RECYCLEVIEW.setAdapter(mAdapter);

                        }else {
                            SAToast.makeText(context,"体征数据加载失败").show();
                        }
                    }
                })
                .build()
                .post();
    }
    /*@Override
    public void onRefresh() {
       // refresh();
    }
*/
    private void refresh(){
        //REFRESH_LAYOUT.setRefreshing(true);
        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
               RestClent.builder()
                       .url("dataInfo")
                        .params("region_id",fristBean.getRegion_id())
                        .params("gov_id",fristBean.getGov_id())
                        .params("doctor_id",userBean.getId())
                        .params("user_id",mUser_Id)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println("刷新体征数据详情返回的数据***********"+response);
                                final int  code = JSON.parseObject(response).getInteger("code");
                                if(code == 200)
                                {
                                    List<MultipleItemEntity>  list_data = CONVERTER.setJsonData(response).CONVERT();
                                    System.out.println("刷新返回的数据的大小***********"+list_data.size());
                                    mAdapter.refresh(list_data);
                                    //mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                }else {
                                    SAToast.makeText(mContext,"体征数据加载失败").show();
                                }
                                //REFRESH_LAYOUT.setRefreshing(false);
                        }
                        })
                        .build()
                        .post();

            }
        },300);
    }
}
