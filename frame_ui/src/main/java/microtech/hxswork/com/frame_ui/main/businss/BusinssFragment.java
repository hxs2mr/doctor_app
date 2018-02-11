package microtech.hxswork.com.frame_ui.main.businss;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntityBuilder;
import microtech.hxswork.com.frame_core.util.Defaultcontent;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_core.util.ShareUtils;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.Person.team.TeamDataConvert;
import microtech.hxswork.com.frame_ui.main.send.Send_Text_PhotoActivity;
import microtech.hxswork.com.frame_ui.widget.CountNumberView;
import microtech.hxswork.com.frame_ui.widget.MyScrollView;
import microtech.hxswork.com.frame_ui.widget.SelectPopupWindow;
import microtech.hxswork.com.frame_ui.widget.SelectSharePopup;

import static com.netease.nim.uikit.business.session.activity.P2PMessageActivity.mActivity;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.businss.PieInit.initReportChart;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.EVENT_FRAGMENT;//PieInit.initReportChart;
/**
 * Created by microtech on 2017/12/11.
 */

public class BusinssFragment extends MiddleFragment implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private LinearLayoutCompat buniss_back_linear = null;
    private AppCompatImageView busniss_share = null;
    private LinearLayoutCompat buniss_back_linear1 = null;
    private AppCompatImageView busniss_share1 = null;

    private SelectSharePopup menuWindow=  null;
    private PieChart    pie_chart1,pie_chart;//  病情的饼状图
    private RecyclerView pie_recycle = null;
    private PieData pieData = null;
    private PieData pieData1 = null;
    List<CaseItem> pit_color_List;//表示第一个瓶子图的颜色  4种
    List<CaseItem> pit1_color_List;//表示第二个瓶子图的颜色  不规定种类  动态获取
    private  PieAdapter adapter= null;

    private AppCompatTextView tv_ing = null;
    private AppCompatTextView tv_nomal = null;
    private AppCompatTextView tv_good = null;
    private AppCompatTextView tv_bid = null;


    //温度计模块
    private ContentFrameLayout busniss_person_frame = null;
    private ContentFrameLayout busniss_team_frame = null;
    private ContentFrameLayout busniss_follow_frame = null;

    private AppCompatImageView busniss_person_image = null;
    private AppCompatImageView busniss_team_image = null;
    private AppCompatImageView busniss_follow_image = null;

    private CountNumberView bissniss_personal_number = null;
    private CountNumberView bissniss_team_number = null;
    private CountNumberView busniss_follow_number = null;


    private AppCompatTextView bissniss_personal_number1 = null;
    private AppCompatTextView bissniss_team_number1 = null;
    private AppCompatTextView busniss_follow_number1 = null;

    private List<String> effect_list =null;
    private int frist_total = 0 ;
    private int person_total=0;
    private int team_total = 0 ;
    private int visits_total = 0 ;
    private  int total_heigh = 264;
    private  String[]  a_pie_chart ;//= {"正在治疗","效果一般","有效控制","病情恶化"};
    private  String[]  a_pie_chart1 = {"糖尿病","高血压","脑梗病","其他病"};

    //标题悬停
    private  LinearLayoutCompat bussenss_head_linear1 = null;

    private LinearLayoutCompat no_suifan_linear = null;

    private LinearLayoutCompat bussniss_nodata_linear = null;

    private NestedScrollView bussess_scrollview = null;
    private AppBarLayout abl_bar;
    private View tl_expand, tl_collapse,nodata_view;
    private View v_pay_mask,v_expand_mask, v_collapse_mask;
    private int mMaskColor;
    @Override
    public Object setLayout() {
        return R.layout.busniss_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mMaskColor = getResources().getColor(R.color.piechart_blue);
        busniss_person_frame = bind(R.id.busniss_person_frame);
        busniss_team_frame = bind(R.id.busniss_team_frame);
        busniss_follow_frame = bind(R.id.busniss_follow_frame);
        a_pie_chart = new String[4];
        abl_bar = bind(R.id.abl_bar);
        tl_expand = bind(R.id.tl_expand);
        nodata_view = bind(R.id.nodata_view);
        tl_collapse = bind(R.id.tl_collapse);
        v_pay_mask = bind(R.id.v_pay_mask);
        v_expand_mask = bind(R.id.v_expand_mask);
        v_collapse_mask = bind(R.id.v_collapse_mask);

        busniss_share1 = bind(R.id.busniss_share1);
        buniss_back_linear1 = bind(R.id.buniss_back_linear1);

        bissniss_personal_number1 =bind(R.id.bissniss_personal_number1);
        bissniss_team_number1 =bind(R.id.bissniss_team_number1);
        busniss_follow_number1 =bind(R.id.busniss_follow_number1);

        no_suifan_linear = bind(R.id.no_suifan_linear);
        bussniss_nodata_linear = bind(R.id.bussniss_nodata_linear);
        bussess_scrollview = bind(R.id.bussess_scrollview);

        bussenss_head_linear1 = bind(R.id.bussess_head_linear1);

        busniss_person_image = bind(R.id.busniss_person_image);//个人占团队总人数占比
        busniss_team_image = bind(R.id.busniss_team_image);//团队总人数
        busniss_follow_image = bind(R.id.busniss_follow_image);//随访率占比

        bissniss_personal_number = bind(R.id.bissniss_personal_number);//个人随访的人数
        bissniss_team_number = bind(R.id.busniss_team_number);//团队随访的人数
        busniss_follow_number = bind(R.id.busniss_follow_number);//自己的随访率

        buniss_back_linear = bind(R.id.buniss_back_linear);
        busniss_share = bind(R.id.busniss_share);
        pie_chart = bind(R.id.pie_chart);
        pie_chart1 = bind(R.id.pie_chart1);
        pie_recycle = bind(R.id.pie_recycle);
        tv_ing = bind(R.id.tv_ing);
        tv_nomal = bind(R.id.tv_nomal);
        tv_good = bind(R.id.tv_good);
        tv_bid = bind(R.id.tv_bid);
        buniss_back_linear.setOnClickListener(this);
        busniss_share.setOnClickListener(this);
        buniss_back_linear1.setOnClickListener(this);
        busniss_share1.setOnClickListener(this);

        pit_color_List = new ArrayList<>();
        pit1_color_List = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        pie_recycle.setLayoutManager(manager);
        abl_bar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClent.builder()
                .url("performance")
                .loader(getContext())
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        System.out.println("********业绩统计返回的数据*******" + response);

                        JSONArray array = JSON.parseObject(response).getJSONObject("obj").getJSONArray("marking");

                        if (array.size() != 0 )
                        {
                      /*      no_suifan_linear.setVisibility(View.VISIBLE);

                            bussniss_nodata_linear.setVisibility(View.GONE);

                            bussess_scrollview.setVisibility(View.GONE);

                        }else {*/

                            no_suifan_linear.setVisibility(View.GONE);

                            nodata_view.setVisibility(View.VISIBLE);

                            bussess_scrollview.setVisibility(View.VISIBLE);

                        List<MultipleItemEntity> entity = new PietDataConvert().setJsonData(response).CONVERT();
                         effect_list= new ArrayList<>();
                        effect_list =  entity.get(0).getField(OrderQianFields.IMAGE_ARRAY);
                        for (int i =0 ; i < effect_list.size() ; i++)
                        {
                            System.out.println("******effect_list***********:"+effect_list.get(i));
                            String[]  effect_zhu = effect_list.get(i).split("#");
                            frist_total= frist_total+ Integer.parseInt(effect_zhu[1]);//
                        }
                        person_total = entity.get(0).getField(PieFileds.TOTAL);
                        team_total =  entity.get(0).getField(PieFileds.TEAM_TOTAL);
                        visits_total =  entity.get(0).getField(PieFileds.VISITS_COUNT);


                            bissniss_personal_number1.setText(entity.get(0).getField(PieFileds.TOTAL)+"");
                            bissniss_team_number1.setText(entity.get(0).getField(PieFileds.TEAM_TOTAL)+"");

                            DecimalFormat   df   =   new   DecimalFormat("#0.00%")   ;//转为百分数
                            int end_total =0;
                            if(team_total != 0)
                            {
                                end_total =team_total;
                            }else {
                                if(person_total!=0)
                                {
                                    end_total =person_total;
                                }
                            }
                            if(end_total!=0)
                            {
                                busniss_follow_number1.setText(df.format(visits_total/(float)end_total));
                            }else {
                                busniss_follow_number1.setText(df.format(0.0f));
                            }

                            //busniss_follow_number1.setText(entity.get(0).getField(PieFileds.VISITS_COUNT)+"");
                      /*  bissniss_team_number.setText(team_total+"");
                        bissniss_personal_number.setText(person_total+"");
                        busniss_follow_number.setText(visits_total+"");*/

                        bissniss_personal_number.showNumberWithAnimation(person_total, CountNumberView.INTREGEX);//数字递增
                        bissniss_team_number.showNumberWithAnimation(team_total, CountNumberView.INTREGEX);

                            if(end_total!=0)
                            {
                                busniss_follow_number.setText(df.format(visits_total/(float)end_total));
                            }else {
                                busniss_follow_number.setText(df.format(0.0f));
                            }
                        //busniss_follow_number.showNumberWithAnimation(visits_total, CountNumberView.INTREGEX);

                        int buss_total =team_total;

                        /*
                        * LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mGrid.getLayoutParams(); // 取控件mGrid当前的布局参数
linearParams.height = 75;// 当控件的高强制设成75象素
mGrid.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件mGrid2
                        * */

                        if(team_total!=0)
                        {
                            init_height(buss_total);
                        }
                        //init_height(buss_total);

                        init_CaseItem();

                        initChart();

                        init_pie1_list(entity);

                        busniss_person_image.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.busniss_load));//加载动画
                        busniss_team_image.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.busniss_load));//加载动画
                        busniss_follow_image.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.busniss_load));//加载动画
                        }
                    }
                })
                .build()
                .post();

    }

    private void init_height(int total) {

        System.out.println("***h1:"+ total_heigh / (total/person_total) );
        ContentFrameLayout.LayoutParams linearParams = (ContentFrameLayout.LayoutParams) busniss_person_image.getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams.height = (int) ((person_total/(float)total) * total_heigh);// 当控件的高强制设成75象素

        busniss_person_image.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件mGrid2

       /* LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) busniss_person_image.getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams.height = total_heigh;// 当控件的高强制设成75象素
        busniss_person_image.setLayoutParams(linearParams1); // 使设置好的布局参数应用到控件mGrid2*/

        ContentFrameLayout.LayoutParams linearParams2 = (ContentFrameLayout.LayoutParams) busniss_follow_image.getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams2.height = (int) ((visits_total/(float)total) * total_heigh);// 当控件的高强制设成75象素
        busniss_follow_image.setLayoutParams(linearParams2); // 使设置好的布局参数应用到控件mGrid2

        System.out.println("***h2:"+(visits_total/(float)total) * total_heigh);
    }

    private void init_CaseItem() {
        DecimalFormat   df   =   new   DecimalFormat("#0.00%")   ;//转为百分数

        if (effect_list.size() == 0 || effect_list.size() == 1) {
            String[] effect_data;
            effect_data =  effect_list.get(0).split("#");
            CaseItem item = new CaseItem("0,191,255", Integer.parseInt(effect_data[1]));
            pit_color_List.add(item);
            tv_ing.setText(df.format( Integer.parseInt(effect_data[1])/(float)frist_total));
            tv_nomal.setText(df.format(0));
            tv_good.setText(df.format(0));
            tv_bid.setText(df.format(0));

        }else {
            int size = effect_list.size();
            String[] effect_data;
            CaseItem item;
            int max_number=0;
            String  max="0";
            int a_i=0;//业绩统计的标题下标
            for(int i =0 ; i < size ; i++)
            {
                effect_data =  effect_list.get(i).split("#");
                if(effect_data[0].equals("0"))
                {
                    a_pie_chart[a_i]="正在治疗";
                    tv_ing.setText(df.format(  Integer.parseInt(effect_data[1])/(float)frist_total));
                    item = new CaseItem("55,187,251",Integer.parseInt(effect_data[1]));
                }else  if(effect_data[0].equals("1")){
                    a_pie_chart[a_i]="效果一般";
                    item = new CaseItem("218,234,242",Integer.parseInt(effect_data[1]));
                    tv_nomal.setText(df.format(  Integer.parseInt(effect_data[1])/(float)frist_total));
                }else  if(effect_data[0].equals("2")){
                    a_pie_chart[a_i]="有效控制";
                    item = new CaseItem("98,212,217",Integer.parseInt(effect_data[1]));
                    tv_good.setText(df.format(  Integer.parseInt(effect_data[1])/(float)frist_total));
                }else {
                    a_pie_chart[a_i]="病情恶化";
                    tv_bid.setText(df.format(  Integer.parseInt(effect_data[1])/(float)frist_total));
                    item = new CaseItem("255,89,125",Integer.parseInt(effect_data[1]));
                }
                if(Integer.parseInt(effect_data[1])>max_number)
                {
                    max_number = Integer.parseInt(effect_data[1]);
                    max  =effect_data[0];
                }
                pit_color_List.add(item);
                a_i++;
            }

            if(max.equals("0"))
            {
                pie_chart.setCenterText("有效控制");
            }else   if(max.equals("1")){
                pie_chart.setCenterText("效果一般");
            }else   if(max.equals("2")){
                pie_chart.setCenterText("有效控制");
            }else   if(max.equals("3")){
                pie_chart.setCenterText("病情恶化");
            }
        }
    }
    private void init_pie1_list(List<MultipleItemEntity> entity) {
    int size  = entity.size();
    int bin_0 = 0 ;//患病0的人数
    int bin_1 = 0 ;//患病1的人数
    int bin_2= 0;//患病2的人数
    int bin_3 = 0;//患病3的人数
int end_total=0;
    String[] a;
    for (int j=0 ; j < size ; j++)
    {
        a=entity.get(j).getField(PieFileds.ARRAY);
        for(int l = 0 ;l < a.length;l++)
        {
            if(a[l].equals("0"))
            {
                bin_0=bin_0+(Integer) entity.get(j).getField(PieFileds.VALUE);
            }else if(a[l].equals("1"))
            {
                bin_1=bin_1+(Integer) entity.get(j).getField(PieFileds.VALUE);
            }
            else if(a[l].equals("2"))
            {
                bin_2=bin_2+(Integer) entity.get(j).getField(PieFileds.VALUE);
            }else {
                bin_3=bin_3+(Integer) entity.get(j).getField(PieFileds.VALUE);
            }
        }
    }

        CaseItem item = new CaseItem("187,255,255", bin_0);
        CaseItem item1 = new CaseItem("102,139,139", bin_1);
        CaseItem item2 = new CaseItem("205,92,92",bin_2);
        CaseItem item3 = new CaseItem("255,165,0", bin_3);
        pit1_color_List.add(item);
        pit1_color_List.add(item1);
        pit1_color_List.add(item2);
        pit1_color_List.add(item3);

        end_total = bin_0+bin_1+bin_2+bin_3;
        System.out.println("*****end_total*****"+end_total);
        MultipleItemEntity e1= MultipleItemEntity.builder()
                .setItemType(PieItem.MY_PIE)
                .setField(PieFileds.VALUE,bin_0)
                .setField(PieFileds.TITLE,"糖尿病")
                .setField(PieFileds.COLOR,"187,255,255")
                .setField(PieFileds.TOTAL,end_total)
                .build();
        MultipleItemEntity e2= MultipleItemEntity.builder()
                .setItemType(PieItem.MY_PIE)
                .setField(PieFileds.VALUE,bin_1)
                .setField(PieFileds.TITLE,"高血压")
                .setField(PieFileds.COLOR,"102,139,139")
                .setField(PieFileds.TOTAL,end_total)
                .build();
        MultipleItemEntity e3= MultipleItemEntity.builder()
                .setItemType(PieItem.MY_PIE)
                .setField(PieFileds.VALUE,bin_2)
                .setField(PieFileds.TITLE,"脑梗病")
                .setField(PieFileds.COLOR,"205,92,92")
                .setField(PieFileds.TOTAL,end_total)
                .build();
        MultipleItemEntity e4= MultipleItemEntity.builder()
                .setItemType(PieItem.MY_PIE)
                .setField(PieFileds.VALUE,bin_3)
                .setField(PieFileds.TITLE,"其他病")
                .setField(PieFileds.COLOR,"255,165,0")
                .setField(PieFileds.TOTAL,end_total)
                .build();

        List<MultipleItemEntity> new_entity = new ArrayList<>();
        new_entity.add(e1);
        new_entity.add(e2);
        new_entity.add(e3);
        new_entity.add(e4);
        adapter = new PieAdapter(new_entity);
        pie_recycle.setAdapter(adapter);

        initChart1();
    }

    private void initChart1() {
        pieData1= transCashItem2PieData(pit1_color_List,pie_chart1,1);
        //initReportChart(pie_chart1,pieData);
        initReportChart(pie_chart1,pieData1);
        pie_chart1.setCenterText("糖尿病");  //设置饼状图中间文字，我需求里面并没有用到这个。。

        //设置单个点击事件
        pie_chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Description description = new Description();
                description.setTextSize(15);
                description.setEnabled(true);
                description.setText(a_pie_chart1[(int) h.getX()]+"\n"+(int)e.getY() + "人");
                pie_chart1.setDescription(description);
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void initChart() {

        pieData= transCashItem2PieData(pit_color_List,pie_chart,0);
        //initReportChart(pie_chart1,pieData);
        initReportChart(pie_chart,pieData);

        //pie_chart.setCenterText("有效控制");  //设置饼状图中间文字，我需求里面并没有用到这个。。

        //设置单个点击事件
        pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Description description = new Description();
                description.setTextSize(15);
                description.setText(a_pie_chart[(int) h.getX()]+"  "+(int)e.getY() + "人");
                pie_chart.setDescription(description);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.busniss_share) {

            menuWindow = new SelectSharePopup(getContext(), itemsOnClick,getActivity());
            menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            menuWindow.showAtLocation(getView(),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }else if(i == R.id.buniss_back_linear)
        {
            getSupportDelegate().pop();
        }else if(i == R.id.buniss_back_linear1)
        {
            getSupportDelegate().pop();
        }else if(i == R.id.busniss_share1)
        {
            menuWindow = new SelectSharePopup(getContext(), itemsOnClick,getActivity());
            menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            menuWindow.showAtLocation(getView(),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }




    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // menuWindow.dismiss();
            int i = v.getId();
            if (i == R.id.wechat_linear)//微信分享
            {
                share_wxyou();
                menuWindow.dismiss();
            } else if (i == R.id.wechat_com_linear) {//朋友圈分享

                share_wx();
                menuWindow.dismiss();
            } else if (i == R.id.qq_linear) {//qq分享

                share_qq();
            } else if (i == R.id.cancle) {//取消
                menuWindow.dismiss();
            }
        }
    };


    /**
     *饼状统计图 数据交互

     *@paramtempItemList填充统计图的数据集合

     *@return环状统计图所需的数据对象

     */

    private PieData transCashItem2PieData(List<CaseItem> tempItemList,PieChart mChart,int flage) {   //接口返回的数据list

        ArrayList entries =new ArrayList<>();

        ArrayList colors =new ArrayList<>();

        if(flage == 0) {
            if (tempItemList.size() == 0) {

                entries.add(new PieEntry(100f, "暂无数据"));  //没有数据的状态给设置默认值

                colors.add(Color.rgb(228, 228, 228));  //默认为灰色

            } else {

                for (int i = 0; i < tempItemList.size(); i++) {

                    CaseItem item = tempItemList.get(i);

                    entries.add(new PieEntry(item.getValue(), ""));//可填名称

                    String color = item.getColor();  //从数组里取出RGB色值

                    if (color != null && !TextUtils.isEmpty(color)) {

                        String a[] = color.split(",");

                        int r = Integer.parseInt(a[0]);

                        int g = Integer.parseInt(a[1]);

                        int b = Integer.parseInt(a[2]);

                        colors.add(Color.rgb(r, g, b));  //给绘制区域设置颜色

                    }

                }

            }
        }else {
            for (int i = 0; i < tempItemList.size(); i++) {

                CaseItem item = tempItemList.get(i);

                entries.add(new PieEntry(item.getValue(), "天天"));//可填名称

                String color = item.getColor();  //从数组里取出RGB色值

                if (color != null && !TextUtils.isEmpty(color)) {

                    String a[] = color.split(",");

                    int r = Integer.parseInt(a[0]);

                    int g = Integer.parseInt(a[1]);

                    int b = Integer.parseInt(a[2]);

                    colors.add(Color.rgb(r, g, b));  //给绘制区域设置颜色

                }

            }
        }

        PieDataSet dataSet =new PieDataSet(entries,"");

        dataSet.setSliceSpace(0f);  //设置不同区域之间的间距

        dataSet.setSelectionShift(8f);

        dataSet.setColors(colors);

        PieData data =new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());

        data.setValueTextSize(11f);

        data.setValueTextColor(Color.TRANSPARENT);

        mChart.highlightValues(null);

        mChart.setDrawEntryLabels(false);

        mChart.setDrawMarkerViews(false);

        return data;

    }
    private void share_qq(){
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
        }
        LoadingDialog.cancelDialogForLoading();
        Defaultcontent.url="https://doc.newmicrotech.cn/mk/app/share/"+userBean.getId();
        Defaultcontent.title="蜂鸟医生";
        ShareUtils.shareWeb(getActivity(), Defaultcontent.url, Defaultcontent.title
                , "秀一下我的业绩!","", R.mipmap.app_logo, SHARE_MEDIA.QZONE
                ,0);
    }
    private void share_wx(){
        //1，创建一个WXWebPagerObject对象  用于封装要发送的url
        Defaultcontent.url="https://doc.newmicrotech.cn/mk/app/share/"+userBean.getId();
        Defaultcontent.title="蜂鸟医生";
        ShareUtils.shareWeb(getActivity(), Defaultcontent.url, Defaultcontent.title
                , "秀一下我的业绩!", "", R.mipmap.app_logo, SHARE_MEDIA.WEIXIN_CIRCLE
                ,0);
    }

    private void share_wxyou(){
        Defaultcontent.url="https://doc.newmicrotech.cn/mk/app/share/"+userBean.getId();
        Defaultcontent.title="蜂鸟医生";
        ShareUtils.shareWeb((Activity) getContext(), Defaultcontent.url, Defaultcontent.title
                ,"秀一下我的业绩!","", R.mipmap.app_logo, SHARE_MEDIA.WEIXIN
                ,0);
    }

    private boolean isWebchatAAvaliable() {
        //检测手机上是否安装了微信
        try {
            mActivity.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int offset = Math.abs(verticalOffset);
        int total = appBarLayout.getTotalScrollRange();
        int alphaIn = offset;
        int alphaOut = (300-offset)<0?0:300-offset;
        int maskColorIn = Color.argb(alphaIn, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        int maskColorInDouble = Color.argb(alphaIn*2, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        int maskColorOut = Color.argb(alphaOut*2, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        if (offset <= total / 1.5) {
            tl_expand.setVisibility(View.VISIBLE);
            tl_collapse.setVisibility(View.GONE);
            //v_expand_mask.setBackgroundColor(maskColorInDouble);
        } else {
            tl_expand.setVisibility(View.GONE);
            tl_collapse.setVisibility(View.VISIBLE);
            //v_collapse_mask.setBackgroundColor(maskColorOut);
        }
        //v_pay_mask.setBackgroundColor(maskColorIn);
    }
}
