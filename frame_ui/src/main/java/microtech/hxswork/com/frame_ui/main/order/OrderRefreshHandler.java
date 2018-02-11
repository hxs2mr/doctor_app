package microtech.hxswork.com.frame_ui.main.order;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.LoaderStyle;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number_total;

/**
 * Created by microtech on 2017/11/17.
 */

public class OrderRefreshHandler implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{


    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final OrderPageBean BEAN;
    private final RecyclerView RECYCLEVIEW;
    public static OrderAdapter mAdapter=null;
    private final DataConVerter CONVERTER;
    private String TYPE = null;
    private String mUrl =null;
    private String next_start ="";
    private int new_data_flage =0 ;
    private Context mContext;
    private AppCompatTextView order_day= null;
    private   AppCompatTextView  order_xin = null;
    private  AppCompatTextView  order_year = null;
    View vHeadder = null;
    public OrderRefreshHandler(SwipeRefreshLayout REFRESH_LAYOUT, RecyclerView recycleview, DataConVerter conVerter, OrderPageBean bean,String type) {
        this.REFRESH_LAYOUT = REFRESH_LAYOUT;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        this.RECYCLEVIEW = recycleview;
        this.CONVERTER = conVerter;
        this.BEAN = bean;
        this.TYPE = type;
    }

