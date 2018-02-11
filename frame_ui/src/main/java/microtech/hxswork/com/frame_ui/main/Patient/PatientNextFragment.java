package microtech.hxswork.com.frame_ui.main.Patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.activity.P2PContextImpl;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.send.Send_Text_PhotoActivity;
import microtech.hxswork.com.frame_ui.main.sign.SignFragment;
import microtech.hxswork.com.frame_ui.widget.SelectPopupWindow;

import static android.content.Context.MODE_PRIVATE;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientItemClickListener.chat;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.NIM_Flage_Edit;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.NIM_Flage;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.nim_flage;

/**
 * Created by microtech on 2017/12/8.
 */

public class PatientNextFragment extends MiddleFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
   private RecyclerView patient_next_recycler = null;
    private LinearLayoutCompat patient_next_back_linear= null;
    public static AppCompatTextView patient_next_name = null;
    private AppCompatImageView  patient_next_persion = null;
    private AppCompatImageView patient_next_add = null;
    public static  PatientNextAdapter adapter = null;
    View vHeadder = null;
    List<MultipleItemEntity> entity = null;
    SelectPopupWindow menuWindow = null;
    private LinearLayout head_follow_linear = null;
    private  String[] user = null;
    public static String this_suifan_time ="";
    private AppCompatTextView  zuihou_time = null;

    private List<String> header_dataList = null;
    //头部分
    private  LinearLayoutCompat head_linear1= null;
    private  LinearLayoutCompat head_linear2= null;
    private  LinearLayoutCompat head_linear3= null;

    private  AppCompatTextView head_content1= null;
    private  AppCompatTextView head_content2= null;
    private  AppCompatTextView head_content3= null;

    private  AppCompatTextView head_flage1= null;
    private  AppCompatTextView head_flage2= null;
    private  AppCompatTextView head_flage3= null;

    private  AppCompatTextView head_time1= null;
    private  AppCompatTextView head_time2= null;
    private  AppCompatTextView head_time3= null;

    private AppCompatTextView head_title1 = null;

    private AppCompatTextView head_title2 = null;
    private AppCompatTextView head_title3 = null;
    private  LinearLayoutCompat patietn_sign_linear = null;
    private SwipeRefreshLayout patient_next_swipe = null;
    public static PatientNextFragment create(){
        return new PatientNextFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user  = new String[6];
        Bundle arg = getArguments();
        user = arg.getStringArray("user");
        System.out.println("******已经收到数据"+user[0]);
    }

    @Override
    public Object setLayout() {
        return R.layout.patient_next_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        header_dataList = new ArrayList<>();
        patient_next_swipe = bind(R.id.patient_next_swipe);
        patient_next_recycler = bind(R.id.patient_next_recycler);
        patient_next_back_linear = bind(R.id.patient_next_back_linear);
        patient_next_name = bind(R.id.patient_next_name);
        patient_next_persion = bind(R.id.patient_next_persion);
        patient_next_add = bind(R.id.patient_next_add);
        patient_next_add.setOnClickListener(this);
        patient_next_back_linear.setOnClickListener(this);
        patient_next_persion.setOnClickListener(this);
        System.out.println("******已经收到数据0");
        vHeadder = View.inflate(getContext(),R.layout.patient_next_header,null);
        head_follow_linear = vHeadder.findViewById(R.id.head_follow_linear);
        patietn_sign_linear = vHeadder.findViewById(R.id.patietn_sign_linear);
        //头部分
        zuihou_time = vHeadder.findViewById(R.id.zuihou_time);
        head_linear1  = vHeadder.findViewById(R.id.head_linear1);
        head_linear2  = vHeadder.findViewById(R.id.head_linear2);
        head_linear3 = vHeadder.findViewById(R.id.head_linear3);

        head_content1  = vHeadder.findViewById(R.id.head_content1);
        head_content2  = vHeadder.findViewById(R.id.head_content2);
        head_content3  = vHeadder.findViewById(R.id.head_content3);

        head_flage1  = vHeadder.findViewById(R.id.head_flage1);
        head_flage2  = vHeadder.findViewById(R.id.head_flage2);
        head_flage3  = vHeadder.findViewById(R.id.head_flage3);

        head_time1 = vHeadder.findViewById(R.id.head_time1);
        head_time2= vHeadder.findViewById(R.id.head_time2);
        head_time3 = vHeadder.findViewById(R.id.head_time3);

        head_title1 = vHeadder.findViewById(R.id.head_title1);
        head_title2 = vHeadder.findViewById(R.id.head_title2);
        head_title3 = vHeadder.findViewById(R.id.head_title3);
        patietn_sign_linear.setOnClickListener(this);//体征数据详情
        head_follow_linear.setOnClickListener(this);

        patient_next_swipe.setOnRefreshListener(this);
        patient_next_name.setText(user[0]);
        //entity= new SearchDataConvert().CONVERT();
        //adapter  = new PatientNextAdapter(entity);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        patient_next_recycler.setLayoutManager(manager);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.patient_next_add) {
            menuWindow = new SelectPopupWindow(getContext(), itemsOnClick, getActivity());
            menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            menuWindow.showAtLocation(getView(),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (i == R.id.patient_next_back_linear) {
            getSupportDelegate().pop();
        } else if (i == R.id.patient_next_persion) {
            Patient_Detail_Fragment fragment = new Patient_Detail_Fragment();
            Bundle args = new Bundle();
            args.putStringArray("user",user);
            fragment.setArguments(args);
            getSupportDelegate().start(fragment);
        } else if (i == R.id.head_follow_linear)//随访记录
        {
            PatientFollowFragment followFragment = new PatientFollowFragment();
            Bundle bundle=  new Bundle();
            String[] a= new String[5];
            a[1] =user[2];
            a[2] =user[3];
            a[3] =user[1];
            bundle.putStringArray("data",a);
            followFragment.setArguments(bundle);
            getSupportDelegate().start(followFragment);
        }else if(i == R.id.patietn_sign_linear)
        {
            SignFragment signFragment = new SignFragment();
            Bundle bundle=  new Bundle();
            String[] a= new String[5];
            a[1] =user[2];
            a[2] =user[3];
            a[3] =user[1];
            bundle.putStringArray("data",a);
            signFragment.setArguments(bundle);
            getSupportDelegate().start(signFragment);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        onfrist_data();//第一次加载数据
    }


    private void onfrist_data(){
        patient_next_swipe.setRefreshing(true);
        RestClent.builder()
                .url("patients_info")
                .params("region_id",user[2])
                .params("gov_id",user[3])
                .params("user_id",user[1])
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("患者详情数据加载成功********"+response);
                        if(JSON.parseObject(response).getInteger("code") == 200){
                            this_suifan_time= JSON.parseObject(response).getJSONObject("obj").getString("visitTime");
                            entity = new PatientNextDataConvert().setJsonData(response).CONVERT();//测试的DataConvert
                            System.out.println("******已经收到数据5");
                            final JSONObject object1 = JSON.parseObject(response).getJSONObject("obj");
                            final JSONArray array = object1.getJSONArray("dataList");
                            init_header(array);
                            adapter = new PatientNextAdapter(entity,getActivity());
                            adapter.addHeaderView(vHeadder);
                            patient_next_recycler.setAdapter(adapter);
                            zuihou_time.setText(this_suifan_time);
                            patient_next_swipe.setRefreshing(false);
                        }else {
                            SAToast.makeText(getContext(),"数据为空").show();
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onIFailure() {
                        final MultipleItemEntity data = MultipleItemEntity.builder()
                                .setItemType(-1)
                                .setField(OrderQianFields.TEXT, "数据加载错误")
                                .build();
                        entity.add(data);
                        adapter = new PatientNextAdapter(entity,getActivity());
                        patient_next_recycler.setAdapter(adapter);
                        patient_next_swipe.setRefreshing(false);
                    }
                })
                .build()
                .post();
    }
   @Override
    public void onRefresh() {//刷新操作跟新头部
        refresh();
    }
    private void refresh(){
        patient_next_swipe.setRefreshing(true);
        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RestClent.builder()
                        .url("patients_info")
                        .params("region_id",user[2])
                        .params("gov_id",user[3])
                        .params("user_id",user[1])
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println("刷新数据加载成功********"+response);
                                if(JSON.parseObject(response).getInteger("code") == 200){
                                    this_suifan_time= JSON.parseObject(response).getJSONObject("obj").getString("visitTime");
                                    final JSONObject object1 = JSON.parseObject(response).getJSONObject("obj");
                                    final JSONArray array = object1.getJSONArray("dataList");
                                    init_header(array);
                                    zuihou_time.setText(this_suifan_time);
                                    patient_next_swipe.setRefreshing(false);
                                }else {
                                    SAToast.makeText(getContext(),"刷新数据没有").show();
                                }
                            }
                        })
                        .failure(new IFailure() {
                            @Override
                            public void onIFailure() {
                                final MultipleItemEntity data = MultipleItemEntity.builder()
                                        .setItemType(-1)
                                        .setField(OrderQianFields.TEXT, "数据加载错误")
                                        .build();
                                entity.add(data);
                                adapter = new PatientNextAdapter(entity,getActivity());
                                patient_next_recycler.setAdapter(adapter);
                                patient_next_swipe.setRefreshing(false);
                            }
                        })
                        .build()
                        .post();
            }
        },300);
    }
    private void init_header(JSONArray array) {//初始化头部份
        int size = array.size();
        if(size == 0){
            head_linear1.setVisibility(View.GONE);
            head_linear2.setVisibility(View.GONE);
            head_linear3.setVisibility(View.GONE);
        }else if(size == 1) {
            head_linear1.setVisibility(View.VISIBLE);
            String title = array.getJSONObject(0).getString("type");
            head_title1.setText(title+":");
            if(title.equals("体温"))
            {
                head_content1.setText(array.getJSONObject(0).getString("value")+"℃");
            }else  if(title.equals("血压")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmhg");
            } else  if(title.equals("血氧")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"%");
            }else  if(title.equals("脉搏")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"次/分");
            }else  if(title.equals("脉搏")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmol/L");
            }
            int flage = array.getJSONObject(0).getInteger("status");
            String time = array.getJSONObject(0).getString("times");
            head_time1.setText(time);
            if (flage == 0)
            {
                head_flage1.setText("正常");
                head_flage1.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage == 1)
            {
                head_flage1.setText("偏高");
                head_flage1.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage1.setText("偏低");
                head_flage1.setTextColor(Color.parseColor("#FCD210"));
            }

            head_linear2.setVisibility(View.GONE);
            head_linear3.setVisibility(View.GONE);
        }else if(size == 2){
            String title1 = array.getJSONObject(0).getString("type");
            String title2 = array.getJSONObject(1).getString("type");

            head_title1.setText(title1+":");
            head_title2.setText(title2+":");


            if(title1.equals("体温"))
            {
                head_content1.setText(array.getJSONObject(0).getString("value")+"℃");
            }else  if(title1.equals("血压")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmhg");
            } else  if(title1.equals("血氧")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"%");
            }else  if(title1.equals("脉搏")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"次/分");
            }else  if(title1.equals("血糖")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmol/L");
            }

            if(title2.equals("体温"))
            {
                head_content2.setText(array.getJSONObject(1).getString("value")+"℃");
            }else  if(title2.equals("血压")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"mmhg");
            } else  if(title2.equals("血氧")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"%");
            }else  if(title2.equals("脉搏")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"次/分");
            }else  if(title2.equals("血糖")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"mmol/L");
            }


            head_linear1.setVisibility(View.VISIBLE);
            head_linear2.setVisibility(View.VISIBLE);
            head_linear3.setVisibility(View.GONE);
            String time = array.getJSONObject(0).getString("times");
            String time1 = array.getJSONObject(1).getString("times");
            head_time1.setText(time);
            head_time2.setText(time1);

            int flage = array.getJSONObject(0).getInteger("status");
            int flage1 = array.getJSONObject(1).getInteger("status");
            if (flage == 0)
            {
                head_flage1.setText("正常");
                head_flage1.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage == 1)
            {
                head_flage1.setText("偏高");
                head_flage1.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage1.setText("偏低");
                head_flage1.setTextColor(Color.parseColor("#FCD210"));
            }

            if (flage1 == 0)
            {
                head_flage2.setText("正常");
                head_flage2.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage1 == 1)
            {
                head_flage2.setText("偏高");
                head_flage2.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage2.setText("偏低");
                head_flage2.setTextColor(Color.parseColor("#FCD210"));
            }

        }else if(size ==3){

            String title1 = array.getJSONObject(0).getString("type");
            String title2 = array.getJSONObject(1).getString("type");
            String title3 = array.getJSONObject(2).getString("type");
            head_title1.setText(title1+":");
            head_title2.setText(title2+":");
            head_title3.setText(title3+":");



            if(title1.equals("体温"))
            {
                head_content1.setText(array.getJSONObject(0).getString("value")+"℃");
            }else  if(title1.equals("血压")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmhg");
            } else  if(title1.equals("血氧")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"%");
            }else  if(title1.equals("脉搏")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"次/分");
            }else  if(title1.equals("血糖")){
                head_content1.setText(array.getJSONObject(0).getString("value")+"mmol/L");
            }

            if(title2.equals("体温"))
            {
                head_content2.setText(array.getJSONObject(1).getString("value")+"℃");
            }else  if(title2.equals("血压")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"mmhg");
            } else  if(title2.equals("血氧")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"%");
            }else  if(title2.equals("脉搏")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"次/分");
            }else  if(title2.equals("血糖")){
                head_content2.setText(array.getJSONObject(1).getString("value")+"mmol/L");
            }
            if(title3.equals("体温"))
            {
                head_content3.setText(array.getJSONObject(2).getString("value")+"℃");
            }else  if(title3.equals("血压")){
                head_content3.setText(array.getJSONObject(2).getString("value")+"mmhg");
            } else  if(title3.equals("血氧")){
                head_content3.setText(array.getJSONObject(2).getString("value")+"%");
            }else  if(title3.equals("脉搏")){
                head_content3.setText(array.getJSONObject(2).getString("value")+"次/分");
            }else  if(title3.equals("血糖")){
                head_content3.setText(array.getJSONObject(2).getString("value")+"mmol/L");
            }
            head_linear1.setVisibility(View.VISIBLE);
            head_linear2.setVisibility(View.VISIBLE);
            head_linear3.setVisibility(View.VISIBLE);
            String time = array.getJSONObject(0).getString("times");
            String time1 = array.getJSONObject(1).getString("times");
            String time2 = array.getJSONObject(2).getString("times");
            head_time1.setText(time);
            head_time2.setText(time1);
            head_time3.setText(time2);
            int flage = array.getJSONObject(0).getInteger("status");
            int flage1 = array.getJSONObject(1).getInteger("status");
            int flage2 = array.getJSONObject(2).getInteger("status");
            if (flage == 0)
            {
                head_flage1.setText("正常");
                head_flage1.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage == 1)
            {
                head_flage1.setText("偏高");
                head_flage1.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage1.setText("偏低");
                head_flage1.setTextColor(Color.parseColor("#FCD210"));
            }

            if (flage1 == 0)
            {
                head_flage2.setText("正常");
                head_flage2.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage1 == 1)
            {
                head_flage2.setText("偏高");
                head_flage2.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage2.setText("偏低");
                head_flage2.setTextColor(Color.parseColor("#FCD210"));
            }

            if (flage2 == 0)
            {
                head_flage3.setText("正常");
                head_flage3.setTextColor(Color.parseColor("#37BBFB"));
            }else if(flage1 == 1)
            {
                head_flage3.setText("偏高");
                head_flage3.setTextColor(Color.parseColor("#FF597D"));
            }else {
                head_flage3.setText("偏低");
                head_flage3.setTextColor(Color.parseColor("#FCD210"));
            }
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // menuWindow.dismiss();
            int i = v.getId();
            if (i == R.id.tv_send_text)//发送文本
            {
                Intent  intent = new Intent(getActivity(), Send_Text_PhotoActivity.class);
                String[] a= new String[5];
                a[0] = "文本";
                a[1] =user[2];
                a[2] =user[3];
                a[3] =user[4];
                a[4] =user[1];
                Bundle bundle = new Bundle();
                bundle.putStringArray("user", a);
                intent.putExtras(bundle);
                startActivity(intent);
                menuWindow.dismiss();
            } else if (i == R.id.tv_send_photo) {//发送图片
                Intent  intent = new Intent(getActivity(), Send_Text_PhotoActivity.class);
                String[] a= new String[5];
                a[0] = "图片";
                a[1] =user[2];
                a[2] =user[3];
                a[3] =user[4];
                a[4] =user[1];
                Bundle bundle = new Bundle();
                bundle.putStringArray("user", a);
                intent.putExtras(bundle);
                startActivity(intent);
                menuWindow.dismiss();
            } else if (i == R.id.tv_send_speech) {//发送语音
                SAToast.makeText(getContext(),"该功能正在努力开发中。。。。").show();
                menuWindow.dismiss();
            } else if (i == R.id.tv_send_video) {//发送视频
                SAToast.makeText(getContext(),"该功能正在努力开发中。。。。").show();
                menuWindow.dismiss();
            } else if (i == R.id.tv_send_follow) {//发送随访
                if(!user[5].equals(""))
                {
                    //nim_flage = 1;
                   /* new P2PContextImpl(getContext());
                    Intent intent = new Intent(getActivity(), P2PMessageActivity.class);
                    String[] data= new String[2];
                    data[0] = user[5];
                    data[1] = user[0];
                    intent.putExtra("data",data);
                    startActivityForResult(intent,103);*/
                   if(chat==1)
                   {
                       NIM_Flage_Edit.putString("nim_flage", "1");
                       NIM_Flage_Edit.putString("nim_acc",user[5]);
                       NIM_Flage_Edit.commit();
                       NimUIKit.startP2PSession(getContext(),user[5],user[0]);//对方的accid 如：医生的accid
                       //初始化云信的弹框部分
                   }else{
                       SAToast.makeText(getContext(),"该用户还没有加入云病房不能进行随访!").show();
                   }
                }else {
                    SAToast.makeText(getContext(),"暂时不能进行聊天!").show();
                }
                menuWindow.dismiss();
            } else if (i == R.id.patient_next_cancel) {
                menuWindow.dismiss();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("聊天界面返回的东西在这**********************");
    }
}
