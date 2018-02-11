package microtech.hxswork.com.frame_core.ui.refresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.util.SAToast;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by microtech on 2017/11/17.
 */

public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{
    public  static int patient_postion =-1;
    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final PageBean BEAN;
    private final RecyclerView RECYCLEVIEW;
    public static MultipleRecyclerAdapter  patient_mAdapter=null;
    private final DataConVerter CONVERTER;
    private SharedPreferences patient_pre ;//
    private SharedPreferences.Editor patient_edit;//
    private List<MultipleItemEntity> list_data = null;
    private String mR="";
    private String mG="";
    private String mD="";
    private Context mContext;
    private int new_data_flage =0  ;
    private String next_start ="";
    public RefreshHandler(SwipeRefreshLayout REFRESH_LAYOUT,RecyclerView recycleview,DataConVerter conVerter,PageBean bean) {
        this.REFRESH_LAYOUT = REFRESH_LAYOUT;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        this.RECYCLEVIEW = recycleview;
        this.CONVERTER = conVerter;
        this.BEAN = bean;
    }

    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recycleview, DataConVerter conVerter){
        return new RefreshHandler(swipeRefreshLayout,recycleview,conVerter,new PageBean());

    }
    private void refresh(){
        REFRESH_LAYOUT.setRefreshing(true);
        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                RestClent.builder()
                        .url("patients")
                        .params("region_id",mR)
                        .params("gov_id",mG)
                        .params("doctor_id",mD)
                        .params("conditions","")
                        .params("next_start", "")
                        .params("item_count",3)
                        .params("type",1)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                System.out.println("****刷新的数据***********"+response);
                                int code = JSON.parseObject(response).getInteger("code");
                                if(code == 200)
                                {
                                    if(patient_pre.getString("patient","0").equals("1"))
                                    {
                                        if(patient_postion!=-1)
                                        {
                                            String a= patient_pre.getString("patient_result","0");
                                            MultipleItemEntity  entity = patient_mAdapter.getItem(patient_postion);
                                            entity.setField(MultipleFields.STATUE,a);
                                            patient_edit.putString("patient","0");
                                            patient_edit.commit();
                                            patient_mAdapter.setData(patient_postion,entity);
                                        }else {
                                            patient_edit.putString("patient","0");
                                            patient_edit.commit();
                                        }
                                    }

                                    init_add_new(response);
                                    REFRESH_LAYOUT.setRefreshing(false);
                                    if(new_data_flage == 0)
                                    {
                                        SAToast.makeText(mContext,"没有最新患者数据").show();
                                    }
                                }else {
                                    REFRESH_LAYOUT.setRefreshing(false);
                                    SAToast.makeText(mContext,"服务器异常").show();
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
            if(id_card.equals(patient_mAdapter.getItem(0).getField(MultipleFields.USER_ID)))//判断第一个是否和也有的数据相等
            {
                    if(i>0)
                    {
                        final JSONObject data = array.getJSONObject(i-1);
                        new_data_flage ++;
                        String name = data.getString("name");
                        String sex = data.getString("sex");
                        //long birthday = data.getLong("birthday");
                        String thumb ="";
                        if(data.getString("avatar")!=null)
                        {
                            thumb= data.getString("avatar");
                        }
                        String statue = data.getString("effect");
                        String time = data.getString("timeu");
                        String number = data.getString("number_no");
                        String address = data.getString("address");
                        String bin = data.getString("marking");
                        String userid = data.getString("_id");
                        String region_id = data.getString("region_id");
                        String gov_id = data.getString("gov_id");
                        String doctor_id = data.getString("doctor_id");
                        String phone = data.getString("phone");
                        String age = data.getString("age");
                        String peoples = data.getString("peoples");
                        String chat ="";//= data.getInteger("chat")+"";
                        if(data.getInteger("chat")!=null)
                        {
                            chat= data.getInteger("chat")+"";
                        }
                        String accid = "";
                        String tokent = "";
                        final JSONObject im_conment = data.getJSONObject("im_account");
                        if (im_conment != null) {
                            accid = im_conment.getString("accid");
                            tokent = im_conment.getString("token");
                        }
                        List<String> list_bin = new ArrayList<>();
                        final JSONArray array_list_bin = data.getJSONArray("diseases");
                        if(array_list_bin!=null)
                        {
                            for(int j= 0 ; j < array_list_bin.size() ; j ++)
                            {
                                list_bin.add(array_list_bin.getString(j));
                            }
                        }

                        MultipleItemEntity entity = MultipleItemEntity.builder()
                                .setItemType(114)
                                .setField(MultipleFields.NAME, name)
                                .setField(MultipleFields.SPAN_SIZE, 2)
                                .setField(MultipleFields.SEX, sex)
                                .setField(MultipleFields.BIN, bin)
                                .setField(MultipleFields.BIRTHDAY, time)
                                .setField(MultipleFields.THUMB, thumb)
                                .setField(MultipleFields.STATUE, statue)
                                .setField(MultipleFields.TIME, time)
                                .setField(MultipleFields.NUMBER, number)
                                .setField(MultipleFields.USER_ID, userid)
                                .setField(OrderQianFields.ZHU, peoples)
                                .setField(OrderQianFields.AGE, age)
                                .setField(OrderQianFields.ID_CARD, id_card)
                                .setField(OrderQianFields.REGON_ID, region_id)
                                .setField(OrderQianFields.GOV_ID, gov_id)
                                .setField(OrderQianFields.DOCTOR_ID, doctor_id)
                                .setField(OrderQianFields.PHONE, phone)
                                .setField(OrderQianFields.ADDRESS, address)
                                .setField(MultipleFields.ACCID, accid)
                                .setField(MultipleFields.TOKEN, tokent)
                                .setField(MultipleFields.CHAT, chat)
                                .setField(OrderQianFields.IMAGE_ARRAY, list_bin)
                                .build();
                        patient_mAdapter.addData(0,entity);
                    }
            }
        }
    }

    public void refresh_add(Context context, final String r, final String g, final String d){
        REFRESH_LAYOUT.setRefreshing(true);
        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                RestClent.builder()
                        .url("patients")
                        .params("region_id",r)
                        .params("gov_id",g)
                        .params("doctor_id",d)
                        .params("conditions","")
                        .params("next_start", "")
                        .params("item_count",1)
                        .params("type",1)
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
        final JSONObject object = JSON.parseObject(response).getJSONObject("obj");
        final JSONArray array = object.getJSONArray("list");
        final int size = array.size();
        final JSONObject data = array.getJSONObject(0);
        String name = data.getString("name");
        String sex = data.getString("sex");
        //long birthday = data.getLong("birthday");
        String thumb ="";
        if(data.getString("avatar")!=null)
        {
            thumb= data.getString("avatar");
        }
        String statue = data.getString("effect");
        String time = data.getString("timeu");
        String bir="";
        if (data.get("birthday") != null)
        {
            final JSONObject bir_data = data.getJSONObject("birthday");
            bir = bir_data.getString("$numberLong")+"";
        }

        String number = data.getString("number_no");

        String id_card ="";
        if(data.getString("id_card")!=null)
        {
            id_card= data.getString("id_card");
        }


        String address = data.getString("address");
        String bin = data.getString("marking");
        String userid = data.getString("_id");
        String region_id = data.getString("region_id");
        String gov_id = data.getString("gov_id");
        String doctor_id = data.getString("doctor_id");
        String phone = data.getString("phone");
        String age = data.getString("age");
        String peoples ="";
        if(data.getString("peoples")!=null)
        {
            peoples = data.getString("peoples");
        }
        String accid = "";
        String tokent = "";
        final JSONObject im_conment = data.getJSONObject("im_account");
        if (im_conment != null) {
            accid = im_conment.getString("accid");
            tokent = im_conment.getString("token");
        }

        String chat ="";//= data.getInteger("chat")+"";
        if(data.getInteger("chat")!=null)
        {
            chat= data.getInteger("chat")+"";
        }

        List<String> list_bin = new ArrayList<>();
        final JSONArray array_list_bin = data.getJSONArray("diseases");
        if(array_list_bin!=null)
        {
            for(int j= 0 ; j < array_list_bin.size() ; j ++)
            {
                list_bin.add(array_list_bin.getString(j));
            }
        }
        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(114)
                .setField(MultipleFields.NAME, name)
                .setField(MultipleFields.SPAN_SIZE, 2)
                .setField(MultipleFields.SEX, sex)
                .setField(MultipleFields.BIN, bin)
                .setField(MultipleFields.BIRTHDAY, bir)
                .setField(MultipleFields.THUMB, thumb)
                .setField(MultipleFields.STATUE, statue)
                .setField(MultipleFields.TIME, time)
                .setField(MultipleFields.NUMBER, number)
                .setField(MultipleFields.USER_ID, userid)
                .setField(OrderQianFields.ZHU, peoples)
                .setField(OrderQianFields.AGE, age)
                .setField(OrderQianFields.ID_CARD, id_card)
                .setField(OrderQianFields.REGON_ID, region_id)
                .setField(OrderQianFields.GOV_ID, gov_id)
                .setField(OrderQianFields.DOCTOR_ID, doctor_id)
                .setField(OrderQianFields.PHONE, phone)
                .setField(OrderQianFields.ADDRESS, address)
                .setField(MultipleFields.ACCID, accid)
                .setField(MultipleFields.CHAT, chat)
                .setField(MultipleFields.TOKEN, tokent)
                .setField(OrderQianFields.IMAGE_ARRAY, list_bin)
                .build();
        patient_mAdapter.addData(0,entity);
    }

    public void firstPage(String url, Context context,String r,String g,String d)
    {
        patient_pre= context.getSharedPreferences("patient_refresh", MODE_PRIVATE);
        patient_edit = patient_pre.edit();
        this.mR = r;
        this.mG = g;
        this.mD = d;
        this.mContext=  context;
        REFRESH_LAYOUT.setRefreshing(true);
        BEAN.setDelayed(1000);
        RestClent.builder()
                .url(url)
                //.loader(context)
                .params("region_id",r)
                .params("gov_id",g)
                .params("doctor_id",d)
                .params("conditions","")
                .params("next_start", "")
                .params("item_count",8)
                .params("type",1)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                        System.out.println("****患者列表返回的数据***********"+response);

                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            final JSONObject object = JSON.parseObject(response);
                            next_start = object.getJSONObject("obj").getString("next_start");
                            BEAN.setTotal(6)
                                    .setPageSize(12);
                            //设置Adapter
                            list_data= (CONVERTER.setJsonData(response)).CONVERT();
                            patient_mAdapter= MultipleRecyclerAdapter.create(list_data);
                            patient_mAdapter.setOnLoadMoreListener(RefreshHandler.this,RECYCLEVIEW);//设置加载更多
                            RECYCLEVIEW.setAdapter(patient_mAdapter);
                            BEAN.addIndex();
                            REFRESH_LAYOUT.setRefreshing(false);
                            if(list_data.size()<=0)
                            {
                                SAToast.makeText(mContext,"没有患者数据").show();
                                REFRESH_LAYOUT.setRefreshing(false);
                            }
                        }else {
                            REFRESH_LAYOUT.setRefreshing(false);
                            SAToast.makeText(mContext,"服务器异常!").show();
                        }
                    }
                })
                .build()
                .post();
    }
    @Override
    public void onRefresh() {
        refresh();
    }

    public MultipleRecyclerAdapter return_adapter(){
        return patient_mAdapter;
    }

    @Override
    public void onLoadMoreRequested() {//加载更多
        loadmore();
    }

    private void loadmore() {

        patient_mAdapter.setEnableLoadMore(true);
        if(next_start!=null) {
            Frame.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进行一些网络请求
                    //加载完成
                    //进行一些网络请求
                    RestClent.builder()
                            .url("patients")
                            .params("region_id", mR)
                            .params("gov_id", mG)
                            .params("doctor_id", mD)
                            .params("conditions", "")
                            .params("next_start", next_start)
                            .params("item_count", 3)
                            .params("type", 1)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                    System.out.println("****加载更多返回的数据***********" + response);
                                    init_loadmore_data(response);
                                    patient_mAdapter.loadMoreComplete();
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
                    patient_mAdapter.loadMoreEnd();
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
                final JSONObject data = array.getJSONObject(i);
                String name = data.getString("name");
                String sex = data.getString("sex");
                //long birthday = data.getLong("birthday");
                String statue = data.getString("effect");
                String time = data.getString("timeu");
                String number = data.getString("number_no");

                String id_card ="";
                if(data.getString("id_card")!=null)
                {
                    id_card= data.getString("id_card");
                }
                String address = data.getString("address");
                String bin = data.getString("marking");
                String userid = data.getString("_id");
                String region_id = data.getString("region_id");
                String gov_id = data.getString("gov_id");
                String doctor_id = data.getString("doctor_id");
                String phone = data.getString("phone");
                String age = data.getString("age");

                String peoples ="";
                if(data.getString("peoples")!=null)
                {
                    peoples = data.getString("peoples");
                }
                String accid = "";
                String tokent = "";

                String chat ="";//= data.getInteger("chat")+"";
                if(data.getInteger("chat")!=null)
                {
                    chat= data.getInteger("chat")+"";
                }
                final JSONObject im_conment = data.getJSONObject("im_account");
                if (im_conment != null) {
                    accid = im_conment.getString("accid");
                    tokent = im_conment.getString("token");
                }

                List<String> list_bin = new ArrayList<>();
                final JSONArray array_list_bin = data.getJSONArray("diseases");
                if(array_list_bin!=null)
                {
                    for(int j= 0 ; j < array_list_bin.size() ; j ++)
                    {
                        list_bin.add(array_list_bin.getString(j));
                    }
                }
                String thumb = "";
                if (data.getString("avatar") != null) {
                    thumb = data.getString("avatar");
                }
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(114)
                        .setField(MultipleFields.NAME, name)
                        .setField(MultipleFields.SPAN_SIZE, 2)
                        .setField(MultipleFields.SEX, sex)
                        .setField(MultipleFields.BIN, bin)
                        .setField(MultipleFields.BIRTHDAY, time)
                        .setField(MultipleFields.THUMB, thumb)
                        .setField(MultipleFields.STATUE, statue)
                        .setField(MultipleFields.TIME, time)
                        .setField(MultipleFields.NUMBER, number)
                        .setField(MultipleFields.USER_ID, userid)
                        .setField(OrderQianFields.ZHU, peoples)
                        .setField(OrderQianFields.AGE, age)
                        .setField(OrderQianFields.ID_CARD, id_card)
                        .setField(OrderQianFields.REGON_ID, region_id)
                        .setField(OrderQianFields.GOV_ID, gov_id)
                        .setField(OrderQianFields.DOCTOR_ID, doctor_id)
                        .setField(OrderQianFields.PHONE, phone)
                        .setField(OrderQianFields.ADDRESS, address)
                        .setField(MultipleFields.ACCID, accid)
                        .setField(MultipleFields.TOKEN, tokent)
                        .setField(MultipleFields.CHAT, chat)
                        .setField(OrderQianFields.IMAGE_ARRAY, list_bin)
                        .build();
                patient_mAdapter.addData((patient_mAdapter.getItemCount()-1),entity);
            }
        }else {
           patient_mAdapter.loadMoreEnd();
        }
    }
}