    public static OrderRefreshHandler create(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recycleview, DataConVerter conVerter,String type){
        return new OrderRefreshHandler(swipeRefreshLayout,recycleview,conVerter,new OrderPageBean(),type);
    }
    private void refresh(){
        REFRESH_LAYOUT.setRefreshing(true);
        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                RestClent.builder()
                        .url(mUrl)
                        .params("region_id",fristBean.getRegion_id())
                        .params("gov_id",fristBean.getGov_id())
                        .params("doctor_id",userBean.getId())
                        .params("next_start","")
                        .params("item_count",3)
                        .params("type", 0)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                                System.out.println("****刷新的数据***********"+response);
                                init_add_new(response);
                                REFRESH_LAYOUT.setRefreshing(false);
                                if(new_data_flage == 0)
                                {
                                    SAToast.makeText(mContext,"没有最新患者数据").show();
                                }
                            }
                        })
                        .build()
                        .post();
            }
        },0);
    }

    private void init_add_new(String response){//比较刷新的数据
        final JSONObject object = JSON.parseObject(response).getJSONObject("obj");
        final JSONArray array = object.getJSONArray("list");
        final int size = array.size();
        for(int i =0 ; i<size ; i++)
        {
            final JSONObject data1 = array.getJSONObject(i);
            String id_card = data1.getString("_id");
            if(id_card.equals(mAdapter.getItem(0).getField(OrderQianFields._ID)))//判断第一个是否和也有的数据相等
            {
                if(i>0)
                {
                    if(mUrl.equals("signings"))//签约界面
                    {
                        new_data_flage = 1;
                        final JSONObject data = array.getJSONObject(i-1);
                        String name = data.getString("name");
                        String sex = data.getString("sex");
                        String birthday = data.getString("age");
                        String thumb="";
                        if( data.getString("avatar")!= null) {
                            thumb = data.getString("avatar");
                        }
                        String statue = data.getString("effect");

                        String time = data.getString("timeu");

                        String number = data.getString("number_no");
                        String bin = data.getString("marking");
                        String userid = data.getString("user_id");

                        String region_id = data.getString("user_id");
                        String gov_id = data.getString("gov_id");
                        String doctor_id = data.getString("doctor_id");
                        String _id =  data.getString("_id");
                        MultipleItemEntity entity = MultipleItemEntity.builder()
                                .setItemType(OrderListItemType.ITEM_ORDER_QIAN)
                                .setField(OrderQianFields.NAME,name)
                                .setField(OrderQianFields.SEX,sex)
                                .setField(OrderQianFields.BIN,bin)
                                .setField(OrderQianFields.BIRTHDAY,birthday)
                                .setField(OrderQianFields.THUMB,thumb)
                                .setField(OrderQianFields.STATUE,statue)
                                .setField(OrderQianFields.TIME,time)
                                .setField(OrderQianFields.NUMBER,number)
                                .setField(OrderQianFields.USER_ID,userid)
                                .setField(OrderQianFields.GOV_ID,gov_id)
                                .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                                .setField(OrderQianFields.REGON_ID,region_id)
                                .setField(OrderQianFields._ID,_id)
                                .build();
                        mAdapter.addData(0,entity);
                    }else {

                        new_data_flage = 1;
                        final JSONObject data= array.getJSONObject(i-1);
                        String _id = data.getString("_id");
                        String gov_id = data.getString("gov_id");
                        String region_id = data.getString("region_id");
                        String doctor_id = data.getString("doctor_id");
                        String sui_flage = data.getString("visitsType");
                        String name = data.getString("name");
                        String time = data.getString("timed");
                        String shen_time = data.getString("day");
                        String phone = data.getString("phone");
                        String bin = data.getString("marking");
                        String type = data.getString("type");
                        //又是一堆的解析
                        MultipleItemEntity entity = MultipleItemEntity.builder()
                                .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP)
                                .setField(OrderQianFields.NAME,name)
                                .setField(OrderQianFields.SUI_FLAGE,sui_flage)
                                .setField(OrderQianFields.TIME,time)
                                .setField(OrderQianFields.PHONE,phone)
                                .setField(OrderQianFields.SHEN_TIME,shen_time)
                                .setField(OrderQianFields.GOV_ID,gov_id)
                                .setField(OrderQianFields.REGON_ID,region_id)
                                .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                                .setField(OrderQianFields._ID,_id)
                                .setField(OrderQianFields.STATUE,type)
                                .build();
                        mAdapter.addData(0,entity);
                    }
                }
            }
        }
    }

    public void refresh_add(Context context, final String r, final String g, final String d){
        REFRESH_LAYOUT.setRefreshing(true);
        Frame.getHandler().postDelayed(  new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                RestClent.builder()
                        .url(mUrl)
                        .params("region_id",fristBean.getRegion_id())
                        .params("gov_id",fristBean.getGov_id())
                        .params("doctor_id",userBean.getId())
                        .params("next_start","")
                        .params("item_count",1)
                        .params("type", 0)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                System.out.println("****新人的数据***********"+response);
                                REFRESH_LAYOUT.setRefreshing(false);
                                init_add_head(response);
                            }
                        })
                        .build()
                        .post();
            }
        },0);
    }


    private void init_add_head(String response ) {
        if(mUrl.equals("signings"))//签约界面
        {
              final JSONObject object = JSON.parseObject(response).getJSONObject("obj");
            final JSONArray array  = object.getJSONArray("list");
            final  int size = array.size();
            final JSONObject data = array.getJSONObject(0);
            String name = data.getString("name");
            String sex = data.getString("sex");
            String birthday = data.getString("age");
            String thumb="";
            if( data.getString("avatar")!= null) {
                thumb = data.getString("avatar");
            }

            String statue = data.getString("effect");

            String time = data.getString("timeu");

            String number = data.getString("number_no");
            String bin = data.getString("marking");
            String userid = data.getString("user_id");

            String region_id = data.getString("user_id");
            String gov_id = data.getString("gov_id");
            String doctor_id = data.getString("doctor_id");
            String _id =  data.getString("_id");

            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_QIAN)
                    .setField(OrderQianFields.NAME,name)
                    .setField(OrderQianFields.SEX,sex)
                    .setField(OrderQianFields.BIN,bin)
                    .setField(OrderQianFields.BIRTHDAY,birthday)
                    .setField(OrderQianFields.THUMB,thumb)
                    .setField(OrderQianFields.STATUE,statue)
                    .setField(OrderQianFields.TIME,time)
                    .setField(OrderQianFields.NUMBER,number)
                    .setField(OrderQianFields.USER_ID,userid)
                    .setField(OrderQianFields.GOV_ID,gov_id)
                    .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                    .setField(OrderQianFields.REGON_ID,region_id)
                    .setField(OrderQianFields._ID,_id)
                    .build();
        mAdapter.addData(0,entity);
        }else {
            JSONObject object = JSON.parseObject(response).getJSONObject("obj");
            JSONArray array = object.getJSONArray("list");
            int size = array.size();
            if(size >=1)
            {
                final JSONObject data= array.getJSONObject(0);
                String _id = data.getString("_id");
                String gov_id = data.getString("gov_id");
                String region_id = data.getString("region_id");
                String doctor_id = data.getString("doctor_id");

                String sui_flage = data.getString("visitsType");

                String name = data.getString("name");
                String time = data.getString("timed");
                String shen_time = data.getString("day");
                String phone = data.getString("phone");

                String bin = data.getString("marking");
                //又是一堆的解析
                String type = data.getString("type");
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP)
                        .setField(OrderQianFields.NAME,name)
                        .setField(OrderQianFields.SUI_FLAGE,sui_flage)
                        .setField(OrderQianFields.TIME,time)
                        .setField(OrderQianFields.PHONE,phone)
                        .setField(OrderQianFields.SHEN_TIME,shen_time)
                        .setField(OrderQianFields.GOV_ID,gov_id)
                        .setField(OrderQianFields.REGON_ID,region_id)
                        .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                        .setField(OrderQianFields._ID,_id)
                        .setField(OrderQianFields.STATUE,type)
                        .build();
            mAdapter.addData(0,entity);
            }
        }
    }


    public void firstPage(String url, Context context)
    {
        REFRESH_LAYOUT.setRefreshing(true);
        this.mContext = context;
        this.mUrl = url;
        vHeadder = View.inflate(context ,R.layout.order_follow_head,null);
        order_day = vHeadder.findViewById(R.id.order_day);
        order_xin = vHeadder.findViewById(R.id.order_xin);
        order_year = vHeadder.findViewById(R.id.order_year);

        System.out.println("_____id___"+userBean.getId());
        System.out.println("_____region_id___"+fristBean.getRegion_id());
        System.out.println("_____gov_id___"+fristBean.getGov_id());

        BEAN.setDelayed(1000);
        RestClent.builder()
                .url(url)
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("next_start","")
                .params("item_count",8)
                .params("item_count",8)
                .params("type", 0)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                        System.out.println("********签约列表**********"+response);
                        final JSONObject object = JSON.parseObject(response);
                        next_start = object.getJSONObject("obj").getString("next_start");

                        BEAN.setTotal(6)
                                .setPageSize(12);
                        //设置Adapter
                        mAdapter= OrderAdapter.create(CONVERTER.setJsonData(response),TYPE);

                        if(TYPE.equals("follow")){
                           mAdapter.addHeaderView(vHeadder);
                            order_xin.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date()));
                            order_day.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
                            order_year.setText(new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date()));
                        }
                        mAdapter.setOnLoadMoreListener(OrderRefreshHandler.this,RECYCLEVIEW);//设置加载更多
                        RECYCLEVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                        REFRESH_LAYOUT.setRefreshing(false);
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMoreRequested() {
        loadmore();
    }

    private void loadmore() {
        mAdapter.setEnableLoadMore(true);
        if(next_start!=null) {
            Frame.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进行一些网络请求
                    //加载完成
                    //进行一些网络请求
                    RestClent.builder()
                            .url(mUrl)
                            .params("region_id",fristBean.getRegion_id())
                            .params("gov_id",fristBean.getGov_id())
                            .params("doctor_id",userBean.getId())
                            .params("next_start",next_start)
                            .params("item_count",3)
                            .params("type", 0)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                    System.out.println("****加载更多返回的数据***********" + response);
                                    final JSONObject object = JSON.parseObject(response);
                                    init_loadmore_data(response);
                                    mAdapter.loadMoreComplete();
                                    //patient_mAdapter.setEnableLoadMore(false);
                                }
                            })
                            .build()
                            .post();
                }
            }, 1000);
        }else {
            Frame.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMoreEnd();
                }
            }, 1000);
        }
    }
    private void init_loadmore_data(String response) {//加载更多
        final JSONObject object = JSON.parseObject(response).getJSONObject("obj");
        final JSONArray array = object.getJSONArray("list");
        next_start = object.getString("next_start");
        final int size = array.size();
        if(size>0)
        {
            for (int i =0;i < size ; i++)
            {
                if(mUrl.equals("signings"))//签约界面
                {
                    final JSONObject data = array.getJSONObject(i);
                    String name = data.getString("name");
                    String sex = data.getString("sex");
                    String birthday = data.getString("age");
                    String thumb="";
                    if( data.getString("avatar")!= null) {
                        thumb = data.getString("avatar");
                    }

                    String statue = data.getString("effect");

                    String time = data.getString("timeu");

                    String number = data.getString("number_no");
                    String bin = data.getString("marking");
                    String userid = data.getString("user_id");

                    String region_id = data.getString("user_id");
                    String gov_id = data.getString("gov_id");
                    String doctor_id = data.getString("doctor_id");
                    String _id =  data.getString("_id");
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(OrderListItemType.ITEM_ORDER_QIAN)
                            .setField(OrderQianFields.NAME,name)
                            .setField(OrderQianFields.SEX,sex)
                            .setField(OrderQianFields.BIN,bin)
                            .setField(OrderQianFields.BIRTHDAY,birthday)
                            .setField(OrderQianFields.THUMB,thumb)
                            .setField(OrderQianFields.STATUE,statue)
                            .setField(OrderQianFields.TIME,time)
                            .setField(OrderQianFields.NUMBER,number)
                            .setField(OrderQianFields.USER_ID,userid)
                            .setField(OrderQianFields.GOV_ID,gov_id)
                            .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                            .setField(OrderQianFields.REGON_ID,region_id)
                            .setField(OrderQianFields._ID,_id)
                            .build();
                    mAdapter.addData(mAdapter.getItemCount()-1,entity);
            }else {
                    final JSONObject data= array.getJSONObject(i);
                    String _id = data.getString("_id");
                    String gov_id = data.getString("gov_id");
                    String region_id = data.getString("region_id");
                    String doctor_id = data.getString("doctor_id");
                    String sui_flage = data.getString("visitsType");
                    String name = data.getString("name");
                    String time = data.getString("timed");
                    String shen_time = data.getString("day");
                    String phone = data.getString("phone");
                    String bin = data.getString("marking");
                    String type = data.getString("type");
                    //又是一堆的解析
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP)
                            .setField(OrderQianFields.NAME,name)
                            .setField(OrderQianFields.SUI_FLAGE,sui_flage)
                            .setField(OrderQianFields.TIME,time)
                            .setField(OrderQianFields.PHONE,phone)
                            .setField(OrderQianFields.SHEN_TIME,shen_time)
                            .setField(OrderQianFields.GOV_ID,gov_id)
                            .setField(OrderQianFields.REGON_ID,region_id)
                            .setField(OrderQianFields.DOCTOR_ID,doctor_id)
                            .setField(OrderQianFields._ID,_id)
                            .setField(OrderQianFields.STATUE,type)
                            .build();
                    mAdapter.addData(mAdapter.getItemCount()-2,entity);
                }
            }
        }else {
            mAdapter.loadMoreEnd();
        }
    }
}
